package com.sarality.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Schema Update operation for adding a new Index to an existing table.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class AddIndexSchemaUpdate implements SchemaUpdate {

  private final String tableName;
  private final String indexName;
  private final List<Column> columns;
  private final Integer dbVersion;

  //primary and required columns not supported currently
  public AddIndexSchemaUpdate(Integer dbVersion, String tableName, String indexName, Column column, Column... columns) {
    this.dbVersion = dbVersion;
    this.tableName = tableName;
    this.indexName = indexName;
    this.columns = new ArrayList<>();
    this.columns.add(column);
    if (columns != null) {
      Collections.addAll(this.columns, columns);
    }
  }


  @Override
  public SchemaUpdateType getUpdateType() {
    return SchemaUpdateType.ADD_INDEX;
  }

  @Override
  public Integer getDbVersion() {
    return dbVersion;
  }

  @Override
  public String getTableName() {
    return tableName;
  }

  public String getIndexName() {
    return indexName;
  }

  public List<Column> getColumns() {
    return columns;
  }
}
