package com.sarality.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the Database name, version and columns for Table.
 * <p/>
 * Used by {@link SQLiteDatabaseProvider} to manage to Table Schema.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class TableDefinition {

  // Name of the database e.g. users.db.
  private final String databaseName;
  // Name of the database e.g. Users.
  private final String tableName;
  // The version of the database defined by the given set of columns
  private final int tableVersion;
  // A list of updates for each version
  private List<SchemaUpdate> schemaUpdateList = new ArrayList<>();

  private final Column[] columns;

  public TableDefinition(String databaseName, String tableName, int tableVersion, Column[] columns) {
    this.databaseName = databaseName;
    this.tableName = tableName;
    this.tableVersion = tableVersion;
    this.columns = columns;
  }

  public String getDatabaseName() {
    return databaseName;
  }

  public String getTableName() {
    return tableName;
  }

  public int getTableVersion() {
    return tableVersion;
  }

  public Column[] getColumns() {
    return columns;
  }

  public TableDefinition withUpdateForVersion(SchemaUpdate schemaUpdate) {
    schemaUpdateList.add(schemaUpdate);
    return this;
  }

  List<SchemaUpdate> getSchemaUpdates() {

    return schemaUpdateList;
  }


}
