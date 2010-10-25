/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.cash;

import javax.time.calendar.ZonedDateTime;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.financial.convention.ConventionBundle;
import com.opengamma.financial.convention.ConventionBundleSource;
import com.opengamma.financial.convention.businessday.HolidaySourceCalendarAdapter;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.financial.convention.daycount.DayCountFactory;
import com.opengamma.financial.interestrate.cash.definition.Cash;
import com.opengamma.financial.security.cash.CashSecurity;
import com.opengamma.financial.world.holiday.master.HolidaySource;

/**
 * Converts a CashSecurity to an OG-Analytics Cash object (see {@link Cash})
 */
public class CashSecurityToCashConverter {

  private final HolidaySource _holidaySource;
  private final ConventionBundleSource _conventionSource;

  public CashSecurityToCashConverter(final HolidaySource holidaySource, final ConventionBundleSource conventionSource) {
    _holidaySource = holidaySource;
    _conventionSource = conventionSource;
  }

  public Cash getCash(final CashSecurity security, final String curveName, final double marketRate, final ZonedDateTime now) {
    final ConventionBundle conventions = _conventionSource.getConventionBundle(security.getIdentifiers());
    final Calendar calendar = new HolidaySourceCalendarAdapter(_holidaySource, security.getCurrency());
    final ZonedDateTime startDate = conventions.getBusinessDayConvention().adjustDate(calendar, now.plusDays(conventions.getSettlementDays()));
    final DayCount dayCount = conventions.getDayCount();
    final double tradeTime = dayCount.getDayCountFraction(now, startDate);
    final ZonedDateTime maturityDate = security.getMaturity().toZonedDateTime();
    final DayCount actAct = DayCountFactory.INSTANCE.getDayCount("Actual/Actual");
    final double paymentTime = actAct.getDayCountFraction(now, maturityDate);
    final double yearFraction = dayCount.getDayCountFraction(startDate, maturityDate);
    if (startDate.isAfter(maturityDate)) {
      throw new OpenGammaRuntimeException("startDate "+startDate+" is after maturity date "+maturityDate+" probably caused by market holiday, so no data anyway");
    }
    return new Cash(paymentTime, marketRate, tradeTime, yearFraction, curveName);
  }
}
