package com.sarality.db;

/**
 * Enumeration for DataType for a Column.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum DataType {
  INTEGER("INTEGER"),
  TEXT("TEXT"),
  ENUM("TEXT");

  private final String sqlType;

  DataType(String sqlType) {
    this.sqlType = sqlType;
  }

  public String getSqlType() {
    return sqlType;
  }
}
