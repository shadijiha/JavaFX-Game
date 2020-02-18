/***
 *
 * Shado Complex class library
 *
 * */

package shadoMath;

public class Complex {

	private double a = 0.0;
	private double b = 0.0;
	private double r = 0.0;
	private double phi = 0.0;

	/***
	 *
	 * @param _a The real part of the the Complex number
	 * @param _b The imaginary part of the Complex number
	 */
	public Complex(double _a, double _b) {
		this.a = _a;
		this.b = _b;
		this.r = Math.sqrt(_a * _a + _b * _b);
		this.phi = (double) Math.atan(_b / _a);
	}

	/***
	 *
	 * @param other The Complex number you want to copy
	 */
	public Complex(Complex other) {
		this(other.getA(), other.getB());
	}

	/***
	 * Default constructor initializes a to 0 and b to 0
	 */
	public Complex() {
		this(0.0, 0.0);
	}

	/***
	 * Initializes a Complex number from a polar form
	 * @param magnitude The magnitude of the Complex Number
	 * @param angle The angle (direction) of the Complex Number
	 */
	public void fromPolar(double magnitude, double angle) {
		this.r = magnitude;
		this.phi = angle;
		this.a = this.r * Math.cos(this.phi);
		this.a = this.r * Math.sin(this.phi);
	}

	/***
	 *
	 * @return Returns the magnitude of the calling Complex number
	 */
	public double mag() {
		return Math.sqrt(this.a * this.a + this.b * this.b);
	}

	/***
	 * Addes a Complex number to the calling Complex
	 * @param other The Complex you want to add
	 * @return Returns the result of the addition
	 */
	public Complex add(final Complex other) {
		return new Complex(this.a + other.getA(), this.b + other.getB());
	}

	/***
	 * Subtract a Complex number from the calling complex number																																		       a Complex number to the calling Complex
	 * @param other The Complex you want to subtract
	 * @return Returns the result of the subtraction
	 */
	public Complex subtract(final Complex other) {
		return new Complex(this.a - other.getA(), this.b - other.getB());
	}

	/***
	 * Multiply a Complex number by the calling complex number																																		       a Complex number to the calling Complex
	 * @param other The Complex you want to multiply with
	 * @return Returns the result of the multiplication
	 */
	public Complex multiply(final Complex other) {
		Complex temp = new Complex();
		temp.fromPolar(this.r * other.getR(), this.phi + other.getPhi());
		return temp;
	}

	/***
	 * Divides a Complex number by the calling complex number																																		       a Complex number to the calling Complex
	 * @param other The Complex you want to divide
	 * @return Returns the result of the division
	 */
	public Complex divide(final Complex other) {
		Complex temp = new Complex();
		temp.fromPolar(this.r / other.getR(), this.phi - other.getPhi());
		return temp;
	}

	/***
	 * Raises the calling Complex number to an exponent																																	       a Complex number to the calling Complex
	 * @param exponent The exponent
	 * @return Returns the result of the operation
	 */
	public Complex power(int exponent) {
		Complex temp = new Complex();
		temp.fromPolar(Math.pow(this.r, exponent), this.phi * exponent);
		return temp;
	}

	/***
	 * Computes the roots of the Complex number to an exponent																																	       a Complex number to the calling Complex
	 * @param exponent The strength of the root
	 * @return Returns the result of the operation
	 */
	public Complex[] root(int exponent) {

		Complex result[] = new Complex[exponent];

		double tempR = Math.pow(this.r, 1 / exponent);

		for (int i = 0; i < exponent; i++) {
			double tempPhi = (phi + 2 * Math.PI * i) / exponent;
			Complex temp = new Complex();
			temp.fromPolar(tempR, tempPhi);

			result[i] = temp;
		}

		return result;
	}

	/***
	 * Converts the current Complex number to a Vector
	 * @return Returns the resulting Vector
	 */
	public Vector toVector() {
		return new Vector(this.a, this.b);
	}

	// Setters

	/***
	 * Modifies the real part of the complex number. Recalculates the magnitude and the angle accordingly
	 * @param _a The new Real part
	 */
	public void setA(double _a) {
		this.a = _a;
		this.r = Math.sqrt(this.a * this.a + this.b * this.b);
		this.phi = Math.atan(this.b / this.a);
	}

	/***
	 * Modifies the imaginary part of the complex number. Recalculates the magnitude and the angle accordingly
	 * @param _b The new imaginary part
	 */
	public void setB(double _b) {
		this.b = _b;
		this.r = Math.sqrt(this.a * this.a + this.b * this.b);
		this.phi = Math.atan(this.b / this.a);
	}

	/***
	 * Modifies the magnitude of the complex number. Recalculates the real part and the imaginary part accordingly
	 * @param _r The new magnitude
	 */
	public void setR(double _r) {
		this.r = _r;
		this.a = this.r * Math.cos(this.phi);
		this.b = this.r * Math.sin(this.phi);
	}

	/***
	 * Modifies the angle of the complex number. Recalculates the real part and the imaginary part accordingly
	 * @param angle The new angle
	 */
	public void setAngle(double angle) {
		this.phi = angle;
		this.a = this.r * Math.cos(this.phi);
		this.b = this.r * Math.sin(this.phi);
	}

	// Getters

	/***
	 * @return Returns the real part of the calling Complex
	 */
	public double getA() {
		return this.a;
	}

	/***
	 * @return Returns the imaginary part of the calling Complex
	 */
	public double getB() {
		return this.b;
	}

	/***
	 * @return Returns the magnitude of the calling Complex
	 */
	public double getR() {
		return this.r;
	}

	/***
	 * @return Returns the angle of the calling Complex
	 */
	public double getPhi() {
		return this.phi;
	}

	/***
	 * @return Returns the angle of the calling Complex
	 */
	public double getAngle() {
		return this.getPhi();
	}

	/***
	 * @return Returns a string representing the Complex. Format: "<i>real part</i> + <i>imaginary part</i> i";
	 */
	public String toString() {
		return String.format("%1.2f + %1.2f i", this.a, this.b);
	}

	/***
	 * @return Returns a string representing the Complex in polar form. Format: "<i>mag</i>f * (cos(<i>angle</i>) + i * sin(<i>angle</i>))";
	 */
	public String toStringPolar() {
		return String.format("%1.2f * (cos(%f) + i * sin(%f))", this.r, this.phi, this.phi);
	}
}

