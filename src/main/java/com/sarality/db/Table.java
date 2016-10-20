package com.sarality.db;

import android.content.Context;

import com.sarality.db.query.Query;

import java.util.List;

/**
 * Interface for all database tables used to store and query data
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface Table<T> {

  String getName();

  TableDefinition getTableDefinition();

  void initDatabase(Context context, DatabaseRegistry dbRegistry);

  void open();

  void close();

  TransactionManager getTransactionManager();

  Long create(T data);

  List<T> readAll(Query query);

  int update(T data, Query query);

  int delete(Query query);
}
