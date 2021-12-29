package lhn.validation.msg;

import java.util.Set;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;

public class ValidationMsgSettingRB4 {
	
	 /**
	 * 在bean上定义的error message中使用ResourceeBundle,必须使用文件的字符编码格式为iso8859-1
	 * 
	 * ResourceeBundle中
	 * user.name.length = 输入用户名${validatedValue}长度应该在{min}和{max}之间.
	 * ${validatedValue}是用户输入的值
	 * {min}，{max}是参数
	 * 
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
		    // 在bean上定义的error message中使用ResourceeBundle
	        @NotNull(message="请输入用户名")
	        @Length(min=5,max=10, message="{user.name.length}")
	        private String name;
	        
	    			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

	 }
	 
	 public static void main(String[] args) {
			
		 ValidationMsgSettingRB4 v = new ValidationMsgSettingRB4();
		 v.getErrSettingMsg();
			
	}
	
	

}
