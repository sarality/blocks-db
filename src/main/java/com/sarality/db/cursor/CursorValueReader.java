package com.sarality.db.cursor;

import android.database.Cursor;

import com.sarality.db.Column;
import com.sarality.db.DataType;
import com.sarality.db.common.EnumMapper;

/**
 * Utility class to read values from a database cursor.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class CursorValueReader {

  private final String prefix;

  public CursorValueReader(String prefix) {
    this.prefix = prefix;
  }

  public Integer getInt(Cursor cursor, Column column) {
    // TODO: Move to a DataType based IntegerColumnValueReader
    if (!column.getDataType().equals(DataType.INTEGER)) {
      throw new IllegalStateException("Cannot extract Integer from Column " + column + " with data type "
          + column.getDataType());
    }
    return cursor.getInt(cursor.getColumnIndex(getColumnName(column)));
  }

  public Long getLong(Cursor cursor, Column column) {
    if (!column.getDataType().equals(DataType.INTEGER)) {
      throw new IllegalStateException("Cannot extract Long from Column " + column + " with data type "
          + column.getDataType());
    }
    return cursor.getLong(cursor.getColumnIndex(getColumnName(column)));
  }

  public String getString(Cursor cursor, Column column) {
    // TODO: Move to a DataType based TextColumnValueReader
    return cursor.getString(cursor.getColumnIndex(getColumnName(column)));
  }

  public Boolean getBoolean(Cursor cursor, Column column) {
    // TODO: Move to a DataType based IntegerColumnValueReader and TextColumnValueReader
    if (column.getDataType().equals(DataType.INTEGER)) {
      Integer value = getInt(cursor, column);
      if (column.isRequired() && value == null) {
        throw new IllegalStateException("Required column " + column + " has no value");
      }
      return cursor.getInt(cursor.getColumnIndex(getColumnName(column))) != 0;
    } else if (column.getDataType().equals(DataType.TEXT)) {
      return cursor.getString(cursor.getColumnIndex(getColumnName(column))).equals(String.valueOf(Boolean.TRUE));
    } else {
      throw new IllegalStateException("Cannot extract boolean from Column " + column + " with data type "
          + column.getDataType());
    }
  }

  public <T extends Enum<T>> Boolean getBoolean(Cursor cursor, Column column, EnumMapper<Boolean, T> mapper) {
    if (!column.getDataType().equals(DataType.ENUM)) {
      throw new IllegalStateException("Cannot extract boolean from Column " + column + " with data type "
          + column.getDataType() + " using this method");
    }
    if (mapper == null) {
      throw new IllegalArgumentException("Cannot extract boolean value from Column " + column
          + " with a way to may Enum value to Boolean values");
    }
    String dbValue = getString(cursor, column);
    if (dbValue == null) {
      return null;
    }
    T enumValue;
    try {
      enumValue = mapper.valueOf(dbValue);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("No enum found for " + dbValue);
    }
    if (enumValue != null && mapper.hasValue(enumValue)) {
      return mapper.getValue(enumValue);
    }
    throw new IllegalStateException("No mapped value for " + dbValue + " in Column " + column);
  }

  private String getColumnName(Column column) {
    if (prefix == null) {
      return column.getName();
    } else {
      return prefix + "." + column.getName();
    }
  }
}
