/**
 *
 */
package shadoMath;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shadi
 *
 */
public class Vector implements Cloneable {

	public double x;
	public double y;
	public double z;

	/**
	 * The constructor used to initialize a 3D vector
	 *
	 * @param _x The x position of the vector
	 * @param _y The y position of the vector
	 * @param _z the z position of the vector
	 */
	public Vector(double _x, double _y, double _z) {
		this.x = _x;
		this.y = _y;
		this.z = _z;
	}

	/**
	 * The constructor used to initialize a 2D vector. This constructor initializes
	 * <b>z</b> to 0
	 *
	 * @param _x The x position of the vector
	 * @param _y The y position of the vector
	 */
	public Vector(double _x, double _y) {
		this(_x, _y, 0);
	}

	/**
	 * The constructor used to initialize a 2D vector with random <b>x</b> and
	 * <b>y</b>
	 *
	 * @param scale The maximum value that the coordinates can have
	 */
	public Vector(int scale) {
		this(0, 0);
		this.random2D(scale);
		List<Integer> test = new ArrayList<Integer>();
	}

	/**
	 * The default constructor initializes a 2D vector with a scale of 0
	 *
	 */
	public Vector() {
		this(0, 0, 0);
	}

	/**
	 * The copy constructor copies any given vector
	 *
	 * @param vectorTocopy The vector that you want to copy
	 */
	public Vector(final Vector vectorTocopy) {
		this.x = vectorTocopy.x;
		this.y = vectorTocopy.y;
		this.z = vectorTocopy.z;
	}

	/**
	 * Modifies the current <b>x</b> and <b>y</b> to have random values
	 *
	 * @param scale The maximum value that the coordinates can have
	 */
	public void random2D(int scale) {
		this.x = Math.floor(Math.random() * scale);
		this.y = Math.floor(Math.random() * scale);
	}

	/**
	 * Modifies the current <b>x</b> and <b>y</b> to have random values with a scale
	 * of 10
	 *
	 */
	public void random2D() {
		this.random2D(10);
	}

	/**
	 * Modifies the current <b>x</b>, <b>y</b> and <b>z</b> to have random values
	 *
	 * @param scale The maximum value that the coordinates can have
	 */
	public void random3D(int scale) {
		this.random2D(scale);
		this.z = Math.floor(Math.random() * scale);
	}

	/**
	 * Modifies the current <b>x</b>, <b>y</b> and <b>z</b> to have random values
	 * with a scale of 10
	 */
	public void random3D() {
		this.random3D(10);
	}

