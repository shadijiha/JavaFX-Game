package shadoMath;

import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.function.Predicate;

public class Util {

	/***
	 * Gives a random number from a specified range
	 *
	 * @param min The minimum number of the range
	 * @param max The maximum number of the range
	 *
	 * @return Returns a random number >= min and <= max
	 * */
	public static double random(double min, double max) {
		return Math.random() * (max - min) + min;
	}

	/***
	 * Gives a random number from a specified range
	 *
	 * @param min The minimum number of the range
	 * @param max The maximum number of the range
	 *
	 * @return Returns a random number >= min and <= max
	 * */
	public static int random(int min, int max) {
		return (int) random((double) min, (double) max);
	}

	/***
	 * Gives a random Color
	 *
	 * @return Returns a random Color
	 * */
	public static Color randomColor() {
		return new Color(random(0, 255), random(0, 255), random(0, 255));
	}

	/***
	 * Gives the name of the passed object
	 *
	 * @param obj The object you want to get its name
	 *
	 * @return Returns the name of the passed object
	 * */
	public static String getObjectName(Object obj) {
		String[] temp = obj.getClass().getName().split("\\.");
		return temp[temp.length - 1];
	}

	/***
	 * Prints stuff to the console
	 *
	 * @param args The things you want to print to the console
	 * */
	public static <T> void print(@SuppressWarnings("unchecked") T... args) {
		for (T arg : args)
			System.out.print(arg + " ");
		System.out.println();
	}

	/***
	 * Gives the current hour
	 *
	 * @return Returns the current hour
	 * */
	public static int getHour() {
		Date date = new Date();   // given date
		Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
		calendar.setTime(date);   // assigns calendar to given date
		return calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
	}

	public static <T> void delete_if(List<T> list, Predicate<T> condition) {
		for (int i = list.size() - 1; i >= 0; i--) {
			if (condition.test(list.get(i)))
				list.remove(i);
		}
	}
}
