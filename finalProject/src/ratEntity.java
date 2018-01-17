import java.util.Random;


public class ratEntity extends Entity {
	// The speed at which the ratt moves horizontally */
	private double moveSpeed = 60;
	// The game in which the entity exists */
	private Game game;
	Random rand = new Random(); 
	int individualize = 0;
	int count = 0;
	public ratEntity(Game game,String ref,int x,int y) {
		super(ref,x,y,0);
		individualize = 0; // this variable will cause rats to change paths at different s
		count = 50;
		this.game = game;
		dx = -moveSpeed;
		dy = Game.level; 
	}

	public void move(long delta) {
		
		 count++ ;
		int stepsUntilTurn = rand.nextInt(100)+60;  // this can fluctuate how often they change paths
		if(count > stepsUntilTurn) {
			dx = rand.nextInt( (75 * ((Game.level+1)/2) ) ) ; // speed increases per level
				y+=1; 
				x += (rand.nextInt(2))-1; 
			count = 0; 
			game.updateLogic();
		}
		if ((dx < 0) && (x < 10) || (dx < 0) && ( (320-x) > ((570-y)) )) {
			//System.out.println("left boundary hit");
			x+=6; 
			game.updateLogic();
		}
		//dy+= rand.nextDouble(); 
		// and vice vesa, if we have reached the right hand side of 
		// RIGHT side
		/**
		 * y = mx + b for slope of the rails
		 */
		if ((dx > 0) && (x > 700) || (dx > 0) && ( (x-500) > ( 1.1* (570-y)) )  ) {
			//System.out.println("right boundary hit");
			x-=5; 
			game.updateLogic();
		}
		
		// proceed with normal move
		super.move(delta);
	}
	
	/**
	 * Update the game logic related to rats
	 */
	public void doLogic() {
		// swap over horizontal movement and move down the
		// screen a bit
		if(individualize < 1) { // if a few rats havent changed direction, this one can
			dx = -dx;
			individualize = 3; 
		}
		individualize--; 
		// if we've reached the bottom of the screen then the player
		// dies
		if (y > 530) {
			game.notifyDeath();
		}
	}
	
	/**
	 * Notification that this rat has collided with another entity
	 * 
	 * @param other The other entity
	 */
	public void collidedWith(Entity other) {
		// collisions with rats are handled elsewhere
	}
}