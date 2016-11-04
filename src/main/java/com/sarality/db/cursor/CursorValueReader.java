package com.sarality.db.cursor;

import android.database.Cursor;

import com.sarality.db.Column;
import com.sarality.db.common.EnumMapper;
import com.sarality.db.io.BooleanColumn;
import com.sarality.db.io.ColumnValueReader;
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

  private final ColumnValueReader<String> stringColumnValueReader;
  private final ColumnValueReader<Integer> intColumnValueReader;
  private final ColumnValueReader<Long> longColumnValueReader;
  private final ColumnValueReader<Double> doubleColumnValueReader;
  private final ColumnValueReader<DateTime> dateColumnValueReader;

  private final String prefix;

  public CursorValueReader(String prefix) {
    this.prefix = prefix;
    this.stringColumnValueReader = new StringColumn(prefix);
    this.intColumnValueReader = new IntegerColumn(prefix);
    this.longColumnValueReader = new LongColumn(prefix);
    this.doubleColumnValueReader = new DoubleColumn(prefix);
    this.dateColumnValueReader = new DateTimeColumn(prefix);
  }

  public String getString(Cursor cursor, Column column) {
    return stringColumnValueReader.getValue(cursor, column);
  }

  public Integer getInt(Cursor cursor, Column column) {
    return intColumnValueReader.getValue(cursor, column);
  }

  public Long getLong(Cursor cursor, Column column) {
    return longColumnValueReader.getValue(cursor, column);
  }

  public Double getDouble(Cursor cursor, Column column) {
    return doubleColumnValueReader.getValue(cursor, column);
  }

  public DateTime getDate(Cursor cursor, Column column) {
    return dateColumnValueReader.getValue(cursor, column);
  }

  public <T extends Enum<T>> Boolean getBoolean(Cursor cursor, Column column, EnumMapper<Boolean, T> mapper) {
    ColumnValueReader<Boolean> reader = new BooleanColumn<>(prefix, mapper);
    return reader.getValue(cursor, column);
  }

  public <V, T extends Enum<T>> V getEnum(Cursor cursor, Column column, EnumMapper<V, T> mapper) {
    ColumnValueReader<V> reader = new EnumColumn<>(prefix, mapper);
    return reader.getValue(cursor, column);
  }
}
