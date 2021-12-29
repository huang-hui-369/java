package lhn.sqlparser;

import static org.junit.Assert.*;

import org.junit.Test;

import lhn.sqlparser.DuridSqlParserDemo1;

public class DuridSqlParserDemo1Test {

	@Test
	public void testParse() {
		DuridSqlParserDemo1 parser1 = new DuridSqlParserDemo1();
		parser1.parseOraPackage("D:\\project\\saitamaMigration-git\\saitama_sql_win\\PROCEDURE", "*.sql");
	}

}
