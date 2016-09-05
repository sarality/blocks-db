package com.sarality.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sarality.db.cursor.CursorDataExtractor;
import com.sarality.db.query.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation of a table to store and query data from a SQLite database.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SQLiteTable<T> implements Table<T> {

  private static final Logger logger = LoggerFactory.getLogger(SQLiteTable.class);

  // Metadata for the Table like table name and version.
  private final TableDefinition tableDefinition;
  // Class to extract data object from a Cursor
  private final CursorDataExtractor<T> extractor;
  // Class to populate a Content Values from a data object.
  private final ContentValuesPopulator<T> populator;
  // Utility class used to access the underlying database as well as manage the schema of the table.
  private final SQLiteDatabaseProvider dbProvider;

  // Reference of the underlying database instance that is used to query and update the data.
  private SQLiteDatabase database;
  private AtomicInteger dbOpenCounter = new AtomicInteger();

  public SQLiteTable(Context context, TableDefinition tableDefinition,
      CursorDataExtractor<T> extractor, ContentValuesPopulator<T> populator) {
    this.tableDefinition = tableDefinition;
    this.extractor = extractor;
    this.populator = populator;
    this.dbProvider = new SQLiteDatabaseProvider(context.getApplicationContext(), tableDefinition);
  }

  /**
   * Open a writable instance of the database.
   *
   * @throws SQLException if there is an error opening the database for writing.
   */
  @Override
  public synchronized final void open() throws SQLException {
    logger.debug("Opening database for Table {} ", tableDefinition.getTableName());
    if (dbOpenCounter.incrementAndGet() == 1) {
      // This will automatically create or update the table as needed
      this.database = dbProvider.getWritableDatabase();
      logger.info("Opened database for Table {} Open Counter {}", tableDefinition.getTableName(), dbOpenCounter.get());
    }
  }

  /**
   * Close the underlying database instance
   */
  @Override
  public synchronized final void close() {
    logger.debug("Closing database for Table {} ", tableDefinition.getTableName());
    if (dbOpenCounter.decrementAndGet() == 0) {
      this.database.close();
      logger.info("Closed database for Table {} ", tableDefinition.getTableName());
    }
  }

  protected void assertDatabaseOpen() {
    if (database == null) {
      throw new IllegalStateException(
          "Cannot perform operation since the database was either not opened or has already been closed.");
    }
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
  public void delete(Query query) {
    database.delete(tableDefinition.getTableName(), query.getWhereClause(), query.getWhereClauseArguments());
  }

  /**
   * Retrieve the set of rows from the table for the given query.
   *
   * @param query Query to run on the table
   * @return List of data that was returned for the query
   */
  @Override
  public List<T> readAll(Query query) {
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
   * Update all rows that match the query with the data provided in the given data object.
   *
   * @param data Data with the values that need to be updated.
   * @param query Query for the rows that need to be updated.
   */
  @Override
  public void update(T data, Query query) {
    assertDatabaseOpen();

    // TODO(abhideep) Before we update the data, sanitize and validate that the data is valid.

    ContentValues contentValues = new ContentValues();
    populator.populate(contentValues, data);
    int num = database.update(getTableName(), contentValues, query.getWhereClause(), query.getWhereClauseArguments());
    logger.debug("Updated {} number of rows in table {}", num, getTableName());
  }

  private String getTableName() {
    return tableDefinition.getTableName();
  }
}
