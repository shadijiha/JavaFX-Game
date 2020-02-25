package game;

import shadoMath.Vertex;

public interface Mouse {
	public static final Vertex position = new Vertex(0, 0);
	public static final Vertex lastClick = new Vertex(-1000, -1000);

	public static void setX(double x) {
		position.x = x;
	}

	public static void setY(double y) {
		position.y = y;
	}

	public static void setLastClick(double x, double y) {
		lastClick.x = x;
		lastClick.y = y;
	}

	public static Vertex getLastClick() {
		return lastClick;
	}

	public static void resetLastClicked() {
		lastClick.x = -1000;
		lastClick.y = -1000;
	}

	public static double getX() {
		return position.x;
	}

	public static double getY() {
		return position.y;
	}
}
