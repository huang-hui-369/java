package lhn.annotation.type;

import java.lang.annotation.Annotation;
import java.util.Arrays;

@MyTypeAnnotation(value = { 3,6,9 })
public class MyTypeAnnotationDemo {
	
	public static void main(String[] args) {
		Class<?> c = MyTypeAnnotationDemo.class;
		for (Annotation a : c.getDeclaredAnnotations()) {
		  System.out.println(a);
		  if( a instanceof MyTypeAnnotation){
			  MyTypeAnnotation myTypeA = (MyTypeAnnotation) a;
              System.out.println("MyTypeAnnotation value():" + Arrays.toString(myTypeA.value()));
          }
		}
	}
	
}
