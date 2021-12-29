package lhn.i18n.hello;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18NHello {
	static public void main(String[] args) {

        String[] supportedLanguages = {"zh","en","ja"}; 
        
        ResourceBundle messages = null;
        Locale local = null;
        
        for(int i=0;i<supportedLanguages.length;i++) {
        	local = new Locale(supportedLanguages[i]);
        	 messages = ResourceBundle.getBundle("lhn.i18n.hello.HelloBundle", local);
        	 System.out.format("--------%s-------\n", local.getLanguage());
        	 System.out.format("greetings:[%s]\n", messages.getString("greetings"));
        	 System.out.format("inquiry:[%s]\n", messages.getString("inquiry"));
        	 System.out.format("farewell:[%s]\n", messages.getString("farewell"));
        }
    }
}
