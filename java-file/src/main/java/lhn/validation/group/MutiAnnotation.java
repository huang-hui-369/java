package lhn.validation.group;

import java.util.Set;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;

public class MutiAnnotation {
	
	public void validateUpdate() {
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		User u = new User();
		u.setName("123456");
		
		System.out.println("----- default check ----------");
		Set<ConstraintViolation<User>> errMsgs = validator.validate(u);
		for (ConstraintViolation<User> constraintViolation : errMsgs) {
			System.out.println(constraintViolation.getMessage());
		}
		
		System.out.println("----- update check ----------");
		errMsgs = validator.validate(u, Update.class);
		for (ConstraintViolation<User> constraintViolation : errMsgs) {
			System.out.println(constraintViolation.getMessage());
		}
	}
	
	
	class User {

		// 在bean上定义不同group的多个@Length
		@NotNull(message = "ユーザ名を入力してください", groups= {Insert.class})
	
			
		@Length(min = 5, max = 10, message = "{user.name.length}")
		@Length(min = 7, max = 10, message = "{user.name.length}",  groups=Update.class)
		private String name;
		
	
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		

	}
	
	public static void main(String[] args) {

		MutiAnnotation v = new MutiAnnotation();
		v.validateUpdate();
	}

}
