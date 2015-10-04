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
package BahaiCalendarLibrary;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * The Badi and Gregorian date converter (from 1900-2064). Initial author
 * Soroosh Pezeshki 2015
 */
public class BadiDate implements BaseBadiDate {

	private static final byte[] NAW_RUZ_OFFSET = { 1, 1, 0, 0, 1, 1, 0, 0, 1,
			1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0,
			0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private static final int[] BIRTH_OF_BAB_DOY = { 217, 238, 227, 216, 234,
			223, 213, 232, 220, 210, 228, 217, 235, 224, 214, 233, 223, 211,
			230, 219, 238, 226, 215, 234, 224, 213, 232, 221, 210, 228, 217,
			236, 225, 214, 233, 223, 212, 230, 219, 237, 227, 215, 234, 224,
			213, 232, 220, 209, 228, 218, 236 };
	// TODO: change to enum
	private static final int[] HOLYDAY_DOY = { 1, 32, 40, 43, 65, 70, 112, 214,
			237, 251, 253 };
	private static final String[] HOLYDAYS = { "Naw-Ruz", "1st Ridvan",
			"9th Ridvan", "12th Ridvan", "Decleration of the Bab",
			"Ascension of Baha'u'llah", "Martyrdom of the Bab",
			"Birth of the Bab", "Birth of Baha'u'llah", "Day of the Covenant",
			"Ascension of Abdu'l-Baha" };
	public static final int UPPER_YEAR_LIMIT = 2014 + NAW_RUZ_OFFSET.length - 1;
	public static final int UPPER_YEAR_LIMIT_BADI = 171 + NAW_RUZ_OFFSET.length - 1;
	public static final int ZEROTH_BADI_YEAR_AS_GREGORIAN_YEAR = 1843;
	public static final int DAYS_IN_BADI_MONTH = 19;
	public static final int DAYS_BETWEEN_NAWRUZ_AND_AYYAMIHA_START = 18 * DAYS_IN_BADI_MONTH;

	private final int _badiDay;
	private final int _badiMonth;
	private final int _badiYear;
	private final int _badiDoy;
	private final Calendar _gregDate;
	private final int _yearIndex;
	private final int _yearInVahid;
	private final int _vahid;
	private final int _kullIShay;

	private BadiDate(final int badiDay, final int badiMonth,
			final int badiYear, final int badiDoy, final Calendar gregDate,
			final int yearIndex) {
		_badiDay = badiDay;
		_badiMonth = badiMonth;
		_badiYear = badiYear;
		_badiDoy = badiDoy;
		_gregDate = gregDate;
		_yearIndex = yearIndex;
		_yearInVahid = badiYear % 19 == 0 ? 19 : badiYear % 19;
		_vahid = (badiYear - _yearInVahid) / 19 + 1;
		int tmpkull = badiYear % 361;
		if (tmpkull == 0) {
			tmpkull = 361;
		}
		_kullIShay = (badiYear - tmpkull) / 361 + 1;

	}

	/**
	 * Creates a BadiDate from Badi year, month and day.
	 *
	 * @param badiYear
	 * @param badiMonth
	 * @param badiDay
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static BadiDate createFromBadiDate(final int badiYear,
			final int badiMonth, final int badiDay)
					throws IllegalArgumentException {
		try {
			checkBadiDayAndMonthForValidity(badiDay, badiMonth);
			checkBadiYearForValidity(badiYear);
		} catch (final IllegalArgumentException e) {
			throw new IllegalArgumentException(e);
		}
		final int yearIndex = badiYear - 171;
		final int badiDoy = getBadiDayOfTheYearFromMonthAndDay(badiMonth,
				badiDay, yearIndex);
		final Calendar gregorianDate = gregorianDateFromBadiDoyAndYear(
				badiYear, badiDoy);
		return new BadiDate(badiDay, badiMonth, badiYear, badiDoy,
				gregorianDate, yearIndex);
	}

	/**
	 * Creates a BadiDate from Badi year and day of year.
	 *
	 * @param badiYear
	 * @param badiDayOfYear
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static BadiDate createFromBadiYearAndDayOfYear(final int badiYear,
			final int badiDayOfYear) throws IllegalArgumentException {
		try {
			checkDoyForValidity(badiDayOfYear);
			checkBadiYearForValidity(badiYear);
		} catch (final IllegalArgumentException e) {
			throw new IllegalArgumentException(e);
		}
		final int yearIndex = badiYear - 171;
		final int[] badiDayAndMonth = getBadiDayAndMonth(badiDayOfYear,
				yearIndex);
		final Calendar gregorianDate = gregorianDateFromBadiDoyAndYear(
				badiYear, badiDayOfYear);
		return new BadiDate(badiDayAndMonth[0], badiDayAndMonth[1], badiYear,
				badiDayOfYear, gregorianDate, yearIndex);
	}

	/**
	 * Creates a BadiDate from a Gregorian Calendar.
	 *
	 * @param badiYear
	 * @param badiDayOfYear
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static BadiDate createFromGregorianCalendar(
			final Calendar gregorianDate) throws IllegalArgumentException {
		final Calendar calendar = (Calendar) gregorianDate.clone();
		final int year = calendar.get(Calendar.YEAR);
		try {
			checkGregorianYearForValidity(year);
			;
		} catch (final IllegalArgumentException e) {
			throw new IllegalArgumentException(e);
		}
		final int doy = calendar.get(Calendar.DAY_OF_YEAR);
		final int yearIndex = calendar.get(Calendar.YEAR) - 2014;
		final int leapyear = isLeapYear(year);
		final int nawRuz = nawRuzParameter(yearIndex);
		final int nawRuzLastYear = nawRuzParameter(yearIndex - 1);

		final int badiYear;
		final int badiDayOfYear;
		// Calculate day number of the Badi Year
		if (doy < 79 + leapyear + nawRuz) {
			badiDayOfYear = doy + 287 - nawRuzLastYear;
			badiYear = year - 1844;
		} else {
			badiDayOfYear = doy - 78 - leapyear - nawRuz;
			badiYear = year - 1843;
		}
		final int[] badiDayAndMonth = getBadiDayAndMonth(badiDayOfYear,
				yearIndex);
		return new BadiDate(badiDayAndMonth[0], badiDayAndMonth[1], badiYear,
				badiDayOfYear, calendar, yearIndex);
	}

	@Override
	public int getBadiDay() {
		return _badiDay;
	}

	@Override
	public int getBadiMonth() {
		return _badiMonth;
	}

	@Override
	public int getBadiYear() {
		return _badiYear;
	}

	@Override
	public int getBadiDayOfYear() {
		return _badiDoy;
	}

	@Override
	public int getGregorianDay() {
		return _gregDate.get(Calendar.DAY_OF_MONTH);
	}

	@Override
	public int getGregorianMonth() {
		return _gregDate.get(Calendar.MONTH) + 1;
	}

	@Override
	public int getGregorianYear() {
		return _gregDate.get(Calendar.YEAR);
	}

	@Override
	public int getGregorianDayOfYear() {
		return _gregDate.get(Calendar.DAY_OF_YEAR);
	}

	@Override
	public Calendar getCalendar() {
		return (Calendar) _gregDate.clone();
	}

	@Override
	public int getHolyday() {
		return getHolydayIndex(_badiDoy, _yearIndex);
	}

	private static void checkBadiDayAndMonthForValidity(final int badiDay,
			final int badiMonth) throws IllegalArgumentException {
		if (badiDay < 1) {
			throw new IllegalArgumentException(
					"Badi day has to be greater than or equal to 1.");
		}
		if (badiDay > 19) {
			throw new IllegalArgumentException(
					"Badi day has to be less than or equal to  19.");
		}
		if (badiMonth < 1) {
			throw new IllegalArgumentException(
					"Badi month has to be greater than or equal to 1.");
		}
		if (badiMonth > 20) {
			throw new IllegalArgumentException(
					"Badi month has to be less than or equal to  20.");
		}
	}

	private static void checkDoyForValidity(final int badiDoy)
			throws IllegalArgumentException {
		if (badiDoy < 1) {
			throw new IllegalArgumentException(
					"Day of the year has to be greater than or equal to 1.");
		}
		if (badiDoy > 366) {
			throw new IllegalArgumentException(
					"Day of the year has to be less than or equal to  367.");
		}

	}

	private static void checkBadiYearForValidity(final int badiYear)
			throws IllegalArgumentException {
		if (badiYear < 1) {
			throw new IllegalArgumentException(
					"Badi year has to be greater than or equal to 1.");
		}
		if (badiYear > UPPER_YEAR_LIMIT_BADI) {
			throw new IllegalArgumentException(
					"Badi year has to be less than  " + UPPER_YEAR_LIMIT_BADI);
		}

	}

	private static void checkGregorianYearForValidity(final int year)
			throws IllegalArgumentException {
		if (year < ZEROTH_BADI_YEAR_AS_GREGORIAN_YEAR) {
			throw new IllegalArgumentException(
					"Year has to be greater than or equal to 1844.");
		}
		if (year > UPPER_YEAR_LIMIT) {
			throw new IllegalArgumentException("Year has to be less than  "
					+ UPPER_YEAR_LIMIT);
		}

	}

	/**
	 * Returns the date of the next feast (or 1st day of Ayyam'i'Ha). If the
	 * input day is a feast day the one after that is shown.
	 *
	 */
	public BadiDate getNextFeastDate() {
		if (_badiMonth == 20) {
			return createFromBadiDate(_badiYear + 1, 1, 1);
		}
		return createFromBadiDate(_badiYear, _badiMonth + 1, 1);
	}

	/**
	 * Returns the date of the next holyday. If the input day is a feast day the
	 * one after that is shown.
	 *
	 */
	public BadiDate getNextHolydayDate() {
		for (int i = 1; i < HOLYDAY_DOY.length; i++) {
			final int hd = getHolydayDoy(_yearIndex, i);
			if (hd > _badiDoy) {
				return createFromBadiYearAndDayOfYear(_badiYear, hd);
			}
		}
		return createFromBadiYearAndDayOfYear(_badiYear + 1, 1);
	}

	/**
	 * Returns the day in March that is Naw-Ruz.
	 */
	int nawRuzDayOfMarch() {
		if (_yearIndex < 0) {
			return 21; // Use the western date for Naw-Ruz prior to 2014
		}
		return 20 + nawRuzParameter(_yearIndex);
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
	 * Returns the day of the Badi year from Badi month and day.
	 */
	private static int[] getBadiDayAndMonth(final int badiDoy,
			final int yearIndex) {
		final int nawRuz = nawRuzParameter(yearIndex);
		final int nawRuzLastYear = nawRuzParameter(yearIndex - 1);
		// Month of Ala (19th month; after Ayyam'i'Ha)
		// Bug will occur in Ala 216!
		if (badiDoy > 346 + nawRuz * (1 - nawRuzLastYear)) {
			final int badiDay = badiDoy - 346 - nawRuz * (1 - nawRuzLastYear);
			return new int[] { badiDay, 20 };
		}
		final int t = badiDoy % 19 == 0 ? 19 : badiDoy % 19;
		final int m = (badiDoy - t) / 19 + 1;
		return new int[] { t, m };
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
	 * Returns the Gregorian Calendar calculated from the Badi year and day of
	 * the year.
	 *
	 * @param badiYear
	 * @param bdoy
	 * @return
	 */
	private static Calendar gregorianDateFromBadiDoyAndYear(final int badiYear,
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

		final Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, gregorianYear);
		calendar.set(Calendar.DAY_OF_YEAR, doy);

		return calendar;
	}

	@Override
	public int getVahid() {
		return _vahid;
	}

	@Override
	public int getYearInVahid() {
		return _yearInVahid;
	}

	@Override
	public int getKullIShay() {
		return _kullIShay;
	}
}
