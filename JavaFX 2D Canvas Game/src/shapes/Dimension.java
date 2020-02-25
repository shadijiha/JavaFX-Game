/**
 *
 */

package shapes;

public class Dimension implements Cloneable {
	public double width;
	public double height;

	public Dimension(double w, double h) {
		width = w;
		height = h;
	}

	public Dimension() {
		width = 0.0f;
		height = 0.0f;
	}

	public Dimension(final Dimension other) {
		width = other.width;
		height = other.height;
	}

	public boolean equals(Object o) {
		if (o == null || o.getClass() != getClass()) {
			return false;
		} else {
			Dimension d = (Dimension) o;
			return d.width == width && d.height == height;
		}
	}

	public String toString() {
		return String.format("(w: %f, h: %f)", width, height);
	}

	public Dimension clone() {
		try {
			Dimension clone = (Dimension) super.clone();
			clone.width = width;
			clone.height = height;
			return clone;
		} catch (Exception e) {
			return null;
		}
	}
}
