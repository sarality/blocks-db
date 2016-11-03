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
public class DateTimeColumn extends BaseColumn {

  private static final String ISO_8601_DATE_TIME = "YYYY-MM-DD HH:MM:SS";

  public DateTime getValue(Cursor cursor, Column column, String prefix) {
    DataType dataType = column.getDataType();
    if (dataType.equals(DataType.DATE_AS_INT)) {
      int date = cursor.getInt(cursor.getColumnIndex(getColumnName(column, prefix)));
      int day = date % 100;
      int month = (date - day) % 10000;
      int year = date / 10000;

      return DateTime.forDateOnly(year, month, day);
    } else if (dataType.equals(DataType.DATETIME)) {
      String date = cursor.getString(cursor.getColumnIndex(getColumnName(column, prefix)));
      return new DateTime(date);
    } else {
      throw new IllegalStateException("Cannot extract Date from Column " + column + " with data type " + dataType);
    }
  }

  public void setValue(ContentValues contentValues, Column column, DateTime value) {
    checkForRequiredColumn(column, value);

    if (column.getDataType().equals(DataType.DATE_AS_INT)) {
      int year = value.getYear() * 10000;
      int month = value.getMonth() * 100;
      int date = year + month + value.getDay();
      contentValues.put(column.getName(), date);
    } else if (column.getDataType().equals(DataType.DATE_AS_INT)) {
      String date = value.format(ISO_8601_DATE_TIME);
      contentValues.put(column.getName(), date);
    } else {
      throw new IllegalArgumentException("Cannot add DateTime value to Column " + column.getName() +
          " with data type " + column.getDataType());
    }
  }
}
