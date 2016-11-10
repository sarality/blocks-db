package com.sarality.db;

/**
 * Enumeration for DataType for a Column.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum DataType {
  BIT_MASK("INTEGER"),
  DATE_AS_INT("INTEGER"),
  DATETIME("TEXT"),
  ENUM("TEXT"),
  INTEGER("INTEGER"),
  DOUBLE("REAL"),
  TEXT("TEXT");

  private final String sqlType;

  DataType(String sqlType) {
    this.sqlType = sqlType;
  }

  public String getSqlType() {
    return sqlType;
  }
}
