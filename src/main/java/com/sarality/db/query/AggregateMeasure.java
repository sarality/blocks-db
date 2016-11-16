package com.sarality.db.query;

import com.sarality.db.Column;

/**
 * TODO @Satya what is this for?
 *
 * @author Satya@ () on 16/11/16
 */

public class AggregateMeasure {
  private final AggregateFunction measureFunction;
  private final Column measureColumn;

  public AggregateMeasure(AggregateFunction function, Column column) {
    measureFunction = function;
    measureColumn = column;
  }


  public AggregateFunction getFunction() {
    return measureFunction;
  }

  public Column getColumn() {
    return measureColumn;
  }
}
