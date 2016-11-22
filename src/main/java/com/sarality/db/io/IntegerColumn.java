package com.sarality.db.io;

import android.content.ContentValues;
import android.database.Cursor;

import com.sarality.db.Column;
import com.sarality.db.DataType;

/**
 * Reads and Writes data to/from a Column that stores an Integer.
 *
 * @author abhideep@ (Abhideep Singh)
 */

public class IntegerColumn extends BaseColumn implements ColumnValueReader<Integer>, ColumnValueWriter<Integer> {

  public IntegerColumn(String prefix) {
    super(prefix);
  }

  @Override
  public Integer getValue(Cursor cursor, Column column) {
    if (!column.getDataType().equals(DataType.INTEGER)) {
      throw new IllegalStateException("Cannot extract Integer from Column " + column + " with data type "
          + column.getDataType());
    }
    int index = cursor.getColumnIndex(getColumnName(column));
    if (cursor.isNull(index)) {
      return null;
    }
    return cursor.getInt(index);
  }

  @Override
  public void setValue(ContentValues contentValues, Column column, Integer value) {
    checkForRequiredColumn(column, value);
    checkForColumnDataType(column, Integer.class, DataType.INTEGER);

    contentValues.put(column.getName(), value);
  }
}
