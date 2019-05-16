package com.sarality.db;

/**
 * Schema Update operation for changing the value for a given Column in a Table.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class UpdateColumnValueSchemaUpdate implements SchemaUpdate {

  private final Integer dbVersion;
  private final String tableName;

  private final Column column;
  private final String value;
  private final String whereClause;

  //primary and required columns not supported currently
  public UpdateColumnValueSchemaUpdate(Integer dbVersion, String tableName, Column column, String value,
      String whereClause) {
    this.dbVersion = dbVersion;
    this.tableName = tableName;
    this.column = column;
    this.value = value;
    this.whereClause = whereClause;
  }


  @Override
  public SchemaUpdateType getUpdateType() {
    return SchemaUpdateType.UPDATE_COLUMN_VALUE;
  }

  @Override
  public Integer getDbVersion() {
    return dbVersion;
  }

  @Override
  public String getTableName() {
    return tableName;
  }

  public Column getColumn() {
    return column;
  }

  public String getValue() {
    return value;
  }

  public String getWhereClause() {
    return whereClause;
  }
}
