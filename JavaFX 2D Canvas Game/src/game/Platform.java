/**
 *
 */
package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import shadoMath.Vertex;
import shapes.Dimension;

import java.util.ArrayList;
import java.util.List;

public class Platform extends GameObject {

	private boolean immobile = false;

	public static List<Platform> allPlatforms = new ArrayList<Platform>();

	public Platform(float x, float y, float w, float h) {
		super("platform");
		position.x = x;
		position.y = y;
		dimensions.width = w;
		dimensions.height = h;
		shape.setPosition(new Vertex(x, y));
		shape.setDimensions(new Dimension(w, h));
		allPlatforms.add(this);
	}

	public Platform(Vertex v, Dimension d) {
		this((float) v.x, (float) v.y, d.width, d.height);
	}

	/**
	 * Changes the color of the platform
	 * @param c The new Color
	 */
	public void setFill(Color c) {
		shape.setFill(c);
	}

	/**
	 * Changes the color of the platform
	 * @param c The new Color
	 */
	public void setStroke(Color c) {
		shape.setStroke(c);
	}

	public void flagAsImmobile() {
		immobile = true;
	}

	/**
	 * Checks if a player object is colliding with the calling platform
	 * @param p The player
	 * @return Returns true if it is colliding
	 */
	public boolean collides(Player p) {
		return shape.collides(p.getShape());
	}

	@Override
	public void draw(GraphicsContext g) {
		shape.draw(g);
	}

	// Getters
	public boolean isImmobile() {
		return immobile;
	}
}
