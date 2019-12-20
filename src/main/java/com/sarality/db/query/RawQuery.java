package com.sarality.db.query;

import android.text.TextUtils;

/**
 * Class that represents a raw database query and the arguments passed to it.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class RawQuery extends Query {

  RawQuery(String query, String[] queryArguments) {
    // TODO(abhideep): Do a proper inheritance from Query
    super(query, queryArguments, null);
  }

  RawQuery(String query, String[] queryArguments, String orderByClause) {
    // TODO(abhideep): Do a proper inheritance from Query
    super(query, queryArguments, orderByClause);
  }

  RawQuery(String query, String[] queryArguments,
      String orderByClause, String groupByClause, String havingClause, Long limitSize, Long limitOffset) {
    super(new String[] {}, query, queryArguments, orderByClause, groupByClause, havingClause, limitSize,
        limitOffset);
  }

  public final String getQuery() {
    StringBuilder builder = new StringBuilder(getWhereClause());
    String orderByClause = getOrderByClause();
    if (!TextUtils.isEmpty(orderByClause)) {
      builder.append("\n")
      .append("ORDER BY ")
      .append(orderByClause);
    }
    String limit = getLimit();
    if (!TextUtils.isEmpty(limit)) {
      builder.append("\n")
          .append("LIMIT ")
          .append(limit);
    }
    return builder.toString();
  }

  public final String[] getQueryArguments() {
    return getWhereClauseArguments();
  }
}
