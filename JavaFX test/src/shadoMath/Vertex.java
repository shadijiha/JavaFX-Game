package shadoMath;

public class Vertex implements Cloneable {

	public double x;
	public double y;
	public double z;

	/****
	 * @param _x The x position of the vertex
	 * @param _y The y position of the vertex
	 * @param _z The z position of the vertex
	 */
	public Vertex(double _x, double _y, double _z) {
		this.x = _x;
		this.y = _y;
		this.z = _z;
	}

	/****
	 * @param _x The x position of the vertex
	 * @param _y The y position of the vertex
	 */
	public Vertex(double _x, double _y) {
		this(_x, _y, 0);
	}

	/****
	 * @param v The vertex you want to copy
	 */
	public Vertex(final Vertex v) {
		this(v.x, v.y, v.z);
	}

	/****
	 * Default constructor initializes the vertex to (0, 0, 0)
	 */
	public Vertex() {
		this(0, 0, 0);
	}

	/****
	 * Gives a distance between the calling vertex and the passed vertex
	 *
	 * @param v The vertex you want to compute the distance with
	 * @return Returns the distance between the calling vertex and the passed vertex
	 */
	public double getDistance(Vertex v) {
		return Math.sqrt(Math.pow(v.x - this.x, 2) + Math.pow(v.y - this.y, 2) + Math.pow(v.z - this.z, 2));
	}

	// Setters

	/***
	 * Changes the position of the vertex to the passed vertex
	 *
	 * @param v The vertex you want to copy its position
	 */
	public void set(Vertex v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	/***
	 * @return Returns a clone of the calling object
	 */
	@Override
	public Vertex clone() {
		try {
			Vertex clone = (Vertex) super.clone();
			clone.x = this.x;
			clone.y = this.y;
			clone.z = this.z;
			return clone;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Determines if 2 vertex are identical (Same x, y and z)
	 *
	 * @param o The Vertex to compare with the calling one
	 * @return Return true if they are equal, false otherwise
	 */
	public boolean equals(Object o) {
		if (o == null || o.getClass() != getClass()) {
			return false;
		} else {
			Vertex v = (Vertex) o;
			return x == v.x && y == v.y && z == v.z;
		}
	}

	public String toString() {
		return String.format("(%f, %f, %f)", x, y, z);
	}
}
