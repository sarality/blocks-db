package com.sarality.db.modifier;

import android.content.ContentValues;

import com.sarality.db.Column;
import com.sarality.db.RecordModifier;
import com.sarality.db.content.ContentValueWriter;

/**
 * Update the flag/column that marks a records as locally modified.
 * <p/>
 * Used for syncing data between local app and the server
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class LocallyModifiedColumnUpdater<T extends Enum<T>> implements RecordModifier {

  private final Column locallyModifiedColumn;
  private final T value;

  /**
   * Constructor
   *
   * @param locallyModifiedColumn Column that stores the Locally Modified Flag
   * @param value Value used to indicate whether the record is being marked as fully synced or one with local changes.
   */
  public LocallyModifiedColumnUpdater(Column locallyModifiedColumn, T value) {
    this.locallyModifiedColumn = locallyModifiedColumn;
    this.value = value;
  }

  @Override
  public void onCreate(ContentValues contentValues) {
    ContentValueWriter writer = new ContentValueWriter(contentValues);
    writer.addEnum(locallyModifiedColumn, value);
  }

  @Override
  public void onUpdate(ContentValues contentValues) {
    ContentValueWriter writer = new ContentValueWriter(contentValues);
    writer.addEnum(locallyModifiedColumn, value);
  }
}
