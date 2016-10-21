package com.sarality.db;

/**
 * Column for a Database Table.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class TableColumn implements Column {
  private final String tableName;
  private final String name;
  private final DataType dataType;
  private final boolean isRequired;
  private final boolean isPrimary;

  public TableColumn(String tableName, String name, DataType dataType, boolean isRequired, boolean isPrimary) {
    this.tableName = tableName;
    this.name = name;
    this.dataType = dataType;
    this.isRequired = isRequired;
    this.isPrimary = isPrimary;
  }

  @Override
  public String getTableName() {
    return tableName;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public DataType getDataType() {
    return dataType;
  }

  @Override
  public boolean isRequired() {
    return isRequired;
  }

  @Override public boolean isPrimary() {
    return isPrimary;
  }
}
