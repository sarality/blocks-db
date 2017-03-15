package com.sarality.db;

/**
 * Update operation for adding a new column to an existing table
 *
 * @author Satya@ () on 01/03/17
 */

public class AddColumnSchemaUpdate implements SchemaUpdate {

  private final Column column;
  private final Integer dbVersion;

  //primary and required columns not supported currently
  public AddColumnSchemaUpdate(Integer dbVersion, String tableName, String columnName, DataType dataType) {
    this.dbVersion = dbVersion;
    this.column = new TableColumn(tableName, columnName, dataType, false, false);
  }


  public Column getNewColumn() {
    return column;
  }

  @Override
  public SchemaUpdateType getUpdateType() {
    return SchemaUpdateType.ADD_COLUMN;
  }

  @Override
  public Integer getDbVersion() {
    return dbVersion;
  }

  @Override
  public String getTableName() {
    return column.getTableName();
  }


}
