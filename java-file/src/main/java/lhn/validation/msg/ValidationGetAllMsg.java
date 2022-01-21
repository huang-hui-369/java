package lhn.validation.msg;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ValidationGetAllMsg {
	
	
	/**
	 * 从ConstraintViolation取得所有情報
	 */
	public void getAllErrMsgs() {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<User>> errMsgs = validator.validate(new User());
		for(ConstraintViolation<User> constraintViolation : errMsgs) {
			String msg =
	                "message = " + constraintViolation.getMessage() + "\n" +
	                "messageTemplate = " + constraintViolation.getMessageTemplate() + "\n" +
	                "rootBean = " + constraintViolation.getRootBean() + "\n" +
	                "rootBeanClass = " + constraintViolation.getRootBeanClass() + "\n" +
	                "invalidValue = " + constraintViolation.getInvalidValue() + "\n" +
	                "propertyPath = " + constraintViolation.getPropertyPath() + "\n" +
	                "leafBean = " + constraintViolation.getLeafBean() + "\n" +
	                "descriptor = " + constraintViolation.getConstraintDescriptor() + "\n"
	            ;
	        System.out.println(msg);
		}
	}
	
	 class User{

	        @NotNull
	        @Size(max=10, min=5, message="名前[${validatedValue}]の長さは{min}から{max}の間に入力してください")
	        private String name;
	        
	    			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

	    }
	 
	 public static void main(String[] args) {
			
		 ValidationGetAllMsg v = new ValidationGetAllMsg();
		 v.getAllErrMsgs();
			
	}

}
