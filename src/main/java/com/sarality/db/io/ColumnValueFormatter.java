package com.sarality.db.io;

import com.sarality.db.Column;

/**
 * Formats value so that it can be used to query a column.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface ColumnValueFormatter<T> {

  /**
   * Get the formatted String that can be used to query the Column.
   *
   * @param column The column to be queried.
   * @param value The value of the Argument to be passed to the query,
   * @return Formatted String that can be used to query the column.
   */
  String getQueryArgValue(Column column, T value);
}
