/**
 * All game object are derived from this class
 */

package game;

import javafx.scene.canvas.GraphicsContext;
import shadoMath.Vector;
import shadoMath.Vertex;
import shapes.Dimension;
import shapes.Shado;

import java.util.ArrayList;
import java.util.List;

public abstract class GameObject extends Shado.EventListener<GameObject> {
	protected String name;
	protected int id;
	protected boolean hidden;
	protected Vertex position;
	protected Vector velocity;
	protected Dimension dimensions;
	protected Shado.Shape shape;

	protected String texture_path;
	protected Shado.Image texture;


	public static List<GameObject> allGameObject = new ArrayList<GameObject>();

	protected GameObject(String name) {
		this.name = name;
		this.id = (int) (Math.random() * Integer.MAX_VALUE);
		this.hidden = false;
		this.position = new Vertex(0, 0);
		this.velocity = new Vector(0, 0);
		this.dimensions = new Dimension(0, 0);
		this.shape = new Shado.Rectangle(position, dimensions);
		this.texture = null;

		allGameObject.add(this);
	}

	// Core
	public void draw(GraphicsContext g) {
		// Draw texture if it exists
		if (texture != null)
			texture.draw(g);
		else
			shape.draw(g);

		// Handle click event
		if (clickEvent != null) {
			if (shape.collides(Mouse.getLastClick().x, Mouse.getLastClick().y, 2, 2)) {
				clickEvent.accept(this);
				Mouse.resetLastClicked();
			}
		}

		// Handle Hover event
		if (hoverEvent != null) {
			if (shape.collides(Mouse.getX(), Mouse.getY(), 2, 2)) {
				hoverEvent.accept(this);
				outEventConsumed = false;
			}
		}

		// Handle mouse out event
		if (mouseOutEvent != null) {
			if (!shape.collides(Mouse.getX(), Mouse.getY(), 2, 2) && !outEventConsumed) {
				mouseOutEvent.accept(this);
				outEventConsumed = true;
			}
		}
	}

	/**
	 * Moves the object depending on its velocity
	 */
	public void move() {
		position.x += velocity.x;
		position.y += velocity.y;
		shape.move(velocity);
	}

	/**
	 * Moves an object by a certain X and Y
	 *
	 * @param x The x offset to add
	 * @param y The y offset to add
	 */
	public void move(double x, double y) {
		position.x += x;
		position.y += y;
		shape.move(new Vector(x, y));
	}

	// Setters

	/**
	 * Flags the object to not render
	 */
	public void hide() {
		hidden = true;
	}

	/**
	 * Flags the object to render
	 */
	public void show() {
		hidden = false;
	}

	/**
	 * Changes the velocity of the object
	 *
	 * @param vel The new velocity
	 */
	public void setVelocity(Vector vel) {
		this.velocity = vel;
	}

	/**
	 * Changes the position of the object
	 *
	 * @param v The new position
	 */
	public void setPosition(Vertex v) {
		position.x = v.x;
		position.y = v.y;
	}

	/**
	 * Changes the dimension of the object
	 *
	 * @param d The new dimensions
	 */
	public void setDimensions(Dimension d) {
		dimensions.width = d.width;
		dimensions.height = d.height;
	}

	public void setTexture(String path) {
		texture = new Shado.Image(path, (float) position.x, (float) position.y, dimensions.width, dimensions.height);
		texture_path = path;
	}

	// Getters

	/**
	 * @return Returns the ID of the calling object
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return Returns the name of the calling object
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Returns if the object should be rendered or not
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * @return Returns the position of the object
	 */
	public Vertex getPosition() {
		return position;
	}

	/**
	 * @return Returns the velocity of the object
	 */
	public Vector getVelocity() {
		return velocity;
	}

	/**
	 * @return Returns the dimension of the object
	 */
	public Dimension getDimensions() {
		return dimensions;
	}

	/**
	 * @return Returns the shape of the object
	 */
	public Shado.Shape getShape() {
		return this.shape;
	}
}
