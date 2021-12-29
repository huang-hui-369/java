package lhn.validation;

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;



public class ValidatorHelloTest {
	
	private static Validator validator;

   @BeforeClass
   public static void setUp() {
	   ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
   }
	
	@Test
    public void validateNull(){
		
        Foo foo = new Foo();
        Set<ConstraintViolation<Foo>> constraintViolations = validator.validate(foo);
        for (ConstraintViolation<Foo> msgs : constraintViolations) {
            System.out.println(msgs.getMessage());
          }
        		
    }
	
	@Test
    public void validateMin(){
		
        Foo foo = new Foo();
        foo.setName("7of9");
        Set<ConstraintViolation<Foo>> constraintViolations = validator.validate(foo);
        for (ConstraintViolation<Foo> constraintViolation : constraintViolations) {
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
	
	@Test
    public void validateMax(){
		
        Foo foo = new Foo();
        foo.setName("abcdefabcdefa");
        Set<ConstraintViolation<Foo>> constraintViolations = validator.validate(foo);
        for (ConstraintViolation<Foo> msgs : constraintViolations) {
            System.out.println(msgs.getMessage());
          }
        		
    }
	
	

    class Foo{

        @NotNull
        @Size(max=10, min=5, message="名前[${validatedValue}]の長さは{min}から{max}の間に入力してください")
        private String name;
        
        @Min(0)
        @Max(99)
        @NotNull
        private int age;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}
        
        

    }

}
