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

public abstract class GameObject {
	protected String name;
	protected int id;
	protected boolean hidden;
	protected Vertex position;
	protected Vector velocity;
	protected Dimension dimensions;
	protected Shado.Shape shape;

	public static List<GameObject> allGameObject = new ArrayList<GameObject>();

	protected GameObject(String name) {
		this.name = name;
		this.id = (int) (Math.random() * Integer.MAX_VALUE);
		this.hidden = false;
		this.position = new Vertex(0, 0);
		this.velocity = new Vector(0, 0);
		this.dimensions = new Dimension(0, 0);
		this.shape = new Shado.Rectangle(position, dimensions);

		allGameObject.add(this);
	}

	// Core
	public abstract void draw(GraphicsContext g);

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
