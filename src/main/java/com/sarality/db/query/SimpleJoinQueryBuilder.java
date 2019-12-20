package com.sarality.db.query;

import android.text.TextUtils;

import com.sarality.db.Column;
import com.sarality.db.io.DateTimeColumn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
  private final Map<String, String> tableDbNameMap = new HashMap<>();
  private final Map<String, String> dbAliasMap = new HashMap<>();

  private final String tableName;
  private final List<Column[]> tableColumnList = new ArrayList<>();

  private final List<String> joinTableList = new ArrayList<>();
  private final List<JoinType> joinTypeList = new ArrayList<>();
  private final Map<String, List<JoinClause>> joinClauseListMap = new HashMap<>();

  private final List<QueryClause> clauseList = new ArrayList<>();
  private final Map<Column, SortOrder> orderByColumnMap = new LinkedHashMap<>();

  private Long limitSize;
  private Long limitOffset;

  public SimpleJoinQueryBuilder(String tableName, String tablePrefix, Column[] columns) {
    this(null, null, tableName, tablePrefix, columns);
  }

  public SimpleJoinQueryBuilder(String dbName, String dbAlias, String tableName, String tablePrefix,
      Column[] columns) {
    this.operator = LogicalOperator.AND;
    this.tableName = tableName;
    tableColumnList.add(columns);
    tablePrefixMap.put(tableName, tablePrefix);
    if (dbName != null && dbAlias != null) {
      tableDbNameMap.put(tableName, dbName);
      dbAliasMap.put(dbName, dbAlias);
    }
  }

  public SimpleJoinQueryBuilder join(String tableName, String tablePrefix, Column[] columns, JoinType joinType) {
    return join(null, null, tableName, tablePrefix, columns, joinType);
  }

  public SimpleJoinQueryBuilder join(String dbName, String dbAlias, String tableName, String tablePrefix,
      Column[] columns, JoinType joinType) {
    joinTableList.add(tableName);
    tableColumnList.add(columns);
    tablePrefixMap.put(tableName, tablePrefix);
    joinTypeList.add(joinType);
    if (dbName != null && dbAlias != null) {
      tableDbNameMap.put(tableName, dbName);
      dbAliasMap.put(dbName, dbAlias);
    }
    return this;
  }

  public SimpleJoinQueryBuilder joinFilter(Column column, Column joinColumn) {
    String columnTableName = column.getTableName();
    String joinColumnTableName = joinColumn.getTableName();
    Column lhsColumn = column;
    Column rhsColumn = joinColumn;
    if (joinColumnTableName.equals(tableName)) {
      lhsColumn = joinColumn;
      rhsColumn = column;
    } else if (columnTableName.equals(tableName)) {
      lhsColumn = column;
      rhsColumn = joinColumn;
    } else {
      // If neither is the main table, make sure that the columns appear in the order that the tables joins
      // were defined.
      for (String joinTable: joinTableList) {

        if (joinTable.equals(columnTableName)) {
          lhsColumn = column;
          rhsColumn = joinColumn;
          break;
        }
        if (joinTable.equals(joinColumnTableName)) {
          lhsColumn = joinColumn;
          rhsColumn = column;
          break;
        }
      }
    }
    String rhsColumnTableName = rhsColumn.getTableName();

    if (!joinClauseListMap.containsKey(rhsColumnTableName)) {
      joinClauseListMap.put(rhsColumnTableName, new ArrayList<JoinClause>());
    }
    joinClauseListMap.get(rhsColumnTableName).add(new JoinClause(lhsColumn, Operator.EQUALS, rhsColumn));
    return this;
  }

  public SimpleJoinQueryBuilder withFilter(Column column, Long value) {
    return withFilter(column, Operator.EQUALS, String.valueOf(value));
  }

  public SimpleJoinQueryBuilder withFilter(Column column, Operator operator) {
    return withFilter(column, operator, (String) null);
  }

  public SimpleJoinQueryBuilder withFilter(CompoundQueryClause queryClause) {
    clauseList.add(queryClause);
    return this;
  }

  public SimpleJoinQueryBuilder withFilter(SimpleQueryClause queryClause) {
    clauseList.add(queryClause);
    return this;
  }

  public SimpleJoinQueryBuilder withFilter(Column column, Operator operator, String value) {
    return withFilter(new SimpleQueryClause(column, operator, value));
  }

  public SimpleJoinQueryBuilder withFilter(Column column, Operator operator, Long value) {
    return withFilter(column, operator, String.valueOf(value));
  }

  public SimpleJoinQueryBuilder withFilter(Column column, Operator operator, Double value) {
    return withFilter(column, operator, String.valueOf(value));
  }

  public SimpleJoinQueryBuilder withFilter(Column column, Operator operator, DateTime value) {
    return withFilter(column, operator, new DateTimeColumn(null).getQueryArgValue(column,value));
  }

  public SimpleJoinQueryBuilder orderBy(Column orderColumn) {
    return orderBy(orderColumn, SortOrder.ASC);
  }

  public SimpleJoinQueryBuilder orderBy(Column orderColumn, SortOrder sortOrder) {
    orderByColumnMap.put(orderColumn, sortOrder);
    return this;
  }

  public SimpleJoinQueryBuilder limit(Long size) {
    return limit(size, null);
  }

  public SimpleJoinQueryBuilder limit(Long size, Long offset) {
    this.limitSize = size;
    this.limitOffset = offset;
    return this;
  }

  public List<String> getTablesWithDbAlias() {
    if (tableDbNameMap.isEmpty()) {
      return null;
    }
    return new ArrayList<>(tableDbNameMap.keySet());
  }

  public String getDatabaseAlias(String tableName) {
    String dbName = tableDbNameMap.get(tableName);
    return dbAliasMap.get(dbName);
  }

  private String getTablePrefix(String tableName) {
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
    // FROM DB_A.TABLE_A A
    StringBuilder builder = new StringBuilder(" FROM ");
    String tableDbName = tableDbNameMap.get(tableName);
    if (tableDbName != null) {
      String dbAlias = dbAliasMap.get(tableDbName);
      builder.append(dbAlias).append(".");
    }
    builder.append(tableName).append(" ").append(getTablePrefix(tableName));

    // Add all JOIN tables and join clauses
    int ctr = 0;
    for (String joinTable : joinTableList) {
      // JOIN TYPE
      builder.append(joinTypeList.get(ctr).getSQLString());
      // DB_A.TABLE_B B
      String joinTableDbName = tableDbNameMap.get(joinTable);
      if (joinTableDbName != null) {
        String dbAlias = dbAliasMap.get(joinTableDbName);
        builder.append(dbAlias).append(".");
      }
      builder.append(joinTable).append(" ").append(getTablePrefix(joinTable));

      // ON (A.COL_1 = B.COL_2 AND A.COL_2 = B.COL_3)
      List<JoinClause> joinClauseList = joinClauseListMap.get(joinTable);
      if (joinClauseList != null && !joinClauseList.isEmpty()) {
        builder.append(" ON (");
        int i = 0;
        for (JoinClause joinClause : joinClauseList) {
          if (i > 0) {
            builder.append(" AND ");
          }
          // A.COL_1 =
          Column column = joinClause.column;
          Operator filterOperator = joinClause.operator;
          String tableName = column.getTableName();
          String columnPrefix = getTablePrefix(tableName);
          builder.append(columnPrefix).append(".").append(column.getName())
              .append(" ").append(filterOperator.getSQL());

          // B.COL_2
          Column joinColumn = joinClause.joinColumn;
          String joinTableName = joinColumn.getTableName();
          String joinTablePrefix = getTablePrefix(joinTableName);
          builder.append(" ").append(joinTablePrefix).append(".").append(joinColumn.getName());
          i++;
        }
        builder.append(")");
      }
      ctr++;
    }
    return builder.toString();
  }

  private String getWhereClause() {
    if (clauseList.isEmpty()) {
      return "";
    }
    StringBuilder builder = new StringBuilder();
    int numFilters = clauseList.size();
    for (int ctr = 0; ctr < numFilters; ctr++) {
      QueryClause clause = clauseList.get(ctr);

      if (ctr == 0) {
        builder.append(" WHERE ");
      } else {
        builder.append(" ").append(operator.toString()).append(" ");
      }
      builder.append(clause.getSelection(tablePrefixMap));
    }
    return builder.toString();
  }

  private String[] getArguments() {
    if (clauseList.isEmpty()) {
      return null;
    }
    List<String> valueList = new ArrayList<>();
    for (QueryClause clause : clauseList) {
      List<String> argumentValueList = clause.getSelectionArguments();
      if (argumentValueList != null) {
        valueList.addAll(argumentValueList);
      }
    }
    return valueList.toArray(new String[0]);
  }

  private String getOrderByClause() {
    StringBuilder builder = new StringBuilder();
    int ctr = 0;
    for (Column column : orderByColumnMap.keySet()) {
      if (ctr > 0) {
        builder.append(",");
      }

      String tableName = column.getTableName();
      String columnPrefix = getTablePrefix(tableName);
      if (!TextUtils.isEmpty(columnPrefix)) {
        builder = builder.append(columnPrefix).append(".");
      }
      builder.append(column.getName()).append(" ")
          .append(orderByColumnMap.get(column).toString());
      ctr++;
    }
    return builder.toString();
  }

  public RawQuery build() {
    StringBuilder builder = new StringBuilder().append(getSelectClause()).append("\n");
    builder.append(getFromClause()).append("\n");
    builder.append(getWhereClause());

    if (TextUtils.isEmpty(getOrderByClause())) {
      return new RawQuery(builder.toString(), getArguments());
    } else {
      return new RawQuery(builder.toString(), getArguments(), getOrderByClause(), null, null,
          limitSize, limitOffset);
    }
  }

  private class JoinClause {
    private Column column;
    private Operator operator;
    private Column joinColumn;

    private JoinClause(Column column, Operator operator, Column joinColumn) {
      this.column = column;
      this.operator = operator;
      this.joinColumn = joinColumn;
    }
  }
}
