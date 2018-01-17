
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;

public class Game extends Canvas {
	/**
	 * Master variables. these are important
	 */
	public static int level = 1;
	public static int daveSmall = 0; 
	public int width = 800 ; 
	public int height = 600 ;	
	public int coins = 0; // currency for the game
	// cat turret booleans
	public boolean grumpyCat = false, cougar = false, tiger = false, kitten = true;
	// booleans for abilities
	public boolean fireball = false, rapidFire = false, pauseTime = false; 
	//  The interval between our players shot (ms) */
	long firingInterval = 1500;
	long timeCountDown = 70;
	long countDown = 140; 
	/** The number of rats left on the screen */
	private int ratCount = 3; // start off with 10 rats
	

	// The stragey that allows us to use accelerate page flipping  
	public BufferStrategy strategy;
	// True if the game is currently "running", i.e. the game loop is looping */
	private boolean gameRunning = false;
	//  The list of all the entities that exist in our game 
	private ArrayList entities = new ArrayList();
	// The list of entities that need to be removed from the game this loop */
	private ArrayList removeList = new ArrayList();
	//  The entity representing the player */
	private Entity catHead;
	private Entity shot;
	//  The speed at which the player's catHead should move (pixels/sec) */
	private double moveSpeed = 300;
	//  rotation variable of cat head */
	private double rotateSpeed = 5;	
	// The time at which last fired a shot */
	long lastFire = 0;
	
	/** The message to display which waiting for a key press */
	private String message = "";
	/** boolean variable for starting gameplay */
	private boolean waitingForKeyPress = true;
	// True if the left cursor key is currently pressed */
	private boolean leftPressed = false;
	// True if the right cursor key is currently pressed */
	private boolean rightPressed = false;
	// True if we are firing */
	private boolean firePressed = false;
	// True if game logic needs to be applied this loop, normally as a result of a game event */
	private boolean logicRequiredThisLoop = false;
	
	public static JFrame container ;
	JPanel panel = (JPanel) container.getContentPane();

	// button pressed globals 
	boolean shopButton1Pressed = false;
	boolean shopButton2Pressed = false;
	boolean shopButton3Pressed = false;
	boolean shopButton4Pressed = false;
	boolean shopButton5Pressed = false;
	boolean shopButton6Pressed = false;
	boolean button1Pressed = false; 
	boolean button2Pressed = false; 
	boolean goBackButtonPressed = false; 
	boolean timePaused = false; 
	boolean superCatMode = false; 
	// main menu
	static int button1getX = 150; 
	static int button1getY = 310; 
	static int button2getX = 450; 
	static int button2getY = 310;
	static int goBackButtonX = 30;
	static int goBackButtonY = 20 ;
	// first row of shop
	static int shopButton1X = 50; // top left
	static int shopButton1Y = 190; 
	static int shopButton2X = 340; // top middle
	static int shopButton2Y = 190; 
	static int shopButton3X = 620; //top right
	static int shopButton3Y = 190;
	// 2nd row of shop
	static int shopButton4X = 70; // bottom left
	static int shopButton4Y = 500; 
	static int shopButton5X = 370; // bottom middle
	static int shopButton5Y = 500; 
	static int shopButton6X = 620; //bottom right
	static int shopButton6Y = 500;
	// widths and heights of buttons
	static int standardButtonWidth = 200; 
	static int standardButtonHeight = 50 ;
	static int shorterButtonWidth = 100; 
	static int shorterButtonHeight = 45 ;
	static int pictureButton = 140;
	// left rail starts at (0,220) and goes to (330, 570)
	// right rail starts (750,260) and goes to (500,570) slope 1.25

