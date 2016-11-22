package com.sarality.db.io;

import android.content.ContentValues;
import android.database.Cursor;

import com.sarality.db.Column;
import com.sarality.db.DataType;
import com.sarality.db.common.EnumMapper;

/**
 * Reads and Writes data to/from a Column that stores an Enum based on a value that it is mapped to.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class MappedEnumColumn<V, T extends Enum<T>> extends BaseColumn implements ColumnValueReader<V>,
    ColumnValueWriter<V> {

  private final EnumMapper<V, T> mapper;

  public MappedEnumColumn(String prefix, EnumMapper<V, T> mapper) {
    super(prefix);
    this.mapper = mapper;
  }

  @Override
  public V getValue(Cursor cursor, Column column) {
    if (!column.getDataType().equals(DataType.ENUM)) {
      throw new IllegalStateException("Cannot extract Enum from Column " + column + " with data type "
          + column.getDataType() + " using this method");
    }
    if (mapper == null) {
      throw new IllegalArgumentException("Cannot extract Enum value from Column " + column
          + " without a way to map Enum value to String values");
    }
    int index = cursor.getColumnIndex(getColumnName(column));
    if (cursor.isNull(index)) {
      return null;
    }
    String dbValue = cursor.getString(index);
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

  @Override
  public void setValue(ContentValues contentValues, Column column, V value) {
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
