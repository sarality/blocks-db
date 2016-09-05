package com.sarality.db;

import com.sarality.db.query.Query;

import java.util.List;

/**
 * Interface for all database tables used to store and query data
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface Table<T> {

  void open();

  void close();

  Long create(T data);

  List<T> readAll(Query query);

  void update(T data, Query query);

  void delete(Query query);
}
