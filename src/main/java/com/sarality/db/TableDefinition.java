package com.sarality.db;

/**
 * Defines the Database name, version and columns for Table.
 * <p/>
 * Used by {@link SQLiteDatabaseProvider} to manage to Table Schema.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface TableDefinition {

  String getDatabaseName();

  int getDatabaseVersion();

  String getTableName();

  Column[] getColumns();
}
