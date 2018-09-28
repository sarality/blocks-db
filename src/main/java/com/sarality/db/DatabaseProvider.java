package com.sarality.db;

import java.util.List;

/**
 * Interface for classes that provide a Database
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface DatabaseProvider<D> {

  void init(List<TableDefinition> definitionList);

  D getDatabase();

  void closeDatabase();
}
