package com.sarality.db.io;

import android.content.ContentValues;
import android.database.Cursor;

import com.sarality.db.Column;
import com.sarality.db.DataType;

/**
 * Reads and Writes data to/from a Column that stores a String.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class StringColumn extends BaseColumn {

  public String getValue(Cursor cursor, Column column, String prefix) {
    return cursor.getString(cursor.getColumnIndex(getColumnName(column, prefix)));
  }

  public void setValue(ContentValues contentValues, Column column, String value) {
    checkForRequiredColumn(column, value);
    checkForColumnDataType(column, String.class, DataType.TEXT);

    contentValues.put(column.getName(), value);
  }
}
