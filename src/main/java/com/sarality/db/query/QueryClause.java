package com.sarality.db.query;

import java.util.List;

/**
 * Interface for a Clause of a Query
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface QueryClause {

  String getSelection();

  List<String> getSelectionArguments();
}
