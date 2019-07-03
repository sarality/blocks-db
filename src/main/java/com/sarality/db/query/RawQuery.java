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

  public final String getQuery() {
    StringBuilder builder = new StringBuilder(getWhereClause());
    String orderByClause = getOrderByClause();
    if (!TextUtils.isEmpty(orderByClause)) {
      builder.append("\n").append(getOrderByClause());
    }
    return builder.toString();
  }

  public final String[] getQueryArguments() {
    return getWhereClauseArguments();
  }
}
