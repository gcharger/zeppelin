package org.apache.zeppelin.antlr;

import com.google.gson.Gson;
import org.junit.Test;
import java.util.List;

public class SqlSplitVisitorTest {
    @Test
    public void testSplitSql() {
        String text = "select id,name from (\n" +
                "select id,name from table1\n" +
                "union all\n" +
                "select id,name from table2\n" +
                "union all\n" +
                "select id,name from table3\n" +
                "union all\n" +
                "select id,name from table4\n" +
                ")t group by id,name";
        String paragraphId = "paragraph_1683516605896_1391976205";
        List<String> tableSqlList= SqlSplitVisitor.splitSql(text,paragraphId);
        System.out.println(new Gson().toJson(tableSqlList));
    }
}
