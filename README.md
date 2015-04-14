# BahaiCalendarLibrary
A Baha'i Calendar (also known as Badi Calendar) Java library with the changes from the July 2014 message of the Universal House of Justice.

This library has two public functions:
Gregorian2Badi and Badi2Gregorian.

Badi2Gregorian(int year , int month , int day) returns an integer array with the Gregorian Date [year, month, day of the month, day of the year, holyday]

Gregorian2Badi(int year, int month, int day) returns an integer array with the Badi date in the format
[year, month, day of month, day of the year, holyday]

Holyday has the following values
-1 if the date is not a holyday
0 if it's Naw Ruz
1 if it's 1st day of Ridvan
2 if it's 9th day of Ridvan
3 if it's 12th day of Ridvan
4 if it's Declaration of the Bab
5 if it's Ascension of Baha\'u\'llah
6 if it's Martyrdom of the Bab
7 if it's Birth of the Bab
8 if it's Birth of Baha\'u\'llah
9 if it's Day of the Covenant
10 if it's Ascension of Abdu'l-Baha.
