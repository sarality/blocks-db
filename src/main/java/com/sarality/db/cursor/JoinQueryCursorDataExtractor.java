package com.sarality.db.cursor;

import android.database.Cursor;

import com.sarality.db.common.FieldValueGetter;
import com.sarality.db.common.FieldValueSetter;
import com.sarality.db.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * A Data Extractor that processors the cursor retured by a Join Query
 *
 * @author abhideep@ (Abhideep Singh)
 */

public class JoinQueryCursorDataExtractor<T> implements CursorDataExtractor<T> {

  private final CursorDataExtractor<T> cursorDataExtractor;
  private final List<CursorDataExtractor<?>> extractorList = new ArrayList<>();
  private final List<FieldValueSetter<T, ?>> childDataSetterList = new ArrayList<>();
  private final List<FieldValueGetter<T, ?>> fieldValueCheckerList = new ArrayList<>();

  public JoinQueryCursorDataExtractor(CursorDataExtractor<T> cursorDataExtractor) {
    this.cursorDataExtractor = cursorDataExtractor;
  }

  public <A> JoinQueryCursorDataExtractor<T> withExtractor(CursorDataExtractor<A> extractor,
      FieldValueSetter<T, A> setter) {
    return withExtractor(extractor, setter, null);
  }

  public <A> JoinQueryCursorDataExtractor<T> withExtractor(CursorDataExtractor<A> extractor,
      FieldValueSetter<T, A> setter, FieldValueGetter<T, ?> checker) {
    extractorList.add(extractor);
    childDataSetterList.add(setter);
    fieldValueCheckerList.add(checker);
    return this;
  }

  @Override
  public String getColumnPrefix() {
    return null;
  }

  @Override
  public T extract(Cursor cursor, Query query) {
    T value = cursorDataExtractor.extract(cursor, query);
    for (int ctr = 0; ctr < extractorList.size(); ctr++) {
      FieldValueGetter<T, ?> checker = fieldValueCheckerList.get(ctr);
      if (checker == null || checker.getValue(value) != null) {
        processChild(cursor, query, value, ctr);
      }
    }
    return value;
  }

  @SuppressWarnings("unchecked")
  private <A> void processChild(Cursor cursor, Query query, T value, int ctr) {
    CursorDataExtractor<A> extractor = (CursorDataExtractor<A>) extractorList.get(ctr);
    FieldValueSetter<T, A> setter = (FieldValueSetter<T, A>) childDataSetterList.get(ctr);
    setter.setValue(value, extractor.extract(cursor, query));

  }
}
