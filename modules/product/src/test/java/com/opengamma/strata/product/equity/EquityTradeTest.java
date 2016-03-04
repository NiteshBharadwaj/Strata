/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.equity;

import static com.opengamma.strata.basics.currency.Currency.USD;
import static com.opengamma.strata.collect.TestHelper.assertSerialization;
import static com.opengamma.strata.collect.TestHelper.assertThrows;
import static com.opengamma.strata.collect.TestHelper.coverBeanEquals;
import static com.opengamma.strata.collect.TestHelper.coverImmutableBean;
import static com.opengamma.strata.collect.TestHelper.date;
import static org.testng.Assert.assertEquals;

import java.util.Optional;

import org.testng.annotations.Test;

import com.opengamma.strata.basics.currency.CurrencyAmount;
import com.opengamma.strata.basics.market.ImmutableReferenceData;
import com.opengamma.strata.basics.market.ReferenceData;
import com.opengamma.strata.basics.market.ReferenceDataNotFoundException;
import com.opengamma.strata.product.TradeInfo;
import com.opengamma.strata.product.etd.SecurityId;

/**
 * Test {@link EquityTrade}.
 */
@Test
public class EquityTradeTest {

  private static final Equity SECURITY = EquityTest.sut();
  private static final Equity SECURITY2 = EquityTest.sut2();
  private static final SecurityId SECURITY_ID = SECURITY.getSecurityId();
  private static final SecurityId SECURITY_ID2 = SECURITY2.getSecurityId();
  private static final TradeInfo TRADE_INFO = TradeInfo.builder().tradeDate(date(2016, 6, 30)).build();
  private static final int QUANTITY = 100;
  private static final int QUANTITY2 = 200;
  private static final CurrencyAmount PREMIUM = CurrencyAmount.of(USD, 290);

  //-------------------------------------------------------------------------
  public void test_builder() {
    EquityTrade test = sut();
    assertEquals(test.getTradeInfo(), TRADE_INFO);
    assertEquals(test.getSecurityId(), SECURITY_ID);
    assertEquals(test.getQuantity(), QUANTITY);
    assertEquals(test.getPremium(), Optional.of(PREMIUM));
  }

  public void test_builder2() {
    EquityTrade test = sut2();
    assertEquals(test.getTradeInfo(), TradeInfo.EMPTY);
    assertEquals(test.getSecurityId(), SECURITY_ID2);
    assertEquals(test.getQuantity(), QUANTITY2);
    assertEquals(test.getPremium(), Optional.empty());
  }

  //-------------------------------------------------------------------------
  public void test_resolve() {
    EquityTrade test = sut();
    ResolvedEquityTrade expected = ResolvedEquityTrade.builder()
        .tradeInfo(TRADE_INFO)
        .security(SECURITY)
        .quantity(QUANTITY)
        .premium(PREMIUM)
        .build();
    ReferenceData refData = ImmutableReferenceData.of(SECURITY_ID, SECURITY);
    assertEquals(test.resolve(refData), expected);
    assertEquals(test.resolveSecurity(refData), SECURITY);
    assertEquals(test.resolveSecurity(refData) instanceof Equity, true);
    assertThrows(() -> test.resolve(ReferenceData.empty()), ReferenceDataNotFoundException.class);
    assertThrows(() -> test.resolveSecurity(ReferenceData.empty()), ReferenceDataNotFoundException.class);
  }

  //-------------------------------------------------------------------------
  public void coverage() {
    coverImmutableBean(sut());
    coverBeanEquals(sut(), sut2());
  }

  public void test_serialization() {
    assertSerialization(sut());
  }

  //-------------------------------------------------------------------------
  static EquityTrade sut() {
    return EquityTrade.builder()
        .tradeInfo(TRADE_INFO)
        .securityId(SECURITY_ID)
        .quantity(QUANTITY)
        .premium(PREMIUM)
        .build();
  }

  static EquityTrade sut2() {
    return EquityTrade.builder()
        .securityId(SECURITY_ID2)
        .quantity(QUANTITY2)
        .build();
  }

}
