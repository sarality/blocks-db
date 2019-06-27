package com.sarality.db.query;

import android.text.TextUtils;

import com.sarality.db.Column;
import com.sarality.db.io.DateTimeColumn;
import com.sarality.db.io.EnumColumn;
import com.sarality.db.io.LongColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hirondelle.date4j.DateTime;

/**
 * A Simple Query Clause based on a single Column and Operator
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SimpleQueryClause implements QueryClause {

  private final Column column;
  private final Operator operator;
  private final String argValue;
  private final List<String> argValueList;

  public SimpleQueryClause(Column column, Operator operator, String value) {
    this.column = column;
    this.operator = operator;
    this.argValue = value;
    this.argValueList = null;
  }

  public SimpleQueryClause(Column column, Operator operator) {
    this.column = column;
    this.operator = operator;
    this.argValue = null;
    this.argValueList = null;
  }

  public SimpleQueryClause(Column column, Operator operator, List<String> valueList) {
    this.column = column;
    this.operator = operator;
    this.argValue = null;
    this.argValueList = valueList;
  }

  public static List<String> toStringList(Column column, List<Long> valueList) {
    if (valueList == null) {
      return null;
    }
    if (valueList.isEmpty()) {
      return new ArrayList<>();
    }
    List<String> stringValueList = new ArrayList<>();
    LongColumn longColumn = new LongColumn(null);
    for (Long value : valueList) {
      stringValueList.add(longColumn.getQueryArgValue(column, value));
    }
    return stringValueList;
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
    return getSelection(null);
  }

  @Override
  public String getSelection(Map<String, String> tablePrefixMap) {
    String tableName = column.getTableName();

    StringBuilder builder = new StringBuilder("(");
    if (tablePrefixMap != null) {
      String columnTablePrefix = tablePrefixMap.get(tableName);
      if (!TextUtils.isEmpty(columnTablePrefix)) {
        builder.append(columnTablePrefix).append(".");
      }
    }

    builder.append(column.getName()).append(" ").append(operator.getSQL());
    if (argValue != null) {
      builder.append(" ?");
    }
    if (argValueList != null && !argValueList.isEmpty()) {
      builder.append(" (");
      int ctr = 0;
      for (String argValue : argValueList) {
        if (ctr > 0) {
          builder.append(", ?");
        } else {
          builder.append("?");
        }
        ctr++;
      }
      builder.append(")");
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
    if (argValue == null && argValueList == null) {
      return null;
    }
    List<String> valueList = new ArrayList<>();
    if (argValue != null) {
      valueList.add(argValue);
    } else {
      valueList.addAll(argValueList);
    }
    return valueList;
  }
}
