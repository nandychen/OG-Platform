/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.instrument.annuity;

import java.util.ArrayList;
import java.util.List;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Period;
import org.threeten.bp.ZonedDateTime;

import com.opengamma.analytics.financial.instrument.index.IndexON;
import com.opengamma.analytics.financial.instrument.payment.CouponArithmeticAverageONDefinition;
import com.opengamma.analytics.financial.interestrate.annuity.derivative.Annuity;
import com.opengamma.analytics.financial.interestrate.payments.derivative.Coupon;
import com.opengamma.analytics.financial.schedule.ScheduleCalculator;
import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.timeseries.DoubleTimeSeries;
import com.opengamma.util.ArgumentChecker;

/**
 * A wrapper class for an annuity containing overnight arithmetic averaged coupons (i.e. the floating leg for a Fed funds-like
 * rate).
 */
public class AnnuityCouponArithmeticAverageONDefinition extends AnnuityCouponDefinition<CouponArithmeticAverageONDefinition> {

  /**
   * Constructor from a list of overnight arithmetic average coupons.
   * @param payments The coupons.
   * @param calendar The holiday calendar
   */
  public AnnuityCouponArithmeticAverageONDefinition(final CouponArithmeticAverageONDefinition[] payments, final Calendar calendar) {
    super(payments, calendar);
  }

  /**
   * Build a annuity of overnight arithmetic average coupons from financial details.
   * @param settlementDate The annuity settlement or first fixing date, not null.
   * @param endFixingPeriodDate The end date of the overnight arithmetic average coupons accrual period. Also called the maturity date of the annuity even if the
   * actual payment can take place one or two days later. Not null.
   * @param notional The annuity notional, not null
   * @param isPayer The flag indicating if the annuity is paying (true) or receiving (false).
   * @param indexON The overnight index, not null
   * @param paymentLag The payment lag, not null
   * @param indexCalendar The calendar for the overnight index, not null
   * @param businessDayConvention The business day convention, not null
   * @param paymentPeriod The payment period, not null
   * @param isEOM True if the date schedule is EOM.
   * @return The annuity.
   */
  public static AnnuityCouponArithmeticAverageONDefinition from(final ZonedDateTime settlementDate, final ZonedDateTime endFixingPeriodDate, final double notional, final boolean isPayer,
      final IndexON indexON, final int paymentLag, final Calendar indexCalendar, final BusinessDayConvention businessDayConvention, final Period paymentPeriod, final boolean isEOM) {
    ArgumentChecker.notNull(settlementDate, "settlement date");
    ArgumentChecker.notNull(endFixingPeriodDate, "End fixing period date");
    ArgumentChecker.notNull(indexON, "overnight index");
    ArgumentChecker.notNull(indexCalendar, "index calendar");
    ArgumentChecker.notNull(businessDayConvention, "business day convention");
    ArgumentChecker.notNull(paymentPeriod, "payment period");
    final ZonedDateTime[] endFixingPeriodDates = ScheduleCalculator.getAdjustedDateSchedule(settlementDate, endFixingPeriodDate, paymentPeriod, true,
        false, businessDayConvention, indexCalendar, isEOM); //TODO get rid of hard-codings
    final double sign = isPayer ? -1.0 : 1.0;
    final double notionalSigned = sign * notional;
    final CouponArithmeticAverageONDefinition[] coupons = new CouponArithmeticAverageONDefinition[endFixingPeriodDates.length];
    coupons[0] = CouponArithmeticAverageONDefinition.from(indexON, settlementDate, endFixingPeriodDates[0], notionalSigned, paymentLag, indexCalendar);
    for (int loopcpn = 1; loopcpn < endFixingPeriodDates.length; loopcpn++) {
      coupons[loopcpn] = CouponArithmeticAverageONDefinition.from(indexON, endFixingPeriodDates[loopcpn - 1], endFixingPeriodDates[loopcpn], notionalSigned, paymentLag,
          indexCalendar);
    }
    return new AnnuityCouponArithmeticAverageONDefinition(coupons, indexCalendar);
  }

  /**
   * {@inheritDoc}
   * @deprecated Use the method that does not take yield curve names
   */
  @Deprecated
  @Override
  public Annuity<? extends Coupon> toDerivative(final ZonedDateTime valZdt, final DoubleTimeSeries<ZonedDateTime> indexFixingTS, final String... yieldCurveNames) {
    return toDerivative(valZdt, indexFixingTS);
  }

  @Override
  public Annuity<? extends Coupon> toDerivative(final ZonedDateTime valZdt, final DoubleTimeSeries<ZonedDateTime> indexFixingTS) {
    ArgumentChecker.notNull(valZdt, "date");
    ArgumentChecker.notNull(indexFixingTS, "index fixing time series");
    final List<Coupon> resultList = new ArrayList<>();
    final CouponArithmeticAverageONDefinition[] payments = getPayments();
    final ZonedDateTime valZdtInPaymentZone = valZdt.withZoneSameInstant(payments[0].getPaymentDate().getZone());
    final LocalDate valDate = valZdtInPaymentZone.toLocalDate();

    for (int loopcoupon = 0; loopcoupon < payments.length; loopcoupon++) {
      if (!valDate.isAfter(payments[loopcoupon].getPaymentDate().toLocalDate())) {
        resultList.add(payments[loopcoupon].toDerivative(valZdt, indexFixingTS));
      }
    }
    return new Annuity<>(resultList.toArray(new Coupon[resultList.size()]));
  }
}