package com.sarality.db.query;

import com.sarality.db.Column;
import com.sarality.db.io.DateTimeColumn;
import com.sarality.db.io.EnumColumn;
import com.sarality.db.io.LongColumn;

import java.util.ArrayList;
import java.util.List;

import hirondelle.date4j.DateTime;

/**
 * A Simple Query Clause based on a single Column and Operator
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SimpleQueryClause implements QueryClause {

  private final Column column;
  private final Operator operator;
  private final String value;

  public SimpleQueryClause(Column column, Operator operator, String value) {
    this.column = column;
    this.operator = operator;
    this.value = value;
  }

  public SimpleQueryClause(Column column, Operator operator) {
    this.column = column;
    this.operator = operator;
    this.value = null;
  }

  public SimpleQueryClause(Column column, Operator operator, Long value) {
    this(column, operator, new LongColumn(null).getQueryArgValue(column, value));
  }

  public SimpleQueryClause(Column column, Operator operator, DateTime value) {
    this(column, operator, new DateTimeColumn(null).getQueryArgValue(column, value));
  }

  public <E extends Enum<E>> SimpleQueryClause(Column column, Operator operator, E value, Class<E> enumClass) {
    this(column, operator, new EnumColumn<E>(null, enumClass).getQueryArgValue(column, value));
  }

  @Override
  public String getSelection() {
    StringBuilder builder = new StringBuilder("(");
    builder.append(column.getName()).append(" ").append(operator.getSQL());
    if (value != null) {
      builder.append(" ?");
    }
    String collateFunction = operator.getCollateFunctionSQL();
    if (collateFunction != null) {
      builder.append(" ").append(collateFunction);
    }
    builder.append(")");
    return builder.toString();
  }

  @Override
  public List<String> getSelectionArguments() {
    if (value == null) {
      return null;
    }
    List<String> valueList = new ArrayList<>();
    valueList.add(value);
    return valueList;
  }
}
