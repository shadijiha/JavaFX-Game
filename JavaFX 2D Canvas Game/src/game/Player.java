/**
 *
 */

package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import shapes.Shado;

import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject {

	public static List<Player> allPlayers = new ArrayList<Player>();

	private List<Bullet> bullets = new ArrayList<>();

	private float maxHp = 1000.0f;
	private float hp = 1000.0f;
	private float ad = 60.0f;
	private float armor = 23.0f;
	private int range = 500;

	private double gravity = 0.1;
	private double lift = -5.0;
	private int ms = 10;


	public Player(int x, int y) {
		super("player");
		position.x = x;
		position.y = y;
		dimensions.width = 50;
		dimensions.height = 150;
		shape.setPosition(position);
		shape.setDimensions(dimensions);
		shape.setFill(Color.PINK);
		allPlayers.add(this);
	}

	public Player() {
		this(0, 0);
	}

	/**
	 * Applies gravity on the calling Player
	 */
	public void update() {

		// Apply gravity
		this.force();

		// Detect collisions with platform
		Platform.allPlatforms.parallelStream()
				.forEachOrdered(e -> {
					if (e.collides(this)) {
						// Put the object on the platform
						if (this.position.y < e.position.y) {
							this.position.y = e.position.y - this.dimensions.height;
							this.velocity.y = 0; // Setting velocity to 0 to prevent gravity accumulation
						} else {
							this.position.y = e.position.y + e.dimensions.height;
							this.force();
						}
						this.velocity.y = 0;
					}
				});
	}

	/**
	 * Applies gravity the object
	 */
	private void force() {
		this.velocity.y += this.gravity * Time.deltaTime / 10;
		this.position.y += this.velocity.y * Time.deltaTime / 10;

		if (this.position.y < 0) {
			this.position.y = 0;
			this.velocity.y = 0;
		}
		this.shape.setPosition(position);
	}

	/**
	 * Adds velocity the player for it to jump
	 */
	public void jump() {
//		if (
//				this.energy > this.jumpCost &&
//						this.hp > 0 &&
//						!this.stunned &&
//						!this.rooted
//		) {
		this.velocity.y += this.lift;
		//this.energy -= this.jumpCost;
		//}
	}

	/**
	 * Shoots a bullet
	 */
	public void shoot() {
		bullets.add(new Bullet(this));
	}

	/**
	 * Damages the calling object based on the source AD and calling object armor
	 * @param source The damage source
	 * @return Returns the amount substracted from the calling object health
	 */
	public float damage(Player source) {
		float reduction = armor / (100 + armor);
		float temp = source.ad * (1 - reduction);
		hp -= temp;
		return temp;
	}

	/**
	 * Draw the health bar of the calling object
	 * @param g The GraphicsContext
	 */
	public void drawHealthBar(GraphicsContext g) {
		double percentage = hp / maxHp;

		var main_bar = new Shado.Rectangle((float) position.x, (float) position.y - 30, dimensions.width, 10);
		var sec_bar = new Shado.Rectangle((float) main_bar.getPosition().x, (float) main_bar.getPosition().y, (float) (main_bar.getDimensions().width * percentage), (float) main_bar.getDimensions().height);

		main_bar.setFill(Color.WHITE).draw(g);
		sec_bar.setFill(Color.GREEN).draw(g);
	}

	// Getters

	/**
	 * @return Returns the range of the object
	 */
	public int getRange() {
		return range;
	}

	/**
	 * @return Returns the mouvement speed of the player object
	 */
	public int getMS() {
		return ms;
	}

	/**
	 * @return Returns if the object has <= 0 hp
	 */
	public boolean isDead() {
		return hp <= 0;
	}

	/**
	 * @return Returns the list of bullets shot by the object
	 */
	public List<Bullet> getAllBullets() {
		return bullets;
	}

	/**
	 * Draws the object to the screen
	 * @param g The GraphicsContext
	 */
	@Override
	public void draw(GraphicsContext g) {
		shape.draw(g);
	}
}
