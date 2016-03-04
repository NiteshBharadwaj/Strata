/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.etd;

import static com.opengamma.strata.collect.TestHelper.assertSerialization;
import static com.opengamma.strata.collect.TestHelper.assertThrows;
import static com.opengamma.strata.collect.TestHelper.coverBeanEquals;
import static com.opengamma.strata.collect.TestHelper.coverImmutableBean;
import static com.opengamma.strata.collect.TestHelper.date;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.opengamma.strata.basics.market.ImmutableReferenceData;
import com.opengamma.strata.basics.market.ReferenceData;
import com.opengamma.strata.basics.market.ReferenceDataNotFoundException;
import com.opengamma.strata.product.TradeInfo;

/**
 * Test {@link EtdTrade}.
 */
@Test
public class EtdTradeTest {

  private static final GenericEtd SECURITY = GenericEtdTest.sut();
  private static final GenericEtd SECURITY2 = GenericEtdTest.sut2();
  private static final SecurityId SECURITY_ID = SECURITY.getSecurityId();
  private static final SecurityId SECURITY_ID2 = SECURITY2.getSecurityId();
  private static final TradeInfo TRADE_INFO = TradeInfo.builder().tradeDate(date(2016, 6, 30)).build();
  private static final int QUANTITY = 100;
  private static final int QUANTITY2 = 200;
  private static final double TRADE_PRICE = 2.9;
  private static final double TRADE_PRICE2 = 3.3;

  //-------------------------------------------------------------------------
  public void test_builder() {
    EtdTrade test = sut();
    assertEquals(test.getTradeInfo(), TRADE_INFO);
    assertEquals(test.getSecurityId(), SECURITY_ID);
    assertEquals(test.getQuantity(), QUANTITY);
    assertEquals(test.getInitialPrice(), TRADE_PRICE);
  }

  public void test_builder2() {
    EtdTrade test = sut2();
    assertEquals(test.getTradeInfo(), TradeInfo.EMPTY);
    assertEquals(test.getSecurityId(), SECURITY_ID2);
    assertEquals(test.getQuantity(), QUANTITY2);
    assertEquals(test.getInitialPrice(), TRADE_PRICE2);
  }

  //-------------------------------------------------------------------------
  public void test_resolve() {
    EtdTrade test = sut();
    ResolvedGenericEtdTrade expected = ResolvedGenericEtdTrade.builder()
        .tradeInfo(TRADE_INFO)
        .security(SECURITY)
        .quantity(QUANTITY)
        .initialPrice(TRADE_PRICE)
        .build();
    ReferenceData refData = ImmutableReferenceData.of(SECURITY_ID, SECURITY);
    assertEquals(test.resolve(refData), expected);
    assertEquals(test.resolveSecurity(refData), SECURITY);
    assertEquals(test.resolveSecurity(refData) instanceof GenericEtd, true);
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
  static EtdTrade sut() {
    return EtdTrade.builder()
        .tradeInfo(TRADE_INFO)
        .securityId(SECURITY_ID)
        .quantity(QUANTITY)
        .initialPrice(TRADE_PRICE)
        .build();
  }

  static EtdTrade sut2() {
    return EtdTrade.builder()
        .securityId(SECURITY_ID2)
        .quantity(QUANTITY2)
        .initialPrice(TRADE_PRICE2)
        .build();
  }

}
