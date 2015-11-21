/**
 * Version 2.0
 * Copyright 2015 Soroosh Pezeshki

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package de.pezeshki.badicalendar;

import java.util.Calendar;
import java.util.GregorianCalendar;

import de.pezeshki.bahaiCalendarLibrary.BadiDate;
import de.pezeshki.bahaiCalendarLibrary.BahaiHolyday;

/**
 * Calculates current Badi and Gregorian date (from 1900-2064). Initial author
 * Soroosh Pezeshki April 2015
 */
@Deprecated
public class Badi {

	private static final byte[] NAW_RUZ_OFFSET = { 1, 1, 0, 0, 1, 1, 0, 0, 1,
		1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0,
		0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private static final int[] BIRTH_OF_BAB_DOY = { 217, 238, 227, 216, 234,
		223, 213, 232, 220, 210, 228, 217, 235, 224, 214, 233, 223, 211,
		230, 219, 238, 226, 215, 234, 224, 213, 232, 221, 210, 228, 217,
		236, 225, 214, 233, 223, 212, 230, 219, 237, 227, 215, 234, 224,
		213, 232, 220, 209, 228, 218, 236 };
	private static final int[] HOLYDAY_DOY = { 1, 32, 40, 43, 65, 70, 112, 214,
		237, 251, 253 };
	private static final String[] HOLYDAYS = { "Naw-Ruz", "1st Ridvan",
		"9th Ridvan", "12th Ridvan", "Decleration of the Bab",
		"Ascension of Baha'u'llah", "Martyrdom of the Bab",
		"Birth of the Bab", "Birth of Baha'u'llah", "Day of the Covenant",
	"Ascension of Abdu'l-Baha" };
	private static final int[] ERROR_OUTPUT = { 0, 0, 0, 0, -1, 0, 0, 0 };
	public static final int UPPER_YEAR_LIMIT = 2014 + NAW_RUZ_OFFSET.length - 1;
	public static final int UPPER_YEAR_LIMIT_BADI = 171 + NAW_RUZ_OFFSET.length - 1;
	public static final int ZEROTH_BADI_YEAR_AS_GREGORIAN_YEAR = 1843;
	public static final int DAYS_IN_BADI_MONTH = 19;
	public static final int DAYS_BETWEEN_NAWRUZ_AND_AYYAMIHA_START = 18 * DAYS_IN_BADI_MONTH;

	/**
	 * Converter from Badi date to Gregorian.
	 *
	 * @return NonNull int array [year, month, day of month, day of year,
	 *         holyday]
	 * @param badiYear
	 *            , badiMonth, and badiDay: all NonNull integers
	 */
	public static int[] Badi2Gregorian(final int badiYear, final int badiMonth,
			final int badiDay) {
		if (badiDay < 1 || badiDay > 19 || badiMonth < 1 || badiMonth > 20
				|| badiYear < 1 || badiYear > UPPER_YEAR_LIMIT_BADI) {
			return ERROR_OUTPUT;
		}
		final int yearIndex = badiYear - 171;
		final int bdoy = getBadiDayOfTheYearFromMonthAndDay(badiMonth, badiDay,
				yearIndex);
		return badiToGregorianDateFromDoyAndYear(badiYear, bdoy);
	}

	/**
	 * Converter from Badi date to Gregorian.
	 *
	 * @return NonNull int array [year, month, day of month, day of year,
	 *         holyday]
	 * @param badiYear
	 *            : Badi year, bdoy: day of the Badi year
	 */
	public static int[] badiToGregorianDateFromDoyAndYear(final int badiYear,
			final int bdoy) {
		int gregorianYear = badiYear + 1843;
		final int yearIndex = badiYear - 171;
		final int nawRuz = nawRuzParameter(yearIndex);
		final int leapYear = isLeapYear(gregorianYear);
		int doy;
		doy = bdoy + 78 + leapYear + nawRuz;
		if (doy > 365 + leapYear) {
			doy = bdoy - 287 + nawRuz;
			gregorianYear = badiYear + 1844;
		}

		// update a date
		final Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, gregorianYear);
		calendar.set(Calendar.DAY_OF_YEAR, doy);
		final int m = calendar.get(Calendar.MONTH) + 1;
		final int d = calendar.get(Calendar.DAY_OF_MONTH);

		final int holyday = getHolydayIndex(bdoy, badiYear - 171);

		final int[] result = { gregorianYear, m, d, doy, holyday };
		return result;
	}

