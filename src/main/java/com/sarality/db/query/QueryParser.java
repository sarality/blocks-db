package com.sarality.db.query;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given a query, reverses the Query Builder operations and extracts the columnlist, operators and arguments
 *
 * @author satya@ Satya Puniani
 */
public class QueryParser {

  //TODO strongly type these lists to Column and Operator
  private final LogicalOperator operator = LogicalOperator.AND;
  private final List<String> columnList = new ArrayList<>();
  private final List<String> operatorList = new ArrayList<>();
  private final List<String> argumentValueList = new ArrayList<>();

  public QueryParser(Query query){

    //TODO get logical operator

    //get where clause as string array
    String operatorNames = TextUtils.join("|",LogicalOperator.values());
    String[] whereClause = query.getWhereClause().split(operatorNames);

    //for each where clause, get columnname, operator, argument
    for (String predicate : whereClause){
      String[] expression = predicate.split(" ");
      columnList.add(expression[0]);
      operatorList.add(expression[1]);
    }

    argumentValueList.addAll(Arrays.asList(query.getWhereClauseArguments()));
  }


  public LogicalOperator getOperator() {
    return operator;
  }

  public List<String> getColumnList() {
    return columnList;
  }

  public List<String> getOperatorList() {
    return operatorList;
  }

  public List<String> getArgumentValueList() {
    return argumentValueList;
  }
}
