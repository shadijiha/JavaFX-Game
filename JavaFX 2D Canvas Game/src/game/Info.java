/**
 *
 */

package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import shadoMath.Vector;
import shadoMath.Vertex;
import shapes.Shado;

import java.util.ArrayList;
import java.util.List;

public class Info extends GameObject {

	public static final List<Info> allInfos = new ArrayList<>();
	private static final int MAX_DISTANCE = 150;

	private Shado.Text text;
	private Shado.Image image;

	private Vertex initial_position;

	public Info(String text, Vertex pos, Vector vel, Color col, String img) {
		super("info");
		this.position = new Vertex(pos);

		// Compute velocity
		this.velocity = new Vector(vel);

		this.image = new Shado.Image(img, pos.x, pos.y, 30, 30);
		this.text = new Shado.Text(text, image.getPosition().x + image.getDimensions().width, image.getPosition().y + image.getDimensions().height * 0.75, new Font(25), col, col);

		this.initial_position = new Vertex(pos);

		allInfos.add(this);
	}

	public Info(String text, Vertex pos, Vector vel, Color col) {
		this(text, pos, vel, col, null);
	}

	public void update() {
		this.position.x += this.velocity.x * Timer.deltaTime / 15;
		this.position.y += this.velocity.y * Timer.deltaTime / 15;

		// Update image and text position
		image.setPosition(position);
		text.setPosition(new Vertex(image.getPosition().x + image.getDimensions().width, image.getPosition().y + image.getDimensions().height * 0.75));
	}

	public void draw(GraphicsContext g) {

		// Remove this if the distance traveled is greater than 300 px
		double eval_distance = initial_position.getDistance(position);
		if (eval_distance > MAX_DISTANCE) {
			this.delete();
			return;
		}

		// Update position of info
		this.update();

		// Update alpha
		g.setGlobalAlpha(1 - eval_distance / MAX_DISTANCE);

		// Draw the image and the text
		image.draw(g);
		text.draw(g);

		// Reset alpha
		g.setGlobalAlpha(1);

		// Handle Events
		super.draw(g);
	}
}