	/**
	 * Converter from Gregorian date to Badi.
	 *
	 * @param year
	 * @param month
	 * @param day
	 * @return array with the Badi Dates: Day of the month, month, year day of
	 *         the year, holyday, year in vahid, vahid, kull-i-shay
	 */
	public static int[] Gregorian2Badi(final int year, final int month,
			final int day) {
		if (day > 31 || day < 1 || month > 12 || month < 1
				|| year < ZEROTH_BADI_YEAR_AS_GREGORIAN_YEAR
				|| year > UPPER_YEAR_LIMIT) {
			return ERROR_OUTPUT;
		}
		final Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		final int doy = calendar.get(Calendar.DAY_OF_YEAR);
		return getBadiDateFromGregorianDoyAndYear(year, doy);
	}

	/**
	 * Converter from Gregorian date to Badi.
	 *
	 * @param year
	 *            : Gregorian year
	 * @param doy
	 *            : day of the year
	 * @return array with the Badi Dates: Day of the month, month, year day of
	 *         the year, holyday, year in vahid, vahid, kull-i-shay
	 */
	public static int[] getBadiDateFromGregorianDoyAndYear(final int year,
			final int doy) {
		if (doy > 366 || doy < 1 || year < ZEROTH_BADI_YEAR_AS_GREGORIAN_YEAR
				|| year > UPPER_YEAR_LIMIT) {
			return ERROR_OUTPUT;
		}
		final int yearIndex = year - 2014;
		final int leapyear = isLeapYear(year);
		final int nawRuz = nawRuzParameter(yearIndex);
		final int nawRuzLastYear = nawRuzParameter(yearIndex - 1);

		final int badiYear;
		final int dayOfBadiYear;
		// Calculate day number of the Badi Year
		if (doy < 79 + leapyear + nawRuz) {
			dayOfBadiYear = doy + 287 - nawRuzLastYear;
			badiYear = year - 1844;
		} else {
			dayOfBadiYear = doy - 78 - leapyear - nawRuz;
			badiYear = year - 1843;
		}

		return getFullBadiDateFromYearAndDoy(badiYear, dayOfBadiYear);
	}

	/**
	 * Returns the Gregorian Date of the next feast (or 1st day of Ayyam'i'Ha).
	 * If the input day is a feast day the one after that is shown.
	 *
	 */
	public static int[] getNextFeastDate(final int year, final int month,
			final int day) {
		final int[] badiDate = Gregorian2Badi(year, month, day);
		final int bm = badiDate[1];
		if (bm == 20) {
			return Badi2Gregorian(badiDate[0] + 1, 1, 1);
		}
		return Badi2Gregorian(badiDate[0], bm + 1, 1);
	}

	/**
	 * Returns the Badi Date of the next holyday. If the input day is a feast
	 * day the one after that is shown.
	 *
	 */
	public static int[] getNextHolydayBadiDate(final int year, final int month,
			final int day) {
		final int yearIndex = year - 171;
		final int dayOfBadiYear = getBadiDayOfTheYearFromMonthAndDay(month,
				day, yearIndex);
		for (int i = 1; i < HOLYDAY_DOY.length; i++) {
			final int hd = getHolydayDoy(yearIndex, i);
			if (hd > dayOfBadiYear) {
				return getFullBadiDateFromYearAndDoy(year, hd);
			}
		}
		return getFullBadiDateFromYearAndDoy(year + 1, 1);
	}

	/**
	 * Returns the Gregorian Date of the next holyday. If the input day is a
	 * feast day the one after that is shown.
	 *
	 */
	public static int[] getNextHolydayGregorianDate(final int year,
			final int month, final int day) {
		final int[] badiDate = Gregorian2Badi(year, month, day);
		final int[] nextBadiDate = getNextHolydayBadiDate(badiDate[0],
				badiDate[1], badiDate[2]);
		return badiToGregorianDateFromDoyAndYear(nextBadiDate[0],
				nextBadiDate[3]);
	}

	/**
	 * Returns the day in March that is Naw-Ruz.
	 */
	static int nawRuzDayOfMarch(final int gregorianYear) {
		if (gregorianYear < 1900) {
			throw new IllegalArgumentException(
					"Naw-Ruz only defined for dates after 1900");
		}
		if (gregorianYear < 2014) {
			return 21; // Use the western date for Naw-Ruz prior to 2014
		}
		final int yearIndex = gregorianYear - 2014;
		return 20 + nawRuzParameter(yearIndex);
	}

