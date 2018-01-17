
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * An entity represents any element that appears in the game.
 */
public abstract class Entity {
	
	/** Class variables */ 
	
	protected double x;
	// The current y location of this entity */
	protected double y;
	// The sprite that represents this entity */
	protected Sprite sprite;
	// The current speed of this entity horizontally (pixels/sec) */
	protected double dx;
	// The current speed of this entity vertically (pixels/sec) */
	protected double dy;
	protected double delta; // rotation variable
	
	/** The rectangle used for this entity during collisions  resolution */
	private Rectangle me = new Rectangle();
	/** The rectangle used for other entities during collision resolution */
	private Rectangle him = new Rectangle();
	public String ref; 
	/**
	 * Construct a entity based on a sprite image and a location.
	 */
	public Entity(String ref,int x,int y, int cat) {
		this.sprite = SpriteStore.get().getSprite(ref,cat); // the spite object associated with this entity is found in sprite store
		this.x = x;
		this.y = y;
		this.ref = ref; 
	}
	
	/**
	 * Request that this entity move itself based on a certain ammount
	 * of time passing.
	 */
	public void move(long delta) {
		// update the location of the entity based on move speeds
		x += (delta * dx) / 1000;
		y += (delta * dy) / 1000;
	}
	
	// when this is called, it changes the entity dx variable
	public void setHorizontalMovement(double dx) {
		this.dx = dx;
	}
	
	public void rotate(double delta, Graphics2D g, Entity catHead) {

		sprite.rotateSprite(delta,(int)x,(int)y,catHead.sprite.image);

	}

	public void setVerticalMovement(double dy) {
		this.dy = dy;
	}
	public void color(Entity rat,Color color) {
		BufferedImage img = rat.sprite.image; 
		Graphics2D g = (img).createGraphics();
        g.drawImage(rat.sprite.image, 0,0, null);
        g.setComposite(AlphaComposite.SrcAtop);
        g.setColor(color);
        g.fillRect(0,0,img.getWidth(),img.getHeight());
        g.dispose();
	}
	public double getHorizontalMovement() {
		return dx;
	}

	public double getVerticalMovement() {
		return dy;
	}
	
	/**
	 * Draw this entity to the graphics context provided
	 */
	public void draw(Graphics2D g, Sprite catHead, boolean isRat) {
		sprite.draw(g,(int) x,(int) y, catHead, isRat);
	}
	
	/**
	 * Do the logic associated with this entity. This method
	 * will be called periodically based on game events
	 */
	public void doLogic() {
		
	}
	
	/**
	 * Get the x location of this entity
	 */
	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}
	
	/**
	 * Check if this entity collised with another.
	 */
	public boolean collidesWith(Entity other) {
		boolean HIT = false ;
		if(y>100 && y<500) {
			//System.out.println(y);
		}
		me.setBounds((int) x,(int) y,sprite.getWidth(),sprite.getHeight()); // me rect
		him.setBounds((int) other.x,(int) other.y,other.sprite.getWidth(),other.sprite.getHeight()); // other rect
		if(me.intersects(him)) {
			//System.out.println("HIT");
			HIT = true; 
		}
		HIT = false; 
		return me.intersects(him);
	}
	
	/**
	 * Notification that this entity collided with another.
	 */
	public abstract void collidedWith(Entity other);
}