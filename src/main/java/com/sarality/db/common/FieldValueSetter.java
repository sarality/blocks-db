package com.sarality.db.common;

/**
 * Interface for classes that sets the Value of a Field on a data object
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface FieldValueSetter<T, A> {

  void setValue(T data, A fieldValue);
}
