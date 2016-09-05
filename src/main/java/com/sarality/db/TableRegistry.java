package com.sarality.db;

import java.util.HashMap;
import java.util.Map;

/**
 * A registry for Database tables in the system.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class TableRegistry {
  private final Map<String, Table<?>> tableMap = new HashMap<>();

  public final void registerTable(String tableName, Table<?> table) {
    tableMap.put(tableName, table);
  }

  @SuppressWarnings("unchecked")
  public <T> Table<T> getTable(String tableName) {
    return (Table<T>) tableMap.get(tableName);
  }
}
