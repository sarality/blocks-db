package com.sarality.db.query;

import com.sarality.db.Column;
import com.sarality.db.io.DateTimeColumn;

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
public class SimpleJoinQueryBuilder {

  private final LogicalOperator operator;

  private final Map<String, String> tablePrefixMap = new HashMap<>();

  private final String tableName;
  private final List<String> tableList = new ArrayList<>();
  private final List<Column[]> tableColumnList = new ArrayList<>();
  private final List<JoinType> joinTypeList = new ArrayList<>();

  private final List<Column> columnList = new ArrayList<>();
  private final List<Operator> operatorList = new ArrayList<>();
  private final List<String> argumentValueList = new ArrayList<>();
  private final List<Column> joinColumnList = new ArrayList<>();

  public SimpleJoinQueryBuilder(String tableName, String tablePrefix, Column[] columns) {
    this.operator = LogicalOperator.AND;
    this.tableName = tableName;
    tableColumnList.add(columns);
    tablePrefixMap.put(tableName, tablePrefix);
  }

  public SimpleJoinQueryBuilder join(String tableName, String tablePrefix, Column[] columns, JoinType joinType) {
    tableList.add(tableName);
    tableColumnList.add(columns);
    tablePrefixMap.put(tableName, tablePrefix);
    joinTypeList.add(joinType);
    return this;
  }

  public SimpleJoinQueryBuilder joinFilter(Column column, Column joinColumn) {
    columnList.add(column);
    operatorList.add(Operator.EQUALS);
    argumentValueList.add(null);
    joinColumnList.add(joinColumn);
    return this;
  }

  public SimpleJoinQueryBuilder withFilter(Column column, Long value) {
    return withFilter(column, Operator.EQUALS, String.valueOf(value));
  }

  public SimpleJoinQueryBuilder withFilter(Column column, Operator operator, String value) {
    columnList.add(column);
    operatorList.add(operator);
    argumentValueList.add(value);
    joinColumnList.add(null);
    return this;
  }

  public SimpleJoinQueryBuilder withFilter(Column column, Operator operator, DateTime value) {
    return withFilter(column, operator, new DateTimeColumn(null).getQueryArgValue(column,value));
  }

  public String getTablePrefix(String tableName) {
    return tablePrefixMap.get(tableName);
  }

  private String getSelectClause() {
    StringBuilder builder = new StringBuilder("SELECT ");
    int ctr = 0;
    for (Column[] columns : tableColumnList) {
      if (columns != null) {
        for (Column column : columns) {
          if (ctr > 0) {
            builder.append(",");
          }
          String tableName = column.getTableName();
          String prefix = getTablePrefix(tableName);
          builder.append(" ").append(prefix).append(".").append(column.getName())
              .append(" AS ").append(prefix).append("_").append(column.getName());
          ctr++;
        }
      }
    }
    return builder.toString();
  }

  private String getFromClause() {
    StringBuilder builder = new StringBuilder(" FROM ").append(tableName).append(" ")
        .append(getTablePrefix(tableName));
    int ctr = 0;
    for (String joinTable : tableList) {
      builder.append(joinTypeList.get(ctr).getSQLString()).append(joinTable).append(" ")
          .append(getTablePrefix(joinTable));
      ctr++;
    }
    return builder.toString();
  }

  private String getWhereClause() {
    if (columnList.isEmpty()) {
      return "";
    }
    StringBuilder builder = new StringBuilder();
    int numFilters = columnList.size();
    for (int ctr = 0; ctr < numFilters; ctr++) {
      Column column = columnList.get(ctr);
      Column joinColumn = joinColumnList.get(ctr);
      String tableName = column.getTableName();
      String columnPrefix = getTablePrefix(tableName);

      Operator filterOperator = operatorList.get(ctr);

      if (ctr == 0) {
        builder.append(" WHERE ");
      } else {
        builder.append(" ").append(operator.toString()).append(" ");
      }
      builder.append(columnPrefix).append(".").append(column.getName())
          .append(" ").append(filterOperator.getSQL());
      if (joinColumn != null) {
        String joinTableName = joinColumn.getTableName();
        String joinTablePrefix = getTablePrefix(joinTableName);
        builder.append(" ").append(joinTablePrefix).append(".").append(joinColumn.getName());
      } else if (argumentValueList.get(ctr) != null) {
        builder.append(" ?");
        String collateFunction = filterOperator.getCollateFunctionSQL();
        if (collateFunction != null) {
          builder.append(" ").append(collateFunction);
        }
      }
    }
    return builder.toString();
  }

  private String[] getArguments() {
    if (argumentValueList.isEmpty()) {
      return null;
    }
    List<String> valueList = new ArrayList<>();
    for (String argumentValue : argumentValueList) {
      if (argumentValue != null) {
        valueList.add(argumentValue);
      }
    }
    return valueList.toArray(new String[valueList.size()]);
  }

  public RawQuery build() {
    StringBuilder builder = new StringBuilder().append(getSelectClause()).append("\n");
    builder.append(getFromClause()).append("\n");
    builder.append(getWhereClause());

    return new RawQuery(builder.toString(), getArguments());
  }
}
