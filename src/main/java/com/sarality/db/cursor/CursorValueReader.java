package com.sarality.db.cursor;

import android.database.Cursor;

import com.sarality.db.Column;
import com.sarality.db.common.EnumMapper;

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

  public Boolean getBoolean(Cursor cursor, Column column) {
    return cursor.getInt(cursor.getColumnIndex(column.getName())) != 0;
  }

  public <T extends Enum<T>> Boolean getBoolean(Cursor cursor, Column column, EnumMapper<Boolean, T> mapper) {
    String dbValue = getString(cursor, column);
    if (dbValue == null) {
      return false;
    }
    T enumValue = mapper.valueOf(dbValue);
    return mapper.getValue(enumValue);
  }
}
