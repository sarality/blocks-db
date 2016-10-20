package com.sarality.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Add description here
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class DatabaseRegistry {
  private final TableRegistry tableRegistry;
  private final Map<String, DatabaseProvider> providerMap = new HashMap<>();

  public DatabaseRegistry(TableRegistry tableRegistry) {
    this.tableRegistry = tableRegistry;
  }

  public void init(TableDefinition definition, SQLiteDatabaseProvider provider) {
    String dbName = definition.getDatabaseName();
    if (!providerMap.containsKey(dbName)) {
      List<TableDefinition> definitionList = tableRegistry.getTables(dbName);
      provider.init(definitionList);
      providerMap.put(dbName, provider);
    }
  }

  public DatabaseProvider getProvider(String dbName) {
    return providerMap.get(dbName);
  }
}