	/**
	 * Returns the Holyday.
	 */
	static String getHolyday(final int index) {
		if (index < HOLYDAYS.length && index >= 0) {
			return HOLYDAYS[index];
		}
		return "";
	}

	/**
	 * Returns the full Badi date from the badi year and day of year.
	 */
	private static int[] getFullBadiDateFromYearAndDoy(final int badiYear,
			final int dayOfBadiYear) {
		final int yearIndex = badiYear - 171;
		final int nawRuz = nawRuzParameter(yearIndex);
		final int nawRuzLastYear = nawRuzParameter(yearIndex - 1);

		final int badiMonth;
		int yearInVahid;
		int tmpkull;
		// Calculate current Badi date
		int badiDay = dayOfBadiYear % 19;
		if (badiDay == 0) {
			badiDay = 19;
		}

		// Month of Ala (19th month; after Ayyam'i'Ha)
		// Bug will occur in Ala 216!
		if (dayOfBadiYear > 346 + nawRuz * (1 - nawRuzLastYear)) {
			badiMonth = 20;
			badiDay = dayOfBadiYear - 346 - nawRuz * (1 - nawRuzLastYear);
		} else {
			badiMonth = (dayOfBadiYear - badiDay) / 19 + 1;
		}

		yearInVahid = badiYear % 19;
		if (yearInVahid == 0) {
			yearInVahid = 19;
		}
		final int vahid = (badiYear - yearInVahid) / 19 + 1;
		tmpkull = badiYear % 361;
		if (tmpkull == 0) {
			tmpkull = 361;
		}
		final int kull = (badiYear - tmpkull) / 361 + 1;

		// Check if date is a Holy day
		final int holyday = getHolydayIndex(dayOfBadiYear, yearIndex);

		final int[] date = { badiYear, badiMonth, badiDay, dayOfBadiYear,
				holyday, yearInVahid, vahid, kull };
		return date;
	}

	/**
	 * Returns the day of the Badi year from Badi month and day.
	 */
	private static int getBadiDayOfTheYearFromMonthAndDay(final int badiMonth,
			final int badiDay, final int yearIndex) {
		final int bdoy;
		final int nawRuz = nawRuzParameter(yearIndex);
		// special case Month of Ala after Ayyam'i'Ha
		if (badiMonth == 20) {
			bdoy = 346 + nawRuzParameter(yearIndex + 1) * (1 - nawRuz)
					+ badiDay;
		} else {
			bdoy = (badiMonth - 1) * 19 + badiDay;
		}
		return bdoy;
	}

