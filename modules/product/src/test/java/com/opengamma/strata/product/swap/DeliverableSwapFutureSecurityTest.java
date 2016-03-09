/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.swap;

import static com.opengamma.strata.collect.TestHelper.assertSerialization;
import static com.opengamma.strata.collect.TestHelper.assertThrowsIllegalArg;
import static com.opengamma.strata.collect.TestHelper.coverBeanEquals;
import static com.opengamma.strata.collect.TestHelper.coverImmutableBean;
import static com.opengamma.strata.collect.TestHelper.date;
import static org.testng.Assert.assertEquals;

import java.util.Optional;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.opengamma.strata.basics.market.ReferenceData;
import com.opengamma.strata.product.SecurityInfoType;
import com.opengamma.strata.product.TradeInfo;

/**
 * Test {@link DeliverableSwapFutureSecurity}.
 */
@Test
public class DeliverableSwapFutureSecurityTest {

  private static final DeliverableSwapFuture PRODUCT = DeliverableSwapFutureTest.sut();
  private static final DeliverableSwapFuture PRODUCT2 = DeliverableSwapFutureTest.sut2();
  private static final ImmutableMap<SecurityInfoType<?>, Object> INFO_MAP = ImmutableMap.of(SecurityInfoType.NAME, "Test");

  //-------------------------------------------------------------------------
  public void test_builder() {
    DeliverableSwapFutureSecurity test = sut();
    assertEquals(test.getInfo(), INFO_MAP);
    assertEquals(test.getInfo(SecurityInfoType.NAME), "Test");
    assertEquals(test.findInfo(SecurityInfoType.NAME), Optional.of("Test"));
    assertEquals(test.getSecurityId(), PRODUCT.getSecurityId());
    assertEquals(test.getCurrency(), PRODUCT.getCurrency());
    assertEquals(test.getUnderlyingIds(), ImmutableSet.of());
    assertEquals(sut2().findInfo(SecurityInfoType.NAME), Optional.empty());
    assertThrowsIllegalArg(() -> sut2().getInfo(SecurityInfoType.NAME));
  }

  //-------------------------------------------------------------------------
  public void test_createProduct() {
    DeliverableSwapFutureSecurity test = sut();
    assertEquals(test.createProduct(ReferenceData.empty()), PRODUCT);
    TradeInfo tradeInfo = TradeInfo.builder().tradeDate(date(2016, 6, 30)).build();
    DeliverableSwapFutureTrade expectedTrade = DeliverableSwapFutureTrade.builder()
        .tradeInfo(tradeInfo)
        .product(PRODUCT)
        .quantity(100)
        .price(123.50)
        .build();
    assertEquals(test.createTrade(date(2016, 6, 30), 100, 123.50, ReferenceData.empty()), expectedTrade);
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
  static DeliverableSwapFutureSecurity sut() {
    return DeliverableSwapFutureSecurity.builder()
        .product(PRODUCT)
        .info(INFO_MAP)
        .build();
  }

  static DeliverableSwapFutureSecurity sut2() {
    return DeliverableSwapFutureSecurity.builder()
        .product(PRODUCT2)
        .build();
  }

}
