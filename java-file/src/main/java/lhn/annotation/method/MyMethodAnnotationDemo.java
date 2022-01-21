package lhn.annotation.method;

import java.lang.reflect.Method;

public class MyMethodAnnotationDemo {
	
	@MyMethodAnnotation
	public void run() {
		
	}
	
	public static void main(String[] args) throws SecurityException, NoSuchMethodException {
		Class<?> c = MyMethodAnnotationDemo.class;
		Method m = c.getDeclaredMethod("run");
		
		MyMethodAnnotation[] as = m.getDeclaredAnnotationsByType(MyMethodAnnotation.class);
		
		for(MyMethodAnnotation a : as) {
			System.out.println(a);
			
		}
		
	}

}
