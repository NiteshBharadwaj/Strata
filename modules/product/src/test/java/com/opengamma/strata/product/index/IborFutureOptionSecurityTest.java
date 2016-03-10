/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.index;

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
import com.opengamma.strata.basics.market.ImmutableReferenceData;
import com.opengamma.strata.basics.market.ReferenceData;
import com.opengamma.strata.product.SecurityId;
import com.opengamma.strata.product.SecurityInfoType;
import com.opengamma.strata.product.TradeInfo;

/**
 * Test {@link IborFutureOptionSecurity}.
 */
@Test
public class IborFutureOptionSecurityTest {

  private static final IborFutureOption OPTION = IborFutureOptionTest.sut();
  private static final IborFutureOption OPTION2 = IborFutureOptionTest.sut2();
  private static final IborFuture FUTURE = OPTION.getUnderlyingFuture();
  private static final IborFuture FUTURE2 = OPTION2.getUnderlyingFuture();
  private static final IborFutureSecurity FUTURE_SECURITY = IborFutureSecurityTest.sut();
  private static final SecurityId FUTURE_ID = FUTURE.getSecurityId();
  private static final SecurityId FUTURE_ID2 = FUTURE2.getSecurityId();
  private static final SecurityId SECURITY_ID = SecurityId.of("OG-Test", "IborFutureOption");
  private static final SecurityId SECURITY_ID2 = SecurityId.of("OG-Test", "IborFutureOption2");
  private static final ImmutableMap<SecurityInfoType<?>, Object> INFO_MAP = ImmutableMap.of(SecurityInfoType.NAME, "Test");

  //-------------------------------------------------------------------------
  public void test_builder() {
    IborFutureOptionSecurity test = sut();
    assertEquals(test.getInfo(), INFO_MAP);
    assertEquals(test.getInfo(SecurityInfoType.NAME), "Test");
    assertEquals(test.findInfo(SecurityInfoType.NAME), Optional.of("Test"));
    assertEquals(test.getSecurityId(), OPTION.getSecurityId());
    assertEquals(test.getCurrency(), OPTION.getCurrency());
    assertEquals(test.getPutCall(), OPTION.getPutCall());
    assertEquals(test.getPremiumStyle(), OPTION.getPremiumStyle());
    assertEquals(test.getUnderlyingFutureId(), FUTURE_ID);
    assertEquals(test.getUnderlyingIds(), ImmutableSet.of(FUTURE_ID));
    assertEquals(sut2().findInfo(SecurityInfoType.NAME), Optional.empty());
    assertThrowsIllegalArg(() -> sut2().getInfo(SecurityInfoType.NAME));
  }

  //-------------------------------------------------------------------------
  public void test_createProduct() {
    IborFutureOptionSecurity test = sut();
    ReferenceData refData = ImmutableReferenceData.of(FUTURE_ID, FUTURE_SECURITY);
    assertEquals(test.createProduct(refData), OPTION);
    TradeInfo tradeInfo = TradeInfo.builder().tradeDate(date(2016, 6, 30)).build();
    IborFutureOptionTrade expectedTrade = IborFutureOptionTrade.builder()
        .tradeInfo(tradeInfo)
        .product(OPTION)
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
  static IborFutureOptionSecurity sut() {
    return IborFutureOptionSecurity.builder()
        .securityId(SECURITY_ID)
        .info(INFO_MAP)
        .currency(OPTION.getCurrency())
        .putCall(OPTION.getPutCall())
        .strikePrice(OPTION.getStrikePrice())
        .expiryDate(OPTION.getExpiryDate())
        .expiryTime(OPTION.getExpiryTime())
        .expiryZone(OPTION.getExpiryZone())
        .premiumStyle(OPTION.getPremiumStyle())
        .rounding(OPTION.getRounding())
        .underlyingFutureId(FUTURE_ID)
        .build();
  }

  static IborFutureOptionSecurity sut2() {
    return IborFutureOptionSecurity.builder()
        .securityId(SECURITY_ID2)
        .currency(OPTION2.getCurrency())
        .putCall(OPTION2.getPutCall())
        .strikePrice(OPTION2.getStrikePrice())
        .expiryDate(OPTION2.getExpiryDate())
        .expiryTime(OPTION2.getExpiryTime())
        .expiryZone(OPTION2.getExpiryZone())
        .premiumStyle(OPTION2.getPremiumStyle())
        .rounding(OPTION2.getRounding())
        .underlyingFutureId(FUTURE_ID2)
        .build();
  }

}
