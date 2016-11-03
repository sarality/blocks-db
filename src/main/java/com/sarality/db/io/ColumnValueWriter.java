package com.sarality.db.io;

import android.content.ContentValues;

import com.sarality.db.Column;

/**
 * Interfaces that write data for a Column.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface ColumnValueWriter<T> {

  void setValue(ContentValues contentValues, Column column, T value);
}
