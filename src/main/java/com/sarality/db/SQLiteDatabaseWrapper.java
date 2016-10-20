package com.sarality.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * A wrapper over the SQLIteDatabase that implements the Database interface for transaction support.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SQLiteDatabaseWrapper implements Database {
  private final String name;
  private final SQLiteDatabase database;

  public SQLiteDatabaseWrapper(String name, SQLiteDatabase database) {
    this.name = name;
    this.database = database;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void beginTransaction() {
    database.beginTransaction();
  }

  @Override
  public void setTransactionSuccessful() {
    database.setTransactionSuccessful();
  }

  @Override
  public void endTransaction() {
    database.endTransaction();
  }
}
