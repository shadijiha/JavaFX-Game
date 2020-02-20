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

	public void move() {
		position.x += velocity.x;
		position.y += velocity.y;
		shape.move(velocity);
	}

	public void move(double x, double y) {
		position.x += x;
		position.y += y;
		shape.move(new Vector(x, y));
	}

	// Setters
	public void hide() {
		hidden = true;
	}

	public void show() {
		hidden = false;
	}

	public void setVelocity(Vector vel) {
		this.velocity = vel;
	}

	public void setPosition(Vertex v) {
		position.x = v.x;
		position.y = v.y;
	}

	public void setDimensions(Dimension d) {
		dimensions.width = d.width;
		dimensions.height = d.height;
	}

	// Getters
	public int getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public boolean isHidden() {
		return hidden;
	}

	public Vertex getPosition() {
		return position;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public Dimension getDimensions() {
		return dimensions;
	}

	public Shado.Shape getShape() {
		return this.shape;
	}

}