package com.sarality.db;

import android.content.ContentValues;
import android.database.Cursor;

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

  int update(T data, Query query);

  int delete(Query query);
}