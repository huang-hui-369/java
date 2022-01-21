package lhn.annotation.demo.entity;

@Entity(tableName = "USER")
public class User {
	@Column(cname = "UID", ctype = "number", pk = true)
	private long id;
	
	@Column(cname = "UNAME", ctype = "varchar(64)")
	private String name;
	
}
