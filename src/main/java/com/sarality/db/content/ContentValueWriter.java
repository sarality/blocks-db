package com.sarality.db.content;

import android.content.ContentValues;

import com.sarality.db.Column;
import com.sarality.db.DataType;
import com.sarality.db.common.BooleanEnum;
import com.sarality.db.common.EnumMapper;

/**
 * Utility to add values to a ContentValues object
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ContentValueWriter {

  private final ContentValues contentValues;

  public ContentValueWriter(ContentValues contentValues) {
    this.contentValues = contentValues;
  }

  public void addString(Column column, String value) {
    checkForRequiredColumn(column, value);
    checkForColumnDataType(column, String.class, DataType.TEXT);

    contentValues.put(column.getName(), value);
  }

  public void addLong(Column column, Long value) {
    checkForRequiredColumn(column, value);
    checkForColumnDataType(column, Long.class, DataType.INTEGER);

    contentValues.put(column.getName(), value);
  }

  public void addInt(Column column, Integer value) {
    checkForRequiredColumn(column, value);
    checkForColumnDataType(column, Integer.class, DataType.INTEGER);

    contentValues.put(column.getName(), value);
  }

  public void addBoolean(Column column, Boolean value) {
    checkForRequiredColumn(column, value);

    if (column.getDataType() == DataType.INTEGER) {
      if (value.equals(Boolean.FALSE)) {
        contentValues.put(column.getName(), 0);
      } else if (value.equals(Boolean.TRUE)) {
        contentValues.put(column.getName(), 1);
      }
    } else if (column.getDataType() == DataType.TEXT || column.getDataType() == DataType.ENUM) {
      if (value.equals(Boolean.FALSE)) {
        contentValues.put(column.getName(), BooleanEnum.FALSE.name());
      } else if (value.equals(Boolean.TRUE)) {
        contentValues.put(column.getName(), BooleanEnum.TRUE.name());
      }
    } else {
      throw new IllegalStateException("Cannot set Boolean value for Column " + column.getName() + " with data type "
          + column.getDataType());
    }
  }

  public <V, T extends Enum<T>> void addEnum(Column column, V value, EnumMapper<V, T> mapper) {
    T mappedValue = mapper.getMappedValue(value);
    checkForRequiredColumn(column, mappedValue);
    checkForColumnDataType(column, mapper.getEnumClass(), DataType.ENUM);

    String dbValue = null;
    if (mappedValue != null) {
      dbValue = mappedValue.name();
    }
    contentValues.put(column.getName(), dbValue);
  }

  private <V> void checkForRequiredColumn(Column column, V value) {
    if (column.isRequired() && value == null) {
      throw new IllegalArgumentException("Cannot add null value to required Column " + column.getName());
    }
  }

  private void checkForColumnDataType(Column column, Class<?> valueClass, DataType dataType) {
    if (column.getDataType() != dataType) {
      throw new IllegalArgumentException("Cannot add " + valueClass.getSimpleName() + " value to Column " +
          column.getName() + " with data type " + column.getDataType());
    }
  }

}
