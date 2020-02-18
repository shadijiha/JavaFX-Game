/***
 *
 * Shado Fraction class library
 *
 * */

package shadoMath;

public class Fraction {

	private int numerator;
	private int denominator;

	/***
	 *
	 * @param numerator The numerator of the fraction
	 * @param denominator The denominator of the fraction
	 */
	public Fraction(int numerator, int denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}

	/***
	 *
	 * @param number the decimal number you want to convert to a fraction
	 */
	public Fraction(double number) {

		double tempNum = number;
		int tries = 0;

		while (tempNum - (int) tempNum != 0.0) {
			tempNum *= 10;
			tries++;
		}

		this.numerator = (int) tempNum;
		this.denominator = (int) Math.pow(10, tries);

		this.simplify();

	}

	/***
	 * Copy constructor
	 * @param other The fraction you want to copy
	 */
	public Fraction(final Fraction other) {
		this(other.numerator, other.denominator);
	}

	/***
	 * Simplifies the calling Fraction
	 */
	public void simplify() {
		for (int i = 10; i > 0; i--) {
			if (this.numerator % i == 0 && this.denominator % i == 0) {
				this.numerator = this.numerator / i;
				this.denominator = this.denominator / i;
			}
		}
	}

	/***
	 * Inverses the calling Fraction
	 */
	public void inverse() {
		int temp = this.numerator;
		this.numerator = this.denominator;
		this.denominator = temp;
		this.simplify();
	}

	/***
	 * Multiplies the fraction by -1
	 */
	public void opposite() {
		this.numerator = -1 * this.numerator;
	}

	/***
	 * Adds a Fraction to the calling Fraction
	 * @param other The fraction you want to add to the calling Fraction
	 * @return Returns the result of the addition
	 */
	public Fraction add(final Fraction other) {
		Fraction commonA = new Fraction(this.numerator * other.denominator, this.denominator * other.denominator);
		Fraction commonB = new Fraction(other.numerator * this.denominator, other.denominator * this.denominator);

		Fraction result = new Fraction(commonA.getNumerator() + commonB.getNumerator(), commonA.getDenominator());
		result.simplify();
		return result;
	}

	/***
	 * Subtracts a Fraction to the calling Fraction
	 * @param other The fraction you want to subtract to the calling Fraction
	 * @return Returns the result of the subtraction
	 */
	public Fraction subtracts(final Fraction other) {
		other.opposite();
		return this.add(other);
	}

	/***
	 * Multiplies a Fraction to the calling Fraction
	 * @param other The fraction you want to multiply to the calling Fraction
	 * @return Returns the result of the multiplication
	 */
	public Fraction multiply(final Fraction other) {
		return new Fraction(this.numerator * other.getNumerator(), this.denominator * other.getDenominator());
	}

	/***
	 * Divides a Fraction to the calling Fraction
	 * @param other The fraction you want to divide to the calling Fraction
	 * @return Returns the result of the division
	 */
	public Fraction divide(final Fraction other) {
		other.inverse();
		return this.multiply(other);
	}

	/***
	 * Raises the numerator and to denominator of the Fraction to a exponent
	 * @param exponent The specific exponent
	 * @return Returns the result of the power
	 */
	public Fraction power(int exponent) {
		return new Fraction((int) Math.pow(this.numerator, exponent), (int) Math.pow(this.denominator, exponent));
	}

	// Getters

	/***
	 * @return Returns the numerator of the power
	 */
	public int getNumerator() {
		return this.numerator;
	}

	/***
	 * @return Returns the denominator of the power
	 */
	public int getDenominator() {
		return this.denominator;
	}

	// String and print

	/***
	 * @return Returns "numerator / denominator"
	 */
	public String toString() {
		return String.format("%d / %d", this.numerator, this.denominator);
	}

	/***
	 * prints the calling Fraction to the console
	 */
	public void print() {
		System.out.println(this.toString());
	}
}
