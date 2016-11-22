package com.sarality.db.io;

import android.content.ContentValues;
import android.database.Cursor;

import com.sarality.db.Column;
import com.sarality.db.DataType;
import com.sarality.db.common.EnumMapper;

import java.util.HashSet;
import java.util.Set;

/**
 * Reads and Writes data to/from a Column that stores an BitMask.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class BitMaskColumn<T extends Enum<T>> extends BaseColumn implements ColumnValueReader<Set<T>>,
    ColumnValueWriter<Set<T>> {

  private final EnumMapper<BitPosition, T> mapper;

  public BitMaskColumn(String prefix, EnumMapper<BitPosition, T> mapper) {
    super(prefix);
    this.mapper = mapper;
  }

  @Override
  public Set<T> getValue(Cursor cursor, Column column) {
    if (!column.getDataType().equals(DataType.BIT_MASK)) {
      throw new IllegalStateException("Cannot extract Enum Set from Column " + column + " with data type "
          + column.getDataType() + " using this method");
    }
    if (mapper == null) {
      throw new IllegalArgumentException("Cannot extract Enum Set value from Column " + column
          + " without a way to map Enum value to Integer values");
    }
    int index = cursor.getColumnIndex(getColumnName(column));
    if (cursor.isNull(index)) {
      return null;
    }
    int dbValue = cursor.getInt(index);
    Set<T> valueSet = new HashSet<>();
    for (BitPosition bitPosition : mapper.getValues()) {
      int value = bitPosition.intValue();
      if ((dbValue & value) == value) {
        valueSet.add(mapper.getMappedValue(bitPosition));
      }
    }
    return valueSet;
  }

  @Override
  public void setValue(ContentValues contentValues, Column column, Set<T> values) {
    checkForRequiredColumn(column, values);
    if (values == null) {
      contentValues.put(column.getName(), (String) null);
      return;
    }
    int dbValue = 0;
    for (T value : values) {
      BitPosition bitPosition = mapper.getValue(value);
      dbValue = dbValue | bitPosition.intValue();
    }
    contentValues.put(column.getName(), dbValue);
  }
}
