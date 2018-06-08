package com.sarality.db;

/**
 * enum for list of operations to be supported when a database is upgraded.
 *
 * @author Satya@ (Satya Puniani) on 01/03/17
 */

public enum SchemaUpdateType {
  CREATE_TABLE,
  ADD_COLUMN,
  ADD_INDEX,
  DROP_COLUMN,
  CHANGE_DATA_TYPE,
  ADD_REQUIRED_CONSTRAINT,
  REMOVE_REQUIRED_CONSTRAINT;

}
