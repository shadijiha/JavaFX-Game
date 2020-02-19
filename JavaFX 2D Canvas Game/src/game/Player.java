/**
 *
 */

package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Player extends GameObject {

	public Player(int x, int y) {
		super("player");
		position.x = x;
		position.y = y;
		dimensions.width = 50;
		dimensions.height = 150;
		shape.setPosition(position);
		shape.setDimensions(dimensions);
		shape.setFill(Color.PINK);
	}

	@Override
	public void draw(GraphicsContext g) {

	}
}
