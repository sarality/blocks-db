package com.sarality.db;

/**
 * A transaction manager implementation for an InMemoryTable
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class InMemoryTransactionManager implements TransactionManager {

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
