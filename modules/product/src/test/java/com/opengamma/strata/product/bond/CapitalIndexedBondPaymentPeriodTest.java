/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.bond;

import static com.opengamma.strata.basics.currency.Currency.GBP;
import static com.opengamma.strata.basics.currency.Currency.USD;
import static com.opengamma.strata.basics.index.PriceIndices.GB_RPI;
import static com.opengamma.strata.basics.index.PriceIndices.US_CPI_U;
import static com.opengamma.strata.collect.TestHelper.assertSerialization;
import static com.opengamma.strata.collect.TestHelper.assertThrowsIllegalArg;
import static com.opengamma.strata.collect.TestHelper.coverBeanEquals;
import static com.opengamma.strata.collect.TestHelper.coverImmutableBean;
import static org.testng.Assert.assertEquals;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;
import com.opengamma.strata.basics.currency.CurrencyAmount;
import com.opengamma.strata.basics.index.Index;
import com.opengamma.strata.product.rate.FixedRateObservation;
import com.opengamma.strata.product.rate.InflationBondMonthlyRateObservation;

/**
 * Test {@link CapitalIndexedBondPaymentPeriod}.
 */
@Test
public class CapitalIndexedBondPaymentPeriodTest {

  private static final LocalDate START_UNADJ = LocalDate.of(2008, 1, 13);
  private static final LocalDate END_UNADJ = LocalDate.of(2008, 7, 13);
  private static final LocalDate START = LocalDate.of(2008, 1, 14);
  private static final LocalDate END = LocalDate.of(2008, 7, 14);
  private static final YearMonth REF_END = YearMonth.of(2008, 4);
  private static final double NOTIONAL = 10_000_000d;
  private static final double REAL_COUPON = 0.01d;
  private static final LocalDate DETACHMENT = LocalDate.of(2008, 1, 11);
  private static final double START_INDEX = 198.475;
  private static final InflationBondMonthlyRateObservation OBSERVATION_INTERP =
      InflationBondMonthlyRateObservation.of(US_CPI_U, START_INDEX, REF_END);
  private static final InflationBondMonthlyRateObservation OBSERVATION_MONTH =
      InflationBondMonthlyRateObservation.of(US_CPI_U, START_INDEX, REF_END);

  public void test_builder_full() {
    CapitalIndexedBondPaymentPeriod test = CapitalIndexedBondPaymentPeriod.builder()
        .currency(USD)
        .notional(NOTIONAL)
        .detachmentDate(DETACHMENT)
        .startDate(START)
        .endDate(END)
        .unadjustedStartDate(START_UNADJ)
        .unadjustedEndDate(END_UNADJ)
        .rateObservation(OBSERVATION_INTERP)
        .realCoupon(REAL_COUPON)
        .build();
    assertEquals(test.getCurrency(), USD);
    assertEquals(test.getNotional(), NOTIONAL);
    assertEquals(test.getDetachmentDate(), DETACHMENT);
    assertEquals(test.getStartDate(), START);
    assertEquals(test.getEndDate(), END);
    assertEquals(test.getUnadjustedStartDate(), START_UNADJ);
    assertEquals(test.getUnadjustedEndDate(), END_UNADJ);
    assertEquals(test.getRateObservation(), OBSERVATION_INTERP);
    assertEquals(test.getRealCoupon(), REAL_COUPON);
  }

  public void test_builder_min() {
    CapitalIndexedBondPaymentPeriod test = CapitalIndexedBondPaymentPeriod.builder()
        .currency(USD)
        .notional(NOTIONAL)
        .startDate(START)
        .endDate(END)
        .rateObservation(OBSERVATION_MONTH)
        .realCoupon(REAL_COUPON)
        .build();
    assertEquals(test.getCurrency(), USD);
    assertEquals(test.getNotional(), NOTIONAL);
    assertEquals(test.getDetachmentDate(), END);
    assertEquals(test.getStartDate(), START);
    assertEquals(test.getEndDate(), END);
    assertEquals(test.getUnadjustedStartDate(), START);
    assertEquals(test.getUnadjustedEndDate(), END);
    assertEquals(test.getRateObservation(), OBSERVATION_MONTH);
    assertEquals(test.getRealCoupon(), REAL_COUPON);
  }

