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
package de.pezeshki.bahaiCalendarLibrary;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.base.BaseDateTime;

/**
 * The Badi and Gregorian date converter (from 1900-2064). Initial author
 * Soroosh Pezeshki 2015
 */
public class BadiDate implements BaseBadiDate {

	private static final byte[] NAW_RUZ_OFFSET = { 1, 1, 0, 0, 1, 1, 0, 0, 1,
			1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0,
			0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
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
	private final int _gregorianYear;
	private final int _gregorianMonth;
	private final int _gregorianDay;
	private final BahaiHolyday _holyday;

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
		final int tmpkull = badiYear % 361;
		// Uncomment if data is available
		//		if (tmpkull == 0) {
		//			tmpkull = 361;
		//		}
		_kullIShay = (badiYear - tmpkull) / 361 + 1;
		_gregorianDay = _gregDate.get(Calendar.DAY_OF_MONTH);
		_gregorianMonth = _gregDate.get(Calendar.MONTH) + 1;
		_gregorianYear = _gregDate.get(Calendar.YEAR);
		_holyday = BahaiHolyday.getHolyday(_badiDoy, _badiYear);
	}

	/**
	 * Creates a BadiDate from Badi year, month and day.
	 *
	 * @param badiYear
	 *            the Baha'i year
	 * @param badiMonth
	 *            the Baha'i month
	 * @param badiDay
	 *            the Baha'i day of the month
	 * @return the Badi date
	 * @throws IllegalArgumentException
	 *             arguments are out of bound
	 */
	public static BadiDate createFromBadiDate(final int badiYear,
			final int badiMonth, final int badiDay)
			throws IllegalArgumentException {
		checkBadiDayAndMonthForValidity(badiDay, badiMonth);
		checkBadiYearForValidity(badiYear);
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
	 *            the Baha'i year
	 * @param badiDayOfYear
	 *            the day of the Baha'i year
	 * @return the Badi date
	 * @throws IllegalArgumentException
	 *             year or day of the year are out of bound
	 */
	public static BadiDate createFromBadiYearAndDayOfYear(final int badiYear,
			final int badiDayOfYear) throws IllegalArgumentException {
		checkDoyForValidity(badiDayOfYear);
		checkBadiYearForValidity(badiYear);
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
	 * @param Calendar
	 *            (GregorianCalendar)
	 * @return The Badi date
	 * @throws IllegalArgumentException
	 *             Year is less than 1844 or greater than UPPER_YEAR_LIMIT
	 */
	public static BadiDate createFromGregorianCalendar(final Calendar calendar)
			throws IllegalArgumentException {
		final int year = calendar.get(Calendar.YEAR);
		final int doy = calendar.get(Calendar.DAY_OF_YEAR);
		checkGregorianYearForValidity(year);
		return createFromGregorianDoyAndYear(year, doy);
	}

	/**
	 * Creates a BadiDate from Joda DateTime.
	 *
	 * @param Joda
	 *            DateTime
	 * @return The Badi date
	 * @throws IllegalArgumentException
	 *             Year is less than 1844 or greater than UPPER_YEAR_LIMIT
	 */
	public static BadiDate createFromDateTime(final BaseDateTime gregorianDate)
			throws IllegalArgumentException {
		final int year = gregorianDate.getYear();
		checkGregorianYearForValidity(year);
		final int doy = gregorianDate.getDayOfYear();
		return createFromGregorianDoyAndYear(year, doy);
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
		return _gregorianDay;
	}

	@Override
	public int getGregorianMonth() {
		return _gregorianMonth;
	}

	@Override
	public int getGregorianYear() {
		return _gregorianYear;
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
	public BaseDateTime getDateTime() {
		return new DateTime(_gregorianYear, _gregorianMonth, _gregorianDay, 0,
				0);
	}

	@Override
	public BahaiHolyday getHolyday() {
		return _holyday;
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
		if (year <= ZEROTH_BADI_YEAR_AS_GREGORIAN_YEAR) {
			throw new IllegalArgumentException(
					"Year has to be greater than or equal to 1844.");
		}
		if (year > UPPER_YEAR_LIMIT) {
			throw new IllegalArgumentException("Year has to be less than  "
					+ UPPER_YEAR_LIMIT);
		}

	}

	@Override
	public BadiDate getNextFeastDate() {
		if (_badiMonth == 20) {
			return createFromBadiDate(_badiYear + 1, 1, 1);
		}
		return createFromBadiDate(_badiYear, _badiMonth + 1, 1);
	}

	@Override
	public BadiDate getNextHolydayDate() {
		return BahaiHolyday.getNextHolydayDate(this);
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
	static int nawRuzParameter(final int yearIndex) {
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

	private static BadiDate createFromGregorianDoyAndYear(final int year,
			final int doy) {
		final int yearIndex = year - 2014;
		final Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.DAY_OF_YEAR, doy);
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
	

	/**
	 * Creates a BadiDate from a Gregorian Calendar and considers if the sun has set.
	 *
	 * @param Calendar
	 *            (GregorianCalendar)
	 * @param sunset
	 * 			  Has the sun set?           
	 * @return The Badi date
	 * @throws IllegalArgumentException
	 *             Year is less than 1844 or greater than UPPER_YEAR_LIMIT
	 */
	public static BadiDate createFromGregorianCalendarWithSunset(final Calendar calendar, 
			final boolean sunset) throws IllegalArgumentException {
		
		final int year = calendar.get(Calendar.YEAR);
		final int doy = calendar.get(Calendar.DAY_OF_YEAR) + (sunset==true ? 1 : 0);
		checkGregorianYearForValidity(year);
		return createFromGregorianDoyAndYear(year, doy);
	}

	/**
	 * Creates a BadiDate from Joda DateTime and considers if the sun has set.
	 *
	 * @param Joda
	 *            DateTime
	 * @param sunset
	 * 			  Has the sun set?           
	 * @return The Badi date
	 * @throws IllegalArgumentException
	 *             Year is less than 1844 or greater than UPPER_YEAR_LIMIT
	 */
	public static BadiDate createFromDateTimeWithSunset(final BaseDateTime gregorianDate,
			final boolean sunset) throws IllegalArgumentException {
		final int year = gregorianDate.getYear();
		checkGregorianYearForValidity(year);
		final int doy = gregorianDate.getDayOfYear() + (sunset==true ? 1 : 0);
		return createFromGregorianDoyAndYear(year, doy);
	}

}
