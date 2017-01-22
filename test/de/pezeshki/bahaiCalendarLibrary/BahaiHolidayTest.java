package de.pezeshki.bahaiCalendarLibrary;

import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.pezeshki.bahaiCalendarLibrary.BahaiHolyday;
import de.pezeshki.bahaiCalendarLibrary.BadiDate;

public class BahaiHolidayTest {

  @Test
  public void getNextHolydayDate() {
	  final BadiDate firstJanuary = BadiDate.createFromDateTime(new DateTime(2017, 1, 1, 12, 0));
	  final BadiDate nawRuz = BahaiHolyday.getNextHolydayDate(firstJanuary);
	  Assert.assertEquals(nawRuz.getBadiDay(), 1);
	  Assert.assertEquals(nawRuz.getBadiMonth(), 1);
	  Assert.assertEquals(nawRuz.getBadiYear(), 174);
	  Assert.assertEquals(nawRuz.getGregorianYear(), 2017);
	  Assert.assertEquals(nawRuz.getGregorianDay(), 20);
	  Assert.assertEquals(nawRuz.getGregorianMonth(), 3);
	  Assert.assertEquals(nawRuz.getBadiDayOfYear(), 1);
	  Assert.assertEquals(nawRuz.getHolyday(), BahaiHolyday.NAW_RUZ );
	  
	  final BadiDate ridvan1 = BahaiHolyday.getNextHolydayDate(nawRuz);
	  Assert.assertEquals(ridvan1.getBadiDay(), 13);
	  Assert.assertEquals(ridvan1.getBadiMonth(), 2);
	  Assert.assertEquals(ridvan1.getBadiYear(), 174);
	  Assert.assertEquals(ridvan1.getGregorianYear(), 2017);
	  Assert.assertEquals(ridvan1.getGregorianDay(), 20);
	  Assert.assertEquals(ridvan1.getGregorianMonth(), 4);
	  Assert.assertEquals(ridvan1.getBadiDayOfYear(), 32);
	  Assert.assertEquals(ridvan1.getHolyday(), BahaiHolyday.RIDVAN1ST );

	  final BadiDate ridvan9 = BahaiHolyday.getNextHolydayDate(ridvan1);
	  Assert.assertEquals(ridvan9.getBadiDay(), 2);
	  Assert.assertEquals(ridvan9.getBadiMonth(), 3);
	  Assert.assertEquals(ridvan9.getGregorianDay(), 28);
	  Assert.assertEquals(ridvan9.getGregorianMonth(), 4);
	  Assert.assertEquals(ridvan9.getHolyday(), BahaiHolyday.RIDVAN9TH );

	  final BadiDate ridvan12 = BahaiHolyday.getNextHolydayDate(ridvan9);
	  Assert.assertEquals(ridvan12.getBadiDay(), 5);
	  Assert.assertEquals(ridvan12.getBadiMonth(), 3);
	  Assert.assertEquals(ridvan12.getGregorianDay(), 1);
	  Assert.assertEquals(ridvan12.getGregorianMonth(), 5);
	  Assert.assertEquals(ridvan12.getHolyday(), BahaiHolyday.RIDVAN12TH );

	  final BadiDate decleration = BahaiHolyday.getNextHolydayDate(ridvan12);
	  Assert.assertEquals(decleration.getBadiDay(), 8);
	  Assert.assertEquals(decleration.getBadiMonth(), 4);
	  Assert.assertEquals(decleration.getGregorianDay(), 23);
	  Assert.assertEquals(decleration.getGregorianMonth(), 5);
	  Assert.assertEquals(decleration.getHolyday(), BahaiHolyday.DECLEARTION_OF_THE_BAB );
	  
	  final BadiDate ascenson = BahaiHolyday.getNextHolydayDate(decleration);
	  Assert.assertEquals(ascenson.getBadiDay(), 13);
	  Assert.assertEquals(ascenson.getBadiMonth(), 4);
	  Assert.assertEquals(ascenson.getGregorianDay(), 28);
	  Assert.assertEquals(ascenson.getGregorianMonth(), 5);
	  Assert.assertEquals(ascenson.getHolyday(), BahaiHolyday.ASCENSION_OF_BAHAULLAH );
	  
	  final BadiDate martyerdom = BahaiHolyday.getNextHolydayDate(ascenson);
	  Assert.assertEquals(martyerdom.getBadiDay(), 17);
	  Assert.assertEquals(martyerdom.getBadiMonth(), 6);
	  Assert.assertEquals(martyerdom.getGregorianDay(), 9);
	  Assert.assertEquals(martyerdom.getGregorianMonth(), 7);
	  Assert.assertEquals(martyerdom.getHolyday(), BahaiHolyday.MARTYRDOM_OF_THE_BAB );
	  
	  final BadiDate birthBab = BahaiHolyday.getNextHolydayDate(martyerdom);
	  Assert.assertEquals(birthBab.getBadiDay(), 7);
	  Assert.assertEquals(birthBab.getBadiMonth(), 12);
	  Assert.assertEquals(birthBab.getGregorianDay(), 21);
	  Assert.assertEquals(birthBab.getGregorianMonth(), 10);
	  Assert.assertEquals(birthBab.getHolyday(), BahaiHolyday.BIRTH_OF_THE_BAB );
	  
	  final BadiDate birthBahaullah = BahaiHolyday.getNextHolydayDate(birthBab);
	  Assert.assertEquals(birthBahaullah.getBadiDay(), 8);
	  Assert.assertEquals(birthBahaullah.getBadiMonth(), 12);
	  Assert.assertEquals(birthBahaullah.getGregorianDay(), 22);
	  Assert.assertEquals(birthBahaullah.getGregorianMonth(), 10);
	  Assert.assertEquals(birthBahaullah.getHolyday(), BahaiHolyday.BIRTH_OF_BAHAULLAH );
	  
	  final BadiDate covenant = BahaiHolyday.getNextHolydayDate(birthBahaullah);
	  Assert.assertEquals(covenant.getBadiDay(), 4);
	  Assert.assertEquals(covenant.getBadiMonth(), 14);
	  Assert.assertEquals(covenant.getGregorianDay(), 25);
	  Assert.assertEquals(covenant.getGregorianMonth(), 11);
	  Assert.assertEquals(covenant.getHolyday(), BahaiHolyday.DAY_OF_THE_COVENANT );
	  
	  final BadiDate ascension2 = BahaiHolyday.getNextHolydayDate(covenant);
	  Assert.assertEquals(ascension2.getBadiDay(), 6);
	  Assert.assertEquals(ascension2.getBadiMonth(), 14);
	  Assert.assertEquals(ascension2.getGregorianDay(), 27);
	  Assert.assertEquals(ascension2.getGregorianMonth(), 11);
	  Assert.assertEquals(ascension2.getHolyday(), BahaiHolyday.ASCENSION_OF_ABDUL_BAHA );
	  
	  final BadiDate nawRuzNext = BahaiHolyday.getNextHolydayDate(ascension2);
	  Assert.assertEquals(nawRuzNext.getBadiDay(), 1);
	  Assert.assertEquals(nawRuzNext.getBadiMonth(), 1);
	  Assert.assertEquals(nawRuzNext.getBadiYear(), 175);
	  Assert.assertEquals(nawRuzNext.getGregorianYear(), 2018);
	  Assert.assertEquals(nawRuzNext.getGregorianDay(), 21);
	  Assert.assertEquals(nawRuzNext.getGregorianMonth(), 3);
	  Assert.assertEquals(nawRuzNext.getHolyday(), BahaiHolyday.NAW_RUZ );

	  final BadiDate firstJanuary2014 = BadiDate.createFromDateTime(new DateTime(2014, 1, 1, 12, 0));
	  final BadiDate nawRuz2014 = BahaiHolyday.getNextHolydayDate(firstJanuary2014);
	  Assert.assertEquals(nawRuz2014.getBadiDay(), 1);
	  Assert.assertEquals(nawRuz2014.getBadiMonth(), 1);
	  Assert.assertEquals(nawRuz2014.getBadiYear(), 171);
	  Assert.assertEquals(nawRuz2014.getGregorianYear(), 2014);
	  Assert.assertEquals(nawRuz2014.getGregorianDay(), 21);
	  Assert.assertEquals(nawRuz2014.getGregorianMonth(), 3);
	  Assert.assertEquals(nawRuz2014.getBadiDayOfYear(), 1);
	  Assert.assertEquals(nawRuz2014.getHolyday(), BahaiHolyday.NAW_RUZ );

  }
  