	/**
	 * Construct our game and set it running.
	 */
	public Game() {
		// create a frame to contain our game
		addMenu();
		/** 
		 * Set panel settings
		 **/
		
		panel.setPreferredSize(new Dimension(800,600));
		panel.setLayout(null);
		
		setBounds(0,0,800,600);
		panel.add(this);
		
		// Tell AWT not to bother repainting our canvas since we're
		// going to do that our self in accelerated mode
		setIgnoreRepaint(true);
		
		// finally make the window visible 
		container.pack();
		container.setResizable(false);
		container.setVisible(true);
		
		// add a listener to respond to the user closing the window. If they
		// do we'd like to exit the game
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});		
		// request the focus so mouse events come to us
		 requestFocus();
		 
		// create the buffering strategy which will allow AWT
		// to manage our accelerated graphics
		 createBufferStrategy(2);
		 strategy = getBufferStrategy();

		addKeyListener(new KeyInputHandler());

		
		addMouseListener(new MouseAdapter() { 
			
		      // check to see if mouse is pressed
			  public void mousePressed(MouseEvent e) { 
		            if( buttonCheck(e,button1getX,button1getY, standardButtonWidth, standardButtonHeight)==true ) {	
		            	button1Pressed = true; 
		            }
		            if( buttonCheck(e,button2getX,button2getY, standardButtonWidth, standardButtonHeight)==true ) {	
		            	button2Pressed = true; 
		            }
		            if( buttonCheck(e,shopButton1X,shopButton1Y, pictureButton, pictureButton)==true ) { shopButton1Pressed = true;  }
		            if( buttonCheck(e,shopButton2X,shopButton2Y, pictureButton, pictureButton)==true ) { shopButton2Pressed = true;  }
		            if( buttonCheck(e,shopButton3X,shopButton3Y, pictureButton, pictureButton)==true ) { shopButton3Pressed = true;  }
		            if( buttonCheck(e,shopButton4X,shopButton4Y, shorterButtonWidth, shorterButtonHeight)==true ) { shopButton4Pressed = true;  }
		            if( buttonCheck(e,shopButton5X,shopButton5Y, shorterButtonWidth, shorterButtonHeight)==true ) { shopButton5Pressed = true;  }
		            if( buttonCheck(e,shopButton6X,shopButton6Y, shorterButtonWidth, shorterButtonHeight)==true ) { shopButton6Pressed = true;  }		            
		            if( buttonCheck(e,goBackButtonX,goBackButtonY, shorterButtonWidth, shorterButtonHeight)==true ) { goBackButtonPressed = true;  }
		           
		            // Secret buttons u need to unlock
		            if(pauseTime==true)
		            	if( buttonCheck(e,shopButton4X-30,shopButton4Y, shorterButtonWidth, shorterButtonHeight)==true ) { timePaused = true; System.out.print(" time Paused ");  } // these serve as pressed variables
		            if(rapidFire==true)
		            	if( buttonCheck(e,shopButton6X+40,shopButton6Y, shorterButtonWidth, shorterButtonHeight)==true ) { superCatMode = true;  }		            
     
			  }
			  // MOUSE CLICKED
	          public void mouseClicked(MouseEvent e) { 
		            
		    		int mouseX = (int) (e.getX()); 
		    		int mouseY = (int) (e.getY()); 
		    		System.out.println("click at x: " + mouseX + " y: " + mouseY); 
		            if( !Menu.shop && buttonCheck(e,button1getX,button1getY, standardButtonWidth, standardButtonHeight)==true ) {	            	
		            	System.out.println("click in BUTTON1 ");
		            	gameRunning = true;
		            	Menu.menuRunning = false ;
		            }
		            else if( Menu.menuRunning && buttonCheck(e,button2getX,button2getY, standardButtonWidth, standardButtonHeight)==true  ) 
		            {	
		            	Menu.shop = true; 
		            	goBackButtonPressed = false; // make button look normal again
		            }
		            else if( buttonCheck(e,goBackButtonX,goBackButtonY, shorterButtonWidth, shorterButtonHeight)==true  ) {	
		            	Menu.shop = false; 
		            	button2Pressed = false; // make button look normal again
		            }
		            if( buttonCheck(e,shopButton1X,shopButton1Y, pictureButton, pictureButton)==true  )  {	
		            	if(coins > 10) {
			            	cougar = true; 
			            	coins-=10; 
			            	shopButton2Pressed = false; // make button look normal again
			            	shopButton3Pressed = false;	grumpyCat = false; tiger = false; kitten = false;	            	}
		            	else {
		            		System.out.println("Not enough money");
		            		shopButton1Pressed = false; 
		            	} 
		            }
		            if( buttonCheck(e,shopButton2X,shopButton2Y, pictureButton, pictureButton)==true  ) {	
		            	if(coins > 300) {
		            		grumpyCat = true;
		            		coins-=300;
			            	shopButton1Pressed = false; // make button look normal again
			            	shopButton3Pressed = false;
			            	cougar = false; tiger = false; kitten = false; 	            	}
		            	else {
		            		System.out.println("Not enough money");
		            		shopButton2Pressed = false; 
		            	} 
		            }
		            if( buttonCheck(e,shopButton3X,shopButton3Y, pictureButton, pictureButton)==true  ) {	
		            	if(coins > 600) {
			            	tiger = true;
			            	coins-=600;
			            	shopButton1Pressed = false; // make button look normal again
			            	shopButton3Pressed = false; grumpyCat = false; cougar = false; kitten = false;		            	}
		            	else {
		            		System.out.println("Not enough money");
		            		shopButton3Pressed = false; 
		            	} 
		            }
		            if( buttonCheck(e,shopButton4X,shopButton4Y, shorterButtonWidth, shorterButtonHeight)==true  ) {	
		            	if(coins > 200) {
		            	pauseTime = true; // new ability unlocked
		            	coins-=200;
		            	}
		            	else {
		            		System.out.println("Not enough money");
		            		shopButton4Pressed = false;
		            	}
		            	//shopButton4Pressed = false; // make button look normal again
		            }
		            if( buttonCheck(e,shopButton5X,shopButton5Y, shorterButtonWidth, shorterButtonHeight)==true  ) {	
		            	if(coins > 100) {
			            	fireball = true; // new ability unlocked
			            	coins-=100;
		            	}
		            	else {
		            		System.out.println("Not enough money");
		            		shopButton5Pressed = false;
		            	}
		            	//shopButton5Pressed = false; // make button look normal again
		            }
		            if( buttonCheck(e,shopButton6X,shopButton6Y, shorterButtonWidth, shorterButtonHeight)==true  ) {	
		            	if(coins > 1000) {
		            		rapidFire = true; // new ability unlocked
		            		coins-=1000;
		            	}
		            	else {
		            		System.out.println("Not enough money");
		            		shopButton6Pressed = false;
		            	}
		            	//shopButton6Pressed = false; // make button look normal again
		            }
		            /**
		             * SECRET BUTTONS
		             */
		            if( pauseTime && buttonCheck(e,shopButton4X,shopButton4Y, shorterButtonWidth, shorterButtonHeight)==true  ) {	
		            	
		            	//timePaused = false; // time has been paused
		            }
		            if( superCatMode && buttonCheck(e,shopButton6X+40,shopButton6Y, shorterButtonWidth, shorterButtonHeight)==true  ) {	
		            	removeEntity(catHead);
		            	catHead = new catEntity(null,"sprites/rainbowCannon.png",400,520);
		            	entities.add(catHead);
		            	firingInterval = 100; 
		            	//superCatMode = false; // time has been paused
		            }
		            
		      } 
		}); 		
	}
	
	public boolean buttonCheck(MouseEvent e, int buttonX, int buttonY, int w, int h){
		PointerInfo a = MouseInfo.getPointerInfo();
		Point b = a.getLocation();
		boolean temp = false; 
		int mouseX = (int) (e.getX()); 
		int mouseY = (int) (e.getY()); 
        if(mouseX > buttonX && mouseY > buttonY  &&  
        		mouseX < (buttonX + w) && 
        		mouseY <  (buttonY +  h)  ) 
        {	            	
        	temp = true;
        }
		return temp;
		
	}
	   private void displayGUI() // not usable
	    {
	        JFrame frame = new JFrame("Card Layout Example");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        JPanel contentPane = new JPanel();
	        contentPane.setBorder(
	            BorderFactory.createEmptyBorder(5, 5, 5, 5));
	        contentPane.setLayout(new CardLayout());

	        /*
	        panel1 = new MyPanel(contentPane, this);
	        panel2 = new MyPanel2(contentPane
	                , Color.GREEN.darker().darker(), this);
	        panel3 = new MyPanel2(contentPane
	                , Color.DARK_GRAY, this);   

	        contentPane.add(panel1, "Panel 1"); 
	        contentPane.add(panel2, "Panel 2");
	        contentPane.add(panel3, "Panel 3");         

	        frame.getContentPane().add(choiceBox, BorderLayout.PAGE_START);
	        frame.getContentPane().add(contentPane, BorderLayout.CENTER);       
	        frame.pack();   
	        frame.setLocationByPlatform(true);
	        frame.setVisible(true);
	        */
	    }
	
	
	/*public JFrame getFrame() {
		return container; 
	}*/
	
	/**
	 * 
	 * Make the menu on top bar
	 * 
	 */
	public  void addMenu() {
		//setup the frame's menu bar
		JMenu menu = new JMenu("File");
		System.out.println("add menu");
		
		////   EVENT - GAME   /////
		JMenuItem jItem = new JMenuItem("Game");
		jItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Menu.menuRunning = false;
				gameRunning = true; 
				//return; 
			}


		});
		menu.add(jItem);
		
		///////////   SAVE IMAGE   ////////////
		JMenuItem save = new JMenuItem("Shop");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
            	Menu.shop = true;
            	gameRunning = false; 
            	Menu.menuRunning = true; 
            }
		});
		menu.add(save);
		
		//Exit
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		menu.add(exitItem);
		
		//attach a menu to a menu bar
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menu);
		container.setJMenuBar(menuBar);
	} // end of top menu
	
	
	private void setGameRunning(boolean b) {
		gameRunning = b; 
		
	}
	

	
	/**
	 * 
	 * Start a new game
	 * 
	 */
	private void startGame() {
		// clear out any existing entities and intialise a new set
				
		if(getBufferStrategy() == null) {
			createBufferStrategy(2);
			strategy = getBufferStrategy();
			System.out.println("buffer strategy initialized");
		}
		
		entities.clear();
		initEntities();
		
		// blank out any keyboard settings we currently have
		leftPressed = false;
		rightPressed = false;
		firePressed = false;
	}
	
	// Place Sprites HERE
	private void initEntities() {
		// Clear up entities from before
		for (int i=0;i<entities.size();i++) {
			Entity entity = (Entity) entities.get(i);
			removeEntity(entity); 
		}
		entities.removeAll(removeList);
		removeList.clear();
		// declare entities
		// there are 4 different types of cat turrets
		if(cougar == true) {
			catHead = new catEntity(this,"sprites/cougar_cannon.png",400,520);
		}
		else if(grumpyCat == true) {
			catHead = new catEntity(this,"sprites/grumpyCat.png",400,520);
		}
		else if(tiger == true) {
			catHead = new catEntity(this,"sprites/tiger.png",370,530);
		}
		else {
			catHead = new catEntity(this,"sprites/catHead1.png",400,525);
		}
		if(fireball==true) {
			shot = new ShotEntity(this,"sprites/shot.gif",catHead.getX()+10,catHead.getY()-20);					
		}
		else {
			shot = new ShotEntity(this,"sprites/hair_ball.png",catHead.getX()+10,catHead.getY()-20);						
		}
		// add cathead
		entities.add(catHead);
		
		// create a block of rats 
		//ratCount = 0;
		int counting = 0;
		int x = 0;
		while(counting < ratCount) {
				Random pos = new Random();
				x = pos.nextInt(width-10); 
				if(x<0) {
					x = 10;
				}
				int y = pos.nextInt(15)-10;
				Entity rat = new ratEntity(this,"sprites/rat.PNG",x,y);
				entities.add(rat);
				counting++;
			}
		System.out.println(" rats entering the game: " + ratCount);
	}
	
	/**
	 * 
	 * 
	 * Notifications
	 * 
	 * 
	 */
	public void updateLogic() {
		logicRequiredThisLoop = true;
	}
	
	/**
	 * Remove an entity from the game. The entity removed will
	 * no longer move or be drawn.
	 */
	public void removeEntity(Entity entity) {
		removeList.add(entity);
	}
	
	/**
	 * Notification that the player has died. 
	 */
	public void notifyDeath() {
		message = "Oh no! They got you, try again?";
		initEntities();
		waitingForKeyPress = true;
		Menu.menuRunning = true; 
		gameRunning = false ;
	}
	
	/**
	 * Notification that the player has won since all the rats
	 * are dead.
	 */
	public void notifyWin() {
		message = "You Beat Level " + level;
		level++; 
		ratCount = 0;
		ratCount = (level * 2); 
		initEntities();
		waitingForKeyPress = true;
		Menu.menuRunning = true; 
		gameRunning = false; // this will exit the loop, so put this last
	}
	
	/**
	 * Notification that an rat has been killed
	 */
	public void notifyratKilled() {
		// reduce the ratt count, if there are none left, the player has won!
		ratCount--;
		
		if (ratCount == 0) {
			notifyWin();
		}
		
		// if there are still some rats left then they all need to get faster, so
		// speed up all the existing rats
		for (int i=0;i<entities.size();i++) {
			Entity entity = (Entity) entities.get(i);
			
			if (entity instanceof ratEntity) {
				// speed up by 2%
				entity.setHorizontalMovement((entity.getHorizontalMovement()*((level+1)/4)) * 1.02); // rat speed starts at 75 and then goes to 112, 150, 188...
			}
		}
	} // rat killed method
	
	
	/**
	 * 
	 * fire method
	 * 
	 */
	public void tryToFire() {
		// check that we have waiting long enough to fire
		if (System.currentTimeMillis() - lastFire < firingInterval) {
			return;
		}
		
		// if we waited long enough, create the shot entity, and record the time.
		lastFire = System.currentTimeMillis();
		ShotEntity shot = null; 
		if(fireball == true) {
			shot = new ShotEntity(this,"sprites/shot.gif",catHead.getX()+10,catHead.getY()-20);
			shot.rotateShot(shot);
		}
		else {
			shot = new ShotEntity(this,"sprites/hair_ball.png",catHead.getX()+10,catHead.getY()-20);			
			shot.rotateShot(shot);
		}
		//System.out.println("is shot null?: " + shot);
		entities.add(shot);
	}
	
	/**
	 * 
	 * Menu Loop
	 * 
	 * Tried putting this class in its own method
	 * after over 6 hours of trying to get it to work, I gave up
	 * There is an issue with interacting with a panel with 2 different classes
	 * All the sprites are BROUGHT INTO this class
	 * any sort of rendering outside of this class cases the game not to appear
	 * 
	 */
	
	public void menuLoop() {
		long lastLoopTime = System.currentTimeMillis(); // start a menu timer just cuz
		/**
		 * menu will run until we quit or go into the shop menu
		 */
		
		
		while (Menu.menuRunning) { 
			if(Menu.shop == true) {
				shop();
			}
			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();
			//System.out.print(" menu loop " );
			Graphics2D g2d = (Graphics2D) strategy.getDrawGraphics();
			Image menuBackground = null;	
			URL url = null , url2 = null; 
			//URLConnection con = null;
			//InputStream in = null;
			ImageIcon icon = null;
			try {
				//final URL url3 = new URL("http://pscode.org/media/starzoom-thumb.gif");
				//BufferedImage img = ImageIO.read(this.getClass().getResource("sprites/menu_back.gif"));
				//icon = new ImageIcon(img);
				
				url = this.getClass().getClassLoader().getResource("sprites/menu_back.gif");
				//url2 = this.getClass().getResource("sprites/menu_back.gif");
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
			        //menuBackground = container.getToolkit().createImage(url);
					//menuBackground = new ImageIcon(new URL("https://www.google.com/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwi97peGrezXAhXDQSYKHePMAzoQjRwIBw&url=https%3A%2F%2Ftenor.com%2Fview%2Fheavy-breathing-cat-space-galaxy-meme-gif-7562274&psig=AOvVaw2jNQdZ-RPYdFCMHqrfmctu&ust=1512339261204701")).getImage();

					menuBackground = ImageIO.read(url);
					//System.out.print("Got image ");
				}	
				
			}//end try 
			
			// this doesnt work either
			catch (Exception e1) {
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
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			menuBackground = menuBackground.getScaledInstance(width+60, height+30, Image.SCALE_DEFAULT);
			//JLabel label = new JLabel(icon);
			//container.add(label);
			g2d.drawImage(menuBackground, 0, 0,  null);
			
			// Put Text on screen! 
			g2d.setColor(Color.white);
			Font bigFont = new Font("LucidaSans", Font.BOLD, 36);
			// 1st line
			if(message == "") {
				message = "Click Start Game to Play kill rat = 10 coins" ;
			}
			AttributedString menuPopUp= new AttributedString(message);
			menuPopUp.addAttribute(TextAttribute.FONT, bigFont);
			g2d.drawString(menuPopUp.getIterator(),50,250);

			/**
			 *   Buttons ---------------------
			 */
				
			///////  Button 1 ////////
			Color [] colorArray = new Color[100]; 
			String button1Color =  "#ef3e76";
			String button1Color2 =  "#fcbdf2";
		
			buttonMaker(button1Color, button1Color2, g2d,button1getX, button1getY, button1Pressed, standardButtonWidth, standardButtonHeight, colorArray);
			
			g2d.setColor(Color.WHITE);
			g2d.drawString("Start Game", button1getX + 75, button1getY + 28);
			
			///////  Button 2 ////////
			Color [] colorArray2 = new Color[20]; 
			String button2Color =  "#71edeb";
			String button2Color2 =  "#4d7bcc";
			
			buttonMaker(button2Color, button2Color2,g2d, button2getX, button2getY, button2Pressed, standardButtonWidth, standardButtonHeight, colorArray2);
			
			g2d.setColor(Color.WHITE);
			g2d.drawString("Shop Menu", button2getX + 75, button2getY + 28);
		
			g2d.dispose();
			strategy.show();
			
			// finally pause for a bit. Note: this should run us at about
			// 100 fps but on windows this might vary each loop due to
			// a bad implementation of timer
			try { Thread.sleep(10); } catch (Exception e) {}
			
		}
			
		
	}
	/**
	 * 
	 * Button Maker
	 * 
	 * 
	 */	
	public void buttonMaker(String buttonColor1, String buttonColor2, Graphics2D g2d, int buttonGetX, int buttonGetY, boolean buttonPressed, int standardButtonWidth, int standardButtonHeight, Color [] ColorArray) {
		// outer
		g2d.setColor(Color.decode(buttonColor1));
		g2d.drawRect(buttonGetX-4, buttonGetY-3, standardButtonWidth+8, standardButtonHeight+7);
				
		// inner
		g2d.drawRect(buttonGetX, buttonGetY, standardButtonWidth, standardButtonHeight);	
		if(buttonPressed) {
			g2d.setColor(Color.decode(buttonColor2));
			g2d.fillRect(buttonGetX, buttonGetY, standardButtonWidth, standardButtonHeight); 
		}
		else if(!buttonPressed) {
			g2d.fillRect(buttonGetX-4, buttonGetY-3, standardButtonWidth+8, standardButtonHeight+7); 					
			//Menu.fillColorArray(colorArray2, 0x71edeb, 0x919bd6,20);
	        GradientPaint gp = new GradientPaint(buttonGetX-4, buttonGetY-3, Color.decode(buttonColor1), buttonGetX + standardButtonWidth, buttonGetY + standardButtonHeight, Color.decode(buttonColor2));
	        g2d.setPaint(gp);
	        //g2d.setColor(Color.WHITE);
			g2d.fill(new RoundRectangle2D.Double(buttonGetX, buttonGetY, standardButtonWidth, standardButtonHeight, 10, 10)); 

		}
		g2d.fillRect(buttonGetX, buttonGetY, standardButtonWidth, standardButtonHeight);	
	}
	/**
	 * 
	 * Shop Loop
	 * 
	 * 
	 * 
	 */	
	public void shop() {
		System.out.print("shop");
		while (Menu.shop == true) {
			Graphics2D g2d = (Graphics2D) strategy.getDrawGraphics();
			Image menuBackground = null;	
			URL url = null ;
			try {		
				url = this.getClass().getClassLoader().getResource("sprites/shop.png");
				//url2 = this.getClass().getResource("sprites/menu_back.gif");
				if (url == null) {
					System.out.print("Can't find ref: ");
				}
				else {
					menuBackground = ImageIO.read(url);
				}	
			}//end try 
			catch (Exception e1) {
				e1.printStackTrace();
			}
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			menuBackground = menuBackground.getScaledInstance(width+70, height+30, Image.SCALE_DEFAULT);
			g2d.drawImage(menuBackground, 0, 0,  null);

			try { Thread.sleep(10); } catch (Exception e) {}
			g2d.setColor(Color.WHITE);
			/**
			 * Buttons
			 */	
			// font and text set up
			Font font = new Font("LucidaSans", Font.BOLD, 14);
			Font bigFont = new Font("LucidaSans", Font.BOLD, 36);
			AttributedString title1= new AttributedString("Back to Menu");
			AttributedString title2= new AttributedString("Cougar");
			AttributedString title3= new AttributedString("Grumpy Cat");
			AttributedString title4= new AttributedString("Tiger");
			AttributedString title5= new AttributedString("Pause Time");
			AttributedString title6= new AttributedString("Fire Shot");
			AttributedString title7= new AttributedString("Rapid Fire");
			AttributedString shopTitle= new AttributedString("Upgrades");
	
			title1.addAttribute(TextAttribute.FONT, font); title5.addAttribute(TextAttribute.FONT, font);
			title2.addAttribute(TextAttribute.FONT, font); title6.addAttribute(TextAttribute.FONT, font);
			title3.addAttribute(TextAttribute.FONT, font); title7.addAttribute(TextAttribute.FONT, font);
			title4.addAttribute(TextAttribute.FONT, font); shopTitle.addAttribute(TextAttribute.FONT, bigFont);
			
			///  formating  /////
			g2d.drawString(shopTitle.getIterator(), 380 , 70);
			// exit button
			Color [] colorArray = new Color[100];
			buttonMaker("#1310fb", "#233b7b", g2d,goBackButtonX, goBackButtonY, goBackButtonPressed, shorterButtonWidth, shorterButtonHeight, colorArray);
			g2d.setColor(Color.WHITE);
			g2d.drawString(title1.getIterator(), goBackButtonX + 1, goBackButtonY + 24);
			///////  Button 1 ////////			
			String button1Color =  "#71edeb";
			String button1Color2 =  "#4d7bcc";
		
			buttonMaker(button1Color, button1Color2, g2d,shopButton1X, shopButton1Y, shopButton1Pressed, pictureButton, pictureButton, colorArray);
			g2d.setColor(Color.WHITE);
			g2d.drawString(title2.getIterator(), shopButton1X + 45, shopButton1Y + 20);
			// shows image on button
			g2d.drawImage(  ( SpriteStore.get().getSprite("sprites/cougar_cannon.png",1)).image , shopButton1X + 35, shopButton1Y + 55, null );
			
			///////  grumpy cat button ////////			
			buttonMaker(button1Color, button1Color2,g2d, shopButton2X, shopButton2Y, shopButton2Pressed, pictureButton, pictureButton, colorArray);
			g2d.setColor(Color.WHITE);
			g2d.drawString(title3.getIterator(), shopButton2X + 42, shopButton2Y + 20);
			g2d.drawImage(  ( SpriteStore.get().getSprite("sprites/grumpyCat.png",1)).image , shopButton2X + 35, shopButton2Y + 40, null );

			///////  tiger button  ////////			
			buttonMaker(button1Color, button1Color2,g2d, shopButton3X, shopButton3Y, shopButton3Pressed, pictureButton, pictureButton, colorArray);
			g2d.setColor(Color.WHITE);
			g2d.drawString(title4.getIterator(), shopButton3X + 55, shopButton3Y + 20);
			g2d.drawImage(  ( SpriteStore.get().getSprite("sprites/tiger.png",1)).image , shopButton3X + 40, shopButton3Y + 55, null );
			
			Color [] colorArray2 = new Color[20]; 
			String button2Color =  "#71edeb";
			String button2Color2 =  "#4d7bcc";		
			buttonMaker(button2Color, button2Color2, g2d,shopButton4X, shopButton4Y, shopButton4Pressed, shorterButtonWidth, shorterButtonHeight, colorArray);
			g2d.setColor(Color.WHITE);
			g2d.drawString(title5.getIterator(), shopButton4X + 7, shopButton4Y + 24);
			
			///////  Button 2 ////////			
			buttonMaker(button2Color, button2Color2,g2d, shopButton5X, shopButton5Y, shopButton5Pressed, shorterButtonWidth, shorterButtonHeight, colorArray);
			g2d.setColor(Color.WHITE);
			g2d.drawString(title6.getIterator(), shopButton5X + 17, shopButton5Y + 24);
			
			///////  Button 2 ////////			
			buttonMaker(button1Color, button2Color2,g2d, shopButton6X, shopButton6Y, shopButton6Pressed, shorterButtonWidth, shorterButtonHeight, colorArray);
			g2d.setColor(Color.WHITE);
			g2d.drawString(title7.getIterator(), shopButton6X + 13, shopButton6Y + 24);
			
			
			g2d.dispose();
			strategy.show();
			
			
		}// end loop
	}
	
	
	public void gameSetup() {
		
		initEntities();
		gameRunning = true ;
		gameLoop();
		
	}
	/**
	 * 
	 * Game Loop
	 * 
	 * 
	 * 
	 * 
	 */
	public void gameLoop() {
		long lastLoopTime = System.currentTimeMillis();
		System.out.print("Loop about to begin: " + gameRunning);
		// keep looping round til the game ends
		while (gameRunning) {
			// calc delta as time diff between last frame and this frame
			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();
			if(timePaused) { 
				timeCountDown--;
				System.out.println(timeCountDown);
				if(timeCountDown < 0) {
					timePaused = false; // this also should "release" the button
					timeCountDown = 10; 
				}
			}
			if(superCatMode) { 
				countDown--;
				if(countDown < 0) {
					superCatMode = false; // this also should "release" the button
			        removeEntity(catHead);
					// which cat did we have before?
					if(tiger) { catHead = new catEntity(null,"sprites/tiger.png",400,520); }
					else if(cougar) { catHead = new catEntity(null,"sprites/cougar.png",400,520); }
					else if(grumpyCat) { catHead = new catEntity(null,"sprites/grumpyCat.png",400,520); }
					else { catHead = new catEntity(this,"sprites/catHead1.png",400,525); }
					entities.add(catHead); // add it back
			        firingInterval = 500; 					
					countDown = 50; 
				}
			}
			/**
			 * 
			 * Make background
			 * 
			 */
			Graphics2D g2d	= (Graphics2D) strategy.getDrawGraphics();
			Image sourceImage = null;
			// lets generate the background
			try {
				URL url = this.getClass().getClassLoader().getResource("sprites/back_small.png");
				if (url == null) {
					System.out.print("Can't find ref: ");
				}
				sourceImage = ImageIO.read(url);
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			 
			sourceImage = sourceImage.getScaledInstance(width+60, height+30, Image.SCALE_DEFAULT);
			g2d.drawImage(sourceImage, 0, 0, null);
			//System.out.print("drawing");
			
			// cycle round asking each entity to move itself
			if (!waitingForKeyPress) { // if game has started
				for (int i=0;i<entities.size();i++) {
					Entity entity = (Entity) entities.get(i); // entity object cast of get entities

					Color blue =new Color(0f,0f,1f,0.008f );
					Color red =new Color(1f,1f,1f,0.0001f );
					if(entity.ref == "sprites/rat.PNG" && timePaused) {
						entity.color(entity,blue); // make the rats blue
					}
					else { entity.color(entity,red); entity.move(delta) ; }
					
				}
			}
			
			/**
			 * 
			 * These buttons appear only when abilities are purchased
			 * 
			 */
			Color[] colorArray = null;
			Font font = new Font("LucidaSans", Font.BOLD, 14);
			String button1Color =  "#71edeb";
			String button2Color2 =  "#4d7bcc";
			if(pauseTime==true) {
				buttonMaker(button1Color, button2Color2, g2d,shopButton4X-30, shopButton4Y, timePaused, shorterButtonWidth, shorterButtonHeight, colorArray);
				g2d.setColor(Color.WHITE);
				AttributedString title5= new AttributedString("Pause Time");
				title5.addAttribute(TextAttribute.FONT, font);
				g2d.drawString(title5.getIterator(), shopButton4X -23, shopButton4Y + 24);
			}
			///////  Rapid fire button  ////////	
			if(rapidFire == true) {
				buttonMaker(button1Color, button2Color2,g2d, shopButton6X+40, shopButton6Y, superCatMode, shorterButtonWidth, shorterButtonHeight, colorArray);
				g2d.setColor(Color.WHITE);
				AttributedString title7= new AttributedString("Rapid Fire");
				title7.addAttribute(TextAttribute.FONT, font);
				g2d.drawString(title7.getIterator(), shopButton6X + 50, shopButton6Y + 24);
			}
		
			// cycle round drawing all the entities we have in the game
			for (int i=0;i<entities.size();i++) {
				boolean rat = false;
				Entity entity = (Entity) entities.get(i);
				if(entity.ref == "sprites/rat.PNG") {
					rat = true; 
				}
				entity.draw(g2d,catHead.sprite,rat);
			}
			
			// brute force collisions, compare every entity against
			// every other entity. If any of them collide notify 
			// both entities that the collision has occured
			boolean rats = false, collisionOccured = false; // boolean to check for rat on rat collisions
			if(gameRunning == true) {
			for (int p=0;p<entities.size();p++) {
				for (int s=p+1;s<entities.size();s++) {
					Entity me = (Entity) entities.get(p);
					Entity him = (Entity) entities.get(s);
					
					if(me.ref == "sprites/rat.PNG" && him.ref == "sprites/rat.PNG" ) {
						//System.out.print("rat detected");
						rats = true;
					}
					if(!rats) { // rats?
						collisionOccured = me.collidesWith(him); // collide - Entity class
						if(collisionOccured) { // collision = yes, rats = no
							me.collidedWith(him); //  shot entity class
							him.collidedWith(me);
						}
					}
					rats = false; // we can look for rats next loop
				} // end of inner loop
			} // outer loop
			}
			
			// remove any entity that has been marked for clear up
			entities.removeAll(removeList);
			removeList.clear();

			/** 
			 *  Game logic
			**/
			if (logicRequiredThisLoop) {
				for (int i=0;i<entities.size();i++) {
					Entity entity = (Entity) entities.get(i);
					entity.doLogic();
				}
				
				logicRequiredThisLoop = false;
			}
			
			// if we're waiting for an "any key" press then draw the 
			// current message 
			if (waitingForKeyPress) {
				// set font and color
				g2d.setColor(Color.white);
				Font bigFont = new Font("LucidaSans", Font.BOLD, 40);
				// 1st line
				if(message == "") {
					message = "Paused Game" ;
				}
				AttributedString menuPopUp= new AttributedString(message);
				menuPopUp.addAttribute(TextAttribute.FONT, bigFont);
				g2d.drawString(menuPopUp.getIterator(),200,250);
				// 2nd line
				AttributedString anyKey= new AttributedString("Press Space Bar to Begin!");
				anyKey.addAttribute(TextAttribute.FONT, bigFont);
				g2d.drawString(anyKey.getIterator(),180,320);
			}
			
			// finally, we've completed drawing so clear up the graphics
			// and flip the buffer over
			g2d.dispose();
			strategy.show(); 	
			
			//Graphics2D shotgraphic = (Graphics2D)g2d.create();
			// rotate sprite
			if ((leftPressed) && (!rightPressed)) {
				shot.rotate(-rotateSpeed,g2d, catHead);
				g2d.dispose();
				catHead.rotate(-rotateSpeed,g2d, catHead); // it calls rotate method in Entity class with -10 for each key pressed
			} else if ((rightPressed) && (!leftPressed)) {
				shot.rotate(rotateSpeed,g2d,catHead);
				g2d.dispose();
				catHead.rotate(rotateSpeed,g2d,catHead);
			}	
			
			// if we're pressing fire, attempt to fire
			if (firePressed) {
				tryToFire();
			}
		
			// finally pause for a bit. Note: this should run us at about
			// 100 fps but on windows this might vary each loop due to
			// a bad implementation of timer
			try { Thread.sleep(10); } 
			catch (Exception e) {
				
			}
		}
	} // end of game loop
	
	/**
	 * A class to handle keyboard input from the user. The class
	 * handles both dynamic input during game play, i.e. left/right 
	 * and shoot, and more static type input (i.e. press any key to
	 * continue)
	 * 
	 * This has been implemented as an inner class more through 
	 * habbit then anything else. Its perfectly normal to implement
	 * this as seperate class if slight less convienient.
	 * 
	 */
	private class KeyInputHandler extends KeyAdapter {
		/** The number of key presses we've had while waiting for an "any key" press */
		private int pressCount = 1;
		
		/**
		 * Notification from AWT that a key has been pressed. Note that
		 * a key being pressed is equal to being pushed down but *NOT*
		 * released. Thats where keyTyped() comes in.
		 *
		 */
		public void keyPressed(KeyEvent e) {
			// if we're waiting for an "any key" typed then we don't 
			// want to do anything with just a "press"
			if (waitingForKeyPress) {
				return;
			}
			
			
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				firePressed = true;
			}
			
			
		} 
		/**
		 * Notification that a key has been released.
		 *
		 */
		public void keyReleased(KeyEvent e) {
			// if we're waiting for an "any key" typed then we don't 
			// want to do anything with just a "released"
			if (waitingForKeyPress) {
				return;
			}
			
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				firePressed = false;
			}
		}
		/**
		 * Key Typed Triggers can be found here
		 */		
		public void keyTyped(KeyEvent e) {
			// waiting on any key to be pressed
			char keyChar = e.getKeyChar();
	        if (keyChar == 'd') {
	            System.out.println("You typed 'd'");
	            daveSmall = 1; 
	        }
	        if (keyChar == 'a') {
	            System.out.println("You typed 'a'");
	            if(daveSmall == 1)
	            	daveSmall = 2; 
	        }
	        if (keyChar == 'v') {
	            System.out.println("You typed 'v'");
	            if(daveSmall == 2)
	            	daveSmall = 3; 
	        }
	        if (keyChar == 'e') {
	            System.out.println("You typed 'Dave' you win");
	            if(daveSmall == 3) {
	            	daveSmall = 0;
	            	coins = 10000; // cheat CODESSSS
	            	notifyWin(); 
	            }
	        }
			if (waitingForKeyPress) {
				if (pressCount == 1) {
					// since we've now recieved our key typed
					// event we can mark it as such and start 
					// our new game
					waitingForKeyPress = false;
					startGame();
					pressCount = 0;
				} else {
					pressCount++;
				}
			} // 
			
			// if we hit escape, then quit the game
			if (e.getKeyChar() == 27) {
				System.exit(0);
			}
		}
	}
	
	/**
	 * How game starts. 
	 * @throws MalformedURLException 
	 */
	public static void main(String argv[]) throws MalformedURLException  {
		
		container = new JFrame();


		Game g =new Game();
		
		SwingUtilities.invokeLater(new Runnable() { // invoke later method
			public void run() {

				System.out.println("thread");
				container.setTitle( "Feline Defense!" );
				container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit on close
				container.setVisible(true); // set label to be seen by user
			}
	} );
		while(true) {
		if(Menu.menuRunning == true) {
			g.menuLoop();
		}
		System.out.println("running game");
		g.gameSetup();
		try { Thread.sleep(10); } 
		catch (Exception e) {}
		}
	}
	

}
