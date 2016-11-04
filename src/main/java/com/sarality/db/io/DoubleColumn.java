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

public class DoubleColumn extends BaseColumn implements ColumnValueReader<Double>, ColumnValueWriter<Double> {

  public DoubleColumn(String prefix) {
    super(prefix);
  }

  @Override
  public Double getValue(Cursor cursor, Column column) {
    if (!column.getDataType().equals(DataType.INTEGER)) {
      throw new IllegalStateException("Cannot extract Double from Column " + column + " with data type "
          + column.getDataType());
    }
    return cursor.getDouble(cursor.getColumnIndex(getColumnName(column)));
  }

  public void setValue(ContentValues contentValues, Column column, Double value) {
    checkForRequiredColumn(column, value);
    checkForColumnDataType(column, Double.class, DataType.INTEGER);

    contentValues.put(column.getName(), value);
  }
}
