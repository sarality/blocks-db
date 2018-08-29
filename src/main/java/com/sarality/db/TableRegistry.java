package com.sarality.db;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A registry for Database tables in the system.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class TableRegistry {

  private static final TableRegistry INSTANCE = new TableRegistry();
  private final DatabaseRegistry dbRegistry = new DatabaseRegistry();

  public static TableRegistry getInstance() {
    return INSTANCE;
  }

  private TableRegistry() {
    // Private Constructor to be used only by class.
  }

  private final Map<String, Table<?>> tableMap = new HashMap<>();
  private final Map<String, List<TableDefinition>> dbTableDefinitionListMap = new HashMap<>();

  public final void registerTable(Table<?> table) {
    tableMap.put(table.getName(), table);
    TableDefinition definition = table.getTableDefinition();
    String dbName = definition.getDatabaseName();
    if (!dbTableDefinitionListMap.containsKey(dbName)) {
      dbTableDefinitionListMap.put(dbName, new ArrayList<TableDefinition>());
    }
    dbTableDefinitionListMap.get(dbName).add(definition);

    // Register the Table with the Database Registry as well
    dbRegistry.registerTable(definition);
  }

  @SuppressWarnings("unchecked")
  public <T> Table<T> getTable(String tableName) {
    return (Table<T>) tableMap.get(tableName);
  }

  public void initDatabases(Context context) {
    dbRegistry.init(context);
  }

  public void initTables() {
    // Just Open and close the Table to initialize the database and the tables in it
    for (String tableName : tableMap.keySet()) {
      Table<?> table = tableMap.get(tableName);
      try {
        table.open();
      } finally {
        table.close();
      }
    }
  }

  // TODO(abhideep): Find a different way to provide access to the Database Provider
  public DatabaseProvider getDatabaseProvider(String dbName) {
    return dbRegistry.getProvider(dbName);
  }
}
