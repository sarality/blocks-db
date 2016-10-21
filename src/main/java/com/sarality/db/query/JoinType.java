package com.sarality.db.query;

/**
 * Enum for type of Join between the tables
 *
 * @author abhideep@ (Abhideep Singh)
 */

public enum JoinType {
  INNER(" INNER JOIN "),
  LEFT_OUTER(" LEFT OUTER JOIN "),  // Include all from Left even if there are no matching values in the Right
  RIGHT_OUTER(" RIGHT OUT JOIN "); // Include all from Right even if there are no matching values in the Left

  private final String sqlString;

  JoinType(String sqlString) {
    this.sqlString = sqlString;
  }

  public String getSQLString() {
    return sqlString;
  }
}
