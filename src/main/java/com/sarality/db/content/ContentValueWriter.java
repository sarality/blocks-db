package com.sarality.db.content;

import android.content.ContentValues;

import com.sarality.db.Column;
import com.sarality.db.common.EnumMapper;
import com.sarality.db.io.BooleanColumn;
import com.sarality.db.io.DateTimeColumn;
import com.sarality.db.io.DoubleColumn;
import com.sarality.db.io.EnumColumn;
import com.sarality.db.io.IntegerColumn;
import com.sarality.db.io.LongColumn;
import com.sarality.db.io.StringColumn;

import hirondelle.date4j.DateTime;

/**
 * Utility to add values to a ContentValues object
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ContentValueWriter {

  private static final StringColumn STRING_COLUMN = new StringColumn();
  private static final IntegerColumn INTEGER_COLUMN = new IntegerColumn();
  private static final LongColumn LONG_COLUMN = new LongColumn();
  private static final DoubleColumn DOUBLE_COLUMN = new DoubleColumn();
  private static final DateTimeColumn DATE_TIME_COLUMN = new DateTimeColumn();
  private static final BooleanColumn BOOLEAN_COLUMN = new BooleanColumn();
  private static final EnumColumn ENUM_COLUMN = new EnumColumn();

  private final ContentValues contentValues;

  public ContentValueWriter(ContentValues contentValues) {
    this.contentValues = contentValues;
  }

  public void addString(Column column, String value) {
    STRING_COLUMN.setValue(contentValues, column, value);
  }

  public void addInt(Column column, Integer value) {
    INTEGER_COLUMN.setValue(contentValues, column, value);
  }

  public void addLong(Column column, Long value) {
    LONG_COLUMN.setValue(contentValues, column, value);
  }

  public void addDouble(Column column, Double value) {
    DOUBLE_COLUMN.setValue(contentValues, column, value);
  }

  public void addDate(Column column, DateTime value) {
    DATE_TIME_COLUMN.setValue(contentValues, column, value);
  }

  public <V, T extends Enum<T>> void addEnum(Column column, V value, EnumMapper<V, T> mapper) {
    ENUM_COLUMN.setValue(contentValues, column, value, mapper);
  }

  public <T extends Enum<T>> void addBoolean(Column column, Boolean value, EnumMapper<Boolean, T> mapper) {
    BOOLEAN_COLUMN.setValue(contentValues, column, value, mapper);
  }
}
