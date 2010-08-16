/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.interestrate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;

import com.opengamma.financial.model.interestrate.curve.InterpolatedYieldAndDiscountCurve;
import com.opengamma.financial.model.interestrate.curve.InterpolatedYieldCurve;
import com.opengamma.math.function.Function1D;
import com.opengamma.math.interpolation.Interpolator1D;
import com.opengamma.math.interpolation.data.Interpolator1DDataBundle;
import com.opengamma.math.interpolation.sensitivity.Interpolator1DNodeSensitivityCalculator;
import com.opengamma.math.matrix.DoubleMatrix1D;
import com.opengamma.math.matrix.DoubleMatrix2D;
import com.opengamma.math.rootfinding.newton.JacobianCalculator;
import com.opengamma.util.tuple.DoublesPair;

/**
 * 
 */
public class MultipleYieldCurveFinderJacobian implements JacobianCalculator {
  private final InterestRateDerivativeVisitor<Map<String, List<DoublesPair>>> _calculator;
  private final MultipleYieldCurveFinderDataBundle _data;

  public MultipleYieldCurveFinderJacobian(final MultipleYieldCurveFinderDataBundle data, final InterestRateDerivativeVisitor<Map<String, List<DoublesPair>>> calculator) {
    Validate.notNull(data, "data");
    Validate.notNull(calculator, "calculator");
    _data = data;
    _calculator = calculator;
  }

  @SuppressWarnings("unchecked")
  @Override
  public DoubleMatrix2D evaluate(final DoubleMatrix1D x, final Function1D<DoubleMatrix1D, DoubleMatrix1D>... functions) {
    Validate.notNull(x);
    final int totalNodes = _data.getTotalNodes();
    if (x.getNumberOfElements() != totalNodes) {
      throw new IllegalArgumentException("vector is wrong length");
    }

    final YieldCurveBundle curves = new YieldCurveBundle();
    int index = 0;
    final List<String> curveNames = _data.getCurveNames();
    int numberOfNodes;
    double[] unknownCurveNodePoints;
    for (final String name : curveNames) {
      final Interpolator1D<? extends Interpolator1DDataBundle> interpolator = _data.getInterpolatorForCurve(name);
      unknownCurveNodePoints = _data.getCurveNodePointsForCurve(name);
      numberOfNodes = unknownCurveNodePoints.length;
      final double[] yields = Arrays.copyOfRange(x.getData(), index, index + numberOfNodes);
      index += numberOfNodes;
      final InterpolatedYieldAndDiscountCurve curve = new InterpolatedYieldCurve(unknownCurveNodePoints, yields, interpolator);
      curves.setCurve(name, curve);
    }
    final YieldCurveBundle knownCurves = _data.getKnownCurves();
    // set any known (i.e. fixed) curves
    if (knownCurves != null) {
      curves.addAll(knownCurves);
    }

    final double[][] res = new double[totalNodes][totalNodes];
    for (int i = 0; i < totalNodes; i++) { // loop over all instruments
      final Map<String, List<DoublesPair>> senseMap = _calculator.getValue(_data.getDerivative(i), curves);
      int offset = 0;
      for (final String name : curveNames) { // loop over all curves (by name)
        if (senseMap.containsKey(name)) {
          final InterpolatedYieldAndDiscountCurve curve = (InterpolatedYieldAndDiscountCurve) curves.getCurve(name);
          final Interpolator1DDataBundle data = curve.getDataBundles().values().iterator().next();
          @SuppressWarnings("rawtypes")
          final Interpolator1DNodeSensitivityCalculator sensitivityCalculator = _data.getSensitivityCalculatorForName(name);
          final List<DoublesPair> senseList = senseMap.get(name);
          final double[][] sensitivity = new double[senseList.size()][];
          int k = 0;
          for (final DoublesPair timeAndDF : senseList) {
            sensitivity[k++] = sensitivityCalculator.calculate(data, timeAndDF.getFirst());
          }
          for (int j = 0; j < sensitivity[0].length; j++) {
            double temp = 0.0;
            k = 0;
            for (final DoublesPair timeAndDF : senseList) {
              temp += timeAndDF.getSecond() * sensitivity[k++][j];
            }
            res[i][j + offset] = temp;
          }
        }
        offset += _data.getCurveNodePointsForCurve(name).length;
      }
    }
    return new DoubleMatrix2D(res);
  }
}
