package com.sarality.db.query;

import com.sarality.db.Column;

import java.util.ArrayList;
import java.util.List;

/**
 * A builder to generate where clause String and the argument values for the where clause.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SimpleQueryBuilder {

  private final LogicalOperator operator;
  private final List<Column> columnList = new ArrayList<>();
  private final List<Operator> operatorList = new ArrayList<>();
  private final List<String> argumentValueList = new ArrayList<>();

  public SimpleQueryBuilder() {
    this(LogicalOperator.AND);
  }

  public SimpleQueryBuilder(LogicalOperator operator) {
    this.operator = operator;
  }

  public SimpleQueryBuilder withFilter(Column column, Long value) {
    return withFilter(column, Operator.EQUALS, String.valueOf(value));
  }

  public SimpleQueryBuilder withFilter(Column column, Operator operator, String value) {
    columnList.add(column);
    operatorList.add(operator);
    argumentValueList.add(value);
    return this;
  }

  private String getWhereClause() {
    if (columnList.isEmpty()) {
      return null;
    }
    StringBuilder builder = new StringBuilder();
    int numFilters = columnList.size();
    for (int ctr = 0; ctr < numFilters; ctr++) {
      Column column = columnList.get(ctr);
      Operator filterOperator = operatorList.get(ctr);

      if (ctr > 0) {
        builder.append(" ").append(operator.toString()).append(" ");
      }
      builder.append(column.getName()).append(" ").append(filterOperator.getSqlString()).append(" ?");
    }
    return builder.toString();
  }

  private String[] getArguments() {
    if (argumentValueList.isEmpty()) {
      return null;
    }
    return argumentValueList.toArray(new String[argumentValueList.size()]);
  }

  public Query build() {
    return new Query(getWhereClause(), getArguments(), null);
  }
}
