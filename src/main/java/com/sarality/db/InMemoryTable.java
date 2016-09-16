package com.sarality.db;

import com.sarality.db.query.Query;
import com.sarality.db.query.SimpleQueryExtractor;

import java.util.ArrayList;
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

  public abstract Column getPrimaryKeyField();

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

    // Null query, just return everything
    if (query == null) {
      dataList.addAll(dataMap.values());
      return dataList;
    }

    dataList.add(dataMap.get(extractKeyFromQuery(query)));

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

  protected final Long extractKeyFromQuery(Query query){

    Long key;

    // Check if Query format is supported
    SimpleQueryExtractor simpleQueryExtractor = new SimpleQueryExtractor(query);

    // Check the format of the whereClause
    if (simpleQueryExtractor.getColumnList().size() != 1) {
      throw new UnsupportedOperationException("There can be only one predicate in the query");
    }

    // Check the field name should be the same as the primary key
    if (!simpleQueryExtractor.getColumnList().get(0).equalsIgnoreCase(getPrimaryKeyField().getName())) {
      throw new UnsupportedOperationException("Column name must be the name of the primary key field");
    }

    // Assumes that there is only 1 argument in the where clause which is a primary key Id to retrieve.
    if (simpleQueryExtractor.getArgumentValueList().size() !=1 ) {
      throw new UnsupportedOperationException("There should be only one argument which should be a primary key Id");
    }

    return Long.parseLong(simpleQueryExtractor.getArgumentValueList().get(0));
  }
}
