package com.sarality.db;

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

  public static TableRegistry getInstance() {
    return INSTANCE;
  }

  private final Map<String, Table<?>> tableMap = new HashMap<>();
  private final Map<String, List<TableDefinition>> dbTableDefinitionsMap = new HashMap<>();

  public final void registerTable(Table<?> table) {
    tableMap.put(table.getName(), table);
    TableDefinition definition = table.getTableDefinition();
    String dbName = definition.getDatabaseName();
    if (!dbTableDefinitionsMap.containsKey(dbName)) {
      dbTableDefinitionsMap.put(dbName, new ArrayList<TableDefinition>());
    }
    dbTableDefinitionsMap.get(dbName).add(definition);
  }

  @SuppressWarnings("unchecked")
  public <T> Table<T> getTable(String tableName) {
    return (Table<T>) tableMap.get(tableName);
  }

  public List<TableDefinition> getTables(String dbName) {
    return dbTableDefinitionsMap.get(dbName);
  }
}
