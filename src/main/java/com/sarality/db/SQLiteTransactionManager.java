package com.sarality.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * A wrapper over the SQLIteDatabase that implements the Database interface for transaction support.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SQLiteTransactionManager implements TransactionManager {
  private final SQLiteDatabase database;

  SQLiteTransactionManager(SQLiteDatabase database) {
    this.database = database;
  }

  @Override
  public void beginTransaction() {
    database.beginTransaction();
  }

  @Override
  public void beginTransactionImmediate() {
    database.beginTransactionNonExclusive();
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
