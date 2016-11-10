package com.sarality.db.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A Compound QueryClause composed by performing an AND or an OR on multiple Query Clauses
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class CompoundQueryClause implements QueryClause {
  private final LogicalOperator logicalOperator;
  private final List<QueryClause> clauseList = new ArrayList<>();

  public CompoundQueryClause(LogicalOperator logicalOperator, List<QueryClause> clauseList) {
    this.logicalOperator = logicalOperator;
    this.clauseList.addAll(clauseList);
  }

  public CompoundQueryClause(LogicalOperator logicalOperator, QueryClause... clauses) {
    this.logicalOperator = logicalOperator;
    clauseList.addAll(Arrays.asList(clauses));
  }

  @Override
  public String getSelection() {
    StringBuilder builder = new StringBuilder("(");
    int ctr = 0;
    for (QueryClause clause : clauseList) {
      if (ctr > 0) {
        builder.append(" ").append(logicalOperator.toString()).append(" ");
      }
      builder.append(clause.getSelection());
      ctr++;
    }
    builder.append(")");
    return builder.toString();
  }

  @Override
  public List<String> getSelectionArguments() {
    List<String> selectionArgumentList = new ArrayList<>();
    for (QueryClause clause : clauseList) {
      List<String> clauseArgumentList = clause.getSelectionArguments();
      if (clauseArgumentList != null) {
        selectionArgumentList.addAll(clauseArgumentList);
      }
    }
    if (selectionArgumentList.isEmpty()) {
      return null;
    }
    return selectionArgumentList;
  }
}
