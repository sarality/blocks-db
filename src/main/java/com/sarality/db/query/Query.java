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

  //Group By Clause
  private final String groupByClause;
  private final String[] selectColumns;

  public Query(String whereClause, String[] whereClauseArguments, String orderByClause) {
    this(new String[] {}, whereClause, whereClauseArguments, orderByClause, null);
  }

  public Query(String[] selectColumns, String whereClause, String[] whereClauseArguments, String orderByClause,
      String groupByClause) {
    this.whereClause = whereClause;
    this.whereClauseArguments = whereClauseArguments;
    this.orderByClause = orderByClause;
    this.selectColumns = selectColumns;
    this.groupByClause = groupByClause;
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

  public String getGroupByClause() {
    return groupByClause;
  }

  public String[] getSelectColumns() {
    return selectColumns;
  }
}