  @Test
  public void getHolydayStringAndIndex() {
	  Assert.assertEquals(BahaiHolyday.NAW_RUZ.asString(), "Naw-Ruz" );
	  Assert.assertEquals(BahaiHolyday.NAW_RUZ.getIndex(), 0 );
  }
  
  @Test
  public void getDayOfYearTest() {
	  final int nawRuz = BahaiHolyday.getDayOfYear(174, 0);
	  Assert.assertEquals(nawRuz, 1);  
	  final int birthOfBab = BahaiHolyday.getDayOfYear(176, 7);
	  Assert.assertEquals(birthOfBab, 223);  
	  final int birthOfBahaullah = BahaiHolyday.getDayOfYear(174, 8);
	  Assert.assertEquals(birthOfBahaullah, 217);  
	  final int birthOfBab170 = BahaiHolyday.getDayOfYear(170, 7);
	  Assert.assertEquals(birthOfBab170, 214);  
	  final int BirthOfBahaullah170 = BahaiHolyday.getDayOfYear(170, 8);
	  Assert.assertEquals(BirthOfBahaullah170, 237);  
  }
  
  @Test(expectedExceptions = {IllegalArgumentException.class}, expectedExceptionsMessageRegExp = "Badi year has to be less than  221")
  public void throwsExceptionOnBadiYearBiggerThanLimmit() throws Exception {
	  BahaiHolyday.getDayOfYear(222, 7);
  }
  
  @Test(expectedExceptions = {IllegalArgumentException.class}, expectedExceptionsMessageRegExp = "Badi year has to be less than  221")
  public void throwsExceptionOnBadiYearBiggerThanLimmit2() throws Exception {
	  BahaiHolyday.getHolyday(2, 222);
  }
  
  @Test
  public void returnsTheOldBirthdayDates() {
	  final BahaiHolyday birthOfBab = BahaiHolyday.getHolyday(214, 170);
	  Assert.assertEquals(birthOfBab, BahaiHolyday.BIRTH_OF_THE_BAB );

	  final BahaiHolyday birthOfBahaullah = BahaiHolyday.getHolyday(237, 170);
	  Assert.assertEquals(birthOfBahaullah, BahaiHolyday.BIRTH_OF_BAHAULLAH );
  }
  
  @Test
  public void returnsNullIfNotHolliday() {
	  final BahaiHolyday notHolyday = BahaiHolyday.getHolyday(2, 174);
	  Assert.assertNull(notHolyday);
  }

}
