package com.sarality.db.query;

/**
 * Enum for type of Join between the tables
 *
 * @author abhideep@ (Abhideep Singh)
 */

public enum JoinType {
  INNER,
  LEFT_OUTER,  // Include all from Left even if there are no matching values in the Right
  RIGHT_OUTER; // Include all from Right even if there are no matching values in the Left
}
