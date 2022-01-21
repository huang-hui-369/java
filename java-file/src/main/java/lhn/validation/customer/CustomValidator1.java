package lhn.validation.customer;

import java.util.Objects;

import com.google.common.base.Strings;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;



/**
 * @author huanghui
 * 
 * 自定义注解一定要实现ConstraintValidator接口奥，里面的两个参数
 * 第一个为 具体要校验的注解
 * 第二个为 校验的参数类型
 *
 */
public class CustomValidator1 implements ConstraintValidator<CustomValidation1, String>{

    private String value;
    
    private boolean required = false;

    @Override
    public void initialize(CustomValidation1 annotation) {
        System.out.println("initialize() : " + hashCode());
        this.value = annotation.value();
        this.required = annotation.isRequired();
    }

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		 System.out.println("isValid() : " + hashCode());
		 
		 if (required) {
			 return Objects.equals(this.value, value);
		 } else {
			 if (Strings.isNullOrEmpty(value)) {
		            return true;
		     }
			 return false;
		 }
	        

	        
	}
}