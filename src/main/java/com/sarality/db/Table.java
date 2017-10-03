package com.sarality.db;

import android.content.ContentValues;
import android.content.Context;

import com.sarality.db.query.Query;

import java.util.List;

/**
 * Interface for all database tables used to store and query data
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface Table<T> {

  /**
   * @return String name of the Table
   */
  String getName();

  /**
   * @return Metadata for the Table that defines the columns, db, version, and schema upates for the table
   */
  TableDefinition getTableDefinition();

  void initDatabase(Context context, DatabaseRegistry dbRegistry);

  /**
   * Open the table's database for reading / writing
   */
  void open();

  /**
   * Close the table's data for reading / writing
   */
  void close();

  /**
   * @return The currently active Transaction Manager or create a new one if one doesn't exist so far.
   */
  TransactionManager getTransactionManager();

  /**
   * Create a row in the table with the given data.
   *
   * @param data Data for the record to be created.
   * @return Id for the new record that was created.
   */
  Long create(T data);

  /**
   * Create a row in the table woth the given data and additional RecordModifiers
   *
   * @param data Data for the new Row
   * @param additionalModifiers Addition Modifiers to be appilied to the data before it is created
   * @return Id for the new row that was created.
   */
  Long create(ContentValues data, RecordModifier... additionalModifiers);

  /**
   * Read all rows that match the given query.
   *
   * @param query Query to run on the table.
   * @return List of data that match the given query
   */
  List<T> readAll(Query query);

  /**
   * Update rows in the table with the new data provided.
   *
   * @param data Data with the new values for the record
   * @param query Query to match the rows that need to be updated
   * @return Number of rows that were updated.
   */
  int update(T data, Query query);

  /**
   * Update rows in the table with the new data provided.
   *
   * @param data Data with the new values for the record
   * @param query Query to match the rows that need to be updated
   * @param additionalModifiers Addition Modifiers to be applied to the data before it is updated
   * @return Number of rows that were updated.
   */
  int update(ContentValues data, Query query, RecordModifier... additionalModifiers);

  /**
   * Delete All rows that match the given query.
   *
   * @param query Query to match the rows that need to be deleted
   * @return Number of Rows that were deleted.
   */
  int delete(Query query);

  /**
   * Mark all rows that match the given query as Deleted.
   *
   * @param data Data with the new values for the records that need to be marked as deleted.
   * @param query Query to match the rows that need to be marked as deleted.
   * @return Number of rows that were marked as deleted.
   */
  int markAsDeleted(T data, Query query);
}
