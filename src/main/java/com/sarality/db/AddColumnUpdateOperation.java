package com.sarality.db;

/**
 * Update operation for adding a new column to an existing table
 *
 * @author Satya@ () on 01/03/17
 */

public class AddColumnUpdateOperation implements UpdateOperation {

  private final Column column;

  //primary and required columns not supported currently
  public AddColumnUpdateOperation(String tableName, String columnName, DataType dataType) {
    this.column = new TableColumn(tableName, columnName, dataType, false, false);
  }

  @Override
  public Column getExistingColumn() {
    return null;
  }

  @Override
  public Column getNewColumn() {
    return column;
  }

  @Override
  public UpdateOperationType getUpdateType() {
    return UpdateOperationType.ADD_COLUMN;
  }

}
