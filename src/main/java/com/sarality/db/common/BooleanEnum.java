package com.sarality.db.common;

/**
 * Enumeration for all possible Boolean values.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum BooleanEnum {
  TRUE,
  FALSE;

  public static ValueMapper<Boolean, BooleanEnum> getValueMapper() {
    return new ValueMapper<Boolean, BooleanEnum>()
        .withMapping(Boolean.TRUE, BooleanEnum.TRUE)
        .withMapping(Boolean.FALSE, BooleanEnum.FALSE);
  }

  public static EnumMapper<Boolean, BooleanEnum> getEnumMapper() {
    return new EnumMapper<Boolean, BooleanEnum>(BooleanEnum.class)
        .withMapping(Boolean.TRUE, BooleanEnum.TRUE)
        .withMapping(Boolean.FALSE, BooleanEnum.FALSE);
  }

}
