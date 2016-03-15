/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product;

import static com.opengamma.strata.basics.currency.Currency.GBP;
import static com.opengamma.strata.collect.TestHelper.assertSerialization;
import static com.opengamma.strata.collect.TestHelper.coverBeanEquals;
import static com.opengamma.strata.collect.TestHelper.coverImmutableBean;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;
import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.currency.CurrencyAmount;

/**
 * Test {@link GenericSecurity}.
 */
@Test
public class GenericSecurityTest {

  private static final SecurityInfo INFO = SecurityInfo.of(SecurityId.of("Test", "1"));
  private static final ImmutableMap<SecurityInfoType<?>, Object> INFO_MAP = ImmutableMap.of(SecurityInfoType.NAME, "Test");
  private static final SecurityInfo INFO2 = SecurityInfo.of(SecurityId.of("Test", "2"), INFO_MAP);
  private static final double TICK_SIZE = 0.1;
  private static final double TICK_SIZE2 = 0.2;
  private static final CurrencyAmount TICK_VALUE = CurrencyAmount.of(GBP, 25);
  private static final CurrencyAmount TICK_VALUE2 = CurrencyAmount.of(GBP, 40);

  //-------------------------------------------------------------------------
  public void test_of() {
    GenericSecurity test = GenericSecurity.of(INFO.getId(), TICK_SIZE, TICK_VALUE);
    assertEquals(test.getSecurityInfo(), INFO);
    assertEquals(test.getCurrency(), Currency.GBP);
    assertEquals(test.getTickSize(), TICK_SIZE);
    assertEquals(test.getTickValue(), TICK_VALUE);
    assertEquals(test, GenericSecurity.of(INFO, TICK_SIZE, TICK_VALUE));
  }

  public void test_builder() {
    GenericSecurity test = sut();
    assertEquals(test.getSecurityInfo(), INFO);
    assertEquals(test.getCurrency(), Currency.GBP);
    assertEquals(test.getTickSize(), TICK_SIZE);
    assertEquals(test.getTickValue(), TICK_VALUE);
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
  static GenericSecurity sut() {
    return GenericSecurity.builder()
        .securityInfo(INFO)
        .tickSize(TICK_SIZE)
        .tickValue(TICK_VALUE)
        .build();
  }

  static GenericSecurity sut2() {
    return GenericSecurity.builder()
        .securityInfo(INFO2)
        .tickSize(TICK_SIZE2)
        .tickValue(TICK_VALUE2)
        .build();
  }

}
