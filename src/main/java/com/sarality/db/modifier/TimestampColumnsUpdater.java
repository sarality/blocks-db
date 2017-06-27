package com.sarality.db.modifier;

import android.content.ContentValues;

import com.sarality.db.Column;
import com.sarality.db.RecordModifier;
import com.sarality.db.content.ContentValueWriter;

import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * Maintains the Creation and Modification Timestamp of the record
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class TimestampColumnsUpdater implements RecordModifier {

  private final Column creationTimestampColumn;
  private final Column modificationTimestampColumn;

  public TimestampColumnsUpdater(Column creationTimestampColumn, Column modificationTimestampColumn) {
    this.creationTimestampColumn = creationTimestampColumn;
    this.modificationTimestampColumn = modificationTimestampColumn;
  }


  @Override
  public void onCreate(ContentValues contentValues) {
    ContentValueWriter writer = new ContentValueWriter(contentValues);
    DateTime now = DateTime.now(TimeZone.getDefault());

    if (requiresValue(contentValues, creationTimestampColumn)) {
      writer.addDate(creationTimestampColumn, now);
    }

    if (requiresValue(contentValues, modificationTimestampColumn)) {
      writer.addDate(modificationTimestampColumn, now);
    }
  }

  @Override
  public void onUpdate(ContentValues contentValues) {
    ContentValueWriter writer = new ContentValueWriter(contentValues);
    DateTime now = DateTime.now(TimeZone.getDefault());

    if (requiresValue(contentValues, modificationTimestampColumn)) {
      writer.addDate(modificationTimestampColumn, now);
    }
  }

  private boolean requiresValue(ContentValues contentValues, Column column) {
    return column != null && contentValues.get(column.getName()) == null;
  }

}
