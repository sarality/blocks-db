package com.sarality.db.io;

/**
 * Class that defines the position of the bit (starting with zero) used to represent an entity in a BitMap.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class BitPosition {
  private final int intValue;

  public BitPosition(int intValue) {
    this.intValue = intValue;
  }

  public int intValue() {
    return intValue;
  }

  @Override
  public int hashCode() {
    return intValue;
  }

  @Override
  public boolean equals(Object rhs) {
    if (rhs instanceof BitPosition) {
      BitPosition position = (BitPosition) rhs;
      return position.intValue() == intValue;
    }
    return false;
  }
}
