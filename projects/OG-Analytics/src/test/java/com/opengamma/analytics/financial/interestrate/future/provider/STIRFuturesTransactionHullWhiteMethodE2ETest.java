/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.interestrate.future.provider;

import static org.testng.AssertJUnit.assertEquals;

import java.util.LinkedHashMap;

import org.testng.annotations.Test;
import org.threeten.bp.ZonedDateTime;

import com.opengamma.analytics.financial.instrument.future.InterestRateFutureSecurityDefinition;
import com.opengamma.analytics.financial.instrument.future.InterestRateFutureTransactionDefinition;
import com.opengamma.analytics.financial.instrument.index.IborIndex;
import com.opengamma.analytics.financial.interestrate.datasets.StandardDataSetsMulticurveEUR;
import com.opengamma.analytics.financial.interestrate.datasets.StandardDataSetsMulticurveFuturesEUR;
import com.opengamma.analytics.financial.interestrate.future.derivative.InterestRateFutureTransaction;
import com.opengamma.analytics.financial.provider.calculator.discounting.MarketQuoteDiscountingCalculator;
import com.opengamma.analytics.financial.provider.calculator.discounting.PresentValueCurveSensitivityDiscountingCalculator;
import com.opengamma.analytics.financial.provider.calculator.discounting.PresentValueDiscountingCalculator;
import com.opengamma.analytics.financial.provider.calculator.generic.MarketQuoteSensitivityBlockCalculator;
import com.opengamma.analytics.financial.provider.calculator.hullwhite.MarketQuoteHullWhiteCalculator;
import com.opengamma.analytics.financial.provider.calculator.hullwhite.PresentValueCurveSensitivityHullWhiteCalculator;
import com.opengamma.analytics.financial.provider.calculator.hullwhite.PresentValueHullWhiteCalculator;
import com.opengamma.analytics.financial.provider.curve.CurveBuildingBlockBundle;
import com.opengamma.analytics.financial.provider.description.interestrate.HullWhiteOneFactorProviderDiscount;
import com.opengamma.analytics.financial.provider.description.interestrate.HullWhiteOneFactorProviderInterface;
import com.opengamma.analytics.financial.provider.description.interestrate.MulticurveProviderDiscount;
import com.opengamma.analytics.financial.provider.description.interestrate.MulticurveProviderInterface;
import com.opengamma.analytics.financial.provider.sensitivity.multicurve.MultipleCurrencyParameterSensitivity;
import com.opengamma.analytics.financial.provider.sensitivity.parameter.ParameterSensitivityParameterCalculator;
import com.opengamma.analytics.financial.util.AssertSensitivityObjects;
import com.opengamma.analytics.math.matrix.DoubleMatrix1D;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.util.money.Currency;
import com.opengamma.util.money.MultipleCurrencyAmount;
import com.opengamma.util.test.TestGroup;
import com.opengamma.util.time.DateUtils;
import com.opengamma.util.tuple.ObjectsPair;
import com.opengamma.util.tuple.Pair;

/**
 * Tests the STIR Futures Hull-White method with curve calibrated on OIS and futures.
 */
@Test(groups = TestGroup.UNIT)
public class STIRFuturesTransactionHullWhiteMethodE2ETest {

  /** Data */
  private static final ZonedDateTime VALUATION_DATE = DateUtils.getUTCDate(2014, 9, 2);
  private static final IborIndex[] INDEX_IBOR_LIST = StandardDataSetsMulticurveEUR.indexIborArrayEUROisE3();
  private static final IborIndex EUREURIBOR3M = INDEX_IBOR_LIST[0];
  private static final Calendar CALENDAR = StandardDataSetsMulticurveEUR.calendarArray()[0];
  private static final Currency EUR = EUREURIBOR3M.getCurrency();
  private static final double[] MQ_OIS_CAL = StandardDataSetsMulticurveFuturesEUR.oisMarketData();
  private static final double[] MQ_3M_CAL = StandardDataSetsMulticurveFuturesEUR.futIrs3MMarketData();
  
