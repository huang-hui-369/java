package lhn.validation.group;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;

import java.util.Set;

import org.hibernate.validator.constraints.Length;

/**
 * 对于一个Bean，可以分Group check，如果Bean中没有指定groups，默认为Default
 * Default Group做最大，最小check
 * 例如 User的id update时，为必须输入，在insert时，可以不输入
 *     User的name insert时，为必须输入
 *     User的age update时，insert时，为必须输入
 * 
 * 一般只需要指定insert时，那些项目为必须输入，default时，那些项目最大最小，合法性check
 * 参考文档
 *   https://github.com/hibernate/hibernate-validator/tree/master/documentation/src/main/asciidoc
 * 
 */
public class ValidationGroup {

	public void validateDefault() {
		System.out.println("----- default check ----------");
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		User u = new User();
		Set<ConstraintViolation<User>> errMsgs = validator.validate(u);
		for (ConstraintViolation<User> constraintViolation : errMsgs) {
			System.out.println(constraintViolation.getMessage());
		}
	}
	
	public void validateInsert() {
		System.out.println("----- insert, default check ----------");
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		User u = new User();
//		u.setName("aaa");
		u.setId(99991);
		Set<ConstraintViolation<User>> errMsgs = validator.validate(u, Insert.class, Default.class);
		for (ConstraintViolation<User> constraintViolation : errMsgs) {
			System.out.println(constraintViolation.getMessage());
		}
	}
	
	public void validateUpdate() {
		System.out.println("----- update, default check ----------");
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		User u = new User();
		u.setAge(100);
		Set<ConstraintViolation<User>> errMsgs = validator.validate(u, Update.class, Default.class);
		for (ConstraintViolation<User> constraintViolation : errMsgs) {
			System.out.println(constraintViolation.getMessage());
		}
	}
	
	public void validateGroupOrder() {
		System.out.println("----- group order check ----------");
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		User u = new User();
		u.setId(99991);
		Set<ConstraintViolation<User>> errMsgs = validator.validate(u, InsertDefaultGroupOrder.class, Default.class);
		for (ConstraintViolation<User> constraintViolation : errMsgs) {
			System.out.println(constraintViolation.getMessage());
		}
	}


	class User {

		@Min(value=1, message = "ユーザIDを入力してください", groups=Update.class)
		@Max(value=9999)
		private long id = 0;

		// 在bean上定义的error message中使用ResourceeBundle
		@NotNull(message = "ユーザ名を入力してください", groups= {Insert.class})
		@Length(min = 5, max = 10, message = "{user.name.length}")
		private String name;
		
		@Min(value=1, message = "年齢を入力してください", groups={Insert.class,Update.class})
		@Max(value=99, message = "年齢を99以下に入力してください")
		private int age = 0;
		
		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

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

	public static void main(String[] args) {

		ValidationGroup v = new ValidationGroup();
		v.validateDefault();
		v.validateInsert();
		v.validateUpdate();
		v.validateGroupOrder();
	}

}
