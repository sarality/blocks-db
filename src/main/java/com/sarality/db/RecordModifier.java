package com.sarality.db;

import android.content.ContentValues;

/**
 * Interface for classes that modify the record before it is updated or created in the system
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface RecordModifier {

  /**
   * Update values for the record being created.
   *
   * @param contentValues Data for the Record that needs to be created.
   */
  void onCreate(ContentValues contentValues);

  /**
   * Update values for the record being updated.
   *
   * @param contentValues Data for the Record that needs to be updated.
   */
  void onUpdate(ContentValues contentValues);
}
