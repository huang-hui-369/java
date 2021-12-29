package lhn.validation.msg;

import java.util.Set;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;

public class ValidationMsgSetting2 {
	
	/**
	 * 在bean上定义的error message中使用EL表达式
	 */
	public void getErrSettingMsg() {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		User u = new User();
		u.setName("aaa");
		Set<ConstraintViolation<User>> errMsgs = validator.validate(u);
		for(ConstraintViolation<User> constraintViolation : errMsgs) {
	        System.out.println(constraintViolation.getMessage());
		}
	}
	
	 class User{
		    // 在bean上定义的error message中使用EL表达式
	        @NotNull(message="请输入用户名")
	        @Length(min=5,max=10, message="输入用户名[${validatedValue}]长度应该在{min}和{max}之间")
	        private String name;
	        
	    			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

	 }
	 
	 public static void main(String[] args) {
			
		 ValidationMsgSetting2 v = new ValidationMsgSetting2();
		 v.getErrSettingMsg();
			
	}
	
	

}
