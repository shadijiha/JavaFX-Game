/**
 *
 */

package game;

public final class Time {

	// The time between 2 frames
	public static double deltaTime = 0D;

	// Time since the start of the program
	private static double start = 0.0;

	private Time() {
	}

	/**
	 * Only should be used in AnimationTimer to update the time elaped
	 * @param amount
	 */
	public static void addTime(double amount) {
		start += amount;
	}

	/**
	 * @return Returns the time elapsed since the start of the program
	 */
	public static double timeElapsed() {
		return start;
	}
}
