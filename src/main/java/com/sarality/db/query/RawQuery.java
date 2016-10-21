package com.sarality.db.query;

/**
 * Class that represents a raw database query and teh arguments passed to it.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class RawQuery extends Query {

  public RawQuery(String query, String[] queryArguments) {
    // TODO(abhideep): Do a proper inheritance from Query
    super(query, queryArguments, null);
  }

  public final String getQuery() {
    return getWhereClause();
  }

  public final String[] getQueryArguments() {
    return getWhereClauseArguments();
  }
}
