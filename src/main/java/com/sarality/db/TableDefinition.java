package com.sarality.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  private Map<Integer, Map<UpdateOperationType, List<UpdateOperation>>> versionUpdates = new HashMap<>();

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

  public TableDefinition withUpdateForVersion(int dbVersion, UpdateOperation updateOperation) {
    Map<UpdateOperationType, List<UpdateOperation>> updatesMap = versionUpdates.get(dbVersion);

    if (updatesMap == null) {
      updatesMap = new HashMap<>();
    }

    if (!updatesMap.containsKey(updateOperation.getUpdateType())) {
      updatesMap.put(updateOperation.getUpdateType(), new ArrayList<UpdateOperation>());
    }

    updatesMap.get(updateOperation.getUpdateType()).add(updateOperation);
    versionUpdates.put(dbVersion, updatesMap);
    return this;
  }

  Map<UpdateOperationType, List<UpdateOperation>> getUpdatesForVersion(int dbVersion) {
    Map<UpdateOperationType, List<UpdateOperation>> updatesMap = versionUpdates.get(dbVersion);

    return (updatesMap == null ? null : new HashMap<>(updatesMap));
  }


}
