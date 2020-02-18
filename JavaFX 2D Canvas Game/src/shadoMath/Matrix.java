/**
 *
 */
package shadoMath;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shadi Jiha
 *
 */
public class Matrix {

	private Double[][] data;
	private int rows;
	private int cols;

	/**
	 * This constructor initializes the Matrix with the passed data inside 2D array
	 *
	 * @param array The initial data to put in the Matrix
	 */
	public Matrix(Double[][] array) {
		this.data = array;
		this.rows = array.length;
		this.cols = array[0].length;
	}

	/**
	 * This constructor initializes the Matrix with the passed data inside 2D array
	 *
	 * @param array The initial data to put in the Matrix
	 */
	public Matrix(Integer[][] array) {
		this.rows = array.length;
		this.cols = this.getBiggestCol(array);

		this.data = new Double[this.rows][this.cols];
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				this.data[i][j] = (double) array[i][j];
			}
		}
	}

	/**
	 * This constructor initializes the Matrix with a specific row and colon count
	 *
	 * @param rows The desired row count
	 * @param cols The desired colon count
	 */
	public Matrix(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		this.data = new Double[rows][cols];
		this.randomize();
	}

	/**
	 * This copy constructor copies a Matrix object
	 *
	 * @param other The matrix you want to copy
	 */
	public Matrix(final Matrix other) {
		// Copy matrix
		this(other.getRows(), other.getCols());
		this.data = other.data;
	}

	/**
	 * The default constructor initializes a matrix with 2 rows and 2 colons
	 */
	public Matrix() {
		this(2, 2);
	}

	/**
	 * Replaces all the data in the matrix with a random number
	 *
	 * @param scale The maximum number that can be used
	 */
	public void randomize(int scale) {
		for (int i = 0; i < this.data.length; i++) {
			for (int j = 0; j < this.data[i].length; j++) {
				this.data[i][j] = Math.floor(Math.random() * scale);
			}
		}
	}

	/**
	 * Replaces all the data in the matrix with a scale of 10
	 */
	public void randomize() {
		this.randomize(10);
	}

	/**
	 * Replaces all data in matrix with 1 if row equals colon, with 0 otherwise
	 *
	 * @throws Error throws an error if the matrix rows and colon count are not equal
	 */
	public void generateIdentity() throws Error {
		if (this.rows == this.cols) {
			for (int i = 0; i < this.data.length; i++) {
				for (int j = 0; j < this.data[i].length; j++) {
					if (i == j)
						this.data[i][j] = 1.0;
					else
						this.data[i][j] = 0.0;
				}
			}
		} else {
			throw new Error("Cannot build an identity matrix if rows and colonnes are not equal");
		}
	}

	/**
	 * Computes the determinant of a 2x2 Matrix
	 *
	 * @return Returns the determinant of a 2x2 Matrix
	 * @throws Error throws an error if the matrix is not 2 by 2
	 */
	public double determinant2D() throws Error {
		if (this.isSquare()) {
			return this.data[0][0] * this.data[1][1] - this.data[0][1] * this.data[1][0];
		} else {
			throw new Error("Cannot return the determiant of a non 2x2 matrix.");
		}
	}

	/**
	 * Computes the determinant of any n by n matrix
	 *
	 * @return Returns the determinant of a n by n Matrix
	 * @throws Error throws an error if the matrix is not squared (rows == colons)
	 */
	public double determinant() throws Error {

		if (this.isSquare()) {

			// IF 2x2 matrix return ad - bc
			if (this.is2D()) {
				return this.determinant2D();
			}

			// ELSE use the recurtion rule:
			// det(A_nxn) = a_11 * det(A_11) - a_12 * det(A_12) 
			// 				+ a_13 * det(A_13) - ... 
			//				+- a_1xn * det(A_1xn)
			double sum = 0.0;
			int element = 1;    // To know if we have to add or substract

			for (int j = 0; j < this.cols; j++) {
				if (element % 2 == 0) {
					sum -= this.getData(0, j) * this.subMatrix(0, j).determinant();
				} else {
					sum += this.getData(0, j) * this.subMatrix(0, j).determinant();
				}
				element++;
			}

			return sum;

		} else {
			throw new Error("Cannot compute the determinant of a non square matrix");
		}
	}

	/**
	 * Caculates the submatrix that results from ignoring 1 row and 1 colon.
	 * NOTE: Replaces all non desired values with <b style="color: blue;">null</b>
	 *
	 * @param rowToIgnore the row you want to ignore
	 * @param colToIgnore the colon you want to ignore
	 * @return Returns the parent matrix without the row and colon you want to ignore
	 */
	public Matrix subMatrix(int rowToIgnore, int colToIgnore) {

		Matrix result = new Matrix(this);

		for (int i = 0; i < result.getRows(); i++) {
			for (int j = 0; j < result.getCols(); j++) {
				if (i != rowToIgnore && j != colToIgnore) {
					result.setData(i, j, this.getData(i, j));
				} else {
					result.setData(i, j);    // Set to null
				}
			}
		}

		result.compresse();

		return result;
	}

	/**
	 * Computes the result matrix from adding the calling Matrix with the passed Matrix
	 *
	 * @param other The matrix you want to add with the calling matrix
	 * @return Returns the sum result of calling Matrix + other Matrix
	 */
	public Matrix add(final Matrix other) throws Error {

		if (this.rows == other.rows && this.cols == other.cols) {
			Matrix result = new Matrix(this.rows, this.cols);

			for (int i = 0; i < result.getRows(); i++) {
				for (int j = 0; j < result.getCols(); j++) {
					result.setData(i, j, this.data[i][j] + other.getData(i, j));
				}
			}

			return result;

		} else {
			throw new Error("Cannot add 2 Matrix with diffrent rows and colonnes count.");
		}

	}

	/**
	 * Computes the result matrix from multiplying the calling matrix with a number
	 *
	 * @param scale The number you want to multiply to all cells of the callin Matrix
	 * @return Returns the multiplying result of calling Matrix * scale
	 */
	public Matrix multiply(double scale) {
		Matrix result = new Matrix(this.rows, this.cols);

		for (int i = 0; i < result.getRows(); i++) {
			for (int j = 0; j < result.getCols(); j++) {
				result.setData(i, j, this.data[i][j] * scale);
			}
		}

		return result;
	}

	/**
	 * Computes the result matrix from multiplying the calling matrix with another Matrix
	 *
	 * @param other The matrix you want to multiply with the calling matrix
	 * @return Returns the multiplying result of calling Matrix * other Matrix
	 */
	public Matrix multiply(final Matrix other) throws Error {

		if (this.cols == other.getRows() && this.rows == other.getCols()) {
			Matrix result = new Matrix(this.rows, other.cols);

			for (int i = 0; i < result.getRows(); i++) {
				for (int j = 0; j < result.getCols(); j++) {
					double sum = 0;
					for (int k = 0; k < this.cols; k++) {
						sum += this.getData(i, k) * other.getData(k, j);
					}
					result.setData(i, j, sum);
				}
			}

			return result;
		} else {
			throw new Error("Cannot multiply matrices where Acols != Brows && Arows != Bcols");
		}
	}

	/**
	 * Computes the result matrix from multiplying the calling matrix with a Vector
	 *
	 * @param mat The Vector you want to multiply with the calling matrix
	 * @return Returns the multiplying result of calling Matrix * other Vector
	 */
	public Matrix multiply(final Vector mat) {
		Matrix vec = new Matrix(3, 1);
		Matrix result = vec.multiply(mat);

		return result;
	}

	/**
	 * Computes the result matrix from multiplying the calling matrix with it self <i>n</i> times
	 *
	 * @param exponent The number of times you want to multiply the calling matrix with itself
	 * @return Returns the multiplying result of calling Matrix with it self <i>n</i> times
	 * @throws Error throws an error if the calling matrix is not 2 by 2
	 */
	public Matrix power(int exponent) throws Error {
		if (this.isSquare()) {
			Matrix result = new Matrix(this);

			for (int i = 0; i < exponent - 1; i++) {
				result = result.multiply(this);
			}

			return result;
		} else {
			throw new Error("Cannot compute the power of " + exponent + " a non square matrix.");
		}
	}

	/**
	 * Computes the reverse of the current Matrix
	 *
	 * @return Returns the inverse of the calling matrix
	 * @throws Error throws an error because the function hasn't been coded yet
	 */
	public Matrix inverse() throws Error {

		throw new Error("ERROR!!!!!!!");
		/*if (this.is2D())	{
			double temp = 1 / this.determinant2D();
			this.data[0][1] = -1.0 * this.data[0][1];
			this.data[1][0] = -1.0 * this.data[1][0];
			return this.multiply(temp);
		} else	{
			return this.inverse().multiply(1 / this.determinant());
		}*/

	}

	/**
	 * Contructs the transpose the calling matrix
	 *
	 * @return Returns the transpose of the calling matrix
	 */
	public Matrix transpose() {
		Matrix mat = new Matrix(this.cols, this.rows);

		for (int i = 0; i < this.getRows(); i++) {
			for (int j = 0; j < this.getCols(); j++)
				mat.setData(j, i, this.data[i][j]);
		}
		return mat;
	}

	// Private functions	
	protected int factorial(int number) {
		if (number == 1) {
			return 1;
		}
		return number * factorial(number - 1);
	}

	private void compresse() {
		this.data = this.removeAllNulls(this.data);
		this.resize();
	}

	private Double[][] removeAllNulls(Double[][] array) {

		List<Double[]> outerList = new ArrayList<Double[]>(array.length);
		for (Double[] inner : array) {
			if (inner != null) {
				List<Object> list = new ArrayList<Object>(inner.length);
				for (Double aDouble : inner) {
					if (aDouble != null) {
						list.add(aDouble);
					}
				}
				outerList.add(list.toArray(new Double[list.size()]));
			}
		}
		array = outerList.toArray(new Double[outerList.size()][]);

		return array;
	}

	private void resize() {

		// find biggest cols
		this.cols = this.getBiggestCol(this.data);

		// find number of rows that aren't {} and put all {} to null
		for (int i = 0; i < this.data.length; i++) {
			if (this.data[i].length <= 0) {
				this.data[i] = null;
			}
		}

		this.data = this.removeAllNulls(this.data);
		this.rows = this.data.length;
	}

	private <T> int getBiggestCol(T[][] array) {
		int biggest = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i].length > biggest)
				biggest = array[i].length;
		}

		return biggest;
	}

	private void setData(int row, int col) {
		try {
			this.data[row][col] = null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/***
	 * Converts a matrix to a Vector
	 * @return Returns the resulting Vector
	 * @throws Error Throws an error if Matrix colons > 1 OR if Matrix rows > 3
	 */
	public Vector toVector() throws Error {
		if (this.cols == 1) {
			if (this.rows == 1) {
				return new Vector(this.getData(0, 0), 0);
			} else if (this.rows == 2) {
				return new Vector(this.getData(0, 0), this.getData(1, 0));
			} else if (this.rows == 3) {
				return new Vector(this.getData(0, 0), this.getData(1, 0), this.getData(2, 0));
			} else {
				throw new Error("Cannot convert a Matrix with more than 3 rows to a Vector");
			}
		} else {
			throw new Error("Cannot convert a Matrix with more than 1 colon to a Vector");
		}
	}

	// Static methodes

	/**
	 * Gives the X rotation matrix with a given angle
	 *
	 * @param angle the angle you want to generate the matrix with
	 * @return Returns the X rotation with a angle
	 */
	public static Matrix rotationX(double angle) {
		Matrix result = new Matrix(3, 3);

		result.setData(0, 0, 1);
		result.setData(0, 1, 0);
		result.setData(0, 2, 0);

		result.setData(1, 0, 0);
		result.setData(1, 1, Math.cos(angle));
		result.setData(1, 2, Math.sin(angle));

		result.setData(2, 0, 0);
		result.setData(2, 1, -Math.sin(angle));
		result.setData(2, 2, Math.cos(angle));

		return result;
	}

	/**
	 * Gives the Y rotation matrix with a given angle
	 *
	 * @param angle the angle you want to generate the matrix with
	 * @return Returns the Y rotation with a angle
	 */
	public static Matrix rotationY(double angle) {
		Matrix result = new Matrix(3, 3);

		result.setData(0, 0, Math.cos(angle));
		result.setData(0, 1, 0);
		result.setData(0, 2, -Math.sin(angle));

		result.setData(1, 0, 0);
		result.setData(1, 1, 1);
		result.setData(1, 2, 0);

		result.setData(2, 0, 0);
		result.setData(2, 1, Math.sin(angle));
		result.setData(2, 2, Math.cos(angle));

		return result;
	}

	/**
	 * Gives the Z rotation matrix with a given angle
	 *
	 * @param angle the angle you want to generate the matrix with
	 * @return Returns the Z rotation with a angle
	 */
	public static Matrix rotationZ(double angle) {
		Matrix result = new Matrix(3, 3);

		result.setData(0, 0, Math.cos(angle));
		result.setData(0, 1, Math.sin(angle));
		result.setData(0, 2, 0);

		result.setData(1, 0, -Math.sin(angle));
		result.setData(1, 1, Math.cos(angle));
		result.setData(1, 2, 0);

		result.setData(2, 0, 0);
		result.setData(2, 1, 0);
		result.setData(2, 2, 1);

		return result;
	}

	// Setters

	/**
	 * Changes a data in the matrix
	 *
	 * @param row the row where the data is located
	 * @param col the colon where the data is located
	 * @param newData The new Data with which you want to replace the old data
	 */
	public void setData(int row, int col, double newData) {
		try {
			this.data[row][col] = newData;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// Getters

	/**
	 * Gives the data in a specific location in Matrix
	 *
	 * @param row the row where the data is located
	 * @param col the colon where the data is located
	 * @return Returns the data in the given row and colon
	 */
	public double getData(int row, int col) {
		double result = 0.0;

		try {
			result = this.data[row][col];
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return result;
	}

	/**
	 * @return Returns the number of rows the matrix calling contains
	 */
	public int getRows() {
		return this.rows;
	}

	/**
	 * @return Returns the number of colons the calling matrix contains
	 */
	public int getCols() {
		return this.cols;
	}

	/**
	 * Converts the calling matrix to a regular 2D double array
	 * @return Returns the 2D array with all the matrix data inside
	 */
	public double[][] toArray() {
		double[][] clone = new double[this.rows][this.cols];

		for (int i = 0; i < this.data.length; i++) {
			for (int j = 0; j < this.data[i].length; j++) {
				clone[i][j] = this.data[i][j];
			}
		}

		return clone;
	}

	/**
	 * Converts the calling matrix to a regular 2D int array
	 * @return Returns the 2D array with all the matrix data inside
	 */
	public int[][] toIntArray() {
		int[][] clone = new int[this.rows][this.cols];

		for (int i = 0; i < this.data.length; i++) {
			for (int j = 0; j < this.data[i].length; j++) {
				clone[i][j] = this.data[i][j].intValue();
			}
		}

		return clone;
	}

	/**
	 * @return Returns if the calling matrix rows == colons
	 */
	public boolean isSquare() {
		return this.rows == this.cols;
	}

	/**
	 * @return Returns if the calling matrix is 2 by 2
	 */
	public boolean is2D() {
		return this.rows == 2 && this.cols == 2;
	}

	/**
	 * Prints the calling matrix to the console
	 */
	public void print() {
		for (int i = 0; i < this.data.length; i++) {
			for (int j = 0; j < this.data[i].length; j++) {
				System.out.print(this.data[i][j] + "\t");
			}
			System.out.println();
		}
	}

	/**
	 * Converts the calling matrix to HTML code
	 * @return Returns the calling matrix in HTML code
	 */
	public String toHTML() {
		StringBuilder str = new StringBuilder("<html><body><table border=\"1\">");

		for (int i = 0; i < this.rows; i++) {
			str.append("<tr>");
			for (int j = 0; j < this.cols; j++) {
				str.append("<td style=\"padding: 15px;\">").append(this.data[i][j]).append("</td>");
			}
			str.append("</tr>");
		}

		return str.append("</table></body></html>").toString();
	}
}