  private static final Pair<MulticurveProviderDiscount, CurveBuildingBlockBundle> MULTICURVE_DSC_PAIR = 
      StandardDataSetsMulticurveFuturesEUR.getCurvesUSDOisFutL3Discounting(VALUATION_DATE);
  private static final MulticurveProviderDiscount MULTICURVE_DSC = MULTICURVE_DSC_PAIR.getFirst();
  private static final CurveBuildingBlockBundle BLOCK_DSC = MULTICURVE_DSC_PAIR.getSecond();

  
  private static final Pair<HullWhiteOneFactorProviderDiscount, CurveBuildingBlockBundle> MULTICURVE_HW_PAIR = 
      StandardDataSetsMulticurveFuturesEUR.getCurvesUSDOisFutL3HullWhite(VALUATION_DATE);
  private static final HullWhiteOneFactorProviderDiscount MULTICURVE_HW = MULTICURVE_HW_PAIR.getFirst();
  private static final CurveBuildingBlockBundle BLOCK_HW = MULTICURVE_HW_PAIR.getSecond();
  
  /** Instruments */
  private static final ZonedDateTime LAST_TRADING_DATE = DateUtils.getUTCDate(2014, 12, 15);
  private static final double NOTIONAL = 1000000.0; // 1m
  private static final double FUTURE_FACTOR = 0.25;
  private static final String NAME = "ERZ4";
  private static final InterestRateFutureSecurityDefinition ERZ4_SEC_DEFINITION =
      new InterestRateFutureSecurityDefinition(LAST_TRADING_DATE, EUREURIBOR3M, NOTIONAL, FUTURE_FACTOR, NAME, CALENDAR);
  private static final long QUANTITY = -125;
  private static final ZonedDateTime TRADE_DATE = DateUtils.getUTCDate(2013, 5, 7);
  private static final double TRADE_PRICE = 0.999;
  private static final InterestRateFutureTransactionDefinition ERZ4_TRA_DEFINITION =
      new InterestRateFutureTransactionDefinition(ERZ4_SEC_DEFINITION, QUANTITY, TRADE_DATE, TRADE_PRICE);
  private static final double LAST_MARGIN_PRICE = 0.9988; 
  private static final InterestRateFutureTransaction ERZ4_TRA = 
      ERZ4_TRA_DEFINITION.toDerivative(VALUATION_DATE, LAST_MARGIN_PRICE);
  /** Calculators */
  private static final MarketQuoteDiscountingCalculator MQDC = MarketQuoteDiscountingCalculator.getInstance();
  private static final MarketQuoteHullWhiteCalculator MQHWC = MarketQuoteHullWhiteCalculator.getInstance();
  
  private static final PresentValueDiscountingCalculator PVDC = PresentValueDiscountingCalculator.getInstance();
  private static final PresentValueHullWhiteCalculator PVHWC = PresentValueHullWhiteCalculator.getInstance();
  
  private static final PresentValueCurveSensitivityDiscountingCalculator PVCSDC =
      PresentValueCurveSensitivityDiscountingCalculator.getInstance();
  private static final ParameterSensitivityParameterCalculator<MulticurveProviderInterface> PSDC = 
      new ParameterSensitivityParameterCalculator<>(PVCSDC);
  private static final MarketQuoteSensitivityBlockCalculator<MulticurveProviderInterface> MQSBC = 
      new MarketQuoteSensitivityBlockCalculator<>(PSDC);
      
      private static final PresentValueCurveSensitivityHullWhiteCalculator PVCSHWC =
          PresentValueCurveSensitivityHullWhiteCalculator.getInstance();
      private static final ParameterSensitivityParameterCalculator<HullWhiteOneFactorProviderInterface> PSHWC = 
          new ParameterSensitivityParameterCalculator<>(PVCSHWC);
      private static final MarketQuoteSensitivityBlockCalculator<HullWhiteOneFactorProviderInterface> MQSBHWC = 
          new MarketQuoteSensitivityBlockCalculator<>(PSHWC);
  
