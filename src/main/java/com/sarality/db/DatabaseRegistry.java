package com.sarality.db;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A registry of all SQLite databases in the App
 *
 * @author abhideep@ (Abhideep Singh)
 */
class DatabaseRegistry {
  private final Map<String, List<TableDefinition>> tableDefinitionMap = new HashMap<>();
  private final Map<String, Integer> maxDbVersionMap = new HashMap<>();
  private final Map<String, DatabaseProvider> providerMap = new HashMap<>();

  /**
   * Initialize the Databases only after ALL Tables have been registered with it.
   *
   * NOTE!!!!!: Since we create ONLY ONE {@code SQLiteDatabaseProvider} for a database, make sure that All Tables for
   * the database have been registered in the Table Registry BEFORE calling this method.
   *
   * @param context The Context for the Application in which database is being initialized.
   */
  void init(Context context) {
    for (String dbName : tableDefinitionMap.keySet()) {
      List<TableDefinition> definitionList = tableDefinitionMap.get(dbName);
      int maxDbVersion = maxDbVersionMap.get(dbName);
      SQLiteDatabaseProvider provider = new SQLiteDatabaseProvider(
          context.getApplicationContext(), dbName, maxDbVersion);
      provider.init(definitionList);
      providerMap.put(dbName, provider);

    }
  }

  void registerTable(TableDefinition tableDefinition) {
    String dbName = tableDefinition.getDatabaseName();
    int dbVersion = tableDefinition.getTableVersion();
    if (!tableDefinitionMap.containsKey(dbName)) {
      tableDefinitionMap.put(dbName, new ArrayList<TableDefinition>());
    }
    tableDefinitionMap.get(dbName).add(tableDefinition);

    if (!maxDbVersionMap.containsKey(dbName)) {
      maxDbVersionMap.put(dbName, dbVersion);
    }

    int currentMax = maxDbVersionMap.get(dbName);
    if (dbVersion > currentMax) {
      maxDbVersionMap.put(dbName, dbVersion);
    }
  }

  DatabaseProvider getProvider(String dbName) {
    return providerMap.get(dbName);
  }

  void closeDatabase(String dbName) {
    DatabaseProvider dbProvider = getProvider(dbName);
    if (dbProvider != null) {
      dbProvider.closeDatabase();
    }
  }
}
