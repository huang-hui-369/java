package lhn.annotation.field;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
/**
 * 定义一个Field的注解
 *
 */
public @interface MyFieldAnnotation {
	String columnName() default "";
	int columnIndex() default 0;
}
