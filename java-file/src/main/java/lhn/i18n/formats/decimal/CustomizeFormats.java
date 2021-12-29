package lhn.i18n.formats.decimal;

import java.text.DecimalFormat;

public class CustomizeFormats {

	
	public static void displayDecimal(double val) {
		
		
		System.out.format("input \t\tformat \t\t output \t\t\n");
		
		String pattern1 = "###,###.###";
		DecimalFormat formatter1 = new DecimalFormat(pattern1);
		System.out.format("[%f]\t [%s]\t [%s]\n", val , pattern1 ,formatter1.format(val));
		
		String pattern2 = "###.###";
		DecimalFormat formatter2 = new DecimalFormat(pattern2);
		System.out.format("[%f]\t [%s]\t [%s]\n", val , pattern2 ,formatter2.format(val));
		
		String pattern3 = "000000.00";
		DecimalFormat formatter3 = new DecimalFormat(pattern3);
		System.out.format("[%f]\t [%s]\t [%s]\n", val , pattern3 ,formatter3.format(val));
		System.out.println();
	}
	
	public static void displayPercent(double val) {
		System.out.format("input \t\tformat \t\t output \t\t\n");
		
		String pattern1 = "00.00%";
		DecimalFormat formatter1 = new DecimalFormat(pattern1);
		System.out.format("[%f]\t [%s]\t [%s]\n", val , pattern1 ,formatter1.format(val));
		System.out.println();
	}
	
	
	public static void main(String[] args) {
		double val1 = 123456.789;
		displayDecimal(val1);
		
		double val2 = 123.789;
		displayDecimal(val2);
		displayPercent(0.156);
		displayPercent(0.10);
		displayPercent(0.1728);
	
	}

}
