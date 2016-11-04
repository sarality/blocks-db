package com.sarality.db.io;

import android.database.Cursor;

import com.sarality.db.Column;

/**
 * Interface for classes that Read data from a Column
 *
 * @author abhideep@ (Abhideep Singh)
 */

public interface ColumnValueReader<T> {

  T getValue(Cursor cursor, Column column);
}
