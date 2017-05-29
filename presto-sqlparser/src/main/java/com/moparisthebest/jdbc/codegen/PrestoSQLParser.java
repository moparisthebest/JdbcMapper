package com.moparisthebest.jdbc.codegen;

import com.facebook.presto.sql.parser.SqlParser;
import com.facebook.presto.sql.tree.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mopar on 5/31/17.
 */
public class PrestoSQLParser extends AbstractSQLParser {

    private static final SqlParser SQL_PARSER = new SqlParser();

    public PrestoSQLParser() {
        super(null, false);
    }

    private PrestoSQLParser(final String[] columnNames, final boolean isSelect) {
        super(columnNames, isSelect);
    }

    @Override
    public SQLParser parse(final String sql) {
        boolean isSelect = false;
        String[] columnNames = null;
        try {
            //final Expression stmt = SQL_PARSER.createExpression(sql);
            final Statement stmt = SQL_PARSER.createStatement(sql);
            isSelect = stmt instanceof Query;
            if (isSelect) {
                final Query query = (Query) stmt;
                final List<SelectItem> selectItems = ((QuerySpecification)query.getQueryBody()).getSelect().getSelectItems();
                columnNames = new String[selectItems.size() + 1];
                int x = 0;
                for(final SelectItem selectItem : selectItems) {
                    final SingleColumn sc = (SingleColumn) selectItem;
                    if(sc.getAlias().isPresent()) {
                        columnNames[++x] = sc.getAlias().get().toUpperCase();
                        continue;
                    }
                    final Expression scExp = sc.getExpression();
                    //columnNames[++x] = selectItem.toString();
                    if(scExp instanceof Identifier) {
                        columnNames[++x] = ((Identifier)scExp).getName().toUpperCase();
                    } else if(scExp instanceof DereferenceExpression) {
                        columnNames[++x] = ((DereferenceExpression)scExp).getFieldName().toUpperCase();
                    } else {
                        throw new RuntimeException("unknown syntax");
                    }
                }
            }
        } catch(com.facebook.presto.sql.parser.ParsingException e) {
            // ignore
            //e.printStackTrace();
            //throw new RuntimeException(e);
        }
        return new PrestoSQLParser(columnNames, isSelect);
    }

}
