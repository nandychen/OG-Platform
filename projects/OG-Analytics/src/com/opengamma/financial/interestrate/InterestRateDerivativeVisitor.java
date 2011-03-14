/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.interestrate;

import com.opengamma.financial.interestrate.annuity.definition.AnnuityCouponFixed;
import com.opengamma.financial.interestrate.annuity.definition.AnnuityCouponIbor;
import com.opengamma.financial.interestrate.annuity.definition.GenericAnnuity;
import com.opengamma.financial.interestrate.bond.definition.Bond;
import com.opengamma.financial.interestrate.bond.definition.BondForward;
import com.opengamma.financial.interestrate.cash.definition.Cash;
import com.opengamma.financial.interestrate.fra.definition.ForwardRateAgreement;
import com.opengamma.financial.interestrate.future.definition.BondFuture;
import com.opengamma.financial.interestrate.future.definition.InterestRateFuture;
import com.opengamma.financial.interestrate.payments.ContinuouslyMonitoredAverageRatePayment;
import com.opengamma.financial.interestrate.payments.CouponCMS;
import com.opengamma.financial.interestrate.payments.CouponFixed;
import com.opengamma.financial.interestrate.payments.CouponIbor;
import com.opengamma.financial.interestrate.payments.Payment;
import com.opengamma.financial.interestrate.payments.PaymentFixed;
import com.opengamma.financial.interestrate.swap.definition.FixedCouponSwap;
import com.opengamma.financial.interestrate.swap.definition.FixedFloatSwap;
import com.opengamma.financial.interestrate.swap.definition.FloatingRateNote;
import com.opengamma.financial.interestrate.swap.definition.Swap;
import com.opengamma.financial.interestrate.swap.definition.TenorSwap;

/**
 * 
 * @param <S> The type of the data
 * @param <T> The return type of the calculation
 */
public interface InterestRateDerivativeVisitor<S, T> {

  T visit(InterestRateDerivative derivative, S data);

  T visitBondForward(BondForward bondForward, S data);

  T visitBondFuture(BondFuture bondFuture, S data);

  T visitGenericAnnuity(GenericAnnuity<? extends Payment> genericAnnuity, S data);

  T visitFixedCouponAnnuity(AnnuityCouponFixed fixedCouponAnnuity, S data);

  T visitForwardLiborAnnuity(AnnuityCouponIbor forwardLiborAnnuity, S data);

  T visitSwap(Swap<?, ?> swap, S data);

  T visitFixedCouponSwap(FixedCouponSwap<?> swap, S data);

  T visitFixedFloatSwap(FixedFloatSwap swap, S data);

  T visitFloatingRateNote(FloatingRateNote frn, S data);

  T visitTenorSwap(TenorSwap<? extends Payment> tenorSwap, S data);

  T visitBond(Bond bond, S data);

  T visitCash(Cash cash, S data);

  T visitInterestRateFuture(InterestRateFuture future, S data);

  T visitContinuouslyMonitoredAverageRatePayment(ContinuouslyMonitoredAverageRatePayment payment, S data);

  T visitFixedPayment(PaymentFixed payment, S data);

  T visitFixedCouponPayment(CouponFixed payment, S data);

  T visitCouponIbor(CouponIbor payment, S data);

  T visitCouponCMS(CouponCMS payment, S data);

  T visitForwardRateAgreement(ForwardRateAgreement fra, S data);

  T visit(InterestRateDerivative derivative);

  T visitBondForward(BondForward bondForward);

  T visitBondFuture(BondFuture bondFuture);

  T visitGenericAnnuity(GenericAnnuity<? extends Payment> genericAnnuity);

  T visitFixedCouponAnnuity(AnnuityCouponFixed fixedCouponAnnuity);

  T visitForwardLiborAnnuity(AnnuityCouponIbor forwardLiborAnnuity);

  T visitSwap(Swap<?, ?> swap);

  T visitFixedCouponSwap(FixedCouponSwap<?> swap);

  T visitFixedFloatSwap(FixedFloatSwap swap);

  T visitFloatingRateNote(FloatingRateNote frn);

  T visitTenorSwap(TenorSwap<? extends Payment> tenorSwap);

  T visitBond(Bond bond);

  T visitCash(Cash cash);

  T visitInterestRateFuture(InterestRateFuture future);

  T visitContinuouslyMonitoredAverageRatePayment(ContinuouslyMonitoredAverageRatePayment payment);

  T visitFixedPayment(PaymentFixed payment);

  T visitFixedCouponPayment(CouponFixed payment);

  T visitCouponIbor(CouponIbor payment);

  T visitCouponCMS(CouponCMS payment);

  T visitForwardRateAgreement(ForwardRateAgreement fra);

}
