package com.sarality.db;

import com.android.internal.util.Predicate;
import com.sarality.db.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * An In-Memory implementation of a Table
 *
 * @author abhideep@ (Abhideep Singh)
 */
public abstract class InMemoryTable<T> implements Table<T> {

  private static final AtomicLong PRIMARY_KEY = new AtomicLong(1000);
  private Map<Long, T> dataMap = new LinkedHashMap<>();

  public abstract void setId(T data, Long id);

  @Override
  public void open() {
    // Nothing needs to be done here
  }

  @Override
  public void close() {
    // Nothing needs to be done here
  }

  @Override
  public Long create(T data) {
    Long newId = PRIMARY_KEY.getAndIncrement();
    setId(data, newId);
    dataMap.put(newId, data);
    return newId;
  }

  @Override
  public List<T> readAll(Query query) {

    List<T> dataList = new ArrayList<>();

    if (query == null) {
      dataList.addAll(dataMap.values());
      return dataList;
    }

    String[] whereArgs = query.getWhereClauseArguments();

    if (whereArgs.length != 0) {
      //filter the list
      Long key = Long.parseLong(whereArgs[0]);
      dataList.add(dataMap.get(key));
    }

    return dataList;
  }

  @Override
  public int update(T data, Query query) {
    // Not implemented
    return 0;
  }

  @Override
  public int delete(Query query) {
    // Not implemented
    return 0;
  }

}
