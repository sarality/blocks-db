package com.sarality.db.modifier;

import android.content.ContentValues;
import android.text.TextUtils;

import com.sarality.db.Column;
import com.sarality.db.RecordModifier;
import com.sarality.db.content.ContentValueWriter;
import com.sarality.db.io.ColumnValueReader;

/**
 * When updating a row, ignore any updates to a column when the value is null or empty
 * <p/>
 * Useful for columns that are used for syncing like globalId and arent being used locally
 *
 * @author satya@ (Satya Puniani)
 */
public class IgnoreEmptyValueColumnUpdater implements RecordModifier {

  private final Column column;

  /**
   * Constructor
   *
   * @param column Column that is being checked for empty value
   */
  public IgnoreEmptyValueColumnUpdater(Column column) {
    this.column = column;
  }

  @Override
  public void onCreate(ContentValues contentValues) {
    String value = contentValues.getAsString(column.getName());
    if (TextUtils.isEmpty(value)) {
      contentValues.remove(column.getName());
    }

  }

  @Override
  public void onUpdate(ContentValues contentValues) {
    String value = contentValues.getAsString(column.getName());
    if (TextUtils.isEmpty(value)) {
      contentValues.remove(column.getName());
    }
  }
}
