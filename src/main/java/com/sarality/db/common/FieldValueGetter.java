package com.sarality.db.common;

/**
 * Interface for classes that get a Field Value
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface FieldValueGetter<T, V> {

  V getValue(T data);
}
