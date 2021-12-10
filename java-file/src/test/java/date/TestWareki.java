package date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

public class TestWareki {
	
	@Test
	public void testWareki() {

		//ロケールを指定してCalendarインスタンスを取得
		Locale locale = new Locale("ja", "JP", "JP");
		Calendar calendar = Calendar.getInstance(locale);

		//calendar.getTime()で現在日時を取得して和暦にフォーマットする
		System.out.println("-------- GGGG---------------------");
		DateFormat japaseseFormat = new SimpleDateFormat("GGGGy年M月d日", locale);
		String dateStr = japaseseFormat.format(calendar.getTime());
		System.out.println("和暦:" + dateStr);

		//和暦にフォーマットした現在日時を西暦にパースする
		//日付の妥当性をチェックするためsetLenient()にfalseを渡す
		calendar.setLenient(false);
		Date date;
		try {
			date = japaseseFormat.parse(dateStr);
			System.out.println("西暦:" + date);   
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		System.out.println("-------- GGG --------");
		
		DateFormat japaseseFormat2 = new SimpleDateFormat("GGGy年M月d日", locale);
		dateStr = japaseseFormat2.format(calendar.getTime());
		System.out.println("和暦:" + dateStr);
		
		try {
			date = japaseseFormat2.parse(dateStr);
			System.out.println("西暦:" + date);   
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}


}
