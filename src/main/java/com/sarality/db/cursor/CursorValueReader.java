package com.sarality.db.cursor;

import android.database.Cursor;

import com.sarality.db.Column;
import com.sarality.db.common.EnumMapper;
import com.sarality.db.io.BitMaskColumn;
import com.sarality.db.io.BitPosition;
import com.sarality.db.io.BooleanColumn;
import com.sarality.db.io.ColumnValueReader;
import com.sarality.db.io.DateTimeColumn;
import com.sarality.db.io.DoubleColumn;
import com.sarality.db.io.EnumColumn;
import com.sarality.db.io.MappedEnumColumn;
import com.sarality.db.io.IntegerColumn;
import com.sarality.db.io.LongColumn;
import com.sarality.db.io.StringColumn;

import java.util.Set;

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

  public String getString(Cursor cursor, Column column, String prefix) {
    return new StringColumn(prefix).getValue(cursor, column);
  }

  public Integer getInt(Cursor cursor, Column column) {
    return intColumnValueReader.getValue(cursor, column);
  }

  public Integer getInt(Cursor cursor, Column column, String prefix) {
    return new IntegerColumn(prefix).getValue(cursor, column);
  }

  public Long getLong(Cursor cursor, Column column) {
    return longColumnValueReader.getValue(cursor, column);
  }

  public Long getLong(Cursor cursor, Column column, String prefix) {
    return new LongColumn(prefix).getValue(cursor, column);
  }


  public Double getDouble(Cursor cursor, Column column) {
    return doubleColumnValueReader.getValue(cursor, column);
  }


  public Double getDouble(Cursor cursor, Column column, String prefix) {
    DoubleColumn prefixedDoubleColumnReader = new DoubleColumn(prefix);
    return prefixedDoubleColumnReader.getValue(cursor, column);
  }

  public DateTime getDate(Cursor cursor, Column column) {
    return dateColumnValueReader.getValue(cursor, column);
  }

  public DateTime getDate(Cursor cursor, Column column, String prefix) {
    return new DateTimeColumn(prefix).getValue(cursor, column);
  }

  public <T extends Enum<T>> Boolean getBoolean(Cursor cursor, Column column, EnumMapper<Boolean, T> mapper) {
    return getBoolean(cursor, column, mapper, this.prefix);
  }

  public <T extends Enum<T>> Boolean getBoolean(Cursor cursor, Column column, EnumMapper<Boolean, T> mapper, String
      prefix) {
    ColumnValueReader<Boolean> reader = new BooleanColumn<>(prefix, mapper);
    return reader.getValue(cursor, column);
  }

  public <V, T extends Enum<T>> V getEnum(Cursor cursor, Column column, EnumMapper<V, T> mapper, String prefix) {
    ColumnValueReader<V> reader = new MappedEnumColumn<>(prefix, mapper);
    return reader.getValue(cursor, column);
  }

  public <V, T extends Enum<T>> V getEnum(Cursor cursor, Column column, EnumMapper<V, T> mapper) {
    return getEnum(cursor, column, mapper, this.prefix);
  }


  public <T extends Enum<T>> T getEnum(Cursor cursor, Column column, Class<T> enumClass, String prefix) {
    ColumnValueReader<T> reader = new EnumColumn<>(prefix, enumClass);
    return reader.getValue(cursor, column);
  }

  public <T extends Enum<T>> T getEnum(Cursor cursor, Column column, Class<T> enumClass) {
    return getEnum(cursor, column, enumClass, this.prefix);
  }


  public <T extends Enum<T>> Set<T> getEnumSet(Cursor cursor, Column column, EnumMapper<BitPosition, T> mapper,
      String prefix) {
    ColumnValueReader<Set<T>> reader = new BitMaskColumn<>(prefix, mapper);
    return reader.getValue(cursor, column);
  }

  public <T extends Enum<T>> Set<T> getEnumSet(Cursor cursor, Column column, EnumMapper<BitPosition, T> mapper) {
    return getEnumSet(cursor, column, mapper, this.prefix);
  }


}
