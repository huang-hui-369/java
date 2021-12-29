package lhn.i18n.formats.datetimes;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class PredefinedFormats {

	public static void displayDate(Date val, Locale locale) {
//		DEFAULT
//		SHORT
//		MEDIUM
//		LONG
//		FULL
		DateFormat[] dateFormatterArrays = { 
				DateFormat.getDateInstance(DateFormat.DEFAULT, locale),
				DateFormat.getDateInstance(DateFormat.SHORT, locale),
				DateFormat.getDateInstance(DateFormat.MEDIUM, locale),
				DateFormat.getDateInstance(DateFormat.LONG, locale),
				DateFormat.getDateInstance(DateFormat.FULL, locale),
		};
		for(DateFormat dateFormatter : dateFormatterArrays) {
			String dateOut = dateFormatter.format(val);
			System.out.println(dateOut + " " + locale.toString());
		}
		System.out.println();
	}
	
	public static void displayTime(Date val, Locale locale) {
//		DEFAULT
//		SHORT
//		MEDIUM
//		LONG
//		FULL
		DateFormat[] dateFormatterArrays = { 
				DateFormat.getTimeInstance(DateFormat.DEFAULT, locale),
				DateFormat.getTimeInstance(DateFormat.SHORT, locale),
				DateFormat.getTimeInstance(DateFormat.MEDIUM, locale),
				DateFormat.getTimeInstance(DateFormat.LONG, locale),
				DateFormat.getTimeInstance(DateFormat.FULL, locale),
		};
		for(DateFormat dateFormatter : dateFormatterArrays) {
			String dateOut = dateFormatter.format(val);
			System.out.println(dateOut + " " + locale.toString());
		}
		System.out.println();
	}
	
	public static void displayDateTime(Date val, Locale locale) {
//		DEFAULT
//		SHORT
//		MEDIUM
//		LONG
//		FULL
		DateFormat[] dateFormatterArrays = { 
				DateFormat.getDateTimeInstance(DateFormat.DEFAULT
						, DateFormat.DEFAULT
						, locale),
				DateFormat.getDateTimeInstance(DateFormat.SHORT
						, DateFormat.SHORT
						, locale),
				DateFormat.getDateTimeInstance(DateFormat.MEDIUM
						, DateFormat.MEDIUM
						, locale),
				DateFormat.getDateTimeInstance(DateFormat.LONG
						, DateFormat.LONG
						, locale),
				DateFormat.getDateTimeInstance(DateFormat.FULL
						, DateFormat.FULL
						, locale),
		};
		for(DateFormat dateFormatter : dateFormatterArrays) {
			String dateOut = dateFormatter.format(val);
			System.out.println(dateOut + " " + locale.toString());
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		Date now = new Date();
		displayDate(now, Locale.getDefault());
//		displayDate(now, new Locale("zh", "cn"));
//		displayDate(now, new Locale("en", "us"));
		
		displayTime(now, Locale.getDefault());
		displayDateTime(now, Locale.getDefault());
	}

}
