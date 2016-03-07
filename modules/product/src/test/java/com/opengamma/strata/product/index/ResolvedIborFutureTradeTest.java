/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.index;

import static com.opengamma.strata.collect.TestHelper.assertSerialization;
import static com.opengamma.strata.collect.TestHelper.coverBeanEquals;
import static com.opengamma.strata.collect.TestHelper.coverImmutableBean;

import org.testng.annotations.Test;

import com.opengamma.strata.basics.market.ReferenceData;

/**
 * Test {@link ResolvedIborFutureTrade}.
 */
@Test
public class ResolvedIborFutureTradeTest {

  private static final ReferenceData REF_DATA = ReferenceData.standard();

  //-------------------------------------------------------------------------
  public void coverage() {
    ResolvedIborFutureTrade test = sut();
    coverImmutableBean(test);
    ResolvedIborFutureTrade test2 = sut2();
    coverBeanEquals(test, test2);
  }

  public void test_serialization() {
    ResolvedIborFutureTrade test = sut();
    assertSerialization(test);
  }

  //-------------------------------------------------------------------------
  static ResolvedIborFutureTrade sut2() {
    return IborFutureTradeTest.sut().resolve(REF_DATA);
  }

  static ResolvedIborFutureTrade sut() {
    return IborFutureTradeTest.sut2().resolve(REF_DATA);
  }

}