	/**
	 * multiplies all the coodinates of the calling vector by -1
	 */
	public void inverse() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
	}

	/**
	 * Creates a vector from a specific angle in RADIANS
	 * @return Returns a unit vector
	 */
	public static Vector fromAngle(double angle) {
		return new Vector(Math.cos(angle), Math.sin(angle));
	}

	/**
	 * multiplies all the coodinates by a value <b>scale</b>
	 *
	 * @param scale The value you want to multiply the vector with
	 */
	public void scale(double scale) {
		this.x *= scale;
		this.y *= scale;
		this.z *= scale;
	}

	/**
	 * @return Returns the magnitude of the calling vector
	 */
	public double mag() {
		return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}

	/**
	 * Modifies all the coordinates to have a value between 0.0 and 1.0
	 */
	public void normalize() {
		double tempMag = this.mag();
		this.x = this.x / tempMag;
		this.y = this.y / tempMag;
		this.z = this.z / tempMag;
	}

	/**
	 * Adds a vector with the calling vector (does not modify the calling vector)
	 *
	 * @param other The other vector you want to add
	 *
	 * @return Returns the vector sum of other + the calling vector
	 */
	public Vector add(final Vector other) {
		return new Vector(this.x + other.x, this.y + other.y, this.z + other.z);
	}

	/**
	 * Substracts a vector from the calling vector (does not modify the calling
	 * vector)
	 *
	 * @param other The other vector you want to substract
	 *
	 * @return Returns the vector diffrence of the calling vector - other
	 */
	public Vector substract(final Vector other) {
		other.inverse();
		return this.add(other);
	}

	/**
	 * Computes the dot product of the calling vector and the vector b
	 *
	 * @param b The other vector you want to calculate dot product
	 *
	 * @return Returns the dot product of the calling vector and b
	 */
	public double dotProduct(final Vector b) {
		return this.x * b.x + this.y * b.y + this.z * b.z;
	}

	/**
	 * Computes the cross product of the calling vector and the vector b
	 *
	 * @param b The other vector you want to calculate cross product with
	 *
	 * @return Returns a vector representing the cross product between the calling
	 *         vector and b
	 */
	public Vector crossProduct(final Vector b) {
		Matrix i = new Matrix(2, 2);
		i.setData(0, 0, y);
		i.setData(0, 1, z);
		i.setData(1, 0, b.y);
		i.setData(1, 1, b.z);

		Matrix j = new Matrix(2, 2);
		j.setData(0, 0, x);
		j.setData(0, 1, z);
		j.setData(1, 0, b.x);
		j.setData(1, 1, b.z);

		Matrix k = new Matrix(2, 2);
		k.setData(0, 0, x);
		k.setData(0, 1, y);
		k.setData(1, 0, b.x);
		k.setData(1, 1, b.y);

		return new Vector(i.determinant(), -1.0f * j.determinant(), k.determinant());
	}

	/**
	 * Multiplies the calling vector with a given matrix (does not modify the
	 * calling vector)
	 *
	 * @param matrix The matrix you want to multiply with
	 *
	 * @return Returns the result matrix of the multiplication
	 */
	public Matrix multiply(final Matrix matrix) {
		Matrix vec = this.toMatrix();
		return vec.multiply(matrix);
	}

	/**
	 * Multiplies all the coodinates of the calling vector with a number (does not
	 * modify the calling vector)
	 *
	 * @param scale The value you want to multiply the vector with
	 *
	 * @return Returns the resulting vector
	 */
	public Vector multiply(double scale) {
		Vector result = new Vector(this.x, this.y, this.z);
		result.scale(scale);
		return result;
	}

	/**
	 * Computes the resulting vector of the projection of the calling vector on
	 * another vector
	 *
	 * @param other The vector on which you want to project the calling
	 *
	 * @return Returns the resulting projection vector
	 */
	public Vector project(final Vector other) {

		double multiplier = other.dotProduct(this);

		multiplier = multiplier / (other.mag() * other.mag());

		return other.multiply(multiplier);
	}

	/***
	 * Converts the calling vector a matrix and returns it
	 * @return Returns the resulting Matrix
	 */
	public Matrix toMatrix() {
		Double[][] temp = {
				{this.x},
				{this.y},
				{this.z}
		};
		return new Matrix(temp);
	}

	/***
	 * Converts the calling Vector to a Complex number
	 * @return Returns the resulting Complex number
	 */
	public Complex toComplex() {
		return new Complex(this.x, this.y);
	}

	// Overridden java.lang.Object
	public boolean equals(Object o) {
		if (o == null || o.getClass() != getClass()) {
			return false;
		} else {
			Vector v = (Vector) o;
			return v.x == x && v.y == y && v.z == z;
		}
	}

	public Vector clone() {
		try {
			Vector clone = (Vector) super.clone();
			clone.x = x;
			clone.y = y;
			clone.z = z;
			return clone;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	// Printers

	/**
	 *
	 * @return Returns a string with the following format (x: <i>x</i>, y: <i>y</i>,
	 *         z: <i>z</i>)
	 */
	public String toString() {
		return String.format("(x: %f, y: %f, z: %f)", this.x, this.y, this.z);
	}

	/**
	 * Prints the calling vector to the console
	 */
	public void print() {
		System.out.println(this.toString());
	}
}
