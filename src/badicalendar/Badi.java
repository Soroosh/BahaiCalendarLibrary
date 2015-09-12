/**
 * Version 1.2
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
package badicalendar;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Calculates current Badi and Gregorian date (from 1900-2064). Initial author
 * Soroosh Pezeshki April 2015
 */

public class Badi {

    private static final byte[] NAW_RUZ = { 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0,
            1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1,
            0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private static final String[] HOLYDAYS = { "Naw-Ruz", "1st Ridvan",
            "9th Ridvan", "12th Ridvan", "Decleration of the Bab",
            "Ascension of Baha'u'llah", "Martyrdom of the Bab",
            "Birth of the Bab", "Birth of Baha'u'llah", "Day of the Covenant",
            "Ascension of Abdu'l-Baha" };
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
        int gregorianYear = badiYear + 1843;
        final int yearIndex = gregorianYear - 2014;
        final int leapYear = isLeapYear(gregorianYear);
        final int nawRuz = nawRuzParameter(yearIndex);
        final int bdoy;
        int doy;

        // special case Month of Ala after Ayyam'i'Ha
        if (badiMonth == 20) {
            bdoy = 346 + nawRuzParameter(yearIndex + 1) * (1 - nawRuz)
                    + badiDay;
        } else {
            bdoy = (badiMonth - 1) * 19 + badiDay;
        }
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

        final int holyday = getHolydayIndex(bdoy, yearIndex);

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
        final int badiYear;
        final int dayOfBadiYear;
        final int yearIndex = year - 2014;
        final int leapyear = isLeapYear(year);
        final int nawRuz = nawRuzParameter(yearIndex);
        final int nawRuzLastYear = nawRuzParameter(yearIndex - 1);
        final int badiMonth;
        int yearInVahid;
        int tmpkull;

        final Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        final int doy = calendar.get(Calendar.DAY_OF_YEAR);
        // Calculate day number of the Badi Year
        if (doy < 79 + leapyear + nawRuz) {
            dayOfBadiYear = doy + 287 - nawRuzLastYear;
            badiYear = year - 1844;
        } else {
            dayOfBadiYear = doy - 78 - leapyear - nawRuz;
            badiYear = year - 1843;
        }

        // Calculate current Badi date
        int badiDay = dayOfBadiYear % 19;
        if (badiDay == 0)
            badiDay = 19;

        // Month of Ala (19th month; after Ayyam'i'Ha)
        // Bug will occur in Ala 216!
        if ((dayOfBadiYear > (346 + nawRuz * (1 - nawRuzLastYear)))) {
            badiMonth = 20;
            badiDay = dayOfBadiYear - 346 - nawRuz * (1 - nawRuzLastYear);
        } else {
            badiMonth = ((dayOfBadiYear - badiDay) / 19 + 1);
        }

        yearInVahid = badiYear % 19;
        if (yearInVahid == 0) {
            yearInVahid = 19;
        }
        final int vahid = (badiYear - yearInVahid) / 19 + 1;
        tmpkull = badiYear % 361;
        if (tmpkull == 0)
            tmpkull = 361;
        final int kull = (badiYear - tmpkull) / 361 + 1;

        // Check if date is a Holy day
        final int holyday = getHolydayIndex(dayOfBadiYear, yearIndex);

        final int[] date = { badiYear, badiMonth, badiDay, dayOfBadiYear,
                holyday, yearInVahid, vahid, kull };
        return date;
    }

    /**
     * Returns the day in March that is Naw-Ruz.
     */
    static int nawRuzDayOfMarch(final int gregorianYear) {
        if (gregorianYear < 1900)
            throw new IllegalArgumentException(
                    "Naw-Ruz only defined for dates after 1900");
        if (gregorianYear < 2014)
            return 21; // Use the western date for Naw-Ruz prior to 2014
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
     * Returns an int for the Holyday.
     */
    private static int getHolydayIndex(final int dayOfBadiYear,
            final int yearIndex) {
        for (int i = 0; i < 11; i++) {
            int hdday = holydays(i);
            if (yearIndex > 0) {
                if (i == 7) {
                    hdday = twinBDays(yearIndex);
                } else if (i == 8) {
                    hdday = twinBDays(yearIndex) + 1;
                }
            }
            if (hdday == dayOfBadiYear)
                return i;
        }
        return -1;
    }

    /**
     * Returns a 1 if the Gregorian year is a leap year, otherwise 0.
     */
    private static int isLeapYear(final int year) {
        final boolean isleapyear = ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0));
        return (isleapyear) ? 1 : 0;
    }

    /**
     * Returns the day of the year of a Holy day. the values for the Birth of
     * Bab and Baha'u'llah are placeholder and are read from another array
     */
    private static int holydays(final int i) {
        final int[] hdArray = { 1, 32, 40, 43, 65, 70, 112, 214, 237, 251, 253 };
        return hdArray[i];
    }

    /**
     * 1 if Naw-Ruz for the Gregorian year is on March 21st; 0 when on March
     * 20th.
     */
    private static int nawRuzParameter(final int yearIndex) {
        if (yearIndex < 0)
            return 1; // Use the western date for Naw-Ruz prior to 172
        if (yearIndex >= NAW_RUZ.length)
            throw new IllegalArgumentException("Naw-Ruz only defined until "
                    + (2014 + NAW_RUZ.length - 1));
        return NAW_RUZ[yearIndex];
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
        final int[] nrArray = { 217, 238, 227, 216, 234, 223, 213, 232, 220,
                210, 228, 217, 235, 224, 214, 233, 223, 211, 230, 219, 238,
                226, 215, 234, 224, 213, 232, 221, 210, 228, 217, 236, 225,
                214, 233, 223, 212, 230, 219, 237, 227, 215, 234, 224, 213,
                232, 220, 209, 228, 218, 236 };
        if (yearIndex < nrArray.length && yearIndex >= 0) {
            return nrArray[yearIndex];
        } else {
            return -1;
        }
    }

    /**
     * test run in console with arguments. usage: java Badi year month day
     * [method] method:1 Gregorian to Badi (default) method:2 Badi to Gregorian
     * 
     * @param args
     *            console input
     */
    public static void main(final String[] args) {
        final int y = Integer.parseInt(args[0]);
        final int m = Integer.parseInt(args[1]);
        final int d = Integer.parseInt(args[2]);
        int type = 1;
        final int[] date;
        if (args.length > 3)
            type = Integer.parseInt(args[3]);
        if (type == 2) {
            date = Badi2Gregorian(y, m, d);
        } else {
            date = Gregorian2Badi(y, m, d);
        }
        System.out.println(date[0] + "/" + date[1] + "/" + date[2]);
        if (date[4] > -1)
            System.out.println("Holyday: " + getHolyday(date[4]));
        System.out.println("Day of the year: " + date[3]);
        if (type != 2) {
            System.out.println("Year in Vahid: " + date[5]);
            System.out.println("Vahid: " + date[6]);
            System.out.println("Kull-i-Shay: " + date[7]);
        }
    }

}
