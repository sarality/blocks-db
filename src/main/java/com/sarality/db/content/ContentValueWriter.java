package com.sarality.db.content;

import android.content.ContentValues;

import com.sarality.db.Column;
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
    contentValues.put(column.getName(), value);
  }

  public void addLong(Column column, Long value) {
    contentValues.put(column.getName(), value);
  }

  public void addInt(Column column, Integer value) {
    contentValues.put(column.getName(), value);
  }

  public void addBoolean(Column column, Boolean value) {
    if (value == null || value.equals(Boolean.FALSE)) {
      contentValues.put(column.getName(), 0);
    } else {
      contentValues.put(column.getName(), 1);
    }
  }

  public <T extends Enum<T>> void addEnum(Column column, Boolean value, EnumMapper<Boolean, T> mapper) {
    T mappedValue = mapper.getMappedValue(value);
    if (contentValues != null) {
      contentValues.put(column.getName(), mappedValue.name());
    }
  }
}
