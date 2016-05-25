package com.sarality.db.query;

/**
 * Enum for Logical Operators used in a Query
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum Operator {
  EQUALS("="),
  NOT_EQUALS("<>"),
  LESS_THAN("<"),
  LESS_THAN_EQUAL_TO("<="),
  GREATER_THAN(">"),
  GREATER_THAN_EQUAL_TO(">=");

  private String sqlString;

  Operator(String sqlString) {
    this.sqlString = sqlString;
  }

  public String getSqlString() {
    return sqlString;
  }
}