  private static final double TOLERANCE_PV = 1.0E-4;
  private static final double TOLERANCE_PRICE = 1.0E-8;
  private static final double TOLERANCE_PV_DELTA = 1.0E-2;
  private static final double BP1 = 1.0E-4;

  @Test
  /**
   * Test market quote with curves calibrated with and without convexity adjustment.
   */
  public void price() {
    double mqExpected = MQ_3M_CAL[2]; // Dec 14 price
    Double mqComputedDsc = ERZ4_TRA.getUnderlyingSecurity().accept(MQDC, MULTICURVE_DSC);
    assertEquals("STIRFuturesTransactionHullWhiteMethodE2ETest: price - discounting", 
        mqExpected, mqComputedDsc, TOLERANCE_PRICE);
    Double mqComputedHw = ERZ4_TRA.getUnderlyingSecurity().accept(MQHWC, MULTICURVE_HW);
    assertEquals("STIRFuturesTransactionHullWhiteMethodE2ETest: price - Hull-White", 
        mqExpected, mqComputedHw, TOLERANCE_PRICE);
  }

  @Test
  /**
   * Test present value with curves calibrated with and without convexity adjustment.
   */
  public void presentValue() {
    double mqExpected = MQ_3M_CAL[2]; // Dec 14 price
    double pvExpected = (mqExpected - LAST_MARGIN_PRICE) * NOTIONAL * FUTURE_FACTOR * QUANTITY;
    MultipleCurrencyAmount pvComputedDsc = ERZ4_TRA.accept(PVDC, MULTICURVE_DSC);
    assertEquals("STIRFuturesTransactionHullWhiteMethodE2ETest: present value - discounting", 
        pvExpected, pvComputedDsc.getAmount(EUR), TOLERANCE_PV);
    MultipleCurrencyAmount pvComputedHw = ERZ4_TRA.accept(PVHWC, MULTICURVE_HW);
    assertEquals("STIRFuturesTransactionHullWhiteMethodE2ETest: present value - Hull-White", 
        pvExpected, pvComputedHw.getAmount(EUR), TOLERANCE_PV);
  }
  
  @SuppressWarnings("unused")
  @Test
  /**
   * Test present value with curves calibrated with and without convexity adjustment.
   */
  public void marketQuoteBucketedPV01() {
    double deltaUnderlying = QUANTITY * FUTURE_FACTOR * NOTIONAL * BP1; // Sensitivity to the underlying
    final double[] deltaDsc = new double[MQ_OIS_CAL.length];
    final double[] deltaFwd = new double[MQ_3M_CAL.length];
    deltaFwd[2] = deltaUnderlying;
    final LinkedHashMap<Pair<String, Currency>, DoubleMatrix1D> sensitivity = new LinkedHashMap<>();
    sensitivity.put(ObjectsPair.of(MULTICURVE_DSC.getName(EUR), EUR), new DoubleMatrix1D(deltaDsc));
    sensitivity.put(ObjectsPair.of(MULTICURVE_DSC.getName(EUREURIBOR3M), EUR), new DoubleMatrix1D(deltaFwd));
    final MultipleCurrencyParameterSensitivity pvpsExpected = new MultipleCurrencyParameterSensitivity(sensitivity);
    final MultipleCurrencyParameterSensitivity pvpsComputedDsc = MQSBC.fromInstrument(ERZ4_TRA, MULTICURVE_DSC, BLOCK_DSC).
        multipliedBy(BP1);
    AssertSensitivityObjects.assertEquals("STIRFuturesTransactionDiscountingMethodE2ETest: bucketed deltas from standard curves", 
        pvpsExpected, pvpsComputedDsc, TOLERANCE_PV_DELTA);
    final MultipleCurrencyParameterSensitivity pvpsComputedHw = MQSBHWC.fromInstrument(ERZ4_TRA, MULTICURVE_HW, BLOCK_HW).
        multipliedBy(BP1);
    AssertSensitivityObjects.assertEquals("STIRFuturesTransactionDiscountingMethodE2ETest: bucketed deltas from standard curves", 
        pvpsExpected, pvpsComputedDsc, TOLERANCE_PV_DELTA);
  }

}
