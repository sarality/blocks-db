package com.sarality.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * When used - indicates that the table has been created in this version of the database
 *
 * @author Satya@ (Satya Puniani) on 01/03/17
 */

public class CreateTableUpdateOperation implements UpdateOperation {

  public CreateTableUpdateOperation() {
  }

  @Override
  public UpdateOperationType getUpdateType() {
    return UpdateOperationType.CREATE_TABLE;
  }

  @Override
  public Column getExistingColumn() {
    return null;
  }

  @Override
  public Column getNewColumn() {
    return null;
  }
}