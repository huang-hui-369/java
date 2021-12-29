package lhn.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lhn.validation.bean.Book;

import java.util.Set;

/**
 * 
 * maven 导入hibernate-validator和jakarta.el
 * <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-validator</artifactId>
        <version>7.0.2.Final</version>
    </dependency>
 * 
 * <dependency>
	    <groupId>org.glassfish</groupId>
	    <artifactId>jakarta.el</artifactId>
	    <version>4.0.2</version>
	</dependency>
 * 
 * 一个简单的Validator的例子
 * 1, 获取Validator
 * jakarta.validation.Validator validator = jakarta.validation.Validation.buildDefaultValidatorFactory()..getValidator();
 * 2， 创建需要验证的bean
 * Book book = new Book();
 * 3， 进行验证
 * Set<ConstraintViolation<Book>> msgs = validator.validate(book);
 * 4， 打印msg
 * for (ConstraintViolation<Book> msg : msgs) {
 * 		System.out.println(msg.getMessage());
 * }
 *
 */
public class ValidationHello {

	public static void main(String[] args) {
		
		/*
		* javaEE的商标所有权归oracle的原因，Java EE 9以后改为Jakarta EE，所以不能用javax和javaEE包了
	 	* Java EE -> Jakarta EE
        * javax -> jakarta
        */

		// Validator を取得
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

		Validator validator = factory.getValidator();

		// 入力データを用意
		Book book = new Book();

//	      input.setEMailAddress(arg);

		// バリデーションを実行
		Set<ConstraintViolation<Book>> msgs = validator.validate(book);

		// 結果の確認
		for (ConstraintViolation<Book> msg : msgs) {
			System.out.println(msg.getMessage());
		}

	}

}
