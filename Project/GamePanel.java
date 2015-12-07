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

public class GamePanel extends JPanel implements Constants{
	//---------
	Character fireChar;
	Character waterChar;
	Character earthChar;
	ArrayList<Tile> tilemap;
	BufferedImage img;
	Dimension gridSize = new Dimension(40,40);

	public GamePanel() {
		try{
			img = ImageIO.read(new File("img/menu/grass.png"));
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	*	initializes the gamePanel
	*/
	public void init(){
		Dimension d = this.getSize();
		System.out.println(d.getWidth() + " , " + d.getHeight());
	}

	public void paintComponent(Graphics g){
		g.drawImage(img,0,0,getWidth(),getHeight(),null); // draws BG
	}

	
}