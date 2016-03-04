/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.etd;

import static com.opengamma.strata.collect.TestHelper.assertSerialization;
import static com.opengamma.strata.collect.TestHelper.coverBeanEquals;
import static com.opengamma.strata.collect.TestHelper.coverImmutableBean;
import static com.opengamma.strata.collect.TestHelper.date;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.opengamma.strata.product.TradeInfo;

/**
 * Test {@link ResolvedGenericEtdTrade}.
 */
@Test
public class ResolvedGenericEtdTradeTest {

  private static final GenericEtd SECURITY = GenericEtdTest.sut();
  private static final GenericEtd SECURITY2 = GenericEtdTest.sut2();
  private static final TradeInfo TRADE_INFO = TradeInfo.builder().tradeDate(date(2016, 6, 30)).build();
  private static final int QUANTITY = 100;
  private static final int QUANTITY2 = 200;
  private static final double TRADE_PRICE = 2.9;
  private static final double TRADE_PRICE2 = 3.3;

  //-------------------------------------------------------------------------
  public void test_builder() {
    ResolvedGenericEtdTrade test = sut();
    assertEquals(test.getTradeInfo(), TRADE_INFO);
    assertEquals(test.getSecurity(), SECURITY);
    assertEquals(test.getQuantity(), QUANTITY);
    assertEquals(test.getInitialPrice(), TRADE_PRICE);
  }

  public void test_builder2() {
    ResolvedGenericEtdTrade test = sut2();
    assertEquals(test.getTradeInfo(), TradeInfo.EMPTY);
    assertEquals(test.getSecurity(), SECURITY2);
    assertEquals(test.getQuantity(), QUANTITY2);
    assertEquals(test.getInitialPrice(), TRADE_PRICE2);
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
  static ResolvedGenericEtdTrade sut() {
    return ResolvedGenericEtdTrade.builder()
        .tradeInfo(TRADE_INFO)
        .security(SECURITY)
        .quantity(QUANTITY)
        .initialPrice(TRADE_PRICE)
        .build();
  }

  static ResolvedGenericEtdTrade sut2() {
    return ResolvedGenericEtdTrade.builder()
        .security(SECURITY2)
        .quantity(QUANTITY2)
        .initialPrice(TRADE_PRICE2)
        .build();
  }

}
