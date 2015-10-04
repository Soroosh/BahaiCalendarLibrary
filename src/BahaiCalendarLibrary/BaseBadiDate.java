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

/**
 * The Badi and Gregorian date converter (from 1900-2064). Initial author
 * Soroosh Pezeshki 2015
 */
public interface BaseBadiDate {

	/**
	 * Returns the Badi day of the month
	 */
	int getBadiDay();

	/**
	 * Returns the Badi month. Baha =1; Ayyam'i'Ha=19; Ala=20;
	 */
	int getBadiMonth();

	/**
	 * Returns the Badi year.
	 */
	int getBadiYear();

	/**
	 * Returns the Badi day of the year
	 */
	int getBadiDayOfYear();

	/**
	 * Returns the Gregorian day of the month.
	 */
	int getGregorianDay();

	/**
	 * Returns the Gregorian month. January=1; December=12;
	 */
	int getGregorianMonth();

	/**
	 * Returns the Gregorian year.
	 */
	int getGregorianYear();

	/**
	 * Returns the Gregorian day of the year.
	 */
	int getGregorianDayOfYear();

	/**
	 * Returns the Calendar for the Gregorian date.
	 */
	Calendar getCalendar();

	/**
	 * Returns an index for the Baha'i Holyday.
	 */
	int getHolyday();

	/**
	 * Returns the Vahid.
	 */
	int getVahid();

	/**
	 * Returns year in Vahid.
	 */
	int getYearInVahid();

	/**
	 * Returns the Kull'i'Shay.
	 */
	int getKullIShay();

}
