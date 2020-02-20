/**
 *
 */

package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject {

	public static List<Player> allPlayers = new ArrayList<Player>();

	private float maxHp = 1000.0f;
	private float hp = 1000.0f;
	private float ad = 60.0f;
	private float armor = 23.0f;

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
		this.force();

		// Detect collisions with platform
		Platform.allPlatforms.parallelStream()
				.forEachOrdered(e -> {
					if (e.collides(this)) {
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

	private void force() {
		this.velocity.y += this.gravity * Time.deltaTime / 10;
		this.position.y += this.velocity.y * Time.deltaTime / 10;

		if (this.position.y < 0) {
			this.position.y = 0;
			this.velocity.y = 0;
		}
		this.shape.setPosition(position);
	}

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

	public int getMS() {
		return ms;
	}

	@Override
	public void draw(GraphicsContext g) {
		shape.draw(g);
	}
}
