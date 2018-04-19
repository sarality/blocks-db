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
  private final Long limitSize;
  private final Long limitStartOffset;

  public Query(String whereClause, String[] whereClauseArguments, String orderByClause) {
    this(new String[] {}, whereClause, whereClauseArguments, orderByClause, null, null,
        null, null);
  }

  public Query(String[] selectColumns, String whereClause, String[] whereClauseArguments, String orderByClause,
      String groupByClause) {
    this(selectColumns, whereClause, whereClauseArguments, orderByClause, groupByClause, null,
        null, null);
  }

  public Query(String[] selectColumns, String whereClause, String[] whereClauseArguments, String orderByClause,
      String groupByClause, String havingClause, Long limitSize, Long limitStartOffset) {
    this.whereClause = whereClause;
    this.whereClauseArguments = whereClauseArguments;
    this.orderByClause = orderByClause;
    this.selectColumns = selectColumns;
    this.groupByClause = groupByClause;
    this.havingClause = havingClause;
    this.limitSize = limitSize;
    this.limitStartOffset = limitStartOffset;
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

  public String getLimit() {
    if (limitSize == null) {
      return null;
    } else if (limitStartOffset == null) {
      return String.valueOf(limitSize);
    } else {
      return String.valueOf(limitStartOffset) + ", "  + String.valueOf(limitSize);
    }
  }
}
