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

public class IntegerColumn extends BaseColumn {

  public Integer getValue(Cursor cursor, Column column, String prefix) {
    if (!column.getDataType().equals(DataType.INTEGER)) {
      throw new IllegalStateException("Cannot extract Integer from Column " + column + " with data type "
          + column.getDataType());
    }
    return cursor.getInt(cursor.getColumnIndex(getColumnName(column, prefix)));
  }

  public void setValue(ContentValues contentValues, Column column, Integer value) {
    checkForRequiredColumn(column, value);
    checkForColumnDataType(column, Integer.class, DataType.INTEGER);

    contentValues.put(column.getName(), value);
  }
}
