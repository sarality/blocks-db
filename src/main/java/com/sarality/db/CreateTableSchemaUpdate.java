package com.sarality.db;

/**
 * When used - indicates that the table has been created in this version of the database
 *
 * @author Satya@ (Satya Puniani)
 */

public class CreateTableSchemaUpdate implements SchemaUpdate {

  private final Integer dbVersion;
  private final String tableName;

  public CreateTableSchemaUpdate(Integer dbVersion, String tableName) {
    this.dbVersion = dbVersion;
    this.tableName = tableName;
  }

  @Override
  public SchemaUpdateType getUpdateType() {
    return SchemaUpdateType.CREATE_TABLE;
  }

  @Override
  public Integer getDbVersion() {
    return dbVersion;
  }

  @Override
  public String getTableName() {
    return tableName;
  }



}