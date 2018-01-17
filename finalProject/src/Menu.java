
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JButton;
import java.awt.image.BufferStrategy;


class Menu extends Game{
	
	static boolean menuRunning = true;
	static boolean shop = false;
	private int width = 750 ; private int height = 570 ;
	private int x = 0; int y = 0;

	/*
	 * Button initialziation 
	 */
	static int button1getX = 300; 
	static int button1getY = 280; 
	static int standardButtonWidth = 200; 
	static int standardButtonHeight = 50 ;
	
	// constructor for menu
	public Menu() {
		
	} // end constructor
	
	  public static Color [] fillColorArray(Color [] colorArray, int userColor1, int userColor2, int steps) {
			
		    // get rgb from starting color
		    float r1 = colorExtract(userColor1, 'r') ;
		    float g1 = colorExtract(userColor1, 'g') ;
			float b1 = colorExtract(userColor1, 'b') ;
			// get rgb from ending color
			float r2 = colorExtract(userColor2, 'r') ;
			float g2 = colorExtract(userColor2, 'g') ;
			float b2 = colorExtract(userColor2, 'b') ;
			// get differences
			double redIncrement = (r2-r1)/steps ; 
			double blueIncrement = (b2-b1)/steps ; 
			double greenIncrement = (g2-g1)/steps ; 
	
		    // (bval - aval) / n
		    // fill the array with colors ranging from start to end color
		    for(int i = 0; i<colorArray.length; i++) {
		    	 
		    	r1 += redIncrement ; // set red  values within array
		    	b1 += blueIncrement ; // set blue values within array
		    	g1 += greenIncrement ; // set green values within array
		    	Color newCol = new Color( (int)r1,(int)g1,(int)b1 ) ;
		    	colorArray[i] = newCol ; 
		    }	
		    return colorArray ;
	  }
	  
	  public static int colorExtract(int rgb, char pick) {
		  
		  int mask = 0x000000FF ;
		  int r = (rgb >>> 16) & mask ; 
		  int g = (rgb >>> 8) & mask;
		  int b = (rgb) & mask;
		  
		  switch(pick) {
			  
			  case 'r': 
				  return r ; 
			  
			  case 'g': 
				  return g ; 
			  
			  case 'b': 
				  return b ; 
		  }
		  return 1;
	  }
	
	
	public void menuLoop() {
		long lastLoopTime = System.currentTimeMillis(); // start a menu timer just cuz
		/**
		 * menu will run until we quit or go into the shop menu
		 */

		
		while (menuRunning && !shop) { 
			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();
			System.out.print(" menu loop " + menuRunning);
			Graphics2D g2d = (Graphics2D) strategy.getDrawGraphics();
			Image menuBackground = null;			
			URL url = null ;
			//URLConnection con = null;
			//InputStream in = null;
			
			try {
				url = this.getClass().getClassLoader().getResource("sprites/back_small.png");
				if (url == null) {
					System.out.print("Can't find ref: ");
				}
				/*
				// this doesnt work
		        con = url.openConnection();
		        con.setConnectTimeout(100);
		        con.setReadTimeout(100);
		        in = con.getInputStream();
		        */
				else {
					menuBackground = ImageIO.read(url);
					//System.out.print("Got image ");
				}	
				
			}//end try 
			
			// this doesnt work either
			catch (IOException e1) {
				e1.printStackTrace();
			}
			/*
			finally {
			      if(in != null) {
			            try {
			                 in.close(); // this stops that annoying error
			            } catch(IOException ex) {
			                 // handle close failure
			            }
			      }
			}	
			*/		
			menuBackground = menuBackground.getScaledInstance(width+60, height+30, Image.SCALE_DEFAULT);
			g2d.drawImage(menuBackground, 0, 0, null);
			

			// to DO
			
			
			g2d.setColor(Color.RED);
			g2d.drawRect(button1getX, button1getY, standardButtonWidth, standardButtonHeight);
			g2d.fillRect(button1getX, button1getY, standardButtonWidth, standardButtonHeight); 

			g2d.dispose();
			strategy.show();
			
			// finally pause for a bit. Note: this should run us at about
			// 100 fps but on windows this might vary each loop due to
			// a bad implementation of timer
			try { Thread.sleep(10); } catch (Exception e) {}
			
		}
			
		
	}

}



