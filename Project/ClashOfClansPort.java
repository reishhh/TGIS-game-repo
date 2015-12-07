import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.lang.System;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;

public class ClashOfClansPort implements Runnable, ActionListener, Constants, KeyListener{
	ClashOfClansUI ui;
	String server;
	String name;
	String role;
	int x,y;

	JButton playGame;
	JButton exitGame;
	JButton send;
	JButton ok;

	JRadioButton defendButton;
	JRadioButton attackButton;

	Thread t = new Thread(this);
	boolean connected = false;
	DatagramSocket socket = new DatagramSocket();
	String serverData;

	volatile boolean isRunning = true;

	public ClashOfClansPort() throws Exception{
		socket.setSoTimeout(100);
	}

	public void setUI(ClashOfClansUI ui) {
		this.ui = ui;
	}

	public void addListenerToButtons(JButton playGame, JButton exitGame, JButton send, JButton ok){
		this.playGame = playGame;
		this.exitGame = exitGame;
		this.send = send;
		this.ok = ok;

		this.playGame.addActionListener(this);
		this.exitGame.addActionListener(this);
		this.send.addActionListener(this);
		this.ok.addActionListener(this);
	}
	
	public void addKeyListenerToGame() {
		ui.messageField.addKeyListener(this);
		ui.gamePanel.addKeyListener(this);
	}
	
	public void keyPressed(KeyEvent e){
		if(e.getKeyChar()==KeyEvent.VK_ENTER){
			String message = ui.messageField.getText();
			if(message.equals("")) System.out.println("Please enter string");
			else{
				sendMessage("CHAT " + name + " : " + message + "\n");
				ui.messageField.setText("");
			}
		}
		
	}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	
	public void run(){
		while(isRunning){
			try{
				Thread.sleep(1);
			}catch(Exception ioe){}

			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);

			try{
				socket.receive(packet);
			}catch(Exception ieo){}

			serverData = new String(buf);
			serverData = serverData.trim();

			if(!connected && serverData.startsWith("CONNECTED")){
				connected = true;
				System.out.println("Connected");
			}else if(!connected) {
					System.out.println("Connecting..");
					System.out.println("CH: " + role);
					
					sendMessage("CONNECT "+name +" "+role+" "+x+" "+y);
				
			}else if(connected){
				if(serverData.startsWith("PLAYER")){
					String[] playersInfo = serverData.split(":");
					for(int i=0; i<playersInfo.length;i++){
						String[] playerInfo = playersInfo[i].split(" ");
						String pname = playerInfo[1];
						String role = playerInfo[2];
						int x = Integer.parseInt(playerInfo[3]);
						int y = Integer.parseInt(playerInfo[4]);
					}
					ui.repaint();
				}else if(serverData.startsWith("CHAT")){
					System.out.println(serverData);
					String[] arr = serverData.split("@");
					String message = arr[1];
					ui.messageBox.append("\n" + message);
					final int chatLength = ui.messageBox.getText().length();
					ui.messageBox.setCaretPosition(chatLength);
				}
			}
		}
	}

	public void sendMessage(String msg){
		try{
			byte[] buf = msg.getBytes();
			InetAddress address = InetAddress.getByName(server);
			DatagramPacket packet = new DatagramPacket(buf,buf.length,address,PORT);
			socket.send(packet);
		}catch(Exception e){}
	}

	public void setServer(String server) {
		this.server = server;
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource()==playGame){
			ui.showPopup();

		}
		else if(e.getSource()==send){
			String message = ui.messageField.getText();
			sendMessage("CHAT " + name + " : " + message + "\n");
			ui.messageField.setText("");
		}
		else if(e.getSource()==ok){
			this.name = ui.nameField.getText();
			if(name.equals("")) {
				JOptionPane.showMessageDialog(ui.pop,"Please enter your name.", "Error!", JOptionPane.ERROR_MESSAGE);
			}
			else{
				System.out.println(name);
				if(ui.defendButton.isSelected() == true) {
					this.role = "DEFEND";
				}
				else{
					this.role = "ATTACK";
				}
				ui.pop.setVisible(false);
				ui.cl.next(ui.basePanel);
				ui.initGamePanel();
				t.start();
				PlayMidiAudio playerMIDI = new PlayMidiAudio();
	 			playerMIDI.playMidi("music/Fun2.mid",10);
	 			ui.label.setText("ROLE: "+role);
			}
		}
		else{
			System.exit(1);
		}
	}
}