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

  private final String havingClause;
  private final Long Limit;

  public Query(String whereClause, String[] whereClauseArguments, String orderByClause) {
    this(new String[] {}, whereClause, whereClauseArguments, orderByClause, null, null, -1L);
  }

  public Query(String[] selectColumns, String whereClause, String[] whereClauseArguments, String orderByClause,
      String groupByClause) {
    this(selectColumns, whereClause, whereClauseArguments, orderByClause, groupByClause, null, -1L);
  }


  public Query(String[] selectColumns, String whereClause, String[] whereClauseArguments, String orderByClause,
      String groupByClause, String havingClause, Long Limit) {
    this.whereClause = whereClause;
    this.whereClauseArguments = whereClauseArguments;
    this.orderByClause = orderByClause;
    this.selectColumns = selectColumns;
    this.groupByClause = groupByClause;
    this.havingClause = havingClause;
    this.Limit = Limit;
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

  public String getHavingClause() {
    return havingClause;
  }

  public Long getLimit() {
    return Limit;
  }
}
