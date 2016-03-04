/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.equity;

import static com.opengamma.strata.collect.TestHelper.assertSerialization;
import static com.opengamma.strata.collect.TestHelper.assertThrowsIllegalArg;
import static com.opengamma.strata.collect.TestHelper.coverBeanEquals;
import static com.opengamma.strata.collect.TestHelper.coverImmutableBean;
import static org.testng.Assert.assertEquals;

import java.util.Optional;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;
import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.product.etd.SecurityId;
import com.opengamma.strata.product.etd.SecurityInfoType;

/**
 * Test {@link Equity}.
 */
@Test
public class EquityTest {

  private static final SecurityId SECURITY_ID = SecurityId.of("Test", "1");
  private static final SecurityId SECURITY_ID2 = SecurityId.of("Test", "2");
  private static final SecurityInfoType<String> INFO_TYPE = SecurityInfoType.of("Key");
  private static final SecurityInfoType<String> INFO_TYPE2 = SecurityInfoType.of("Key2");
  private static final ImmutableMap<SecurityInfoType<?>, Object> INFO_MAP = ImmutableMap.of(INFO_TYPE, "Test");

  //-------------------------------------------------------------------------
  public void test_builder() {
    Equity test = sut();
    assertEquals(test.getSecurityId(), SECURITY_ID);
    assertEquals(test.getCurrency(), Currency.GBP);
    assertEquals(test.getInfo(), INFO_MAP);
    assertEquals(test.getInfo(INFO_TYPE), "Test");
    assertThrowsIllegalArg(() -> test.getInfo(INFO_TYPE2));
    assertEquals(test.findInfo(INFO_TYPE), Optional.of("Test"));
    assertEquals(test.findInfo(INFO_TYPE2), Optional.empty());
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
  static Equity sut() {
    return Equity.builder()
        .securityId(SECURITY_ID)
        .currency(Currency.GBP)
        .info(INFO_MAP)
        .build();
  }

  static Equity sut2() {
    return Equity.builder()
        .securityId(SECURITY_ID2)
        .currency(Currency.USD)
        .build();
  }

}
