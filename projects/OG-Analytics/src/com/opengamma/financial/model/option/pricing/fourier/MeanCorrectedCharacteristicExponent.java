/**
 * Copyright (C) 2009 - 2011 by OpenGamma Inc.
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.model.option.pricing.fourier;

import static com.opengamma.math.ComplexMathUtils.add;
import static com.opengamma.math.ComplexMathUtils.multiply;
import static com.opengamma.math.number.ComplexNumber.MINUS_I;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.Validate;

import com.opengamma.math.function.Function1D;
import com.opengamma.math.number.ComplexNumber;

/**
 * 
 */
public class MeanCorrectedCharacteristicExponent implements CharacteristicExponent {
  private final CharacteristicExponent _base;

  public MeanCorrectedCharacteristicExponent(final CharacteristicExponent base) {
    Validate.notNull(base);
    _base = base;
  }

  @Override
  public Function1D<ComplexNumber, ComplexNumber> getFunction(final double t) {
    final Function1D<ComplexNumber, ComplexNumber> baseFunction = _base.getFunction(t);
    final ComplexNumber temp = baseFunction.evaluate(MINUS_I);
    Validate.isTrue(Math.abs(temp.getImaginary()) < 1e-12, "problem with CharacteristicExponent");
    final ComplexNumber w = new ComplexNumber(0, -temp.getReal());
    return new Function1D<ComplexNumber, ComplexNumber>() {

      @Override
      public ComplexNumber evaluate(final ComplexNumber u) {
        return add(baseFunction.evaluate(u), multiply(w, u));
      }

    };
  }

  @Override
  public double getLargestAlpha() {
    return _base.getLargestAlpha();
  }

  @Override
  public double getSmallestAlpha() {
    return _base.getSmallestAlpha();
  }

  /**
   * Gets the base field.
   * @return the base
   */
  public CharacteristicExponent getBase() {
    return _base;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + _base.hashCode();
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final MeanCorrectedCharacteristicExponent other = (MeanCorrectedCharacteristicExponent) obj;
    return ObjectUtils.equals(_base, other._base);
  }

}
