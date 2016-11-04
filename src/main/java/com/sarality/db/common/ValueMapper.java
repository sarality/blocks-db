package com.sarality.db.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Maps values used in Data Object to values in used the DB and vice versa.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ValueMapper<V, M> {

  private final Map<M, V> valueLookupMap = new HashMap<>();
  private final Map<V, M> mappedValueLookupMap = new HashMap<>();

  public ValueMapper<V, M> withMapping(V value, M mappedValue) {
    valueLookupMap.put(mappedValue, value);
    mappedValueLookupMap.put(value, mappedValue);
    return this;
  }

  public M getMappedValue(V value) {
    return mappedValueLookupMap.get(value);
  }

  public boolean hasMappedValue(V value) {
    return mappedValueLookupMap.containsKey(value);
  }

  public V getValue(M mappedValue) {
    return valueLookupMap.get(mappedValue);
  }

  public boolean hasValue(M mappedValue) {
    return valueLookupMap.containsKey(mappedValue);
  }

  public Set<V> getValues() {
    return mappedValueLookupMap.keySet();
  }

  public Set<M> getMappedValues() {
    return valueLookupMap.keySet();
  }
}
