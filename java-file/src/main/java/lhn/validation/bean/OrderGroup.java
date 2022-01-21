package lhn.validation.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Min;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;

/**
 * Order bean 中有 Book bean 和 List<Book>
 * 
 * 如果想要在验证Order时同时指定Group来验证Order所拥有的Book bean 和 List<Book>时，
 * 必须在Book bean和 List<Book>的成员变量上加上注解 @Valid @ConvertGroup
 * 
 */
public class OrderGroup {
	
	@Valid @ConvertGroup(from=Default.class, to=OrderValidGroup.class)
	private List<Book> bookList = new ArrayList<>();
	
	@Valid @ConvertGroup(from=Default.class, to=OrderValidGroup.class)
	private Book book;
	
	@Min(1)
	private Integer num = 0;
	
	
	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Integer getNum() {
		return num;
	}



	public void setNum(Integer num) {
		this.num = num;
	}

	public void addBook(Book b) {
		bookList.add(b);
	}


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
		OrderGroup order = new OrderGroup();
		Book b1 = new Book();
		order.setNum(1);
		order.setBook(b1);
		
		Book b2 = new Book();
		order.addBook(b2);

//	      input.setEMailAddress(arg);

		// バリデーションを実行
		Set<ConstraintViolation<OrderGroup>> msgs = validator.validate(order);

		// 結果の確認
		for (ConstraintViolation<OrderGroup> msg : msgs) {
			System.out.println(msg.getMessage());
		}

	}

}
