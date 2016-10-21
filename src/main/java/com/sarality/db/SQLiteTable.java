package com.sarality.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sarality.db.content.ContentValuesPopulator;
import com.sarality.db.cursor.CursorDataExtractor;
import com.sarality.db.query.Query;
import com.sarality.db.query.RawQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a table to store and query data from a SQLite database.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SQLiteTable<T> implements Table<T> {

  private static final Logger logger = LoggerFactory.getLogger(SQLiteTable.class.getSimpleName());

  // Metadata for the Table like table name and version.
  private final TableDefinition tableDefinition;
  // Class to extract data object from a Cursor
  private final CursorDataExtractor<T> extractor;
  // Class to populate a Content Values from a data object.
  private final ContentValuesPopulator<T> populator;

  // Utility class used to access the underlying database as well as manage the schema of the table.
  private SQLiteDatabaseProvider dbProvider;

  // Reference of the underlying database instance that is used to query and update the data.
  private SQLiteDatabase database;
  private SQLiteTransactionManager transactionManager;

  public SQLiteTable(Context context, TableDefinition tableDefinition,
      CursorDataExtractor<T> extractor, ContentValuesPopulator<T> populator) {
    this.tableDefinition = tableDefinition;
    this.extractor = extractor;
    this.populator = populator;
  }

  @Override
  public String getName() {
    return tableDefinition.getTableName();
  }

  @Override
  public TableDefinition getTableDefinition() {
    return tableDefinition;
  }

  /**
   * Open a writable instance of the database.
   *
   * @throws SQLException if there is an error opening the database for writing.
   */
  @Override
  public synchronized final void open() throws SQLException {
    logger.info("Opening database for Table {} ", tableDefinition.getTableName());
    this.database = dbProvider.getDatabase();
    this.transactionManager = new SQLiteTransactionManager(database);
  }

  /**
   * Close the underlying database instance
   */
  @Override
  public synchronized final void close() {
    logger.debug("Closing database for Table {} ", tableDefinition.getTableName());
    if (this.database != null && this.database.isOpen()) {
      dbProvider.resetDatabase();
    }
    this.database = null;
  }

  protected void assertDatabaseOpen() {
    if (database == null || !database.isOpen()) {
      throw new IllegalStateException(
          "Cannot perform operation since the database was either not opened or has already been closed.");
    }
  }

  public void initDatabase(Context context, DatabaseRegistry databaseRegistry) {
    String dbName = tableDefinition.getDatabaseName();
    int dbVersion = tableDefinition.getTableVersion();
    databaseRegistry.init(tableDefinition, new SQLiteDatabaseProvider(context, dbName, dbVersion));

    dbProvider = (SQLiteDatabaseProvider) databaseRegistry.getProvider(dbName);

    // Just Open and close the Table to initialize the database
    try {
      open();
    } finally {
      close();
    }
  }

  @Override
  public TransactionManager getTransactionManager() {
    return transactionManager;
  }

  /**
   * Create a row in the database table.
   *
   * @param data The data for the row that needs to be created.
   * @return The data for the row that was created with the appropriate id also populated.
   */
  @Override
  public Long create(T data) {
    assertDatabaseOpen();

    // TODO(abhideep): Before we create the data, sanitize and validate that the data is valid.

    ContentValues contentValues = new ContentValues();
    if (populator.populate(contentValues, data)) {
      logger.debug("Adding new row to table {} with Content Values {}", getTableName(), contentValues);

      // TODO(abhideep): Call a method that converts a rowd Id to a Long
      return database.insert(getTableName(), null, contentValues);
    }
    return (long) -1;
  }

  /**
   * Delete the row or rows for the given query.
   *
   * @param query Query to define the set of rows that need to be deleted.
   */
  @Override
  public int delete(Query query) {
    assertDatabaseOpen();
    return database.delete(tableDefinition.getTableName(), query.getWhereClause(), query.getWhereClauseArguments());
  }

  /**
   * Retrieve the set of rows from the table for the given query.
   *
   * @param query Query to run on the table
   * @return List of data that was returned for the query
   */
  @Override
  public List<T> readAll(Query query) {
    assertDatabaseOpen();
    Cursor cursor = null;
    List<T> dataList = new ArrayList<T>();

    try {
      if (query == null) {
        cursor = database.query(getTableName(), new String[] {}, null, null, null, null, null);
      } else {
        cursor = database.query(getTableName(), null, query.getWhereClause(), query.getWhereClauseArguments(),
            null, null, query.getOrderByClause());
      }

      cursor.moveToFirst();
      while (!cursor.isAfterLast()) {
        T data = extractor.extract(cursor, query);
        dataList.add(data);
        cursor.moveToNext();
      }
      logger.debug("Query on table {} returned {} values", getTableName(), dataList.size());
    } finally {
      // Make sure to close the cursor
      if (cursor != null) {
        cursor.close();
      }
    }
    return dataList;
  }


  /**
   * Retrieve the set of rows from the table for the given query.
   *
   * @param query Query to run on the table
   * @return List of data that was returned for the query
   */
  public List<T> readAll(RawQuery query, CursorDataExtractor<T> extractor) {
    assertDatabaseOpen();
    Cursor cursor = null;
    List<T> dataList = new ArrayList<T>();

    try {
      cursor = database.rawQuery(query.getQuery(), query.getQueryArguments());

      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
        T data = extractor.extract(cursor, query);
        dataList.add(data);
        cursor.moveToNext();
      }
      logger.debug("Query on table {} returned {} values", getTableName(), dataList.size());
    } finally {
      // Make sure to close the cursor
      if (cursor != null) {
        cursor.close();
      }
    }
    return dataList;
  }

  /**
   * Update all rows that match the query with the data provided in the given data object.
   *
   * @param data Data with the values that need to be updated.
   * @param query Query for the rows that need to be updated.
   */
  @Override
  public int update(T data, Query query) {
    assertDatabaseOpen();

    // TODO(abhideep) Before we update the data, sanitize and validate that the data is valid.

    ContentValues contentValues = new ContentValues();
    populator.populate(contentValues, data);
    int num = database.update(getTableName(), contentValues, query.getWhereClause(), query.getWhereClauseArguments());
    logger.debug("Updated {} number of rows in table {}", num, getTableName());
    return num;
  }

  private String getTableName() {
    return tableDefinition.getTableName();
  }
}
