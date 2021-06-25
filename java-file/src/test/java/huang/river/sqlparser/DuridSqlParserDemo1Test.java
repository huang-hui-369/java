package huang.river.sqlparser;

import static org.junit.Assert.*;

import org.junit.Test;

public class DuridSqlParserDemo1Test {

	@Test
	public void testParse() {
		DuridSqlParserDemo1 parser1 = new DuridSqlParserDemo1();
		parser1.parseOraPackage("D:\\project\\saitamaMigration-git\\saitama_sql_win\\PROCEDURE", "*.sql");
	}

}
