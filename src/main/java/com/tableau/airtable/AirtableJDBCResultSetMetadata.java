package com.tableau.airtable;

import com.sybit.airtable.vo.RecordItem;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AirtableJDBCResultSetMetadata implements ResultSetMetaData {
    private Map<Integer, String> fieldMap;
    private Map<Integer, Class<?>> typeMap;

    AirtableJDBCResultSetMetadata(Map<Integer, String> fieldMap, Map<Integer, Class<?>> typeMap) {
        this.fieldMap = fieldMap;
        this.typeMap = typeMap;
    }

    @Override
    public int getColumnCount() throws SQLException {
        return fieldMap.size();
    }

    @Override
    public boolean isAutoIncrement(int column) throws SQLException {
        return false;
    }

    @Override
    public boolean isCaseSensitive(int column) throws SQLException {
        return true;
    }

    @Override
    public boolean isSearchable(int column) throws SQLException {
        return false;
    }

    @Override
    public boolean isCurrency(int column) throws SQLException {
        return false;
    }

    @Override
    public int isNullable(int column) throws SQLException {
        return columnNullable;
    }

    @Override
    public boolean isSigned(int column) throws SQLException {
        return true;
    }

    @Override
    public int getColumnDisplaySize(int column) throws SQLException {
        return 0;
    }

    @Override
    public String getColumnLabel(int column) throws SQLException {
        return getColumnName(column);
    }

    @Override
    public String getColumnName(int column) throws SQLException {
        if (column > fieldMap.size()) throw new SQLException("Invalid column");
        return fieldMap.get(column);
    }

    @Override
    public String getSchemaName(int column) throws SQLException {
        return "AirtableSchema";
    }

    @Override
    public int getPrecision(int column) throws SQLException {
        return 0;
    }

    @Override
    public int getScale(int column) throws SQLException {
        return 0;
    }

    @Override
    public String getTableName(int column) throws SQLException {
        return "AirtableTable";
    }

    @Override
    public String getCatalogName(int column) throws SQLException {
        return "AirTableCatalog";
    }

    @Override
    public int getColumnType(int column) throws SQLException {
        if (column > typeMap.size()) throw new SQLException("Invalid column");

        Class<?> aClass = typeMap.get(column);
        if (Integer.class.equals(aClass)) {
            return Types.INTEGER;
        } else if (String.class.equals(aClass)) {
            return Types.VARCHAR;
        } else if (Float.class.equals(aClass)) {
            return Types.FLOAT;
        } else if (Double.class.equals(aClass)) {
            return Types.DOUBLE;
        } else if (Float.class.equals(aClass)) {
            return Types.FLOAT;
        } else if (Date.class.equals(aClass)) {
            return Types.DATE;
        } else if (ArrayList.class.equals(aClass)) {
            return Types.ARRAY;
        } else if (Boolean.class.equals(aClass)) {
            return Types.BOOLEAN;
        }
        return Types.OTHER;
    }

    @Override
    public String getColumnTypeName(int column) throws SQLException {
        return getColumnClassName(column);
    }

    @Override
    public String getColumnClassName(int column) throws SQLException {
        if (column > typeMap.size()) throw new SQLException("Invalid column");
        return typeMap.get(column).getName();
    }

    @Override
    public boolean isReadOnly(int column) throws SQLException {
        return true;
    }

    @Override
    public boolean isWritable(int column) throws SQLException {
        return false;
    }

    @Override
    public boolean isDefinitelyWritable(int column) throws SQLException {
        return false;
    }


    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
