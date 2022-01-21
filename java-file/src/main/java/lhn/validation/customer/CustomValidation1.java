package lhn.validation.customer;

import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * 我们可以直接拷贝系统内的注解如@Min，复制到我们新的注解中，然后根据需要修改
 * @author huanghui
 *
 */

//注解的实现类。
@Constraint(validatedBy = CustomValidator1.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CustomValidation1 {

	// 注解的属性值
    String value();

    // 是否强制校验
    boolean isRequired() default false;
    
    // 校验错误的默认信息
    String message() default "{value} と ${validatedValue} は違います";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
