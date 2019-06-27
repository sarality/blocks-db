package com.sarality.db.query;

import java.util.List;
import java.util.Map;

/**
 * Interface for a Clause of a Query
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface QueryClause {

  String getSelection();

  String getSelection(Map<String, String> tablePrefixMap);

  List<String> getSelectionArguments();
}
