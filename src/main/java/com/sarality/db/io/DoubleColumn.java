package com.sarality.db.io;

import android.content.ContentValues;
import android.database.Cursor;

import com.sarality.db.Column;
import com.sarality.db.DataType;

/**
 * Reads and Writes data to/from a Column that stores a Double.
 *
 * @author abhideep@ (Abhideep Singh)
 */

public class DoubleColumn extends BaseColumn
    implements ColumnValueReader<Double>, ColumnValueWriter<Double>, ColumnValueFormatter<Double> {

  public DoubleColumn(String prefix) {
    super(prefix);
  }

  @Override
  public Double getValue(Cursor cursor, Column column) {
    if (!column.getDataType().equals(DataType.DOUBLE)) {
      throw new IllegalStateException("Cannot extract Double from Column " + column + " with data type "
          + column.getDataType());
    }
    int index = cursor.getColumnIndex(getColumnName(column));
    if (cursor.isNull(index)) {
      return null;
    }
    return cursor.getDouble(index);
  }

  public void setValue(ContentValues contentValues, Column column, Double value) {
    checkForRequiredColumn(column, value);
    checkForColumnDataType(column, Double.class, DataType.DOUBLE);

    if (value != null) {
      contentValues.put(column.getName(), value);
    } else {
      contentValues.putNull(column.getName());
    }
  }

  @Override
  public String getQueryArgValue(Column column, Double value) {
    return String.valueOf(value);
  }
}
