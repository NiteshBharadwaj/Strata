/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.etd;

import static com.opengamma.strata.basics.currency.Currency.GBP;
import static com.opengamma.strata.collect.TestHelper.assertSerialization;
import static com.opengamma.strata.collect.TestHelper.assertThrowsIllegalArg;
import static com.opengamma.strata.collect.TestHelper.coverBeanEquals;
import static com.opengamma.strata.collect.TestHelper.coverImmutableBean;
import static org.testng.Assert.assertEquals;

import java.util.Optional;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;
import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.currency.CurrencyAmount;
import com.opengamma.strata.collect.id.StandardId;

/**
 * Test {@link GenericEtd}.
 */
@Test
public class GenericEtdTest {

  private static final SecurityId SECURITY_ID = SecurityId.of("Test", "1");
  private static final SecurityId SECURITY_ID2 = SecurityId.of("Test", "2");
  private static final StandardId PRODUCT_ID = StandardId.of("Exchange", "Sym01");
  private static final StandardId PRODUCT_ID2 = StandardId.of("Exchange", "Sym02");
  private static final SecurityInfoType<String> INFO_TYPE = SecurityInfoType.of("Key");
  private static final SecurityInfoType<String> INFO_TYPE2 = SecurityInfoType.of("Key2");
  private static final ImmutableMap<SecurityInfoType<?>, Object> INFO_MAP = ImmutableMap.of(INFO_TYPE, "Test");
  private static final double CONTRACT_SIZE = 1;
  private static final double CONTRACT_SIZE2 = 5;
  private static final double TICK_SIZE = 0.1;
  private static final double TICK_SIZE2 = 0.2;
  private static final CurrencyAmount TICK_VALUE = CurrencyAmount.of(GBP, 25);
  private static final CurrencyAmount TICK_VALUE2 = CurrencyAmount.of(GBP, 40);

  //-------------------------------------------------------------------------
  public void test_builder() {
    GenericEtd test = sut();
    assertEquals(test.getSecurityId(), SECURITY_ID);
    assertEquals(test.getCurrency(), Currency.GBP);
    assertEquals(test.getInfo(), INFO_MAP);
    assertEquals(test.getInfo(INFO_TYPE), "Test");
    assertThrowsIllegalArg(() -> test.getInfo(INFO_TYPE2));
    assertEquals(test.findInfo(INFO_TYPE), Optional.of("Test"));
    assertEquals(test.findInfo(INFO_TYPE2), Optional.empty());
    assertEquals(test.getProductId(), PRODUCT_ID);
    assertEquals(test.getContractSize(), CONTRACT_SIZE);
    assertEquals(test.getTickSize(), TICK_SIZE);
    assertEquals(test.getTickValue(), TICK_VALUE);
    assertEquals(test.toGenericEtd(), test);
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
  static GenericEtd sut() {
    return GenericEtd.builder()
        .securityId(SECURITY_ID)
        .info(INFO_MAP)
        .productId(PRODUCT_ID)
        .contractSize(CONTRACT_SIZE)
        .tickSize(TICK_SIZE)
        .tickValue(TICK_VALUE)
        .build();
  }

  static GenericEtd sut2() {
    return GenericEtd.builder()
        .securityId(SECURITY_ID2)
        .productId(PRODUCT_ID2)
        .contractSize(CONTRACT_SIZE2)
        .tickSize(TICK_SIZE2)
        .tickValue(TICK_VALUE2)
        .build();
  }

}
