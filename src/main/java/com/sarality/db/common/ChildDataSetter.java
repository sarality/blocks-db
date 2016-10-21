package com.sarality.db.common;

/**
 * Interface for classes that adds the child data to a parent data object
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface ChildDataSetter<T, A> {

  void setChildData(T parentData, A childData);
}
