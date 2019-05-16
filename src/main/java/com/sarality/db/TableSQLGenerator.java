package com.sarality.db;

import android.text.TextUtils;

import java.util.List;

/**
 * Utility class to generate SQL statements
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class TableSQLGenerator {

  static String getCreateSql(String tableName, Column[] columns) {
    StringBuilder builder = new StringBuilder("CREATE TABLE ");
    builder.append(tableName).append(" (\n");
    int columnIndex = 0;
    for (Column column : columns) {
      if (columnIndex > 0) {
        builder.append(",\n");
      }
      builder.append(column.getName()).append(" ").append(column.getDataType().getSqlType());
      if (column.isPrimary()) {
        builder.append(" PRIMARY KEY AUTOINCREMENT");
      } else if (column.isRequired()) {
        builder.append(" NOT NULL");
      }
      columnIndex++;
    }
    builder.append(")");
    return builder.toString();
  }

  static String getUpdateColumnValueSql(String tableName, Column column, String value, String whereClause) {
    //UPDATE tableName SET columnName = 'value'

    StringBuilder builder = new StringBuilder("UPDATE ").append(tableName).append("\n");
    builder = builder.append("SET ").append(column.getName()).append(" = ");
    if (column.getDataType().getSqlType().equals("TEXT")) {
      builder = builder.append("'").append(value).append("'");
    } else {
      builder = builder.append(value);
    }
    if (!TextUtils.isEmpty(whereClause)) {
      builder.append("WHERE ").append(whereClause);
    }
    return builder.toString();
  }

  static String getAddColumnSql(String tableName, Column column) {
    //ALTER TABLE tableName ADD COLUMN

    StringBuilder builder = new StringBuilder("ALTER TABLE ");
    builder.append(tableName).append(" \n ADD COLUMN ");
    builder.append(column.getName()).append(" ").append(column.getDataType().getSqlType());
    if (column.isPrimary()) {
      builder.append(" PRIMARY KEY AUTOINCREMENT");
    } else if (column.isRequired()) {
      builder.append(" NOT NULL");
    }

    return builder.toString();
  }

  static String getCreateIndexSql(String tableName, String indexName, List<Column> columns) {
    // CREATE INDEX indexName ON tableName (column1, column2)

    StringBuilder builder = new StringBuilder("CREATE INDEX ").append(indexName);
    builder.append(" ON ").append(tableName).append(" (");
    int ctr = 0;
    for (Column column : columns) {
      if (ctr > 0) {
        builder.append(", ");
      }
      builder.append(column.getName());
      ctr++;
    }
    builder.append(")");
    return builder.toString();
  }

  static String getDropTableSql(String tableName) {
    return "DROP TABLE IF EXISTS " + tableName;
  }

  public static String getDropColumnsSql(TableDefinition oldTableDefinition, Column[] columnsToDrop) {
    return null;
  }

}

