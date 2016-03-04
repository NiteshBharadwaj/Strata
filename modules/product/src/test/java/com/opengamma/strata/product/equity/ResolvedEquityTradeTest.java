/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.equity;

import static com.opengamma.strata.basics.currency.Currency.USD;
import static com.opengamma.strata.collect.TestHelper.assertSerialization;
import static com.opengamma.strata.collect.TestHelper.coverBeanEquals;
import static com.opengamma.strata.collect.TestHelper.coverImmutableBean;
import static com.opengamma.strata.collect.TestHelper.date;
import static org.testng.Assert.assertEquals;

import java.util.Optional;

import org.testng.annotations.Test;

import com.opengamma.strata.basics.currency.CurrencyAmount;
import com.opengamma.strata.product.TradeInfo;

/**
 * Test {@link ResolvedEquityTrade}.
 */
@Test
public class ResolvedEquityTradeTest {

  private static final Equity SECURITY = EquityTest.sut();
  private static final Equity SECURITY2 = EquityTest.sut2();
  private static final TradeInfo TRADE_INFO = TradeInfo.builder().tradeDate(date(2016, 6, 30)).build();
  private static final int QUANTITY = 100;
  private static final int QUANTITY2 = 200;
  private static final CurrencyAmount PREMIUM = CurrencyAmount.of(USD, 290);

  //-------------------------------------------------------------------------
  public void test_builder() {
    ResolvedEquityTrade test = sut();
    assertEquals(test.getTradeInfo(), TRADE_INFO);
    assertEquals(test.getSecurity(), SECURITY);
    assertEquals(test.getQuantity(), QUANTITY);
    assertEquals(test.getPremium(), Optional.of(PREMIUM));
    assertEquals(test.getCurrency(), SECURITY.getCurrency());
  }

  public void test_builder2() {
    ResolvedEquityTrade test = sut2();
    assertEquals(test.getTradeInfo(), TradeInfo.EMPTY);
    assertEquals(test.getSecurity(), SECURITY2);
    assertEquals(test.getQuantity(), QUANTITY2);
    assertEquals(test.getPremium(), Optional.empty());
    assertEquals(test.getCurrency(), SECURITY2.getCurrency());
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
  static ResolvedEquityTrade sut() {
    return ResolvedEquityTrade.builder()
        .tradeInfo(TRADE_INFO)
        .security(SECURITY)
        .quantity(QUANTITY)
        .premium(PREMIUM)
        .build();
  }

  static ResolvedEquityTrade sut2() {
    return ResolvedEquityTrade.builder()
        .security(SECURITY2)
        .quantity(QUANTITY2)
        .build();
  }

}
