package com.sarality.db;

/**
 * Interface for all classes that manage Transactions
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface TransactionManager {

  void beginTransaction();

  void beginTransactionImmediate();

  void setTransactionSuccessful();

  void endTransaction();
}
