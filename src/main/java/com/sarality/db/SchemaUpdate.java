package com.sarality.db;

/**
 * Interface for defining updates to table schema from version to version
 *
 * @author Satya@ (Satya Puniani)
 */

interface SchemaUpdate {

  SchemaUpdateType getUpdateType();

  Integer getDbVersion();

  String getTableName();

}
