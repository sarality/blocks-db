package com.sarality.db.cursor;

import android.database.Cursor;

import com.sarality.db.Column;

/**
 * Utility class to read values from a database cursor.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class CursorValueReader {

  public Integer getInt(Cursor cursor, Column column) {
    return cursor.getInt(cursor.getColumnIndex(column.getName()));
  }

  public Long getLong(Cursor cursor, Column column) {
    return cursor.getLong(cursor.getColumnIndex(column.getName()));
  }

  public String getString(Cursor cursor, Column column) {
    return cursor.getString(cursor.getColumnIndex(column.getName()));
  }
}
