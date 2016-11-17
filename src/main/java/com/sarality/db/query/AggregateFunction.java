package com.sarality.db.query;

/**
 * Enum for Aggregate Functions in a Query
 *
 * @author satya@ (Satya Puniani)
 */
public enum AggregateFunction {
  AVERAGE("AVG"),
  COUNT("COUNT"),
  MAX("MAX"),
  MIN("MIN"),
  SUM("SUM"),
  TOTAL("TOTAL");

  private String sqlString;

  AggregateFunction(String sqlString) {
    this.sqlString = sqlString;
  }

  public String getSqlString() {
    return sqlString;
  }
}
