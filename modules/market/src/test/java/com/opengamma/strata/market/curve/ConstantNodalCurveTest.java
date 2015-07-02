/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.market.curve;

import static com.opengamma.strata.collect.TestHelper.assertSerialization;
import static com.opengamma.strata.collect.TestHelper.assertThrowsIllegalArg;
import static com.opengamma.strata.collect.TestHelper.coverBeanEquals;
import static com.opengamma.strata.collect.TestHelper.coverImmutableBean;
import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;
import com.opengamma.analytics.math.curve.ConstantDoublesCurve;
import com.opengamma.strata.basics.value.ValueAdjustment;

/**
 * Test {@link ConstantNodalCurve}.
 */
@Test
@SuppressWarnings("deprecation")
public class ConstantNodalCurveTest {

  private static final String NAME = "TestCurve";
  private static final CurveName CURVE_NAME = CurveName.of(NAME);
  private static final CurveMetadata METADATA = DefaultCurveMetadata.of(CURVE_NAME);
  private static final double VALUE = 6d;

  //-------------------------------------------------------------------------
  public void test_of_String() {
    ConstantNodalCurve test = ConstantNodalCurve.of(NAME, VALUE);
    assertThat(test.getName()).isEqualTo(CURVE_NAME);
    assertThat(test.getParameterCount()).isEqualTo(1);
    assertThat(test.getMetadata()).isEqualTo(METADATA);
    assertThat(test.getUnderlyingCurve()).isEqualTo(ConstantDoublesCurve.from(VALUE, NAME));
    assertThat(test.getXValues()).containsExactly(0d);
    assertThat(test.getYValues()).containsExactly(VALUE);
  }

  public void test_of_CurveName() {
    ConstantNodalCurve test = ConstantNodalCurve.of(CURVE_NAME, VALUE);
    assertThat(test.getName()).isEqualTo(CURVE_NAME);
    assertThat(test.getParameterCount()).isEqualTo(1);
    assertThat(test.getMetadata()).isEqualTo(METADATA);
    assertThat(test.getUnderlyingCurve()).isEqualTo(ConstantDoublesCurve.from(VALUE, NAME));
    assertThat(test.getXValues()).containsExactly(0d);
    assertThat(test.getYValues()).containsExactly(VALUE);
  }

  public void test_of_CurveMetadata() {
    ConstantNodalCurve test = ConstantNodalCurve.of(METADATA, VALUE);
    assertThat(test.getName()).isEqualTo(CURVE_NAME);
    assertThat(test.getParameterCount()).isEqualTo(1);
    assertThat(test.getMetadata()).isEqualTo(METADATA);
    assertThat(test.getUnderlyingCurve()).isEqualTo(ConstantDoublesCurve.from(VALUE, NAME));
    assertThat(test.getXValues()).containsExactly(0d);
    assertThat(test.getYValues()).containsExactly(VALUE);
  }

  //-------------------------------------------------------------------------
  public void test_lookup() {
    ConstantNodalCurve test = ConstantNodalCurve.of(CURVE_NAME, VALUE);
    assertThat(test.yValue(0d)).isEqualTo(VALUE);
    assertThat(test.yValue(-10d)).isEqualTo(VALUE);
    assertThat(test.yValue(100d)).isEqualTo(VALUE);

    assertThat(test.yValueParameterSensitivity(0d)).containsExactly(1d);
    assertThat(test.yValueParameterSensitivity(-10d)).containsExactly(1d);
    assertThat(test.yValueParameterSensitivity(100d)).containsExactly(1d);

    assertThat(test.firstDerivative(0d)).isEqualTo(0d);
    assertThat(test.firstDerivative(-10d)).isEqualTo(0d);
    assertThat(test.firstDerivative(100d)).isEqualTo(0d);
  }

  //-------------------------------------------------------------------------
  public void test_withYValues() {
    ConstantNodalCurve base = ConstantNodalCurve.of(CURVE_NAME, VALUE);
    ConstantNodalCurve test = base.withYValues(new double[] {4d});
    assertThat(test.getName()).isEqualTo(CURVE_NAME);
    assertThat(test.getParameterCount()).isEqualTo(1);
    assertThat(test.getMetadata()).isEqualTo(METADATA);
    assertThat(test.getUnderlyingCurve()).isEqualTo(ConstantDoublesCurve.from(4d, NAME));
    assertThat(test.getXValues()).containsExactly(0d);
    assertThat(test.getYValues()).containsExactly(4d);
  }

  public void test_withYValues_badSize() {
    ConstantNodalCurve base = ConstantNodalCurve.of(CURVE_NAME, VALUE);
    assertThrowsIllegalArg(() -> base.withYValues(new double[0]));
    assertThrowsIllegalArg(() -> base.withYValues(new double[] {4d, 6d}));
  }

  public void test_shiftedBy_operator() {
    ConstantNodalCurve base = ConstantNodalCurve.of(CURVE_NAME, VALUE);
    ConstantNodalCurve test = base.shiftedBy((x, y) -> y - 2d);
    assertThat(test.getName()).isEqualTo(CURVE_NAME);
    assertThat(test.getParameterCount()).isEqualTo(1);
    assertThat(test.getMetadata()).isEqualTo(METADATA);
    assertThat(test.getUnderlyingCurve()).isEqualTo(ConstantDoublesCurve.from(4d, NAME));
    assertThat(test.getXValues()).containsExactly(0d);
    assertThat(test.getYValues()).containsExactly(4d);
  }

  public void test_shiftedBy_adjustment() {
    ConstantNodalCurve base = ConstantNodalCurve.of(CURVE_NAME, VALUE);
    ConstantNodalCurve test = base.shiftedBy(ImmutableList.of(ValueAdjustment.ofReplace(4d)));
    assertThat(test.getName()).isEqualTo(CURVE_NAME);
    assertThat(test.getParameterCount()).isEqualTo(1);
    assertThat(test.getMetadata()).isEqualTo(METADATA);
    assertThat(test.getUnderlyingCurve()).isEqualTo(ConstantDoublesCurve.from(4d, NAME));
    assertThat(test.getXValues()).containsExactly(0d);
    assertThat(test.getYValues()).containsExactly(4d);
  }

  //-------------------------------------------------------------------------
  public void coverage() {
    ConstantNodalCurve test = ConstantNodalCurve.of(CURVE_NAME, VALUE);
    coverImmutableBean(test);
    ConstantNodalCurve test2 = ConstantNodalCurve.of("Coverage", 9d);
    coverBeanEquals(test, test2);
  }

  public void test_serialization() {
    ConstantNodalCurve test = ConstantNodalCurve.of(CURVE_NAME, VALUE);
    assertSerialization(test);
  }

}
