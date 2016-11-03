package com.sarality.db.io;

import android.content.ContentValues;
import android.database.Cursor;

import com.sarality.db.Column;
import com.sarality.db.DataType;

/**
 * Reads and Writes data to/from a Column that stores a Long value.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class LongColumn extends BaseColumn {

  public Long getValue(Cursor cursor, Column column, String prefix) {
    if (!column.getDataType().equals(DataType.INTEGER)) {
      throw new IllegalStateException("Cannot extract Long from Column " + column + " with data type "
          + column.getDataType());
    }
    return cursor.getLong(cursor.getColumnIndex(getColumnName(column, prefix)));
  }

  public void setValue(ContentValues contentValues, Column column, Long value) {
    checkForRequiredColumn(column, value);
    checkForColumnDataType(column, Long.class, DataType.INTEGER);

    contentValues.put(column.getName(), value);
  }
}
