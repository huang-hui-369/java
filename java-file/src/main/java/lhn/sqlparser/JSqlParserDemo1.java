package lhn.sqlparser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.JdbcNamedParameter;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;
import net.sf.jsqlparser.statement.select.SelectItemVisitorAdapter;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SelectVisitorAdapter;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.util.TablesNamesFinder;

public class JSqlParserDemo1 {
	
	public String sql1 = "SELECT\r\n"
			+ "			EIGYOUSYO.EIGYOUSYO_ID\r\n"
			+ "		INTO\r\n"
			+ "			BODY_ID\r\n"
			+ "		FROM\r\n"
			+ "			EIGYOUSYO\r\n"
			+ "		INNER JOIN EIGYOU_RIREKI_KIHON_KYOUTUU\r\n"
			+ "			ON EIGYOUSYO.EIGYOUSYO_ID\r\n"
			+ "				= EIGYOU_RIREKI_KIHON_KYOUTUU.EIGYOUSYO_ID\r\n"
			+ "			AND EIGYOUSYO.SAISIN_EIGYOUSYO_RIREKI_NO\r\n"
			+ "				= EIGYOU_RIREKI_KIHON_KYOUTUU.EIGYOUSYO_RIREKI_NO\r\n"
			+ "		WHERE\r\n"
			+ "			EIGYOU_RIREKI_KIHON_KYOUTUU.KIHON_KYOUTUU_ID = TARGET_ID;";
	
	
	public String readFile(String spath) throws IOException {
		
		FileReader fReader = new FileReader(
				Paths.get("D:\\project\\saitama\\src\\saitama_sql2\\procedure\\SAITAMA.CODE_MAPPING$PROC_DOBOKU_KOBETU.StoredProcedure.sql")
				.toFile());
		 BufferedReader br = new BufferedReader(fReader);
		    StringBuilder sb = new StringBuilder();
		    String temp = "";
		    while ((temp = br.readLine()) != null) {
		    	// 拼接换行符
		      sb.append(temp + "\n");
		    }
		    br.close();
		    return sb.toString();
	}
	
	public void parse(String sql) {
		FileReader statementReader = null;
		try {
			statementReader = new FileReader(
					Paths.get("D:\\project\\saitama\\src\\saitama_sql2\\procedure\\SAITAMA.CODE_MAPPING$PROC_DOBOKU_KOBETU.StoredProcedure.sql")
					.toFile());
			
			Statement statement = CCJSqlParserUtil.parse(statementReader);
//			Statement statement = CCJSqlParserUtil.parse(sql1);
			if (statement instanceof Select) {
		        Select select = (Select) statement;
		        System.out.println("print parsed sql statement:");
		        System.out.println(select.toString());
		        System.out.println("show tables:");
		        
		        // parse table name
		        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
		        List<String> tableNames = tablesNamesFinder.getTableList(select);
		        for (String tableName : tableNames) {
		            System.out.println(tableName);
		        }
		        
		        StatementVisitor visitor = new MyStatementVisitor();
				statement.accept(visitor);
		     
		        System.out.println("====================================");
			}
			
		} catch (JSQLParserException | FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				statementReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	static class MyStatementVisitor extends StatementVisitorAdapter {
		private SelectVisitor visitor = new MySelectVisitor();

		@Override
		public void visit(Select select) {
			System.out.println("select\t" + select);

			SelectBody body = select.getSelectBody();
			System.out.println("body\t" + body);
			body.accept(visitor);
		}
	}
	
	static class MySelectVisitor extends SelectVisitorAdapter {
		private SelectItemVisitor selectItemVisitor = new MySelectItemVisitor();
		private ExpressionVisitor expressionVisitor = new MyExpressionVisitor();

		@Override
		public void visit(PlainSelect plainSelect) {
			System.out.println("plainSelect\t" + plainSelect);

			FromItem from = plainSelect.getFromItem();
			System.out.println("FromItem=" + from);

			List<SelectItem> itemList = plainSelect.getSelectItems();
			for (SelectItem item : itemList) {
				System.out.println("SelectItem=" + item);
				item.accept(selectItemVisitor);
			}
			
			List<Table> intoList = plainSelect.getIntoTables();
			
			for(Table t : intoList) {
				System.out.println("select into: " + t.toString());
			}
			
			List<Join> joinList = plainSelect.getJoins();
			for (Join item : joinList) {
				from = item.getRightItem();
				System.out.println("join FromItem=" + from);
				
				Expression onExpression = item.getOnExpression();
				System.out.println("join onExpression" + onExpression.toString());
				onExpression.accept(expressionVisitor);
				
				List<Column> columns = item.getUsingColumns();
				for(Column c : columns) {
					System.out.println("join column name =" + c.getColumnName());
				}
			}

			Expression where = plainSelect.getWhere();
			System.out.println("where=" + where);
			where.accept(expressionVisitor);
		}
	}
	
	static class MySelectItemVisitor extends SelectItemVisitorAdapter {
		private ExpressionVisitor expressionVisitor = new MyExpressionVisitor();

		// *
		@Override
		public void visit(AllColumns columns) {
			System.out.println("AllColumns\t" + columns);
		}

		// t.*
		@Override
		public void visit(AllTableColumns columns) {
			System.out.println("AllTableColumns\t" + columns);
		}

		// 通常のカラム
		@Override
		public void visit(SelectExpressionItem item) {
			System.out.println("SelectExpressionItem\t" + item);

			Alias alias = item.getAlias();
			System.out.println("alias=" + alias);

			Expression expression = item.getExpression();
			System.out.println("expression=" + expression);

			expression.accept(expressionVisitor);
		}
	}
	static class MyExpressionVisitor extends ExpressionVisitorAdapter {

		// 通常のカラム（カラム名）
		@Override
		public void visit(Column column) {
			System.out.println("column=" + column.getColumnName() + "\t" + column.getFullyQualifiedName());
		}

		// 定数（long）
		@Override
		public void visit(LongValue value) {
			System.out.println("longValue=" + value.getValue());
		}

		// 関数
		@Override
		public void visit(Function function) {
			System.out.println("Function\t" + function);

			String name = function.getName();
			System.out.println("name=" + name);

			ExpressionList parameters = function.getParameters();
			System.out.println("parameters=" + parameters);
		}

		// AND
		@Override
		public void visit(AndExpression expr) {
			System.out.println("and");
			super.visit(expr);
		}

		// =（等値比較）
		@Override
		public void visit(EqualsTo expr) {
			System.out.println("=");
			super.visit(expr);
		}

		// ?
		@Override
		public void visit(JdbcParameter parameter) {
			System.out.println("JdbcParameter\t" + parameter.getIndex());
		}

		// :name
		@Override
		public void visit(JdbcNamedParameter parameter) {
			System.out.println("JdbcNamedParameter\t" + parameter.getName());
		}
	}
		
}
