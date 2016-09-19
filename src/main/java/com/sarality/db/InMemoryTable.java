package com.sarality.db;

import com.sarality.db.query.Query;
import com.sarality.db.query.QueryParser;

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

    // parse the query
    QueryParser queryParser = new QueryParser(query);

    // check if this is a supported query
    if (!isSupportedQueryType(queryParser)) {
      throw new UnsupportedOperationException(
          "This type of query is not supported in InMemoryTables. The where clause" +
              " should be of the format primaryKeyId = ?");
    }

    // get the item that matches the query request
    dataList.add(dataMap.get(getKey(queryParser)));
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

  private boolean isSupportedQueryType(QueryParser queryParser) {
    // Check the format of the whereClause
    boolean isSupported = false;

    // Check the field name should be the same as the primary key
    isSupported = queryParser.getColumnList().size() == 1
        & queryParser.getColumnList().get(0).equalsIgnoreCase(getPrimaryKeyField().getName())
        & queryParser.getArgumentValueList().size() == 1;

    return isSupported;

  }

  private Long getKey(QueryParser queryParser) {
    return Long.parseLong(queryParser.getArgumentValueList().get(0));
  }
}
