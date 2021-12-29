package lhn.validation.msg;

import java.util.Set;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;

public class ValidationMsgSetting1 {
	
	
	/**
	 * 在bean上定义任意的error message
	 */
	public void getErrSettingMsg() {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<User>> errMsgs = validator.validate(new User());
		for(ConstraintViolation<User> constraintViolation : errMsgs) {
	        System.out.println(constraintViolation.getMessage());
		}
	}
	
	 class User{
		 // 在bean上定义任意的error message
	        @NotNull(message="请输入用户名")
	        private String name;
	        
	    			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

	 }
	
	 public static void main(String[] args) {
			
		 ValidationMsgSetting1 v = new ValidationMsgSetting1();
		 v.getErrSettingMsg();
			
	}
	
	

}
