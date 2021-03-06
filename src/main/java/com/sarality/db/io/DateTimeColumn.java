package com.sarality.db.io;

import android.content.ContentValues;
import android.database.Cursor;

import com.sarality.db.Column;
import com.sarality.db.DataType;

import hirondelle.date4j.DateTime;

/**
 * Reads and Writes data from a DateTime Column
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class DateTimeColumn extends BaseColumn implements ColumnValueReader<DateTime>, ColumnValueWriter<DateTime>,
    ColumnValueFormatter<DateTime> {

  private static final String ISO_8601_DATE_TIME = "YYYY-MM-DD hh:mm:ss";

  public DateTimeColumn(String prefix) {
    super(prefix);
  }

  @Override
  public DateTime getValue(Cursor cursor, Column column) {
    DataType dataType = column.getDataType();
    int index = cursor.getColumnIndex(getColumnName(column));
    if (cursor.isNull(index)) {
      return null;
    }
    if (dataType.equals(DataType.DATE_AS_INT)) {
      int date = cursor.getInt(index);
      int year = date / 10000;
      int month = (date - year * 10000) / 100;
      int day = date % 100;

      return DateTime.forDateOnly(year, month, day);
    } else if (dataType.equals(DataType.TIME_AS_INT)) {
        int time = cursor.getInt(index);
        int hour = time / 10000;
        int min = (time - hour * 10000) / 100;
        int sec = time % 100;

        return DateTime.forTimeOnly(hour, min, sec, 0);
    } else if (dataType.equals(DataType.DATETIME)) {
      String date = cursor.getString(index);
      return new DateTime(date);
    } else {
      throw new IllegalStateException("Cannot extract Date from Column " + column + " with data type " + dataType);
    }
  }

  @Override
  public String getQueryArgValue(Column column, DateTime value) {
    if (column.getDataType().equals(DataType.DATE_AS_INT)) {
      return String.valueOf(getDateAsIntValue(value));
    } else if (column.getDataType().equals(DataType.TIME_AS_INT)) {
      return String.valueOf(getTimeAsIntValue(value));
    } else if (column.getDataType().equals(DataType.DATETIME)) {
      return getDateAsStringValue(value);
    } else {
      throw new IllegalArgumentException("Cannot query Column " + column.getName()
          + " with data type " + column.getDataType() + " using DateTime value");
    }
  }

  @Override
  public void setValue(ContentValues contentValues, Column column, DateTime value) {
    checkForRequiredColumn(column, value);
    if (value == null) {
      contentValues.putNull(column.getName());
      return;
    }

    if (column.getDataType().equals(DataType.DATE_AS_INT)) {
      contentValues.put(column.getName(), getDateAsIntValue(value));
    } else if (column.getDataType().equals(DataType.TIME_AS_INT)) {
        contentValues.put(column.getName(), getTimeAsIntValue(value));
    } else if (column.getDataType().equals(DataType.DATETIME)) {
      contentValues.put(column.getName(), getDateAsStringValue(value));
    } else {
      throw new IllegalArgumentException("Cannot add DateTime value to Column " + column.getName() +
          " with data type " + column.getDataType());
    }
  }

  private int getTimeAsIntValue(DateTime value) {
    int hour = value.getHour() * 10000;
    int min = value.getMinute() * 100;
    return hour + min + value.getSecond();
  }

  private int getDateAsIntValue(DateTime value) {
    int year = value.getYear() * 10000;
    int month = value.getMonth() * 100;
    return year + month + value.getDay();
  }

  private String getDateAsStringValue(DateTime value) {
    return value.format(ISO_8601_DATE_TIME);
  }
}
