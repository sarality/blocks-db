package com.sarality.db.cursor;

import android.database.Cursor;

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
 * Utility class to read values from a database cursor.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class CursorValueReader {

  private static final StringColumn STRING_COLUMN = new StringColumn();
  private static final IntegerColumn INTEGER_COLUMN = new IntegerColumn();
  private static final LongColumn LONG_COLUMN = new LongColumn();
  private static final DoubleColumn DOUBLE_COLUMN = new DoubleColumn();
  private static final DateTimeColumn DATE_TIME_COLUMN = new DateTimeColumn();
  private static final BooleanColumn BOOLEAN_COLUMN = new BooleanColumn();
  private static final EnumColumn ENUM_COLUMN = new EnumColumn();

  private final String prefix;

  public CursorValueReader(String prefix) {
    this.prefix = prefix;
  }

  public String getString(Cursor cursor, Column column) {
    return STRING_COLUMN.getValue(cursor, column, prefix);
  }

  public Integer getInt(Cursor cursor, Column column) {
    return INTEGER_COLUMN.getValue(cursor, column, prefix);
  }

  public Long getLong(Cursor cursor, Column column) {
    return LONG_COLUMN.getValue(cursor, column, prefix);
  }

  public Double getDouble(Cursor cursor, Column column) {
    return DOUBLE_COLUMN.getValue(cursor, column, prefix);
  }

  public DateTime getDate(Cursor cursor, Column column) {
    return DATE_TIME_COLUMN.getValue(cursor, column, prefix);
  }

  public <T extends Enum<T>> Boolean getBoolean(Cursor cursor, Column column, EnumMapper<Boolean, T> mapper) {
    return BOOLEAN_COLUMN.getValue(cursor, column, prefix, mapper);
  }

  public <V, T extends Enum<T>> V getEnum(Cursor cursor, Column column, EnumMapper<V, T> mapper) {
    return ENUM_COLUMN.getValue(cursor, column, prefix, mapper);
  }
}
