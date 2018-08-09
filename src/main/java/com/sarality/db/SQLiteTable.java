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
import java.util.Arrays;
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
  // Class to populate a Content Values from a data object when the record needs to be marked as deleted.
  private final ContentValuesPopulator<T> deletionRecordPopulator;

  // List of classes that modify the contents of a Row
  private final List<RecordModifier> recordModifierList = new ArrayList<>();

  // Utility class used to access the underlying database as well as manage the schema of the table.
  private SQLiteDatabaseProvider dbProvider;

  // Reference of the underlying database instance that is used to query and update the data.
  private SQLiteDatabase database;
  private SQLiteTransactionManager transactionManager;


  public SQLiteTable(TableDefinition tableDefinition,
      CursorDataExtractor<T> extractor,
      ContentValuesPopulator<T> populator,
      ContentValuesPopulator<T> deletionRecordPopulator,
      RecordModifier... recordModifiers) {
    this.tableDefinition = tableDefinition;
    this.extractor = extractor;
    this.populator = populator;
    this.deletionRecordPopulator = deletionRecordPopulator;
    if (recordModifiers != null) {
      List<RecordModifier> modifierList = Arrays.asList(recordModifiers);
      this.recordModifierList.addAll(modifierList);
    }
  }

  public SQLiteTable(TableDefinition tableDefinition,
      CursorDataExtractor<T> extractor,
      ContentValuesPopulator<T> populator,
      RecordModifier... recordModifiers) {
    this(tableDefinition, extractor, populator, populator, recordModifiers);
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
    if (this.database == null || !this.database.isOpen()) {
      this.database = dbProvider.getDatabase();
      this.transactionManager = new SQLiteTransactionManager(database);
    }
  }

  /**
   * Close the underlying database instance
   */
  @Override
  public synchronized final void close() {
    logger.debug("Closing database for Table {} ", tableDefinition.getTableName());
    // For simplicity in Multithreaded enviornments, we no longer resetDatabase here on the DBProvider
    // The Database remains open and we make sure there is only one instance of the DbProvider per database.
  }

  private void assertDatabaseOpen() {
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
    ContentValues contentValues = new ContentValues();
    if (populator.populate(contentValues, data)) {
      return create(contentValues);
    }
    return -1L;
  }

  /**
   * Create a row in the database table.
   *
   * @param data The data for the row that needs to be created.
   * @return The data for the row that was created with the appropriate id also populated.
   */
  @Override
  public Long create(ContentValues data, RecordModifier... additionalModifiers) {
    assertDatabaseOpen();

    prepareForCreate(data, additionalModifiers);

    logger.debug("Adding new row to table {} with Content Values {}", getTableName(), data);
    return database.insert(getTableName(), null, data);
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
    return readAll(query, extractor);
  }

  /**
   * retrieve a set of rows given a Query and a CDE.
   *
   * @param query     Query to run on the table
   * @param extractor CursorDataExtractor for custom mapping columns for eg aggregate queries
   * @return List of data objects
   */
  public <K> List<K> readAll(Query query, CursorDataExtractor<K> extractor) {
    assertDatabaseOpen();
    Cursor cursor = null;
    List<K> dataList = new ArrayList<>();

    try {
      if (query == null) {
        cursor = database.query(getTableName(), new String[] {}, null, null, null,
            null, null);
      } else {
        cursor = database.query(getTableName(), query.getSelectColumns(), query.getWhereClause(),
            query.getWhereClauseArguments(), query.getGroupByClause(), query.getHavingClause(),
            query.getOrderByClause(), query.getLimit());
      }

      cursor.moveToFirst();
      while (!cursor.isAfterLast()) {
        K data = extractor.extract(cursor, query);
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
    List<T> dataList = new ArrayList<>();

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
   * @param data  Data with the values that need to be updated.
   * @param query Query for the rows that need to be updated.
   */
  @Override
  public int update(T data, Query query) {
    ContentValues contentValues = new ContentValues();
    populator.populate(contentValues, data);
    return update(contentValues, query);
  }

  @Override
  public int update(ContentValues contentValues, Query query, RecordModifier... additionalModifiers) {
    assertDatabaseOpen();

    prepareForUpdate(contentValues, additionalModifiers);

    int num = database.update(getTableName(), contentValues, query.getWhereClause(), query.getWhereClauseArguments());
    logger.debug("Updated {} number of rows in table {}", num, getTableName());
    return num;
  }

  @Override
  public int markAsDeleted(T data, Query query) {
    ContentValues contentValues = new ContentValues();
    deletionRecordPopulator.populate(contentValues, data);
    return update(contentValues, query);
  }


  String getTableName() {
    return tableDefinition.getTableName();
  }

  private void prepareForCreate(ContentValues contentValues, RecordModifier... additionalModifiers) {
    for (RecordModifier recordModifier : recordModifierList) {
      recordModifier.onCreate(contentValues);
    }
    if (additionalModifiers != null) {
      for (RecordModifier recordModifier : additionalModifiers) {
        recordModifier.onCreate(contentValues);
      }
    }
  }

  private void prepareForUpdate(ContentValues contentValues, RecordModifier... additionalModifiers) {
    for (RecordModifier recordModifier : recordModifierList) {
      recordModifier.onUpdate(contentValues);
    }
    if (additionalModifiers != null) {
      for (RecordModifier recordModifier : additionalModifiers) {
        recordModifier.onUpdate(contentValues);
      }
    }
  }
}
