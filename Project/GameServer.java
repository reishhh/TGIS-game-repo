import java.util.StringTokenizer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

/**
 * The main game server. It just accepts the messages sent by one player to
 * another player
 * @author Joseph Anthony C. Hermocilla
 * 
 * some modifications by Team TGIS
 */

public class GameServer implements Runnable, Constants{
	/**
	 * Placeholder for the data received from the player
	 */	 
	String playerData;
	ArrayList charList;
	volatile boolean isRunning = true;
	/**
	 * The number of currently connected player
	 */
	int playerCount=0;
	
	/**
	 * The socket
	 */
    DatagramSocket serverSocket = null;
    
    /**
     * The current game state
     */
	GameState game;

	/**
	 * The current game stage
	 */
	int gameStage=WAITING_FOR_PLAYERS;
	
	/**
	 * Number of players
	 */
	int numPlayers;
	
	/**
	 * The main game thread
	 */
	Thread t = new Thread(this);
	
	/**
	 * Simple constructor
	 */
	public GameServer(int numPlayers){
		
		/**
		 * 5 <= numPlayers >= 3
		 */
		if (numPlayers <= 5) {
			if (numPlayers >= 3) {
				this.numPlayers = numPlayers;
				try {
					serverSocket = new DatagramSocket(PORT);
					serverSocket.setSoTimeout(100);
				} catch (IOException e) {
					System.err.println("Could not listen on port: "+PORT);
					System.exit(-1);
				}catch(Exception e){}
				
				//Create the game state
				game = new GameState();
				charList = new ArrayList();
				System.out.println("Game created...");
				
				//Start the game thread
				t.start();
			}
			else {
				System.err.println("ERROR: Number of players should be atleast 3.\n");
			}
		}
		else {
			System.err.println("ERROR: Number of players should be atmost 5.\n");
		}
	}
	
	/**
	 * Helper method for broadcasting data to all players
	 * @param msg
	 */
	public void broadcast(String msg){
		for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			NetPlayer player=(NetPlayer)game.getPlayers().get(name);			
			send(player,msg);	
		}
	}

	/**
	 * Send a message to a player
	 * @param player
	 * @param msg
	 */
	public void send(NetPlayer player, String msg){
		DatagramPacket packet;	
		byte buf[] = msg.getBytes();		
		packet = new DatagramPacket(buf, buf.length, player.getAddress(),player.getPort());
		try{
			serverSocket.send(packet);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	/**
	 * The juicy part
	 */
	//  Receive from a player and Send to all players
	public void run(){
		while(isRunning){
						
			// Get the data from players
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try{
     			serverSocket.receive(packet);
			}catch(Exception ioe){}
			
			/**
			 * Convert the array of bytes to string
			 */
			playerData=new String(buf);
			
			//remove excess bytes
			playerData = playerData.trim();
		
			// process
			switch(gameStage){
				case WAITING_FOR_PLAYERS:
						if (playerData.startsWith("CONNECT")){
							String tokens[] = playerData.split(" ");
							NetPlayer player=new NetPlayer(tokens[1],tokens[2], Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), packet.getAddress(),packet.getPort());
							System.out.println("Player connected: "+tokens[1]);
							game.update(tokens[1].trim(),player);
							broadcast("CONNECTED "+tokens[1]);
							playerCount++;
							if (playerCount==numPlayers){
								gameStage=GAME_START;
							}						
						}
					break;	
					  
				case GAME_START:
					System.out.println("Game State: START");
					broadcast("START");
					gameStage=IN_PROGRESS;
					break;
					  
				case IN_PROGRESS:
					//Player data was received!
					if (playerData.startsWith("PLAYER")){
						//Tokenize:
						//The format: PLAYER <player name> <x> <y>
						String[] playerInfo = playerData.split(" ");					  
						String pname =playerInfo[1];
						int x = Integer.parseInt(playerInfo[2].trim());
						int y = Integer.parseInt(playerInfo[3].trim());
						  
						//Get the player from the game state
						NetPlayer player=(NetPlayer)game.getPlayers().get(pname);					  
						player.setX(x);
						player.setY(y);
						  
						//Update the game state
						game.update(pname, player);
						  
						//Send to all the updated game state
						System.out.println(game.toString());
						broadcast(game.toString());
					}
					else if(playerData.startsWith("CHAT")){
					  	System.out.println("PD: " + playerData);
					  	StringTokenizer playerInfo = new StringTokenizer(playerData);				  
						String pname = playerInfo.nextToken(); 
						String chat = "";
						  
						while (playerInfo.hasMoreTokens()){
						 chat += playerInfo.nextToken() + " ";
						}
						  
						//chat: <player name> : <msg> 
						System.out.println("chat: " + chat);
						String chatMessage =  "CHAT @" + chat + "\n";
						
						//Send to all the updated game state
						broadcast(chatMessage);
					}
					break;
			}				  
		}
	}	
	
	public static void main(String args[]){
		if (args.length != 1){
			System.out.println("Usage: java GameServer <number of players>");
			System.exit(1);
		}
		
		new GameServer(Integer.parseInt(args[0]));
	}
}