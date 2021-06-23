package huang.river.sqlparser;

import java.io.StringWriter;
import java.util.List;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.sqlserver.parser.SQLServerStatementParser;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerOutputVisitor;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.visitor.SQLASTVisitorAdapter;

public class DuridSqlParserDemo1 {
	
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
	
	public void parse() {
		 SQLStatementParser parser = new SQLServerStatementParser(sql1);
		 
		 // 使用Parser解析生成AST，这里SQLStatement就是AST
	        SQLStatement sqlStatement = parser.parseStatement();

	        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
	        sqlStatement.accept(visitor);

	        System.out.println("getTables:" + visitor.getTables());
	        System.out.println("getParameters:" + visitor.getParameters());
	        System.out.println("getOrderByColumns:" + visitor.getOrderByColumns());
	        System.out.println("getGroupByColumns:" + visitor.getGroupByColumns());
	        System.out.println("---------------------------------------------------------------------------");

	        // 使用select访问者进行select的关键信息打印
	        SelectPrintVisitor selectPrintVisitor = new SelectPrintVisitor();
	        sqlStatement.accept(selectPrintVisitor);

	        System.out.println("---------------------------------------------------------------------------");
	        // 最终sql输出
	        StringWriter out = new StringWriter();
	        TableNameVisitor outputVisitor = new TableNameVisitor(out);
	        sqlStatement.accept(outputVisitor);
	        System.out.println(out.toString());

	}
	
	static class SelectPrintVisitor extends SQLASTVisitorAdapter {

	    public boolean visit(SQLSelectQueryBlock x) {
	        List<SQLSelectItem> selectItemList = x.getSelectList();
	        selectItemList.forEach(selectItem -> {
	            System.out.println("attr:" + selectItem.getAttributes());
	            System.out.println("expr:" + SQLUtils.toMySqlString(selectItem.getExpr()));
	        });

	        System.out.println("table:" + SQLUtils.toMySqlString(x.getFrom()));
	        System.out.println("where:" + SQLUtils.toMySqlString(x.getWhere()));
	        System.out.println("order by:" + SQLUtils.toMySqlString(x.getOrderBy().getItems().get(0)));
	        System.out.println("limit:" + SQLUtils.toMySqlString(x.getLimit()));

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
