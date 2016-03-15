/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.equity;

import static com.opengamma.strata.collect.TestHelper.assertSerialization;
import static com.opengamma.strata.collect.TestHelper.coverBeanEquals;
import static com.opengamma.strata.collect.TestHelper.coverImmutableBean;
import static com.opengamma.strata.collect.TestHelper.date;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;
import com.opengamma.strata.basics.market.ReferenceData;
import com.opengamma.strata.product.SecurityInfo;
import com.opengamma.strata.product.TradeInfo;

/**
 * Test {@link EquitySecurity}.
 */
@Test
public class EquitySecurityTest {

  private static final Equity PRODUCT = EquityTest.sut();
  private static final Equity PRODUCT2 = EquityTest.sut2();
  private static final SecurityInfo INFO = SecurityInfo.of(PRODUCT.getSecurityId());
  private static final SecurityInfo INFO2 = SecurityInfo.of(PRODUCT2.getSecurityId());

  //-------------------------------------------------------------------------
  public void test_builder() {
    EquitySecurity test = sut();
    assertEquals(test.getSecurityInfo(), INFO);
    assertEquals(test.getSecurityId(), PRODUCT.getSecurityId());
    assertEquals(test.getCurrency(), PRODUCT.getCurrency());
    assertEquals(test.getUnderlyingIds(), ImmutableSet.of());
  }

  //-------------------------------------------------------------------------
  public void test_createProduct() {
    EquitySecurity test = sut();
    assertEquals(test.createProduct(ReferenceData.empty()), PRODUCT);
    TradeInfo tradeInfo = TradeInfo.builder().tradeDate(date(2016, 6, 30)).build();
    EquityTrade expectedTrade = EquityTrade.builder()
        .tradeInfo(tradeInfo)
        .product(PRODUCT)
        .quantity(100)
        .price(123.50)
        .build();
    assertEquals(test.createTrade(tradeInfo, 100, 123.50, ReferenceData.empty()), expectedTrade);
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
  static EquitySecurity sut() {
    return EquitySecurity.builder()
        .securityInfo(INFO)
        .currency(PRODUCT.getCurrency())
        .build();
  }

  static EquitySecurity sut2() {
    return EquitySecurity.builder()
        .securityInfo(INFO2)
        .currency(PRODUCT2.getCurrency())
        .build();
  }

}
