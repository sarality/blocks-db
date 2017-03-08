package com.sarality.db;

/**
 * Interface for defining updates to table schema from version to version
 *
 * @author Satya@ (Satya Puniani) on 01/03/17
 */

interface UpdateOperation {

  UpdateOperationType getUpdateType();

  Column getExistingColumn();

  Column getNewColumn();


}
