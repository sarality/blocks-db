package com.sarality.db;

/**
 * Interface for all databases in the system
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface Database {

  String getName();

  void beginTransaction();

  void setTransactionSuccessful();

  void endTransaction();
}
