package com.sarality.db;

import android.content.ContentValues;
import android.content.Context;

import com.sarality.db.query.ParsedQuery;
import com.sarality.db.query.Query;

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

  private final Map<Long, T> dataMap = new LinkedHashMap<>();
  private final TableDefinition tableDefinition;

  private TransactionManager transactionManager;

  public InMemoryTable(TableDefinition tableDefinition) {
    this.tableDefinition = tableDefinition;
  }

  public abstract void setId(T data, Long id);

  public abstract Column getPrimaryKey();

  @Override
  public String getName() {
    return tableDefinition.getTableName();
  }

  @Override
  public TableDefinition getTableDefinition() {
    return tableDefinition;
  }

  @Override
  public void open() {
    this.transactionManager = new InMemoryTransactionManager();
    // Nothing needs to be done here
  }

  @Override
  public void close() {
    this.transactionManager = null;
    // Nothing needs to be done here
  }

  @Override
  public TransactionManager getTransactionManager() {
    return transactionManager;
  }

  @Override
  public void initDatabase(Context context, DatabaseRegistry dbRegistry) {
    // Nothing needs to be done to initialize InMemoryTable
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
    ParsedQuery parsedQuery = new ParsedQuery(query);

    // check if this is a supported query
    if (!isPrimaryKeyQuery(parsedQuery)) {
      throw new UnsupportedOperationException(
          "This type of query is not supported in InMemoryTables. The where clause" +
              " should be of the format primaryKeyId = ?");
    }

    // get the item that matches the query request
    dataList.add(dataMap.get(getKey(parsedQuery)));
    return dataList;
  }

  @Override
  public int update(ContentValues data, Query query) {
    throw new UnsupportedOperationException("Updates using ContentValues is not supported");
  }

  @Override
  public int update(T data, Query query) {
    // Updates a single row in the InMemory Table identified by the primary key.
    if (query == null) {
      throw new UnsupportedOperationException(
          "Only updates of a single row are supported in in-memory tables. A query of the type primaryKeyId = ? is " +
              "needed for the update operation.");
    }

    // parse the query
    ParsedQuery parsedQuery = new ParsedQuery(query);

    // check if this is a supported query
    if (!isPrimaryKeyQuery(parsedQuery)) {
      throw new UnsupportedOperationException(
          "This type of query is not supported in InMemoryTables. The where clause" +
              " should be of the format primaryKeyId = ?");

    }

    // update the object
    dataMap.put(getKey(parsedQuery), data);

    return 1;
  }

  @Override
  public int delete(Query query) {
    // Not implemented
    return 0;
  }

  private boolean isPrimaryKeyQuery(ParsedQuery parsedQuery) {
    // Check the format of the whereClause
    boolean isSupported = false;

    // Check the field name should be the same as the primary key
    isSupported = parsedQuery.getColumnList().size() == 1
        & parsedQuery.getColumnList().get(0).equalsIgnoreCase(getPrimaryKey().getName())
        & parsedQuery.getArgumentValueList().size() == 1;

    return isSupported;

  }

  private Long getKey(ParsedQuery parsedQuery) {
    return Long.parseLong(parsedQuery.getArgumentValueList().get(0));
  }
}
