package com.sarality.db.io;

import com.sarality.db.Column;
import com.sarality.db.DataType;

/**
 * Base implementation for classes that read data from and write data to a Column.
 *
 * @author abhideep@ (Abhideep Singh)
 */

class BaseColumn {

  private final String prefix;

  public BaseColumn(String prefix) {
    this.prefix = prefix;
  }

  String getColumnName(Column column) {
    if (prefix == null) {
      return column.getName();
    } else {
      return prefix + "_" + column.getName();
    }
  }

  <V> void checkForRequiredColumn(Column column, V value) {
    if (column.isRequired() && value == null) {
      throw new IllegalArgumentException("Cannot add null value to required Column " + column.getName());
    }
  }

  void checkForColumnDataType(Column column, Class<?> valueClass, DataType dataType) {
    if (column.getDataType() != dataType) {
      throw new IllegalArgumentException("Cannot add " + valueClass.getSimpleName() + " value to Column " +
          column.getName() + " with data type " + column.getDataType());
    }
  }

  void checkForColumnDataTypes(Column column, Class<?> valueClass, DataType... dataTypes) {
    boolean hasValidDataType = false;
    for (DataType dataType : dataTypes) {
      if (column.getDataType().equals(dataType)) {
        hasValidDataType = true;
      }
    }
    if (!hasValidDataType) {
      throw new IllegalArgumentException("Cannot add " + valueClass.getSimpleName() + " value to Column " +
          column.getName() + " with data type " + column.getDataType());
    }
  }

}
