package com.sarality.db.io;

import android.content.ContentValues;
import android.database.Cursor;

import com.sarality.db.Column;
import com.sarality.db.DataType;
import com.sarality.db.common.EnumMapper;

/**
 * Reads and Writes data from a Boolean Column
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class BooleanColumn extends BaseColumn {

  public <T extends Enum<T>> Boolean getValue(Cursor cursor, Column column, String prefix,
      EnumMapper<Boolean, T> mapper) {
    if (!column.getDataType().equals(DataType.ENUM)) {
      throw new IllegalStateException("Cannot extract boolean from Column " + column + " with data type "
          + column.getDataType() + " using this method");
    }
    if (mapper == null) {
      throw new IllegalArgumentException("Cannot extract boolean value from Column " + column
          + " with a way to may Enum value to Boolean values");
    }
    String dbValue = cursor.getString(cursor.getColumnIndex(getColumnName(column, prefix)));
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

  public <T extends Enum<T>> void setValue(ContentValues contentValues, Column column, Boolean value,
      EnumMapper<Boolean, T> mapper) {
    T mappedValue = mapper.getMappedValue(value);
    checkForRequiredColumn(column, mappedValue);
    checkForColumnDataType(column, mapper.getEnumClass(), DataType.ENUM);

    String dbValue = null;
    if (mappedValue != null) {
      dbValue = mappedValue.name();
    }
    contentValues.put(column.getName(), dbValue);
  }
}
