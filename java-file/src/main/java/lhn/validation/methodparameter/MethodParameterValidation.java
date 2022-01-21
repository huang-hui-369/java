package lhn.validation.methodparameter;

import java.lang.reflect.Method;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ExecutableValidator;

public class MethodParameterValidation {
	
	public void test1(@NotNull String arg) {
		
	}

	public static void main(String[] args) throws NoSuchMethodException, SecurityException {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        // ★ メソッド検証用の Validator を取得する
        ExecutableValidator validator = factory.getValidator().forExecutables();

        MethodParameterValidation bean = new MethodParameterValidation();
        // ★ 対象の Method インスタンスを取得
        Method method = MethodParameterValidation.class.getMethod("test1", String.class);

        Object[] parameters = {null};

        Set<ConstraintViolation<MethodParameterValidation>> constraintViolations
                = validator.validateParameters(bean, method, parameters);
        
        for (ConstraintViolation<MethodParameterValidation> constraintViolation : constraintViolations) {
			System.out.println(constraintViolation.getMessage());
		}
	}

}
