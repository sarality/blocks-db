package com.sarality.db.io;

import android.content.ContentValues;
import android.database.Cursor;

import com.sarality.db.Column;
import com.sarality.db.DataType;

/**
 * Reads and Writes data to/from a Column that stores an Enum.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class EnumColumn<T extends Enum<T>> extends BaseColumn implements ColumnValueReader<T>,
    ColumnValueWriter<T> {

  private final Class<T> enumClass;

  public EnumColumn(String prefix, Class<T> enumClass) {
    super(prefix);
    this.enumClass = enumClass;
  }

  @Override
  public T getValue(Cursor cursor, Column column) {
    if (!column.getDataType().equals(DataType.ENUM)) {
      throw new IllegalStateException("Cannot extract Enum from Column " + column + " with data type "
          + column.getDataType() + " using this method");
    }
    int index = cursor.getColumnIndex(getColumnName(column));
    if (cursor.isNull(index)) {
      return null;
    }
    String dbValue = cursor.getString(index);
    try {
      return Enum.valueOf(enumClass, dbValue);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("No enum found for " + dbValue);
    }
  }

  @Override
  public void setValue(ContentValues contentValues, Column column, T value) {
    checkForRequiredColumn(column, value);
    checkForColumnDataType(column, enumClass, DataType.ENUM);
    String name = null;
    if (value != null) {
      name = value.name();
    }
    contentValues.put(column.getName(), name);
  }
}
