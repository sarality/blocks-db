package com.sarality.db.query;

import com.sarality.db.Column;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hirondelle.date4j.DateTime;

/**
 * A builder to generate where clause String and the argument values for the where clause.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SimpleQueryBuilder {

  private final LogicalOperator operator;
  private final List<QueryClause> clauseList = new ArrayList<>();
  private final List<AggregateMeasure> measuresList = new ArrayList<>();
  private final List<Column> groupByColumnList = new ArrayList<>();
  private final Map<Column, SortOrder> orderByColumnMap = new HashMap<>();

  private Long limitSize;
  private Long limitOffset;

  public SimpleQueryBuilder() {
    this(LogicalOperator.AND);
  }

  public SimpleQueryBuilder(LogicalOperator operator) {
    this.operator = operator;
  }

  public SimpleQueryBuilder withFilter(Column column, Long value) {
    return withFilter(column, Operator.EQUALS, value);
  }

  public SimpleQueryBuilder withFilter(Column column, String value) {
    return withFilter(column, Operator.EQUALS, value);
  }

  public <E extends Enum<E>> SimpleQueryBuilder withFilter(Column column, E value, Class<E> enumClass) {
    return withFilter(column, Operator.EQUALS, value, enumClass);
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

  public <E extends Enum<E>> SimpleQueryBuilder withFilter(Column column, Operator operator, E value,
      Class<E> enumClass) {
    return withFilter(new SimpleQueryClause(column, operator, value, enumClass));
  }

  public SimpleQueryBuilder withFilter(QueryClause clauseBuilder) {
    clauseList.add(clauseBuilder);
    return this;
  }

  public SimpleQueryBuilder aggregateOn(AggregateFunction measureType, Column measureColumn) {
    measuresList.add(new AggregateMeasure(measureType, measureColumn));
    return this;
  }

  public SimpleQueryBuilder orderBy(Column orderColumn) {
    return orderBy(orderColumn, SortOrder.ASC);
  }

  public SimpleQueryBuilder orderBy(Column orderColumn, SortOrder sortOrder) {
    orderByColumnMap.put(orderColumn, sortOrder);
    return this;
  }

  public SimpleQueryBuilder groupBy(Column dimension) {
    groupByColumnList.add(dimension);
    return this;
  }

  public SimpleQueryBuilder limit(Long size) {
    return limit(size, null);
  }

  public SimpleQueryBuilder limit(Long size, Long offset) {
    this.limitSize = size;
    this.limitOffset = offset;
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

  private String getOrderByClause() {
    StringBuilder builder = new StringBuilder();
    int ctr = 0;
    for (Column column : orderByColumnMap.keySet()) {
      if (ctr > 0) {
        builder.append(",");
      }
      builder.append(column.getName()).append(" ").append(orderByColumnMap.get(column).toString());
      ctr++;
    }
    return builder.toString();
  }

  private String getGroupByClause() {
    StringBuilder builder = new StringBuilder();
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
    List<String> tableColumnList = new ArrayList<>();
    for (Column column : groupByColumnList) {
      tableColumnList.add(column.getName());
    }

    for (AggregateMeasure measure : measuresList) {
      String columnName = measure.getFunction() + "(" + measure.getColumn().getName() + ") AS " + measure.getFunction
          () + "_" + measure.getColumn().getName();
      tableColumnList.add(columnName);
    }
    return tableColumnList.toArray(new String[tableColumnList.size()]);
  }

  public Query build() {
    return new Query(getSelectColumns(), getWhereClause(), getArguments(),
        getOrderByClause(), getGroupByClause(), null, limitSize, limitOffset);
  }
}
