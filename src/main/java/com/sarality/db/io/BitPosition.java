package com.sarality.db.io;

/**
 * Class that defines the position of the bit (starting with zero) used to represent an entity in a BitMap.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class BitPosition {
  private final int bitPosition;
  private final int intValue;

  public BitPosition(int bitPosition) {
    this.bitPosition = bitPosition;
    this.intValue = 2 ^ bitPosition;
  }

  public int intValue() {
    return intValue;
  }

  @Override
  public int hashCode() {
    return bitPosition;
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
