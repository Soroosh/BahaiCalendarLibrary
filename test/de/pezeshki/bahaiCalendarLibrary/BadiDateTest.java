package de.pezeshki.bahaiCalendarLibrary;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.pezeshki.bahaiCalendarLibrary.BahaiHolyday;
import de.pezeshki.bahaiCalendarLibrary.BadiDate;

public class BadiDateTest {

	  @Test
	  public void createFromBadiDateTest() {
		  final BadiDate januaryFirst = BadiDate.createFromBadiDate(174, 16, 3);
		  Assert.assertEquals(januaryFirst.getGregorianDay(), 1);
		  Assert.assertEquals(januaryFirst.getGregorianMonth(), 1);
		  Assert.assertEquals(januaryFirst.getGregorianYear(), 2018);

		  final BadiDate nawRuz = BadiDate.createFromBadiDate(171, 1, 1);
		  Assert.assertEquals(nawRuz.getGregorianDay(), 21);
		  Assert.assertEquals(nawRuz.getGregorianMonth(), 3);
		  Assert.assertEquals(nawRuz.getGregorianYear(), 2014);
	  }
	  
	  @Test(expectedExceptions = {IllegalArgumentException.class}, expectedExceptionsMessageRegExp = "Badi month has to be greater than or equal to 1.")
	  public void throwsExceptionOnBadiMonthSmaller1() throws IllegalArgumentException {
		  BadiDate.createFromBadiDate(174, 0, 3);
	  }
	  
	  @Test(expectedExceptions = {IllegalArgumentException.class}, expectedExceptionsMessageRegExp = "Badi day has to be greater than or equal to 1.")
	  public void throwsExceptionOnBadiDaySmaller1() throws Exception {
		  BadiDate.createFromBadiDate(174, 20, 0);
	  }
  
	  @Test(expectedExceptions = {IllegalArgumentException.class}, expectedExceptionsMessageRegExp = "Badi month has to be less than or equal to  20.")
	  public void throwsExceptionOnBadiMonthBigger20() throws Exception {
		  BadiDate.createFromBadiDate(174, 21, 3);
	  }
	  
	  @Test(expectedExceptions = {IllegalArgumentException.class}, expectedExceptionsMessageRegExp = "Badi day has to be less than or equal to  19.")
	  public void throwsExceptionOnBadiDayBigger19() throws Exception {
		  BadiDate.createFromBadiDate(174, 20, 20);
	  }
	  
	  @Test(expectedExceptions = {IllegalArgumentException.class}, expectedExceptionsMessageRegExp = "Badi year has to be greater than or equal to 1.")
	  public void throwsExceptionOnBadiYearSmaller1() throws Exception {
		  BadiDate.createFromBadiYearAndDayOfYear(0, 1);
	  }
	  
	  @Test(expectedExceptions = {IllegalArgumentException.class}, expectedExceptionsMessageRegExp = "Badi year has to be less than  221")
	  public void throwsExceptionOnBadiYearBiggerLimit() throws Exception {
		  BadiDate.createFromBadiYearAndDayOfYear(222, 1);
	  }
	  
	  @Test(expectedExceptions = {IllegalArgumentException.class}, expectedExceptionsMessageRegExp = "Day of the year has to be greater than or equal to 1.")
	  public void throwsExceptionOnBadiDoySmaller1() throws Exception {
		  BadiDate.createFromBadiYearAndDayOfYear(174, 0);
	  }
	  
	  @Test(expectedExceptions = {IllegalArgumentException.class}, expectedExceptionsMessageRegExp = "Day of the year has to be less than or equal to  367.")
	  public void throwsExceptionOnBadiDoyBigger366() throws Exception {
		  BadiDate.createFromBadiYearAndDayOfYear(174, 367);
	  }

