package com.sarality.db.content;

import android.content.ContentValues;

import com.sarality.db.Column;
import com.sarality.db.common.EnumMapper;
import com.sarality.db.io.BitMaskColumn;
import com.sarality.db.io.BitPosition;
import com.sarality.db.io.BooleanColumn;
import com.sarality.db.io.ColumnValueWriter;
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
 * Utility to add values to a ContentValues object
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ContentValueWriter {

  private final ColumnValueWriter<String> stringColumnValueWriter;
  private final ColumnValueWriter<Integer> intColumnValueWriter;
  private final ColumnValueWriter<Long> longColumnValueWriter;
  private final ColumnValueWriter<Double> doubleColumnValueWriter;
  private final ColumnValueWriter<DateTime> dateColumnValueWriter;

  private final ContentValues contentValues;

  public ContentValueWriter(ContentValues contentValues) {
    this.contentValues = contentValues;
    this.stringColumnValueWriter = new StringColumn(null);
    this.intColumnValueWriter = new IntegerColumn(null);
    this.longColumnValueWriter = new LongColumn(null);
    this.doubleColumnValueWriter = new DoubleColumn(null);
    this.dateColumnValueWriter = new DateTimeColumn(null);
  }

  public void addString(Column column, String value) {
    stringColumnValueWriter.setValue(contentValues, column, value);
  }

  public void addInt(Column column, Integer value) {
    intColumnValueWriter.setValue(contentValues, column, value);
  }

  public void addLong(Column column, Long value) {
    longColumnValueWriter.setValue(contentValues, column, value);
  }

  public void addDouble(Column column, Double value) {
    doubleColumnValueWriter.setValue(contentValues, column, value);
  }

  public void addDate(Column column, DateTime value) {
    dateColumnValueWriter.setValue(contentValues, column, value);
  }

  public <T extends Enum<T>> void addBoolean(Column column, Boolean value, EnumMapper<Boolean, T> mapper) {
    ColumnValueWriter<Boolean> writer = new BooleanColumn<>(null, mapper);
    writer.setValue(contentValues, column, value);
  }

  public <V, T extends Enum<T>> void addEnum(Column column, V value, EnumMapper<V, T> mapper) {
    ColumnValueWriter<V> writer = new MappedEnumColumn<>(null, mapper);
    writer.setValue(contentValues, column, value);
  }

  public <T extends Enum<T>> void addEnum(Column column, T value) {
    ColumnValueWriter<T> writer = new EnumColumn<>(null, null);
    writer.setValue(contentValues, column, value);
  }

  public <T extends Enum<T>> void addEnumSet(Column column, Set<T> values, EnumMapper<BitPosition, T> mapper) {
    ColumnValueWriter<Set<T>> writer = new BitMaskColumn<>(null, mapper);
    writer.setValue(contentValues, column, values);
  }
}
