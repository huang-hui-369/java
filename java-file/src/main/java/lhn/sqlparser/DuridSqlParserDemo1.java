package lhn.sqlparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.List;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLBlockStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateProcedureStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreatePackageStatement;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleStatementParser;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitorAdapter;
import com.alibaba.druid.sql.dialect.sqlserver.parser.SQLServerStatementParser;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerASTVisitorAdapter;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerOutputVisitor;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.visitor.SQLASTVisitorAdapter;
import com.alibaba.druid.stat.TableStat.Column;



public class DuridSqlParserDemo1  extends SimpleFileVisitor<Path> {
	
	private PathMatcher matcher;
	
	public static int fromCnt = 0;
	
	public static int whereCnt = 0;
	
	
	public void parseOraPackage(String path, String pattern) {
		matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
		try {
			Files.walkFileTree(Paths.get(path), this);
			
			System.out.format("from count: %d\n", fromCnt);
			
			System.out.format("where count: %d\n", whereCnt);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		Path name = file.getFileName();
		if (name != null && matcher.matches(name)) {
			System.out.println(name.toString());
			parse1file(file);
		}
		return FileVisitResult.CONTINUE;
	}
	
	public String readFile(Path fpath) throws IOException {
		
		
		 BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(fpath.toFile()), Charset.forName("windows-31j") ));
		    StringBuilder sb = new StringBuilder();
		    String temp = "";
		    while ((temp = br.readLine()) != null) {
		    	// 拼接换行符
		      sb.append(temp + "\n");
		    }
		    br.close();
		    return sb.toString();
	}
	
	public String readFile(String spath) throws IOException {
		
		 return readFile(Paths.get(spath));
	}
	
	
	public void parse1file(Path fpath) {
		
		SQLStatementParser parser;
		try {
			
			
			String sqlstr = readFile(fpath);
			
			
			
//			List<SQLStatement> sqlList = SQLUtils.parseStatements(sqlstr, DbType.sqlserver);
			
			
			parser = new SQLServerStatementParser(sqlstr);
			parser = new OracleStatementParser(sqlstr);
//			// 使用Parser解析生成AST，这里SQLStatement就是AST
	        SQLStatement sqlStmt = parser.parseCreatePackage();
	        
	        if(!sqlStmt.toString().toUpperCase().contains("SELECT")) {
	        	return;
	        }
	        
	        if(sqlStmt instanceof OracleCreatePackageStatement) {
	        	OracleCreatePackageStatement packageStmt = (OracleCreatePackageStatement)sqlStmt;
	        	List<SQLStatement> stmtList = packageStmt.getStatements();
	        	 for(SQLStatement o : stmtList) {
	        		 if(!o.toString().toUpperCase().contains("SELECT")) {
	        			 continue;
	        		 }
	        		 
//	        		 if(o instanceof SQLCreateFunctionStatement) {
//	        			 SQLCreateFunctionStatement fstmt = (SQLCreateFunctionStatement)o;
//	        		 } else if (o instanceof SQLCreateProcedureStatement ) {
//	        			 SQLCreateProcedureStatement pstmt = (SQLCreateProcedureStatement)o;
//	        		 }
	        		 
	        		 
	        		 o.accept(new OracleSelectPrintVisitor());
	        		 
//	        		 SQLStatement stmt = pstmt.getBlock();
//	        		 if(stmt instanceof SQLBlockStatement) {
//	        			 SQLBlockStatement block = (SQLBlockStatement)stmt;
//	        			 List<SQLStatement> bstmtList = block.getStatementList();
//	        			 for(SQLStatement b : bstmtList) {
//	        				 if(!b.toString().toUpperCase().contains("SELECT")) {
//	    	        			 continue;
//	    	        		 }
//	        				 if(b instanceof SQLSelectQueryBlock) {
//	        					 SQLSelectQueryBlock q = (SQLSelectQueryBlock)b;
//	        					 SQLUtils.toMySqlString(q.getFrom());
//	        					 SQLUtils.toMySqlString(q.getWhere());
//	        				 } else {
//	        					 b.accept(new SelectPrintVisitor());
//	        				 }
//	        			 }
//	        		 }
//	        		 for(SQLObject b: blockList) {
//	        			 SQLBlockStatement block = (SQLBlockStatement)b;
//	        			 block.getStatementList()
//	        		 }
	 	        }
	        	
	        }
	        
	      
	        
	       
	        
//	        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
//	        sqlStatement.accept(visitor);
//
//	        
//	        System.out.println("Columns:" + visitor.getColumns());
//	        System.out.println("getGroupByColumns:" + visitor.getGroupByColumns());
//	        System.out.println("getTables:" + visitor.getTables());
//	        System.out.println("getParameters:" + visitor.getParameters());
//	        System.out.println("getOrderByColumns:" + visitor.getOrderByColumns());
//	        System.out.println("getGroupByColumns:" + visitor.getGroupByColumns());
//	       
//	        System.out.println("---------------------------------------------------------------------------");
//
//	        // 使用select访问者进行select的关键信息打印
//	        SelectPrintVisitor selectPrintVisitor = new SelectPrintVisitor();
//	        sqlStatement.accept(selectPrintVisitor);
//	        
//
//	        System.out.println("---------------------------------------------------------------------------");
//	        // 最终sql输出
//	        StringWriter out = new StringWriter();
//	        TableNameVisitor outputVisitor = new TableNameVisitor(out);
//	        sqlStatement.accept(outputVisitor);
//	        System.out.println(out.toString());
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		 
		 

	}
	
	static class OracleSelectPrintVisitor extends OracleASTVisitorAdapter {

	    public boolean visit(SQLSelectQueryBlock x) {
//	        List<SQLSelectItem> selectItemList = x.getSelectList();
//	        selectItemList.forEach(selectItem -> {
//	            System.out.println("attr:" + selectItem.getAttributes());
//	            System.out.println("expr:" + SQLUtils.toMySqlString(selectItem.getExpr()));
//	        });
	        
	        SQLTableSource from = x.getFrom();
	        if(from!=null) {
	        	String fromstr = SQLUtils.toOracleString(from);
	        	fromstr = fromstr.toUpperCase();
	        	if(fromstr.contains("SELECT") || fromstr.contains("JOIN")) {
	        		fromCnt ++;
	        		System.out.println("from:" + fromstr);
	        		System.out.println("---------------------------------------------------------------------------");
	        	}
	        		
	        }
	        	
	        SQLExpr where = x.getWhere();
	        if(where!=null) {
	        	String wherestr = SQLUtils.toOracleString(where).toUpperCase();
	        	if(wherestr.contains("SELECT")) {
	        		whereCnt++;
	        		System.out.println("where:" + wherestr);
	        		System.out.println("---------------------------------------------------------------------------");
	        	}
	        		
	        }
	        	
//	        System.out.println("order by:" + SQLUtils.toMySqlString(x.getOrderBy().getItems().get(0)));
//	        System.out.println("limit:" + SQLUtils.toMySqlString(x.getLimit()));

	        return true;
	    }

	}
	
	static class SelectPrintVisitor extends SQLServerASTVisitorAdapter {

	    public boolean visit(SQLSelectQueryBlock x) {
	        List<SQLSelectItem> selectItemList = x.getSelectList();
	        selectItemList.forEach(selectItem -> {
	            System.out.println("attr:" + selectItem.getAttributes());
	            System.out.println("expr:" + SQLUtils.toMySqlString(selectItem.getExpr()));
	        });
	        
	        System.out.println("---------------------------------------------------------------------------");
	        if(x.getFrom()!=null) {
	        	System.out.println("from:" + SQLUtils.toMySqlString(x.getFrom()));
	        }
	        	
	        System.out.println("---------------------------------------------------------------------------");
	        if(x.getWhere()!=null) {
	        	System.out.println("where:" + SQLUtils.toMySqlString(x.getWhere()));
	        }
	        	
//	        System.out.println("order by:" + SQLUtils.toMySqlString(x.getOrderBy().getItems().get(0)));
//	        System.out.println("limit:" + SQLUtils.toMySqlString(x.getLimit()));

	        return true;
	    }

	}
	
	static class TableNameVisitor extends SQLServerOutputVisitor {

	    public TableNameVisitor(Appendable appender) {
	        super(appender);
	    }

	    @Override
	    public boolean visit(SQLExprTableSource x) {
	        SQLName table = (SQLName) x.getExpr();
	        String tableName = table.getSimpleName();

	        // 改写tableName
	        print0("new_" + tableName.toUpperCase());

	        return true;
	    }

	}

}
