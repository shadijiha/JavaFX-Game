/**
 *
 */

package game;

public final class Timer {

	// The time between 2 frames
	public static double deltaTime = 0D;

	// Time since the start of the program
	private static double time_since_start = 0.0;
	private static double fps = 60.0;

	// Every instance of time created keeps trac of the time when it was created
	private double createdTime;

	public Timer() {
		createdTime = time_since_start;
	}

	/**
	 * Computes how many milli seconds this instance of timer has lived
	 * @return Returns the created time - time since start of the application
	 */
	public double timeDiffrence() {
		return time_since_start - createdTime;
	}

	/**
	 * @return Returns the framerate of the application
	 */
	public static double framerate() {
		return fps;
	}

	/**
	 * Sets the framerate of the application
	 * @param num The framerate
	 */
	public static void setFramerate(double num) {
		fps = num;
	}

	/**
	 * Only should be used in AnimationTimer to update the time elaped
	 * @param amount
	 */
	public static void addTime(double amount) {
		time_since_start += amount;
	}

	/**
	 * @return Returns the time elapsed since the start of the program
	 */
	public static double timeElapsed() {
		return time_since_start;
	}
}