  public void test_builder_fail() {
    // not inflation rate observation
    FixedRateObservation fixedRate = FixedRateObservation.of(0.01);
    assertThrowsIllegalArg(() -> CapitalIndexedBondPaymentPeriod.builder()
        .currency(USD)
        .notional(NOTIONAL)
        .detachmentDate(DETACHMENT)
        .startDate(START)
        .endDate(END)
        .unadjustedStartDate(START_UNADJ)
        .unadjustedEndDate(END_UNADJ)
        .rateObservation(fixedRate)
        .realCoupon(REAL_COUPON)
        .build());
    // wrong start date and end date
    assertThrowsIllegalArg(() -> CapitalIndexedBondPaymentPeriod.builder()
        .currency(USD)
        .notional(NOTIONAL)
        .detachmentDate(DETACHMENT)
        .startDate(END.plusDays(1))
        .endDate(END)
        .unadjustedStartDate(START_UNADJ)
        .unadjustedEndDate(END_UNADJ)
        .rateObservation(OBSERVATION_INTERP)
        .realCoupon(REAL_COUPON)
        .build());
    // wrong unadjusted start date and unadjusted end date
    assertThrowsIllegalArg(() -> CapitalIndexedBondPaymentPeriod.builder()
        .currency(USD)
        .notional(NOTIONAL)
        .detachmentDate(DETACHMENT)
        .startDate(START)
        .endDate(END)
        .unadjustedStartDate(START_UNADJ)
        .unadjustedEndDate(START_UNADJ.minusWeeks(1))
        .rateObservation(OBSERVATION_INTERP)
        .realCoupon(REAL_COUPON)
        .build());
  }

  public void test_methods() {
    CapitalIndexedBondPaymentPeriod test = CapitalIndexedBondPaymentPeriod.builder()
        .currency(USD)
        .notional(NOTIONAL)
        .detachmentDate(DETACHMENT)
        .startDate(START)
        .endDate(END)
        .unadjustedStartDate(START_UNADJ)
        .unadjustedEndDate(END_UNADJ)
        .rateObservation(OBSERVATION_INTERP)
        .realCoupon(REAL_COUPON)
        .build();
    assertEquals(test.getNotionalAmount(), CurrencyAmount.of(USD, NOTIONAL));
    assertEquals(test.getPaymentDate(), END);
    assertEquals(test.adjustPaymentDate(TemporalAdjusters.ofDateAdjuster(d -> d.plusDays(2))), test);
    ImmutableSet.Builder<Index> builder = ImmutableSet.builder();
    test.collectIndices(builder);
    ImmutableSet<Index> set = builder.build();
    assertEquals(set.size(), 1);
    assertEquals(set.asList().get(0), US_CPI_U);
    CapitalIndexedBondPaymentPeriod expected = CapitalIndexedBondPaymentPeriod.builder()
        .currency(USD)
        .notional(NOTIONAL)
        .detachmentDate(END)
        .startDate(START)
        .endDate(END)
        .unadjustedStartDate(START_UNADJ)
        .unadjustedEndDate(END_UNADJ)
        .rateObservation(OBSERVATION_INTERP)
        .realCoupon(1d)
        .build();
    assertEquals(test.withUnitCoupon(), expected);
  }

  //-------------------------------------------------------------------------
  public void coverage() {
    CapitalIndexedBondPaymentPeriod test1 = CapitalIndexedBondPaymentPeriod.builder()
        .currency(USD)
        .notional(NOTIONAL)
        .detachmentDate(DETACHMENT)
        .startDate(START)
        .endDate(END)
        .unadjustedStartDate(START_UNADJ)
        .unadjustedEndDate(END_UNADJ)
        .rateObservation(OBSERVATION_INTERP)
        .realCoupon(REAL_COUPON)
        .build();
    coverImmutableBean(test1);
    CapitalIndexedBondPaymentPeriod test2 = CapitalIndexedBondPaymentPeriod.builder()
        .currency(GBP)
        .notional(5.0e6)
        .startDate(LocalDate.of(2008, 1, 15))
        .endDate(LocalDate.of(2008, 7, 15))
        .rateObservation(InflationBondMonthlyRateObservation.of(GB_RPI, 155.32, REF_END))
        .realCoupon(1d)
        .build();
    coverBeanEquals(test1, test2);
  }

  public void test_serialization() {
    CapitalIndexedBondPaymentPeriod test = CapitalIndexedBondPaymentPeriod.builder()
        .currency(USD)
        .notional(NOTIONAL)
        .detachmentDate(DETACHMENT)
        .startDate(START)
        .endDate(END)
        .unadjustedStartDate(START_UNADJ)
        .unadjustedEndDate(END_UNADJ)
        .rateObservation(OBSERVATION_INTERP)
        .realCoupon(REAL_COUPON)
        .build();
    assertSerialization(test);
  }

}
