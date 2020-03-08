/**
 * Bullet class
 */

package game;

import javafx.scene.paint.Color;
import shadoMath.Vertex;
import shapes.Dimension;

public class Bullet extends GameObject {

	private Player source;
	private Vertex initial_position;

	public Bullet(Player source) {
		super("bullet");
		this.source = source;
		this.position = new Vertex(source.position.x + source.dimensions.width, source.position.y + source.dimensions.height / 2);
		this.initial_position = new Vertex(this.position);
		this.shape = this.shape.setPosition(
				this.position
		);
		this.dimensions = new Dimension(30, 10);
		this.shape.setDimensions(this.dimensions);
		this.shape.setFill(Color.ORANGE);

		this.velocity = source.bulletVelocity;
	}

	/**
	 * Updates the position of the bullet depending on its velocity
	 */
	public void update() {
		position.x += velocity.x;
		position.y += velocity.y;
		shape.move(velocity);

		if (texture != null)
			texture.move(velocity);
	}

	/**
	 * @return Returns the position from where the bullet was shot
	 */
	public Vertex getInitialPosition() {
		return this.initial_position;
	}
}
