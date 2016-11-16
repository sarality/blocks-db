package com.sarality.db.query;

import com.sarality.db.Column;

import java.util.ArrayList;
import java.util.List;

import hirondelle.date4j.DateTime;

/**
 * A builder to generate where clause String and the argument values for the where clause.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SimpleQueryBuilder {

  private final LogicalOperator operator;
  private final List<QueryClause> clauseList = new ArrayList<>();
  private final List<String> tableColumnList = new ArrayList<>();
  private final List<Column> groupByColumnList = new ArrayList<>();

  public SimpleQueryBuilder() {
    this(LogicalOperator.AND);
  }

  public SimpleQueryBuilder(LogicalOperator operator) {
    this.operator = operator;
  }

  public SimpleQueryBuilder withFilter(Column column, Long value) {
    return withFilter(column, Operator.EQUALS, value);
  }

  public SimpleQueryBuilder withFilter(Column column, Operator operator, String value) {
    return withFilter(new SimpleQueryClause(column, operator, value));
  }

  public SimpleQueryBuilder withFilter(Column column, Operator operator, Long value) {
    return withFilter(new SimpleQueryClause(column, operator, value));
  }

  public SimpleQueryBuilder withFilter(Column column, Operator operator, DateTime value) {
    return withFilter(new SimpleQueryClause(column, operator, value));
  }

  public SimpleQueryBuilder withFilter(QueryClause clauseBuilder) {
    clauseList.add(clauseBuilder);
    return this;
  }

  //TODO (Satya) make the measureTypes an enum
  public SimpleQueryBuilder withAggregate(String measureType, Column measureColumn) {
    String columnName = measureType + "(" + measureColumn.getName() + ") AS " + measureType + "_" + measureColumn
        .getName();
    tableColumnList.add(columnName);
    return this;
  }

  public SimpleQueryBuilder withGroupBy(Column dimension) {
    tableColumnList.add(dimension.getName());
    groupByColumnList.add(dimension);
    return this;
  }

  private String getWhereClause() {
    QueryClause clause = null;
    if (clauseList.size() == 1) {
      clause = clauseList.get(0);
    } else if (clauseList.size() > 1) {
      clause = new CompoundQueryClause(operator, clauseList);
    }
    if (clause == null) {
      return null;
    }
    return clause.getSelection();
  }

  private String[] getArguments() {
    QueryClause clause = new CompoundQueryClause(operator, clauseList);
    List<String> argumentList = clause.getSelectionArguments();
    if (argumentList == null || argumentList.isEmpty()) {
      return null;
    }
    return argumentList.toArray(new String[argumentList.size()]);
  }

  private String getGroupByClause() {
    StringBuilder builder = new StringBuilder("");
    int ctr = 0;
    for (Column column : groupByColumnList) {
      if (ctr > 0) {
        builder.append(",");
      }
      builder.append(column.getName());
      ctr++;
    }
    return builder.toString();
  }

  private String[] getSelectColumns() {
    return tableColumnList.toArray(new String[tableColumnList.size()]);
  }

  public Query build() {
    return new Query(getSelectColumns(), getWhereClause(), getArguments(),
        null, getGroupByClause());
  }
}
