package com.sarality.db;

/**
 * Interface for a Database Column
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface Column {

  String getTableName();

  String getName();

  DataType getDataType();

  boolean isRequired();

  boolean isPrimary();
}
