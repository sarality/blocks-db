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
    String dbValue = cursor.getString(cursor.getColumnIndex(getColumnName(column)));
    if (dbValue == null) {
      return null;
    }
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

    contentValues.put(column.getName(), value.name());
  }
}
