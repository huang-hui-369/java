package lhn.i18n.formats.decimal;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;

public class PredefinedFormats {
	
	static public void displayByNumberFormats(Locale currentLocale) {
	    Integer quantity = new Integer(123456);
	    Double amount = new Double(345987.246);
	    NumberFormat numberFormatter;
	    String quantityOut;
	    String amountOut;

	    numberFormatter = NumberFormat.getNumberInstance(currentLocale);
	    quantityOut = numberFormatter.format(quantity);
	    amountOut = numberFormatter.format(amount);
	    System.out.println(" ----- number formats ----- ");
	    System.out.println(quantityOut + "   " + currentLocale.toString());
	    System.out.println(amountOut + "   " + currentLocale.toString());
	}
	
	static public void displayCurrency( Locale currentLocale) {

	    Double currencyAmount = new Double(9876543.21);
	    Currency currentCurrency = Currency.getInstance(currentLocale);
	    NumberFormat currencyFormatter = 
	        NumberFormat.getCurrencyInstance(currentLocale);

	    System.out.println(" ----- currency formats ----- ");
	    System.out.println(
	        currentLocale.getDisplayName() + ", " +
	        currentCurrency.getDisplayName() + ": " +
	        currencyFormatter.format(currencyAmount));
	}
	
	static public void displayPercent(Locale currentLocale) {

	    Double percent = new Double(0.75);
	    NumberFormat percentFormatter;
	    String percentOut;

	    percentFormatter = NumberFormat.getPercentInstance(currentLocale);
	    percentOut = percentFormatter.format(percent);
	    System.out.println(" ----- percent formats ----- ");
	    System.out.println(percentOut + "   " + currentLocale.toString());
	}
	
	static public void main(String[] args) {

        String[] supportedLanguages = {"zh","en","ja"}; 
        String[] supportedRegions = {"cn","us","jp"}; 
        
        Locale local = null;
        
        for(int i=0;i<supportedLanguages.length;i++) {
        	local = new Locale(supportedLanguages[i],supportedRegions[i]);
//        	displayByNumberFormats(local); 
        	displayCurrency(local);
//        	displayPercent(local);
        }
    }

}
