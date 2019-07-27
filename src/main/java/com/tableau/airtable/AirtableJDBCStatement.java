package com.tableau.airtable;

import com.sybit.airtable.Base;
import com.sybit.airtable.Table;
import com.sybit.airtable.vo.RecordItem;
import net.sf.jsqlparser.*;
import net.sf.jsqlparser.parser.*;
import net.sf.jsqlparser.statement.select.PlainSelect;

import java.io.StringReader;
import java.net.URLEncoder;
import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AirtableJDBCStatement implements Statement {
    private Base base;
    private boolean cancelled = false;
    private Connection connection;
    private boolean poolable = true;
    private boolean closeOnCompletion = false;
    private ResultSet currentResultSet;
    private final CCJSqlParserManager parserManager = new CCJSqlParserManager();

    private static AirtableJDBCResultSet getSelectOneResultSet() {
        RecordItem item = new RecordItem();
        item.setFields(Collections.singletonMap("1", 1));
        item.setCreatedTime((new Timestamp(System.currentTimeMillis())).toString());
        item.setId("1");
        return new AirtableJDBCResultSet(Collections.singletonList(item), null);
    }

    AirtableJDBCStatement(Base base, Connection connection) {
        this.base = base;
        this.connection = connection;
    }

    public ResultSet executeQuery(String s) throws SQLException {
        if (s.equals("SELECT 1")) {
            currentResultSet = getSelectOneResultSet();
            return currentResultSet;
        }
        try {
            net.sf.jsqlparser.statement.Statement statement = parserManager.parse(new StringReader(s));
            if (! (statement instanceof net.sf.jsqlparser.statement.select.Select))
                throw new SQLException("Only Select operations are supported");
            net.sf.jsqlparser.statement.select.Select select = (net.sf.jsqlparser.statement.select.Select ) statement;
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
            if (! (plainSelect.getFromItem() instanceof net.sf.jsqlparser.schema.Table)) {
                throw new SQLException("Now now, nothing complicated: " + plainSelect.getFromItem().toString() + " class: " + plainSelect.getFromItem().getClass().toString());
            }
            net.sf.jsqlparser.schema.Table tableSelected = (net.sf.jsqlparser.schema.Table)plainSelect.getFromItem();
            String tableName = tableSelected.getName().replaceAll("^\"|\"$", "");
            Table<RecordItem> table = new Table<RecordItem>(URLEncoder.encode(tableName, UTF_8.toString()), RecordItem.class, base);
            List<RecordItem> results = table.select();
            currentResultSet = new AirtableJDBCResultSet(results, this);
            return currentResultSet;
        } catch (Exception ae) {
            throw new SQLException(ae);
            // return new AirtableJDBCResultSet();
        }
    }

    @Override
    public boolean execute(String s) throws SQLException {
        executeQuery(s);
        return true;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        if (currentResultSet == null) {
            throw new SQLException("No query executed");
        }
        if (cancelled) throw new SQLException("Statement cancelled");
        return currentResultSet;
    }

    @Override
    public void close() throws SQLException {
        currentResultSet = null;
    }

    @Override
    public void cancel() throws SQLException {
        cancelled = true;
    }

    @Override
    public int executeUpdate(String s) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public int getMaxRows() throws SQLException {
        return 0;
    }

    @Override
    public void setMaxRows(int i) throws SQLException {

    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return 0;
    }

    @Override
    public void setMaxFieldSize(int i) throws SQLException {

    }

    @Override
    public void setEscapeProcessing(boolean b) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return 0;
    }

    @Override
    public void setQueryTimeout(int i) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return false;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void clearWarnings() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setCursorName(String s) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setFetchDirection(int i) throws SQLException {

    }

    @Override
    public int getFetchDirection() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setFetchSize(int i) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public int getFetchSize() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public int getResultSetType() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void addBatch(String s) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void clearBatch() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }

    @Override
    public boolean getMoreResults(int i) throws SQLException {
        return false;
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public int executeUpdate(String s, int i) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public int executeUpdate(String s, int[] ints) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public int executeUpdate(String s, String[] strings) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public boolean execute(String s, int i) throws SQLException {
        return false;
    }

    @Override
    public boolean execute(String s, int[] ints) throws SQLException {
        return false;
    }

    @Override
    public boolean execute(String s, String[] strings) throws SQLException {
        return false;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return currentResultSet == null;
    }

    @Override
    public void setPoolable(boolean b) throws SQLException {
        this.poolable = b;
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return poolable;
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        this.closeOnCompletion = true;
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return closeOnCompletion;
    }

    @Override
    public long getLargeUpdateCount() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setLargeMaxRows(long max) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public long getLargeMaxRows() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public long[] executeLargeBatch() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public long executeLargeUpdate(String sql) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public long executeLargeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public long executeLargeUpdate(String sql, int[] columnIndexes) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public long executeLargeUpdate(String sql, String[] columnNames) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public String enquoteLiteral(String val) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public String enquoteIdentifier(String identifier, boolean alwaysQuote) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public boolean isSimpleIdentifier(String identifier) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public String enquoteNCharLiteral(String val) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public <T> T unwrap(Class<T> aClass) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public boolean isWrapperFor(Class<?> aClass) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
}
