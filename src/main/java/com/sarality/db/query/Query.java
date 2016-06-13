package com.sarality.db.query;

/**
 * Class that represents a database query.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class Query {

  // Where clause for the query
  private final String whereClause;
  // Values to be used for parameters of where clause.
  private final String[] whereClauseArguments;

  // Order by clause for the query
  private final String orderByClause;

  public Query(String whereClause, String[] whereClauseArguments, String orderByClause) {
    this.whereClause = whereClause;
    this.whereClauseArguments = whereClauseArguments;
    this.orderByClause = orderByClause;
  }

  public final String getWhereClause() {
    return whereClause;
  }

  public final String[] getWhereClauseArguments() {
    return whereClauseArguments;
  }

  public final String getOrderByClause() {
    return orderByClause;
  }
}
