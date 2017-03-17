package com.sarality.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to generate SQL statements
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class TableSQLGenerator {

  public static String getCreateSql(String tableName, Column[] columns) {
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

  public static String getDropSql(String tableName) {
    return "DROP TABLE IF EXISTS " + tableName;
  }

  public static String getAddColumnSql(String tableName, Column column) {
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

  public static String getDropColumnsSql(TableDefinition oldTableDefinition, Column[] columnsToDrop) {
    return null;
  }

}

