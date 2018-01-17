
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * A sprite to be displayed on the screen. Note that a sprite
 * contains no state information, i.e. its just the image and 
 * not the location. This allows us to use a single sprite in
 * lots of different places without having to store multiple 
 * copies of the image.
 *  */



public class Sprite {
	
	protected double angle;
	protected double catAngle =360 ; 
	double shotAngle; 
	public boolean angleSet; 
	/** The image to be drawn for this sprite */
	BufferedImage image;
	/**
	 * Create a new sprite based on an image
	 * 
	 * @param image The image that is this sprite
	 */
	// constructor
	public Sprite(BufferedImage image) {
		this.image = image;
		this.angleSet = false; 
		//this.angle = angle; 
	}
	
	/**
	 * Get the width and height of the drawn sprite
	 */
	public int getWidth() {
		return image.getWidth(null);
	}

	public int getHeight() {
		return image.getHeight(null);
	}
	
	public void rotateSprite(double delta,int x, int y, Image catImg) {
		// method called on key press
		this.catAngle += delta;
		//this.shotAngle+=delta;
	}	
	public void rotateShot(double angle) {
		// method called on key press
		this.shotAngle = angle;
		//this.shotAngle+=delta;
}	
	public void render(Graphics g,int x,int y,Sprite catHead)
	{
	    Graphics2D g2d = (Graphics2D)g.create();
	    g2d.rotate(Math.toRadians(this.catAngle),x+catHead.getWidth()/3,y + catHead.getHeight()/2);
	    g.drawImage(image, x, y, null);
	    g2d.rotate(Math.toRadians(-this.catAngle),x+catHead.getWidth()/3,y + catHead.getHeight()/2);
	    g2d.dispose();
	}
	
	public void draw(Graphics2D g,int x,int y, Sprite catHead, boolean isRat) {
		Graphics2D shotgraphic = (Graphics2D)g.create();
		Graphics2D g2d = (Graphics2D)g.create();
		if(!isRat) {
	    	if(this.catAngle>400) {
	    		this.catAngle = 400; 
	    	}
	    	if(this.catAngle<320) {
	    		this.catAngle = 320;
	    	}
	    	if(y>499) {
	    		shotgraphic.rotate(Math.toRadians(this.catAngle),x+catHead.getWidth()/3,y + catHead.getHeight()/2); 
	    		shotgraphic.drawImage(image, x, y, null);
	    		shotgraphic.rotate(Math.toRadians(-this.catAngle),x+catHead.getWidth()/3,y + catHead.getHeight()/2);
	    		catHead.shotAngle = this.catAngle; // gonna grab the angle i need and give it to shot		    			    		
	    		shotgraphic.dispose(); 
		    	//if(catHead.angleSet == false && this.catAngle != 360) {
		    		//System.out.println(" angle set "+this.catAngle);
		    		catHead.angleSet = true;
		    	//}
		    	//System.out.println(this.catAngle);
	    	}
	    	else { // this should ONLY be shots 
	    		//System.out.println(catHead.shotAngle);
	    		g2d.rotate(Math.toRadians(this.shotAngle),x+catHead.getWidth()/3,y + catHead.getHeight()/2); 
	    		g2d.drawImage(image, x, y, null);	    	
	    		g2d.rotate(Math.toRadians(-this.shotAngle),x+catHead.getWidth()/3,y + catHead.getHeight()/2); 		    	
	    		g2d.dispose();
	    	} // end else
	    	
		}// end rat check
		else {
			//System.out.println("rats cant rotate");
	    	g.drawImage(image, x, y, null);	    				
		}
	}
}