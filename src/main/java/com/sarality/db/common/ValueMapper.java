package com.sarality.db.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps values used in Data Object to values in used the DB and vice versa.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ValueMapper<I, O> {

  private final Map<I, O> valueMap = new HashMap<>();
  private final Map<O, I> mappedValueMap = new HashMap<>();

  public ValueMapper<I, O> withMapping(I input, O output) {
    valueMap.put(input, output);
    mappedValueMap.put(output, input);
    return this;
  }

  public O getMappedValue(I input) {
    return valueMap.get(input);
  }

  public I getValue(O output) {
    return mappedValueMap.get(output);
  }
}
