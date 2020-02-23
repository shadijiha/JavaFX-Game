/**
 *
 */

package game;

public final class Time {

	// The time between 2 frames
	public static double deltaTime = 0D;

	// Time since the start of the program
	private static double start = 0.0;
	private static double fps = 60.0;

	private Time() {
	}

	/**
	 * @return Returns the framerate of the application
	 */
	public static double framerate() {
		return fps;
	}

	public static void setFramerate(double num) {
		fps = num;
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
