package BahaiCalendarLibrary;

public enum BahaiHolydays {
	NAW_RUZ(1), RIDVAN1ST(32), RIDVAN9TH(40), RIDVAN12TH(43), DECLEARTION_OF_THE_BAB(
			65), ASCENSION_OF_BAHAULLAH(70), MARTYRDOM_OF_THE_BAB(112), BIRTH_OF_THE_BAB(
			214), BIRTH_OF_BAHAULLAH(237), DAY_OF_THE_COVENANT(251), ASCENSION_OF_ABDUL_BAHA(
			253);

	private final int _dayOfYear;

	private BahaiHolydays(final int dayOfyear) {
		_dayOfYear = dayOfyear;
	}

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
	public static final int UPPER_YEAR_LIMIT_BADI = 171 + BIRTH_OF_BAB_DOY.length - 1;

}