	/**
	 * Returns the day of the Badi year for the Holyday.
	 */
	private static int getHolydayIndex(final int dayOfBadiYear,
			final int yearIndex) {
		for (int i = 0; i < HOLYDAY_DOY.length; i++) {
			final int hdday = getHolydayDoy(yearIndex, i);
			if (hdday == dayOfBadiYear) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the day of the Badi year of the holyday.
	 */
	private static int getHolydayDoy(final int yearIndex, final int holydayIndex) {
		final int hdday = HOLYDAY_DOY[holydayIndex];
		if (yearIndex > 0) {
			if (holydayIndex == 7) {
				return twinBDays(yearIndex);
			} else if (holydayIndex == 8) {
				return twinBDays(yearIndex) + 1;
			}
		}
		return hdday;
	}

	/**
	 * Returns a 1 if the Gregorian year is a leap year, otherwise 0.
	 */
	private static int isLeapYear(final int year) {
		final boolean isleapyear = year % 4 == 0 && year % 100 != 0
				|| year % 400 == 0;
		return isleapyear ? 1 : 0;
	}

	/**
	 * Returns 1 if Naw-Ruz for the Gregorian year is on March 21st; 0 if on
	 * March 20th.
	 */
	private static int nawRuzParameter(final int yearIndex) {
		if (yearIndex < 0) {
			return 1; // Use the western date for Naw-Ruz prior to 172
		}
		if (yearIndex >= NAW_RUZ_OFFSET.length) {
			throw new IllegalArgumentException("Naw-Ruz only defined until "
					+ (2014 + NAW_RUZ_OFFSET.length - 1));
		}
		return NAW_RUZ_OFFSET[yearIndex];
	}

	/**
	 * Return the Day of the year for the Birth of Bab; list for the years
	 * 2014-2064.
	 *
	 * @param yearIndex
	 *            int>=0; 0 for 2014
	 * @return -1 for error otherwise the day of the year.
	 */
	private static int twinBDays(final int yearIndex) {
		if (yearIndex < BIRTH_OF_BAB_DOY.length && yearIndex >= 0) {
			return BIRTH_OF_BAB_DOY[yearIndex];
		} else {
			return -1;
		}
	}

	/**
	 * test run in console with arguments. usage: java Badi year month day
	 * [method] method:1 Gregorian to Badi (default) method:2 Badi to Gregorian
	 * method:3 Next 19 day feast (input Gregorian date) method:4 Next Holyday
	 * in Badi date (input Badi date) method:5 Next Holyday in Gregorian date
	 * (input Gregorian date)
	 *
	 * @param args
	 *            console input
	 */
	public static void main(final String[] args) {
		if (args.length == 0) {
			return;
		}
		final int y = Integer.parseInt(args[0]);
		final int m = Integer.parseInt(args[1]);
		final int d = Integer.parseInt(args[2]);
		final BadiDate badiDate;
		int type = 1;
		final int[] date;
		if (args.length > 3) {
			type = Integer.parseInt(args[3]);
		}
		if (type == 2) {
			date = Badi2Gregorian(y, m, d);
			badiDate = BadiDate.createFromBadiDate(y, m, d);
		} else if (type == 3) {
			date = getNextFeastDate(y, m, d);
			final Calendar calendar = new GregorianCalendar();
			calendar.set(Calendar.YEAR, y);
			calendar.set(Calendar.MONTH, m - 1);
			calendar.set(Calendar.DAY_OF_MONTH, d);
			badiDate = BadiDate.createFromGregorianCalendar(calendar)
					.getNextFeastDate();
		} else if (type == 4) {
			date = getNextHolydayBadiDate(y, m, d);
			badiDate = BadiDate.createFromBadiDate(y, m, d)
					.getNextHolydayDate();
		} else if (type == 5) {
			date = getNextHolydayGregorianDate(y, m, d);
			final Calendar calendar = new GregorianCalendar();
			calendar.set(Calendar.YEAR, y);
			calendar.set(Calendar.MONTH, m - 1);
			calendar.set(Calendar.DAY_OF_MONTH, d);
			badiDate = BadiDate.createFromGregorianCalendar(calendar)
					.getNextHolydayDate();
		} else {
			date = Gregorian2Badi(y, m, d);
			final Calendar calendar = new GregorianCalendar();
			calendar.set(Calendar.YEAR, y);
			calendar.set(Calendar.MONTH, m - 1);
			calendar.set(Calendar.DAY_OF_MONTH, d);
			badiDate = BadiDate.createFromGregorianCalendar(calendar);
			System.out.println("Year in Vahid: " + date[5]);
			System.out.println("Vahid: " + date[6]);
			System.out.println("Kull-i-Shay: " + date[7]);
		}
		System.out.println(date[0] + "/" + date[1] + "/" + date[2]);
		if (date[4] > -1) {
			System.out.println("Holyday: " + getHolyday(date[4]));
		}
		System.out.println("Day of the year: " + date[3]);

		System.out.println("Year in Vahid: " + badiDate.getYearInVahid());
		System.out.println("Vahid: " + badiDate.getVahid());
		System.out.println("Kull-i-Shay: " + badiDate.getKullIShay());
		System.out.println(badiDate.getBadiYear() + "/"
				+ badiDate.getBadiMonth() + "/" + badiDate.getBadiDay());
		System.out.println(badiDate.getGregorianYear() + "/"
				+ badiDate.getGregorianMonth() + "/"
				+ badiDate.getGregorianDay());
		System.out.println("Day of the Badi year: "
				+ badiDate.getBadiDayOfYear());
		System.out.println("Day of the Gregorian year: "
				+ badiDate.getGregorianDayOfYear());
		final BahaiHolyday bahaiHolyday = badiDate.getHolyday();
		if (bahaiHolyday != null) {
			System.out.println(bahaiHolyday.asString());
		}
	}
}
