package com.sarality.db.modifier;

import android.content.ContentValues;
import android.text.TextUtils;

import com.sarality.db.Column;
import com.sarality.db.RecordModifier;

/**
 * When updating a row, ignore any updates to a column when the value is null or empty
 * <p/>
 * Useful for columns that are used for syncing like globalId and arent being used locally
 *
 * @author satya@ (Satya Puniani)
 */
public class EmptyColumnsValueRemover implements RecordModifier {

  private final Column[] columns;

  /**
   * Constructor
   *
   * @param columns Columns that are being checked for empty value
   */
  public EmptyColumnsValueRemover(Column... columns) {
    this.columns = columns;
  }

  @Override
  public void onCreate(ContentValues contentValues) {
    removeEmptyValues(contentValues);

  }

  @Override
  public void onUpdate(ContentValues contentValues) {
    removeEmptyValues(contentValues);
  }

  private void removeEmptyValues(ContentValues contentValues) {
    for (Column column : columns) {
      String value = contentValues.getAsString(column.getName());
      if (TextUtils.isEmpty(value)) {
        contentValues.remove(column.getName());
      }
    }
  }

}
