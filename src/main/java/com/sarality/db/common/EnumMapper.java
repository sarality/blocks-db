package com.sarality.db.common;

/**
 * Maps a value to an Enum and vice versa.
 * <p/>
 * Used to convert data object values for Enum Type columns.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class EnumMapper<V, T extends Enum<T>> extends ValueMapper<V, T> {

  private final Class<T> enumClass;

  public EnumMapper(Class<T> enumClass) {
    this.enumClass = enumClass;
  }

  public T valueOf(String value) {
    return Enum.valueOf(enumClass, value);
  }

  @Override
  public EnumMapper<V, T> withMapping(V input, T output) {
    super.withMapping(input, output);
    return this;
  }
}
