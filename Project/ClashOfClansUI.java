import java.util.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.lang.System;
import java.io.IOException;
import java.io.File;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import javax.imageio.ImageIO;

public class ClashOfClansUI extends JFrame implements Constants{
	ClashOfClansPort port;

	JPanel basePanel;	
	JPanel mainPanel;
	GamePanel gamePanel;
	JPanel menuPanel;

	JButton playGame;
	JButton exitGame;
	JButton send;
	JButton ok;

	BufferedImage img;
//--------------
	JPanel chatPanel;
	JPanel labelPanel;

	JTextArea messageBox;
	JTextField messageField;
	JScrollPane scroll;
	JScrollPane scroll2;
	JLabel label,label2;
//----------------------

	JPanel level1;
	CardLayout cl;
	CardLayout cl2;

	JFrame pop;
	JTextField nameField;

	JRadioButton defendButton;
	JRadioButton attackButton;

	String server;

//------- LEVEL PORTION -----
	ArrayList<Tile> tilemap;
	Dimension gridSize = new Dimension(40,40);

	public ClashOfClansUI(String server, ClashOfClansPort port){
		super("Clash Of Clans");
		this.server = server;

		// ----- BACKGROUND IMAGE ------
		try{
			img = ImageIO.read(new File("img/menu/menu-4.png"));
		}catch (IOException e) {
			e.printStackTrace();
		}
		menuPanel = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(img,0,0,getWidth(),getHeight(),null);
			}
		};

		cl = new CardLayout();
		basePanel = new JPanel(cl);		
		mainPanel = new JPanel();	
		basePanel.add(menuPanel,MENU);
		basePanel.add(mainPanel,GAME);

		// ------ MENU PANEL ------
		menuPanel.setLayout(null);
		playGame = new JButton("PLAY");
		exitGame = new JButton("EXIT");
		
		//setBounds (x, y, length, height)
		playGame.setBounds(240, 490, 180, 50);
		playGame.setBackground(Color.GREEN);
		playGame.setFont(new Font("Supercell-Magic", Font.PLAIN, 16));
		exitGame.setBounds(460, 490, 180, 50);
		exitGame.setBackground(Color.GREEN);
		exitGame.setFont(new Font("Supercell-Magic", Font.PLAIN, 16));

		menuPanel.add(playGame);
		menuPanel.add(exitGame);

		ok = new JButton("SUBMIT");
		ok.setFont(new Font("Supercell-Magic", Font.PLAIN, 16));

		//------ MAIN PANEL ------
		chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout());
		chatPanel.setBackground(new Color(200,180,230));

		labelPanel = new JPanel();
		labelPanel.setLayout(new BorderLayout());
		label = new JLabel("CHARACTER: ", SwingConstants.CENTER);
		label.setFont(new Font("Supercell-Magic", Font.PLAIN, 12));
		label2 = new JLabel("SCORE: 000", SwingConstants.CENTER);
		label2.setFont(new Font("Supercell-Magic", Font.PLAIN, 12));
		label.setPreferredSize(new Dimension(450,30));
		label2.setPreferredSize(new Dimension(450,30));
		labelPanel.add(label, BorderLayout.WEST);
		labelPanel.add(label2, BorderLayout.EAST);
		labelPanel.setBackground(Color.RED);

		messageBox  = new JTextArea(30,20);
		messageBox.setEditable(false);		
		messageBox.setLineWrap(true);
		scroll = new JScrollPane(messageBox);
		send = new JButton("SEND");
		send.setFont(new Font("Supercell-Magic", Font.PLAIN, 12));
		
		gamePanel = new GamePanel();
		gamePanel.setLayout(null);
		gamePanel.setFocusable(true);

		messageField = new JTextField();
		messageField.setPreferredSize(new Dimension(30,20));

		chatPanel.add(scroll, BorderLayout.NORTH);
		chatPanel.add(messageField, BorderLayout.CENTER);
		chatPanel.add(send, BorderLayout.EAST);

		mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
		
		//labelPanel
        c.weightx = 0;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 10;
        c.ipadx = 1000;
        mainPanel.add(labelPanel, c);
		
		//gamePanel
		c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 1;
        c.ipady = 500;
        c.ipadx = 200;
        mainPanel.add(gamePanel, c);

		//chatPanel
		c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.ipady = 500;
        c.ipadx = 300;
        mainPanel.add(chatPanel, c);

		this.add(basePanel);
		this.setSize(900,600);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		this.port = port;
		port.setUI(this);
		port.setServer(server);
		port.addListenerToButtons(playGame,exitGame,send,ok);
	}

	public void showPopup() {
		JPanel panel = new JPanel(new GridLayout(5,1));
		JPanel panel2 = new JPanel();
		JLabel label = new JLabel("Enter your name");
		label.setFont(new Font("Supercell-Magic", Font.PLAIN, 12));
		JLabel label2 = new JLabel("Select your role:");
		label2.setFont(new Font("Supercell-Magic", Font.PLAIN, 12));
		nameField = new JTextField();
		defendButton = new JRadioButton("DEFEND");
		defendButton.setFont(new Font("Supercell-Magic", Font.PLAIN, 12));
		attackButton = new JRadioButton("ATTACK");
		attackButton.setFont(new Font("Supercell-Magic", Font.PLAIN, 12));
		defendButton.setSelected(true);
		
		ButtonGroup group = new ButtonGroup();
		group.add(defendButton);
		group.add(attackButton);

		panel2.add(defendButton);
		panel2.add(attackButton);
		
		pop = new JFrame();
		panel.add(label);
		panel.add(nameField);
		panel.add(label2);
		panel.add(panel2);
		panel.add(ok);
		pop.add(panel);

		pop.setSize(new Dimension(300,200));
		pop.setLocationRelativeTo(null);
		pop.setVisible(true);		
	}

	public void initGamePanel(){
		gamePanel.init();
	}
	
	public static void main(String args[])throws Exception{
		if(args.length != 1){
			System.out.println("Usage: java ClashOfClansUI <server address>");
		}
		ClashOfClansPort port = new ClashOfClansPort();
		ClashOfClansUI game = new ClashOfClansUI(args[0],port);		

	}

}