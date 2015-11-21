package de.pezeshki.bahaiCalendarLibrary;

import java.util.Arrays;
import java.util.List;

public enum BahaiHolyday {
	NAW_RUZ(0), //
	RIDVAN1ST(1), //
	RIDVAN9TH(2), //
	RIDVAN12TH(3), //
	DECLEARTION_OF_THE_BAB(4), //
	ASCENSION_OF_BAHAULLAH(5), //
	MARTYRDOM_OF_THE_BAB(6), //
	BIRTH_OF_THE_BAB(7), //
	BIRTH_OF_BAHAULLAH(8), //
	DAY_OF_THE_COVENANT(9), //
	ASCENSION_OF_ABDUL_BAHA(10);

	private final int _index;

	private BahaiHolyday(final int index) {
		_index = index;
	}

	private static final int[] BIRTH_OF_BAB_DOY = { 214, 238, 227, 216, 234,
		223, 213, 232, 220, 210, 228, 217, 235, 224, 214, 233, 223, 211,
		230, 219, 238, 226, 215, 234, 224, 213, 232, 221, 210, 228, 217,
		236, 225, 214, 233, 223, 212, 230, 219, 237, 227, 215, 234, 224,
		213, 232, 220, 209, 228, 218, 236 };

	private static final List<Integer> HOLYDAY_DOY = Arrays.asList(1, 32, 40,
			43, 65, 70, 112, 214, 237, 251, 253);

	private static final String[] HOLYDAYS = { "Naw-Ruz", "1st Ridvan",
		"9th Ridvan", "12th Ridvan", "Decleration of the Bab",
		"Ascension of Baha'u'llah", "Martyrdom of the Bab",
		"Birth of the Bab", "Birth of Baha'u'llah", "Day of the Covenant",
	"Ascension of Abdu'l-Baha" };

	private static final int FIRST_YEAR = 171;

	/**
	 * The upper limit for the Badi year. Not enough data yet for years beyond
	 * this limit.
	 */
	public static final int UPPER_YEAR_LIMIT_BADI = FIRST_YEAR
			+ BIRTH_OF_BAB_DOY.length - 1;

	/**
	 * Returns the Baha'i Holyday
	 *
	 * @param dayOfYear
	 *            the day of the Badi year
	 * @param badiYear
	 *            the Badi year
	 * @return the Baha'i Holyday
	 */
	public static BahaiHolyday getHolyday(final int dayOfYear,
			final int badiYear) throws IllegalArgumentException {

		final int yearIndex = badiYear - FIRST_YEAR;

		// Special case: Birth of the Bab and Baha'u'llah are not fixed in
		// the calendar from 172
		if (yearIndex > 0) {
			if (badiYear > UPPER_YEAR_LIMIT_BADI) {
				throw new IllegalArgumentException(
						"Badi year has to be less than  "
								+ UPPER_YEAR_LIMIT_BADI);
			}
			if (BIRTH_OF_BAB_DOY[yearIndex] == dayOfYear) {
				return BahaiHolyday.BIRTH_OF_THE_BAB;
			} else if (BIRTH_OF_BAB_DOY[yearIndex] + 1 == dayOfYear) {
				return BahaiHolyday.BIRTH_OF_BAHAULLAH;
			}
		}

		// The other holydays and the birth of the Bab and Baha'u'llah
		// before 172.
		final Integer i = HOLYDAY_DOY.indexOf(dayOfYear);
		switch (i) {
		case 0:
			return BahaiHolyday.NAW_RUZ;
		case 1:
			return BahaiHolyday.RIDVAN1ST;
		case 2:
			return BahaiHolyday.RIDVAN9TH;
		case 3:
			return BahaiHolyday.RIDVAN12TH;
		case 4:
			return BahaiHolyday.DECLEARTION_OF_THE_BAB;
		case 5:
			return BahaiHolyday.ASCENSION_OF_BAHAULLAH;
		case 6:
			return BahaiHolyday.MARTYRDOM_OF_THE_BAB;
		case 9:
			return BahaiHolyday.DAY_OF_THE_COVENANT;
		case 10:
			return BahaiHolyday.ASCENSION_OF_ABDUL_BAHA;
		case 7:
			if (yearIndex < 1) {
				return BahaiHolyday.BIRTH_OF_THE_BAB;
			}
		case 8:
			if (yearIndex < 1) {
				return BahaiHolyday.BIRTH_OF_BAHAULLAH;
			}
		}
		return null;
	}

	/**
	 * Returns the name of the Holyday in English.
	 */
	public String asString() {
		return HOLYDAYS[_index];
	}

	/**
	 * Returns the index of the holyday.
	 * NAW_RUZ==0,...,ASCENSION_OF_ABDUL_BAHA==9
	 */
	public int getIndex() {
		return _index;
	}

	/**
	 * Returns the day of the Badi year of the holyday.
	 */
	public static int getDayOfYear(final int badiYear, final int index) {

		final int yearIndex = badiYear - FIRST_YEAR;

		// Special case: Birth of the Bab and Baha'u'llah after 171.
		if (yearIndex > 0 && (index == 7 || index == 8)) {
			if (yearIndex - 171 > UPPER_YEAR_LIMIT_BADI) {
				throw new IllegalArgumentException(
						"Badi year has to be less than  "
								+ UPPER_YEAR_LIMIT_BADI);
			}
			return BIRTH_OF_BAB_DOY[yearIndex] + index - 7;
		}
		return HOLYDAY_DOY.get(index);
	}

	/**
	 * Returns the day of the Badi year of the next holyday. If the input day is
	 * a feast day the one after that is shown.
	 *
	 */
	public static BadiDate getNextHolydayDate(final BadiDate badiDate)
			throws IllegalArgumentException {

		final int badiYear = badiDate.getBadiYear();
		final int dayOfYear = badiDate.getBadiDayOfYear();
		final int yearIndex = badiYear - FIRST_YEAR;
		if (badiYear > UPPER_YEAR_LIMIT_BADI) {
			throw new IllegalArgumentException(
					"Badi year has to be less than  " + UPPER_YEAR_LIMIT_BADI);
		}

		for (int doyHolyday : HOLYDAY_DOY) {
			final Integer index = HOLYDAY_DOY.indexOf(doyHolyday);

			// Special case: Birth of the Bab and Baha'u'llah after 171.
			if (yearIndex > 0 && (index == 7 || index == 8)) {
				doyHolyday = BIRTH_OF_BAB_DOY[yearIndex] + index - 7;
			}
			if (doyHolyday > dayOfYear) {
				return BadiDate.createFromBadiYearAndDayOfYear(badiYear,
						doyHolyday);
			}
		}
		// If the last Holyday of the year passt, the next holyday is going to
		// be Naw-Ruz.
		return BadiDate.createFromBadiYearAndDayOfYear(badiYear + 1, 1);
	}
}
