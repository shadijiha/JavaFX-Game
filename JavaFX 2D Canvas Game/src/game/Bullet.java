/**
 * Bullet class
 */

package game;

import javafx.scene.paint.Color;
import shadoMath.Vector;
import shadoMath.Vertex;
import shapes.Dimension;

public class Bullet extends GameObject {

	private Player source;
	private boolean active = true;

	public Bullet(Player source) {
		super("bullet");
		this.source = source;
		this.position = new Vertex(source.position.x + source.dimensions.width, source.position.y + source.dimensions.height / 2);
		this.shape = this.shape.setPosition(
				this.position
		);
		this.dimensions = new Dimension(30, 10);
		this.shape.setDimensions(this.dimensions);
		this.shape.setFill(Color.ORANGE);

		this.velocity.x = 2;
		this.velocity.y = 0;
	}

	/**
	 * Updates the position of the bullet depending on its velocity
	 */
	public void update() {
		position.x += velocity.x;
		position.y += velocity.y;
		shape.move(new Vector(velocity));
	}

	/**
	 * @return Returns if the calling bullet is active (can damage object) or no
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Set the active stat of the bullet (So it can damage other objects or no)
	 *
	 * @param a The activity state
	 */
	public void setActivityTo(boolean a) {
		active = a;
	}
}