	  @Test
	  public void createFromBadiYearAndDoyTest() {
		  final BadiDate nawRuz = BadiDate.createFromBadiYearAndDayOfYear(174, 1);
		  final Calendar march20 = new GregorianCalendar();
		  march20.set(2017,2,20);
		  final DateTime march = new DateTime(2017, 3, 20, 0, 0);
		  Assert.assertEquals(nawRuz.getBadiDay(), 1);
		  Assert.assertEquals(nawRuz.getBadiMonth(), 1);
		  Assert.assertEquals(nawRuz.getGregorianYear(), 2017);
		  Assert.assertEquals(nawRuz.getCalendar(), march20);
		  Assert.assertEquals(nawRuz.getDateTime(), march);
		  Assert.assertEquals(nawRuz.getGregorianDayOfYear(), march20.get(Calendar.DAY_OF_YEAR));
		  Assert.assertEquals(nawRuz.getKullIShay(), 1);
		  Assert.assertEquals(nawRuz.getVahid(), 10);
		  Assert.assertEquals(nawRuz.getYearInVahid(), 3);
	  }

	  @Test
	  public void createFromGregorianCalendarTest() {
		  final Calendar march20 = new GregorianCalendar();
		  march20.set(2017,2,20);
		  final BadiDate nawRuz = BadiDate.createFromGregorianCalendar(march20);
		  Assert.assertEquals(nawRuz.getHolyday(), BahaiHolyday.NAW_RUZ);
		  Assert.assertEquals(nawRuz.getBadiYear(), 174);
		  Assert.assertEquals(nawRuz.getBadiDayOfYear(), 1);
	  }

	  @Test
	  public void createFromDateTime() {
		  final DateTime march20 = new DateTime(2017,3,20,0,0);
		  final BadiDate nawRuz = BadiDate.createFromDateTime(march20);
		  Assert.assertEquals(nawRuz.getHolyday(), BahaiHolyday.NAW_RUZ);
		  Assert.assertEquals(nawRuz.getBadiYear(), 174);
		  Assert.assertEquals(nawRuz.getBadiDayOfYear(), 1);
		  
		  final DateTime march2 = new DateTime(2017,3,2,0,0);
		  final BadiDate ala = BadiDate.createFromDateTime(march2);
		  Assert.assertEquals(ala.getBadiMonth(), 20);
		  Assert.assertEquals(ala.getBadiYear(), 173);
		  Assert.assertEquals(ala.getBadiDay(), 2);
	  }
	  
	  @Test(expectedExceptions = {IllegalArgumentException.class}, expectedExceptionsMessageRegExp = "Year has to be greater than or equal to 1844.")
	  public void throwsExceptionOnGregoerianYearSmaller1844() throws IllegalArgumentException {
		  BadiDate.createFromDateTime(new DateTime(1843,3,22,0,0));
	  }
	  
	  @Test(expectedExceptions = {IllegalArgumentException.class}, expectedExceptionsMessageRegExp = "Year has to be less than  2064")
	  public void throwsExceptionOnGregorianYearGreatherLimit() throws IllegalArgumentException {
		  BadiDate.createFromDateTime(new DateTime(2065,3,19,0,0));
	  }
	  
	  @Test
	  public void nextFeastAndHolidayTest() {
		  final BadiDate nawRuz = BadiDate.createFromBadiDate(174, 1, 1);
		  Assert.assertEquals(nawRuz.getNextHolydayDate().getHolyday(), BahaiHolyday.RIDVAN1ST);
		  final BadiDate jalal = nawRuz.getNextFeastDate();
		  Assert.assertEquals(jalal.getGregorianDay(), 8);
		  Assert.assertEquals(jalal.getGregorianMonth(), 4);
		  
		  final BadiDate date = BadiDate.createFromBadiDate(174, 17, 5);
		  final BadiDate mulk = date.getNextFeastDate();
		  Assert.assertEquals(mulk.getGregorianDay(), 6);
		  Assert.assertEquals(mulk.getGregorianMonth(), 2);

		  final BadiDate ha = mulk.getNextFeastDate();
		  Assert.assertEquals(ha.getGregorianDay(), 25);
		  Assert.assertEquals(ha.getGregorianMonth(), 2);

		  final BadiDate ala = ha.getNextFeastDate();
		  Assert.assertEquals(ala.getGregorianDay(), 2);
		  Assert.assertEquals(ala.getGregorianMonth(), 3);

		  final BadiDate nextYear = ala.getNextFeastDate();
		  Assert.assertEquals(nextYear.getGregorianDay(), 21);
		  Assert.assertEquals(nextYear.getGregorianMonth(), 3);
		  Assert.assertEquals(nextYear.getBadiYear(), ala.getBadiYear()+1);

	  }
	  
