/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.bond;

import static com.opengamma.strata.collect.TestHelper.assertSerialization;
import static com.opengamma.strata.collect.TestHelper.assertThrowsIllegalArg;
import static com.opengamma.strata.collect.TestHelper.coverBeanEquals;
import static com.opengamma.strata.collect.TestHelper.coverImmutableBean;
import static com.opengamma.strata.collect.TestHelper.date;
import static org.testng.Assert.assertEquals;

import java.util.Optional;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.opengamma.strata.basics.market.ImmutableReferenceData;
import com.opengamma.strata.basics.market.ReferenceData;
import com.opengamma.strata.product.SecurityInfoType;
import com.opengamma.strata.product.TradeInfo;

/**
 * Test {@link BondFutureSecurity}.
 */
@Test
public class BondFutureSecurityTest {

  private static final BondFuture PRODUCT = BondFutureTest.sut();
  private static final BondFuture PRODUCT2 = BondFutureTest.sut2();
  private static final ImmutableMap<SecurityInfoType<?>, Object> INFO_MAP = ImmutableMap.of(SecurityInfoType.NAME, "Test");

  //-------------------------------------------------------------------------
  public void test_builder() {
    BondFutureSecurity test = sut();
    assertEquals(test.getInfo(), INFO_MAP);
    assertEquals(test.getInfo(SecurityInfoType.NAME), "Test");
    assertEquals(test.findInfo(SecurityInfoType.NAME), Optional.of("Test"));
    assertEquals(test.getSecurityId(), PRODUCT.getSecurityId());
    assertEquals(test.getCurrency(), PRODUCT.getCurrency());
    ImmutableList<FixedCouponBond> basket = PRODUCT.getDeliveryBasket();
    assertEquals(test.getUnderlyingIds(), ImmutableSet.of(basket.get(0).getSecurityId(), basket.get(1).getSecurityId()));
    assertEquals(sut2().findInfo(SecurityInfoType.NAME), Optional.empty());
    assertThrowsIllegalArg(() -> sut2().getInfo(SecurityInfoType.NAME));
  }

  //-------------------------------------------------------------------------
  public void test_createProduct() {
    BondFutureSecurity test = sut();
    ImmutableList<FixedCouponBond> basket = PRODUCT.getDeliveryBasket();
    FixedCouponBondSecurity bondSec0 = FixedCouponBondSecurity.builder()
        .product(PRODUCT.getDeliveryBasket().get(0))
        .build();
    FixedCouponBondSecurity bondSec1 = FixedCouponBondSecurity.builder()
        .product(PRODUCT.getDeliveryBasket().get(1))
        .build();
    ReferenceData refData = ImmutableReferenceData.of(ImmutableMap.of(
        basket.get(0).getSecurityId(), bondSec0,
        basket.get(1).getSecurityId(), bondSec1));
    BondFuture product = test.createProduct(refData);
    assertEquals(product.getDeliveryBasket().get(0), PRODUCT.getDeliveryBasket().get(0));
    assertEquals(product.getDeliveryBasket().get(1), PRODUCT.getDeliveryBasket().get(1));
    TradeInfo tradeInfo = TradeInfo.builder().tradeDate(date(2016, 6, 30)).build();
    BondFutureTrade expectedTrade = BondFutureTrade.builder()
        .tradeInfo(tradeInfo)
        .product(product)
        .quantity(100)
        .price(123.50)
        .build();
    assertEquals(test.createTrade(date(2016, 6, 30), 100, 123.50, refData), expectedTrade);
    assertEquals(test.createTrade(tradeInfo, 100, 123.50, refData), expectedTrade);
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
  static BondFutureSecurity sut() {
    ImmutableList<FixedCouponBond> basket = PRODUCT.getDeliveryBasket();
    return BondFutureSecurity.builder()
        .securityId(PRODUCT.getSecurityId())
        .info(INFO_MAP)
        .currency(PRODUCT.getCurrency())
        .deliveryBasketIds(basket.get(0).getSecurityId(), basket.get(1).getSecurityId())
        .conversionFactors(1d, 2d)
        .firstNoticeDate(PRODUCT.getFirstNoticeDate())
        .firstDeliveryDate(PRODUCT.getFirstDeliveryDate().get())
        .lastNoticeDate(PRODUCT.getLastNoticeDate())
        .lastDeliveryDate(PRODUCT.getLastDeliveryDate().get())
        .lastTradeDate(PRODUCT.getLastTradeDate())
        .rounding(PRODUCT.getRounding())
        .build();
  }

  static BondFutureSecurity sut2() {
    ImmutableList<FixedCouponBond> basket = PRODUCT2.getDeliveryBasket();
    return BondFutureSecurity.builder()
        .securityId(PRODUCT2.getSecurityId())
        .currency(PRODUCT2.getCurrency())
        .deliveryBasketIds(basket.get(0).getSecurityId())
        .conversionFactors(1d)
        .firstNoticeDate(PRODUCT2.getFirstNoticeDate())
        .lastNoticeDate(PRODUCT2.getLastNoticeDate())
        .lastTradeDate(PRODUCT2.getLastTradeDate())
        .rounding(PRODUCT2.getRounding())
        .build();
  }

}
