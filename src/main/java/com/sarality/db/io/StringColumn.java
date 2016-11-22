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
public class StringColumn extends BaseColumn implements ColumnValueReader<String>, ColumnValueWriter<String> {

  public StringColumn(String prefix) {
    super(prefix);
  }

  @Override
  public String getValue(Cursor cursor, Column column) {
    int index = cursor.getColumnIndex(getColumnName(column));
    if (cursor.isNull(index)) {
      return null;
    }
    return cursor.getString(index);
  }

  @Override
  public void setValue(ContentValues contentValues, Column column, String value) {
    checkForRequiredColumn(column, value);
    checkForColumnDataType(column, String.class, DataType.TEXT);

    if (value != null) {
      contentValues.put(column.getName(), value);
    } else {
      contentValues.putNull(column.getName());
    }
  }
}
