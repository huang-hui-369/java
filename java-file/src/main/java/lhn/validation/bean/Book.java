package lhn.validation.bean;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lhn.validation.group.Update;

public class Book {

		@Min(value=1, groups= {OrderValidGroup.class,Update.class})
	 	private long id;

	    /**
	     * 书名
	     */
	    @NotEmpty(message = "书名不能为空", groups=OrderValidGroup.class)
	    private String bookName;
	    /**
	     * ISBN号
	     */
	    @NotNull(message = "ISBN号不能为空", groups=OrderValidGroup.class)
	    private String bookIsbn;
	    /**
	     * 单价
	     */
	    @DecimalMin(value = "0.1",message = "单价最低为0.1", groups=OrderValidGroup.class)    
	    private double price;
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getBookName() {
			return bookName;
		}
		public void setBookName(String bookName) {
			this.bookName = bookName;
		}
		public String getBookIsbn() {
			return bookIsbn;
		}
		public void setBookIsbn(String bookIsbn) {
			this.bookIsbn = bookIsbn;
		}
		public double getPrice() {
			return price;
		}
		public void setPrice(double price) {
			this.price = price;
		} 
	
}
