package lhn.validation.customer;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class CustomValidation1App {
	
	public void customValidation1() {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		CustomValidationBean u = new CustomValidationBean();
		
		Set<ConstraintViolation<CustomValidationBean>> errMsgs = validator.validate(u);
		for (ConstraintViolation<CustomValidationBean> constraintViolation : errMsgs) {
			System.out.println(constraintViolation.getMessage());
		}
		
	}

	class CustomValidationBean {
	    @CustomValidation1("test")
	    private String value = "Test";

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	    
	}
	
	public static void main(String[] args) {

		CustomValidation1App v = new CustomValidation1App();
		v.customValidation1();
	}
	
}
