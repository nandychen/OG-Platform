/**
 * Copyright (C) 2009 - 2009 by OpenGamma Inc.
 * 
 * Please see distribution for license.
 */
package com.opengamma.math.regression;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * @author emcleod
 */
public class GeneralizedLeastSquaresRegressionTest {
  @Test
  public void test() {
    final double eps = 1e-9;
    final Double a0 = 2.3;
    final Double a1 = -4.5;
    final Double a2 = 0.76;
    final Double a3 = 3.4;
    final int n = 10;
    final Double[][] x = new Double[n][3];
    final Double[] yIntercept = new Double[n];
    final Double[] yNoIntercept = new Double[n];
    final Double[][] w = new Double[n][n];
    Double y, x1, x2, x3;
    for (int i = 0; i < n; i++) {
      x1 = (double) i;
      x2 = x1 * x1;
      x3 = Math.sqrt(x1);
      x[i] = new Double[] { x1, x2, x3 };
      y = x1 * a1 + x2 * a2 + x3 * a3;
      yNoIntercept[i] = y;
      yIntercept[i] = y + a0;
      for (int j = 0; j < n; j++) {
        w[i][j] = 0.;
      }
      w[i][i] = 1.;
    }
    final LeastSquaresRegression regression = new GeneralizedLeastSquaresRegression();
    LeastSquaresRegressionResult result = regression.regress(x, w, yIntercept, true);
    testRegression(4, 3, result, a0, a1, a2, a3, eps);
    result = regression.regress(x, w, yNoIntercept, false);
    testRegression(3, result, a1, a2, a3, eps);
  }

  private void testRegression(final int n, final int k, final LeastSquaresRegressionResult result, final double a0, final double a1, final double a2, final double a3,
      final double eps) {
    final Double[] betas = result.getBetas();
    final Double[] residuals = result.getResiduals();
    assertEquals(betas.length, n);
    assertEquals(residuals.length, k);
    assertEquals(betas[0], a0, eps);
    assertEquals(betas[1], a1, eps);
    assertEquals(betas[2], a2, eps);
    assertEquals(betas[3], a3, eps);
    testStatistics(result, eps);
  }

  private void testRegression(final int n, final LeastSquaresRegressionResult result, final double a1, final double a2, final double a3, final double eps) {
    final Double[] betas = result.getBetas();
    final Double[] residuals = result.getResiduals();
    assertEquals(betas.length, n);
    assertEquals(residuals.length, n);
    assertEquals(betas[0], a1, eps);
    assertEquals(betas[1], a2, eps);
    assertEquals(betas[2], a3, eps);
    testStatistics(result, eps);
  }

  private void testStatistics(final LeastSquaresRegressionResult result, final double eps) {
    for (final double r : result.getResiduals()) {
      assertEquals(r, 0, eps);
    }
    // TODO test statistics
  }
}
