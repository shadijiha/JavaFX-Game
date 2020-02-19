/**
 * All initializations happen here
 */

package shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Game {

	public static void initialize() {
		// Init shapes
		var rect = new Shado.Circle(300, 300, 100);

		var rect2 = new Shado.Rectangle(600, 300, 50, 50).setFill(Color.BLUE);

		Shado.Text txt = new Shado.Text("Hello :D", 500, 500).setFontSize(32);
	}

	public static void render(GraphicsContext gc) {

	}
}
