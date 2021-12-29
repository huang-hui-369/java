package lhn.i18n.formats.message;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class FormatSample {

	private static Locale currentLocale = Locale.getDefault();
	private static ResourceBundle rb = ResourceBundle.getBundle("lhn.i18n.formats.message.MessageBundle",
			currentLocale);
	
	public static void dispNum1() {

		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(currentLocale);

		formatter.applyPattern(rb.getString("num1"));
		Object[] messageArguments = { new Double(7.77) };
		String output = formatter.format(messageArguments);

		System.out.println("num1: " + output);
	}
	
	public static void dispNum2() {

		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(currentLocale);

		formatter.applyPattern(rb.getString("num2"));
		Object[] messageArguments = { new Double(7.77) };
		String output = formatter.format(messageArguments);

		System.out.println("num2: " + output);
	}
	
	public static void dispNum3() {

		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(currentLocale);

		formatter.applyPattern(rb.getString("num3"));
		Object[] messageArguments = { new Double(7.77) };
		String output = formatter.format(messageArguments);

		System.out.println("num2: " + output);
	}

	public static void dispTemp1() {

		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(currentLocale);

		formatter.applyPattern(rb.getString("temp1"));
		Object[] messageArguments = { rb.getString("hello"), new Integer(7), new Date() };
		String output = formatter.format(messageArguments);

		System.out.println("temp1: " + output);
	}

	public static void dispTemp2() {

		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(currentLocale);

		formatter.applyPattern(rb.getString("temp2"));

		Object[] messageArguments = { rb.getString("hello"), NumberFormat.getNumberInstance().format(7),
				DateFormat.getTimeInstance().format(new Date()), DateFormat.getDateInstance().format(new Date()), };

		String output = formatter.format(messageArguments);

		System.out.println("temp2: " + output);
	}

	public static void dispTemp3() {

		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(currentLocale);

		formatter.applyPattern(rb.getString("temp3"));

		Object[] messageArguments = { rb.getString("hello"), new Integer(7), new Date() };
		String output = formatter.format(messageArguments);

		System.out.println("temp3: " + output);
	}

	public static void main(String[] args) {

		dispNum1();
		
		dispNum2();
		
		dispNum3();
		
//		dispTemp1();
//		
//		dispTemp2();
//		
//		dispTemp3();
	}

}
