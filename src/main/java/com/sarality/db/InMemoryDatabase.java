package com.sarality.db;

/**
 * A database implementation for an InMemoryTable
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class InMemoryDatabase implements Database {
  private final String dbName;

  public InMemoryDatabase(String dbName) {
    this.dbName = dbName;
  }

  @Override
  public String getName() {
    return dbName;
  }

  @Override
  public void beginTransaction() {
    // No-Op
  }

  @Override
  public void setTransactionSuccessful() {
    // No-Op
  }

  @Override
  public void endTransaction() {
    // No-Op
  }
}
