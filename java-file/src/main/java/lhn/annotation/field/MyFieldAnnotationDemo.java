package lhn.annotation.field;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class MyFieldAnnotationDemo {
	
	@MyFieldAnnotation(columnName="field1", columnIndex=0)
	private String field1;

	public static void main(String[] args) throws NoSuchFieldException, SecurityException {
		Class<?> c = MyFieldAnnotationDemo.class;
		Field f = c.getDeclaredField("field1");
		
		Annotation[] as = f.getAnnotations();
		
		for(Annotation a : as) {
			System.out.println(a);
			if(a instanceof MyFieldAnnotation) {
				MyFieldAnnotation af = (MyFieldAnnotation)a;
				System.out.println("columnName: " + af.columnName());
				System.out.println("columnIndex: " + af.columnIndex());
			}
			
		}
		
	}

}
