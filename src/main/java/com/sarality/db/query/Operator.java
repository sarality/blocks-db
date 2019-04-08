package com.sarality.db.query;

/**
 * Enum for Logical Operators used in a Query
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum Operator {
  EQUALS("="),
  EQUALS_IGNORE_CASE("=", "COLLATE NOCASE"),
  IN("IN"),
  NOT_EQUALS("<>"),
  LESS_THAN("<"),
  LESS_THAN_EQUAL_TO("<="),
  GREATER_THAN(">"),
  GREATER_THAN_EQUAL_TO(">="),
  IS_NULL("IS NULL"),
  IS_NOT_NULL("IS NOT NULL");

  private String sql;
  private String collateFunctionSql;

  Operator(String sql) {
    this(sql, null);
  }

  Operator(String sql, String collateFunctionSql) {
    this.sql = sql;
    this.collateFunctionSql = collateFunctionSql;
  }

  public String getSQL() {
    return sql;
  }

  public String getCollateFunctionSQL() {
    return collateFunctionSql;
  }
}
