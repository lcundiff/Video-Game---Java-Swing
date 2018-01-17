
/**
 * The entity that represents the players catHead
 */
public class catEntity extends Entity {
	/** The game in which the catHead exists */
	private Game game;
	
	/**
	 * Create a new entity to represent the players catHead
	 */
	public catEntity(Game gameTest,String ref,int x,int y) {
		super(ref,x,y-10,1);
		this.game = gameTest;
	}
	
	/**
	 * Request that the catHead move itself based on an elapsed ammount of
	 * time
	 */
	
	public void move(long delta) {
		// if we're moving left and have reached the left hand side
		// of the screen, don't move
		if ((dx < 0) && (x < 10)) {
			return;
		}
		// if we're moving right and have reached the right hand side
		// of the screen, don't move
		if ((dx > 0) && (x > 750)) {
			return;
		}
		
		super.move(delta);
	}
	
	
	/**
	 * Notification that the player's catHead has collided with something
	 */
	public void collidedWith(Entity other) {
		// if its an rat, notify the game that the player
		// is dead
		if (other instanceof ratEntity) {
			game.notifyDeath();
		}
	}
}