	  @Test
	  public void nextFeastLeapYear() {
		  final BadiDate date = BadiDate.createFromBadiDate(172, 17, 5);
		  final BadiDate mulk = date.getNextFeastDate();
		  Assert.assertEquals(mulk.getGregorianDay(), 7);
		  Assert.assertEquals(mulk.getGregorianMonth(), 2);

		  final BadiDate ha = mulk.getNextFeastDate();
		  Assert.assertEquals(ha.getGregorianDay(), 26);
		  Assert.assertEquals(ha.getGregorianMonth(), 2);

		  final BadiDate ala = ha.getNextFeastDate();
		  Assert.assertEquals(ala.getGregorianDay(), 1);
		  Assert.assertEquals(ala.getGregorianMonth(), 3);

		  final BadiDate nextYear = ala.getNextFeastDate();
		  Assert.assertEquals(nextYear.getGregorianDay(), 20);
		  Assert.assertEquals(nextYear.getGregorianMonth(), 3);
		  Assert.assertEquals(nextYear.getBadiYear(), ala.getBadiYear()+1);

		  final DateTime y2k = new DateTime(2000,2,4,0,0);
		  final BadiDate date2 = BadiDate.createFromDateTime(y2k);
		  Assert.assertEquals(date2.nawRuzDayOfMarch(),21);
	  }
	  
	  @Test
	  public void nawRuzTest() {
		  final DateTime dateGregorian = new DateTime(2017,2,4,0,0);
		  final BadiDate date = BadiDate.createFromDateTime(dateGregorian);
		  Assert.assertEquals(date.nawRuzDayOfMarch(),20);

		  final BadiDate dateOld = BadiDate.createFromBadiDate(170, 17, 5);
		  Assert.assertEquals(dateOld.nawRuzDayOfMarch(),21);
	  }
	  
	  @Test(expectedExceptions = {IllegalArgumentException.class}, expectedExceptionsMessageRegExp = "Naw-Ruz only defined until 2064")
	  public void throwsExceptionOnYearIndexBiggerLimit() throws IllegalArgumentException {
		  BadiDate.nawRuzParameter(55);
	  }

	  @Test
	  public void createFromDateTimeWithSunset() {
		  final DateTime newYearsEve = new DateTime(2016,12,31,0,0);
		  final BadiDate date1 = BadiDate.createFromDateTimeWithSunset(newYearsEve, false);
		  Assert.assertEquals(date1.getBadiYear(), 173);
		  Assert.assertEquals(date1.getGregorianDayOfYear(), 365);
		  Assert.assertEquals(date1.getGregorianYear(), 2016);

		  final BadiDate date2 = BadiDate.createFromDateTimeWithSunset(newYearsEve, true);
		  Assert.assertEquals(date2.getBadiYear(), 173);
		  Assert.assertEquals(date2.getGregorianDayOfYear(), 1);
		  Assert.assertEquals(date2.getGregorianYear(), 2017);
	  }
	  

	  @Test
	  public void createFromGregorianCalendarWithSunsetTest() {
		  final Calendar newYearsEve = new GregorianCalendar();
		  newYearsEve.set(2017,2,20);
		  final BadiDate date1 = BadiDate.createFromGregorianCalendarWithSunset(newYearsEve, false);
		  Assert.assertEquals(date1.getBadiYear(), 173);
		  Assert.assertEquals(date1.getGregorianDayOfYear(), 365);
		  Assert.assertEquals(date1.getGregorianYear(), 2016);

		  final BadiDate date2 = BadiDate.createFromGregorianCalendarWithSunset(newYearsEve, true);
		  Assert.assertEquals(date2.getBadiYear(), 173);
		  Assert.assertEquals(date2.getGregorianDayOfYear(), 1);
		  Assert.assertEquals(date2.getGregorianYear(), 2017);
	  }

}
