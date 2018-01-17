import java.awt.Graphics2D;

/**
 * An entity representing a shot fired by the player's ship
 */
public class ShotEntity extends Entity {
	// The vertical speed at which the players shot moves */
	private double moveSpeed = -300;
	// The game in which this entity exists */
	private Game game;
	// True if this shot has been "used", i.e. its hit something */
	private boolean used = false;
	//private double angle; 
	final double angle = sprite.catAngle;
	boolean fired = false;
	/**
	 * Create a new shot from the player
	 */
	public ShotEntity(Game game,String stringSprite,int x,int y) {
		super(stringSprite,x,y,0);
		this.game = game;
		dy = Math.sin(Math.toRadians(angle + 90)) * moveSpeed;
		dx = Math.cos(Math.toRadians(angle + 90)) * moveSpeed;
//		/System.out.println(" angle: " + sprite.shotAngle);
		
	}
	
	/**
	 * Request that this shot moved based on time elapsed
	 */
	public void rotate(double delta, Graphics2D g, Entity catHead) {
		super.rotate(delta, g, catHead);
	}
	public void rotateShot(Entity bullet) {
		sprite.rotateShot(angle); 
	}
	
	public void move(long delta) {
		// proceed with normal move
		super.move(delta);
		//System.out.println(y);
		// if we shot off the screen, remove 
		if (y < -100) {
			game.removeEntity(this);
		}
	}
	
	/**
	 * Notification that this shot has collided with another
	 * entity
	 */
	public void collidedWith(Entity other) {
		// prevents double kills, if we've already hit something,
		// don't collide
		if (used) {
			return;
		}
		// if we've hit an rat, kill it!
		if (other instanceof ratEntity) {
			// remove the affected entities
			game.removeEntity(this);
			game.removeEntity(other);
			game.coins+=10; 
			// notify the game that the rat has been killed
			game.notifyratKilled();
			used = true;
		}
	}
}