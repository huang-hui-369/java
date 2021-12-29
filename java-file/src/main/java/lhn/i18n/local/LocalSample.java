package lhn.i18n.local;

import java.text.NumberFormat;
import java.util.Locale;

public class LocalSample {
	
	static public void main(String[] args) {
//		The four ways to create a Locale object are:
//
//			Locale.Builder Class
//			Locale Constructors
//			Locale.forLanguageTag Factory Method
//			Locale Constants
		
		Locale[] localeArray = {
				// LocaleBuilder
				new Locale.Builder().setLanguage("en").setRegion("US").build(),
				// Locale Constructors
				new Locale("en"),
				new Locale("fr", "CA"),
				// Locale.forLanguageTag Factory Method
				Locale.forLanguageTag("en"),
				Locale.forLanguageTag("zh"),
				// Locale Constants
				Locale.CHINESE
		};
		
		for (Locale locale : localeArray) {
		    NumberFormat nf = NumberFormat.getNumberInstance(locale);
		    System.out.format("%s : %s\n", locale.toString(), nf.format(573.34));
		}
		
		localeArray = Locale.getAvailableLocales();
		System.out.println("-----  all available locale -----");
		for (Locale locale : localeArray) {
			 System.out.format("%s\n", locale.toString());
		}
		
		System.out.println("-----  zh locale extension keys-----");
		Locale.CHINESE.getExtensionKeys().forEach( key -> {
			System.out.println(key);
		});
		
		System.out.println(Locale.getDefault(Locale.Category.DISPLAY).toString());
		System.out.println(Locale.getDefault(Locale.Category.FORMAT).toString());
		
		System.out.println(Locale.getDefault().toString());
		
	}
}
