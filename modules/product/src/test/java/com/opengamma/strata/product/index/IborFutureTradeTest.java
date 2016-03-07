/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.index;

import static com.opengamma.strata.collect.TestHelper.assertSerialization;
import static com.opengamma.strata.collect.TestHelper.coverBeanEquals;
import static com.opengamma.strata.collect.TestHelper.coverImmutableBean;
import static com.opengamma.strata.collect.TestHelper.date;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.opengamma.strata.basics.market.ReferenceData;
import com.opengamma.strata.product.TradeInfo;

/**
 * Test {@link IborFutureTrade}.
 */
@Test
public class IborFutureTradeTest {

  private static final ReferenceData REF_DATA = ReferenceData.standard();
  private static final TradeInfo TRADE_INFO = TradeInfo.builder().tradeDate(date(2015, 3, 18)).build();
  private static final TradeInfo TRADE_INFO2 = TradeInfo.builder().tradeDate(date(2015, 2, 17)).build();
  private static final long QUANTITY = 35;
  private static final double INITIAL_PRICE = 1.015;
  private static final IborFuture PRODUCT = IborFutureTest.sut();
  private static final IborFuture PRODUCT2 = IborFutureTest.sut2();

  //-------------------------------------------------------------------------
  public void test_builder() {
    IborFutureTrade test = sut();
    assertEquals(test.getTradeInfo(), TRADE_INFO);
    assertEquals(test.getProduct(), PRODUCT);
    assertEquals(test.getInitialPrice(), INITIAL_PRICE);
    assertEquals(test.getQuantity(), QUANTITY);
  }

  //-------------------------------------------------------------------------
  public void test_resolve() {
    IborFutureTrade test = sut();
    ResolvedIborFutureTrade resolved = test.resolve(REF_DATA);
    assertEquals(resolved.getTradeInfo(), TRADE_INFO);
    assertEquals(resolved.getProduct(), PRODUCT.resolve(REF_DATA));
    assertEquals(resolved.getQuantity(), QUANTITY);
    assertEquals(resolved.getInitialPrice(), INITIAL_PRICE);
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
  static IborFutureTrade sut() {
    return IborFutureTrade.builder()
        .tradeInfo(TRADE_INFO)
        .product(PRODUCT)
        .initialPrice(INITIAL_PRICE)
        .quantity(QUANTITY)
        .build();
  }

  static IborFutureTrade sut2() {
    return IborFutureTrade.builder()
        .tradeInfo(TRADE_INFO2)
        .product(PRODUCT2)
        .initialPrice(1.1)
        .quantity(100)
        .build();
  }

}
