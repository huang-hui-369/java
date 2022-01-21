package com.gut.jdbc;

public class Transaction {
	
/*	
	事务的概念

    事务指逻辑上的一组操作，组成这组操作的各个单元，要么全部都成功，要么全部不成功。

    例如：A——B转帐，对应于如下两条sql语句

             update from account set money=money+100 where name=‘b’;

             update from account set money=money-100 where name=‘a’;

    数据库默认事务是自动提交的，也就是发一条sql它就执行一条。如果想多条sql放在一个事务中执行，则需要使用如下语句。

    数据库开启事务命令

    start transaction  开启事务

    Rollback  回滚事务

    Commit   提交事务


12、JDBC控制事务语句

当Jdbc程序向数据库获得一个Connection对象时，默认情况下这个Connection对象会自动向数据库提交在它上面发送的SQL语句。若想关闭这种默认提交方式，让多条SQL在一个事务中执行，可使用下列语句：

(1) JDBC控制事务语句

    Connection.setAutoCommit(false);     //相当于 start transaction

    Connection.rollback();     //相当于  rollback

    Connection.commit();     //相当于   commit

(2) 设置事务回滚点

    Savepoint sp = conn.setSavepoint();

    Conn.rollback(sp);    // 会滚到指定的位置，该位置之前的操作任然有效执行，因为需要被提交

    Conn.commit();   //回滚后必须要提交


13、事务的四大特性(ACID)

(1) 原子性（Atomicity） 原子性是指事务是一个不可分割的工作单位，事务中的操作要么都发生，要么都不发生。

(2) 一致性（Consistency） 事务前后，数据的完整性必须保持一致。

(3) 隔离性（Isolation） 事务的隔离性是多个用户并发访问数据库时，数据库为每一个用户开启的事务，不能被其他事务的操作数据所干扰，多个并发事务之间要相互隔离。

(4) 持久性（Durability） 持久性是指一个事务一旦被提交，它对数据库中数据的改变就是永久性的，接下来即使数据库发生故障也不应该对其有任何影响。

14、事务的問題

更新丢失：最后的更新覆盖了其他事务之前的更新，而事务之间并不知道，发生更新丢失。更新丢失，可以完全避免，应用对访问的数据加锁即可。


脏读(针对未提交的数据)：一个事务在更新一条记录，未提交前，第二个事务读到了第一个事务更新后的记录，那么第二个事务就读到了脏数据，会产生对第一个未提交 数据的依赖。一旦第一个事务回滚，那么第二个事务读到的数据，将是错误的脏数据。

事物A                                                                      事物B

begin                                                                     begin

select blance from account where id = 1 （结果为 200）                  

                                                                          update account set blance = 300 where id = 1
select blance from account where id = 1 （结果为 300）                                                                           

end
                                                                          rollback
                                                                          
                                                                          end

最后读到300为脏数据


不可重复读(读取数据本身的对比)：一个事务在读取某些数据后的一段时间后，再次读取这个数据，发现其读取出来的数据内容已经发生了改变，就是不可重复读。

事物A                                                                      事物B

begin                                                                     begin

select blance from account where id = 1 （结果为 200）                  

                                                                          update account set blance = 300 where id = 1
                                                                          
                                                                          commit
select blance from account where id = 1 （结果为 300） 

                                                                          end
                                                                          
                                                                          
end                                                                       



幻读(读取结果集条数的对比)：一个事务按相同的查询条件查询之前检索过的数据，确发现检索出来的结果集条数变多或者减少(由其他事务插入、删除的)，类似产生幻觉。

begin                                                                     begin

select name from account （结果为 A，B，C）                  

                                                                          insert into account value('D')
                                                                          
                                                                          commit
select name from account （结果为 A，B，C,D）

                                                                          end
end                                                                       


14、事务的隔离级别


    多个线程开启各自事务操作数据库中数据时，数据库系统要负责隔离操作，以保证各个线程在获取数据时的准确性。

    如果不考虑隔离性，可能会引发问题：脏读、不可重复读、虚读(幻读) 。

15、事务的隔离性可避免问题—— 脏读：

脏读： 指一个事务读取了另外一个事务未提交的数据。【针对同一个业务操作】

 这是非常危险的，假设Ａ向Ｂ转帐货款100元，对应sql语句如下所示

 (1) update account set money=money+100 while name=‘b’;  

 (2) update account set money=money-100 while name=‘a’;

 当第1条sql执行完，第2条还没执行(A未提交时)，如果此时Ｂ查询自己的帐户，就会发现自己多了100元钱。如果A等B走后再回滚，B就会损失100元。



16、事务的隔离性可避免问题—— 不可重复读

不可重复读：在一个事务内读取表中的某一行数据，多次读取结果不同。【针对同一行数据】

不可重复读和脏读的区别：脏读是读取前一事务未提交的脏数据，不可重复读是重新读取了前一事务已提交的数据。

     例如银行想查询A帐户余额，第一次查询A帐户为200元，此时A向帐户存了100元并提交了，银行接着又进行了一次查询，此时A帐户为300元了。银行两次查询不一致，可能就会很困惑，不知道哪次查询是准的。


    很多人认为这种情况就对了，无须困惑，当然是后面的为准。我们可以考虑这样一种情况，比如银行程序需要将查询结果分别输出到电脑屏幕和写到文件中，结果在一个事务中针对输出的目的地，进行的两次查询不一致，导致文件和屏幕中的结果不一致，银行工作人员就不知道以哪个为准了。



17、事务的隔离性可避免问题—— 虚读(幻读)



虚读(幻读) ：是指在一个事务内读取到了别的事务插入的数据，导致前后读取不一致。【针对整张表】


如丙存款100元未提交，这时银行做报表统计account表中所有用户的总额为500元，然后丙提交了，这时银行再统计发现帐户为600元了，造成虚读同样会使银行不知所措，到底以哪个为准。



18、事务隔离性的设置语句


数据库共定义了四种隔离级别：


        (1) Serializable：可避免脏读、不可重复读、虚读情况的发生。（串行化，近似于单线程操作。）


        (2) Repeatable read：可避免脏读、不可重复读情况的发生。（可重复读）

        (3) Read committed：可避免脏读情况发生（读已提交）。

        (4) Read uncommitted：最低级别，以上情况均无法保证。(读未提交)


     set   transaction isolation level 设置事务隔离级别

     select @@tx_isolation  查询当前事务隔离级别



Demo样例：Java中设定事务的隔离级别。

public static void main(String[] args) throws SQLException, InterruptedException {

    Connection conn = null;

    PreparedStatement st = null;

    ResultSet rs = null;

    Savepoint sp = null;

    try{

          conn = JdbcUtils.getConnection();   //mysql repeatable read

          conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

          conn.setAutoCommit(false);    //start transaction;

          String sql = "select * from account";

          conn.prepareStatement(sql).executeQuery();

          Thread.sleep(1000*20);     // 等待另一个程序运行同样的代码以校验事务

          conn.commit();

    }finally{

          JdbcUtils.release(conn, st, rs);

    }

}

19、在MySQL客户端窗口界面掩饰事务的四种隔离级别。

(1) 演示脏读发生 

a窗口

set transaction isolation level read uncommitted;

start transaction;

select * from account;

-------------发现a帐户是1000元，转到b窗口

select * from account;

-------------发现a帐户是1100元，发生了脏读（这个事务读取到了别的事务未提交的数据） 


b窗口

start transaction;

update account set money=money+100 where name='aaa';

-------------事务在不提交的情况下，转到a窗口进行查询 

(2) 避免脏读，并演示不可重复读问题的发生 


a窗口

set transaction isolation level read committed;

start transaction;

select * from account;

-------------发现a帐户是1000元，转到b窗口

select * from account;

-------------发现a帐户是1000元，这时我们发现read committed这种级别可以避免脏读

-------------转到b窗口

select * from account;

-------------发现a帐户是1100元，这时就发生不可重复读（指这个事务读取到了别的事务提交的数据）

b窗口

start transaction;

update account set money=money+100 where name='aaa';

-------------事务在不提交的情况下，转到a窗口进行查询

commit;

-------------转到a窗口


(3)避免脏读、不可重复读，并演示虚读问题的发生 

a窗口

set transaction isolation level repeatable read;

start transaction;

select * from account;

--------------------发现a帐户是1000元，并且表的总纪录数是3条,这时转到b窗口

select * from account

--------------------发现a帐户是1000元，这说明repeatable read这种级别可避免脏读

-------------------转到b窗口

select * from account

---------------------发现a帐户是1000元，这说明repeatable read这种级别还可以避免不可重复读

---------------------转到b窗口

select * from account

---------------------发现表中可能会多出一条ddd的记录，这就发生了虚读，也就是在这个事务内读取了别的事务插入的数据（幻影数据）

b窗口

start transaction;

update account set money=money+100 where name='aaa';

---------------------转到a窗口

commit;

---------------------转到a窗口

start transaction;

insert into account(name,money) values('ddd','1000');

commit;

--------------------转到a窗口 

*/
}
