/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.pricer.swaption;

import static com.opengamma.strata.basics.currency.Currency.USD;
import static com.opengamma.strata.basics.date.BusinessDayConventions.MODIFIED_FOLLOWING;
import static com.opengamma.strata.basics.date.DayCounts.ACT_365F;
import static com.opengamma.strata.basics.date.DayCounts.THIRTY_U_360;
import static com.opengamma.strata.basics.date.HolidayCalendarIds.USNY;
import static com.opengamma.strata.basics.index.IborIndices.USD_LIBOR_3M;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.opengamma.strata.basics.date.BusinessDayAdjustment;
import com.opengamma.strata.basics.schedule.Frequency;
import com.opengamma.strata.collect.array.DoubleArray;
import com.opengamma.strata.market.ValueType;
import com.opengamma.strata.market.interpolator.CurveExtrapolators;
import com.opengamma.strata.market.interpolator.CurveInterpolators;
import com.opengamma.strata.market.surface.DefaultSurfaceMetadata;
import com.opengamma.strata.market.surface.InterpolatedNodalSurface;
import com.opengamma.strata.market.surface.NodalSurface;
import com.opengamma.strata.market.surface.SurfaceMetadata;
import com.opengamma.strata.market.surface.SurfaceName;
import com.opengamma.strata.math.impl.interpolation.CombinedInterpolatorExtrapolator;
import com.opengamma.strata.math.impl.interpolation.GridInterpolator2D;
import com.opengamma.strata.math.impl.interpolation.Interpolator1D;
import com.opengamma.strata.pricer.datasets.RatesProviderDataSets;
import com.opengamma.strata.product.swap.type.FixedIborSwapConvention;
import com.opengamma.strata.product.swap.type.FixedRateSwapLegConvention;
import com.opengamma.strata.product.swap.type.IborRateSwapLegConvention;
import com.opengamma.strata.product.swap.type.ImmutableFixedIborSwapConvention;

/**
 * Black volatility data sets for testing.
 */
public class SwaptionNormalVolatilityDataSets {

  private static final double BP1 = 1.0E-4;

  private static final Interpolator1D LINEAR_FLAT = CombinedInterpolatorExtrapolator.of(
      CurveInterpolators.LINEAR.getName(), CurveExtrapolators.FLAT.getName(), CurveExtrapolators.FLAT.getName());
  private static final GridInterpolator2D INTERPOLATOR_2D = new GridInterpolator2D(LINEAR_FLAT, LINEAR_FLAT);

  //     =====     Standard figures for testing     =====
  private static final DoubleArray TIMES =
      DoubleArray.of(0.50, 1.00, 5.00, 10.0, 0.50, 1.00, 5.00, 10.0,
          0.50, 1.00, 5.00, 10.0, 0.50, 1.00, 5.00, 10.0, 0.50, 1.00, 5.00, 10.0);
  private static final DoubleArray TENORS =
      DoubleArray.of(1.0, 1.0, 1.0, 1.0, 2.0, 2.0, 2.0, 2.0,
          5.0, 5.0, 5.0, 5.0, 10.0, 10.0, 10.0, 10.0, 30.0, 30.0, 30.0, 30.0);
  private static final DoubleArray NORMAL_VOL =
      DoubleArray.of(0.010, 0.011, 0.012, 0.013, 0.011, 0.012, 0.013, 0.014,
          0.012, 0.013, 0.014, 0.015, 0.013, 0.014, 0.015, 0.016, 0.014, 0.015, 0.016, 0.017);
  private static final SurfaceMetadata METADATA = DefaultSurfaceMetadata.builder()
      .xValueType(ValueType.YEAR_FRACTION)
      .yValueType(ValueType.YEAR_FRACTION)
      .zValueType(ValueType.NORMAL_VOLATILITY)
      .surfaceName(SurfaceName.of("Normal Vol"))
      .build();
  private static final NodalSurface SURFACE_STD =
      InterpolatedNodalSurface.of(METADATA, TIMES, TENORS, NORMAL_VOL, INTERPOLATOR_2D);

  private static final LocalDate VAL_DATE_STD = RatesProviderDataSets.VAL_DATE_2014_01_22;
  private static final LocalTime VAL_TIME_STD = LocalTime.of(13, 45);
  private static final ZoneId VAL_ZONE_STD = ZoneId.of("Europe/London");
  private static final ZonedDateTime VAL_DATE_TIME_STD = VAL_DATE_STD.atTime(VAL_TIME_STD).atZone(VAL_ZONE_STD);
  private static final BusinessDayAdjustment MOD_FOL_US = BusinessDayAdjustment.of(MODIFIED_FOLLOWING, USNY);
  private static final FixedRateSwapLegConvention USD_FIXED_1Y_30U360 =
      FixedRateSwapLegConvention.of(USD, THIRTY_U_360, Frequency.P6M, MOD_FOL_US);
  private static final IborRateSwapLegConvention USD_IBOR_LIBOR3M =
      IborRateSwapLegConvention.of(USD_LIBOR_3M);
  public static final FixedIborSwapConvention USD_1Y_LIBOR3M =
      ImmutableFixedIborSwapConvention.of("USD-Swap", USD_FIXED_1Y_30U360, USD_IBOR_LIBOR3M);
  public static final NormalSwaptionExpiryTenorVolatilities NORMAL_VOL_SWAPTION_PROVIDER_USD_STD =
      NormalSwaptionExpiryTenorVolatilities.of(SURFACE_STD, USD_1Y_LIBOR3M, VAL_DATE_TIME_STD, ACT_365F);

  /**
   * Returns the swaption normal volatility surface shifted by a given amount. The shift is parallel.
   * @param shift  the shift
   * @return the swaption normal volatility surface
   */
  public static NormalSwaptionExpiryTenorVolatilities normalVolSwaptionProviderUsdStsShifted(double shift) {
    DoubleArray volShifted = NORMAL_VOL.map(v -> v + shift);
    return NormalSwaptionExpiryTenorVolatilities.of(
        SURFACE_STD.withZValues(volShifted), USD_1Y_LIBOR3M, VAL_DATE_TIME_STD, ACT_365F);
  }

  public static NormalSwaptionExpiryTenorVolatilities normalVolSwaptionProviderUsdStd(LocalDate valuationDate) {
    return NormalSwaptionExpiryTenorVolatilities.of(SURFACE_STD, USD_1Y_LIBOR3M,
        valuationDate, VAL_TIME_STD, VAL_ZONE_STD, ACT_365F);
  }

  //     =====     Flat volatilities for testing     =====

  private static final DoubleArray TIMES_FLAT = DoubleArray.of(0.0, 100.0, 0.0, 100.0);
  private static final DoubleArray TENOR_FLAT = DoubleArray.of(0.0, 0.0, 30.0, 30.0);
  private static final DoubleArray NORMAL_VOL_FLAT = DoubleArray.of(0.01, 0.01, 0.01, 0.01);
  private static final InterpolatedNodalSurface SURFACE_FLAT =
      InterpolatedNodalSurface.of(METADATA, TIMES_FLAT, TENOR_FLAT, NORMAL_VOL_FLAT, INTERPOLATOR_2D);

  public static final NormalSwaptionExpiryTenorVolatilities NORMAL_VOL_SWAPTION_PROVIDER_USD_FLAT =
      NormalSwaptionExpiryTenorVolatilities.of(SURFACE_FLAT, USD_1Y_LIBOR3M,
          VAL_DATE_STD, VAL_TIME_STD, VAL_ZONE_STD, ACT_365F);

  //     =====     Market data as of 2014-03-20     =====

  private static final DoubleArray TIMES_20150320 = DoubleArray.of(
      0.25, 0.25, 0.25, 0.25, 0.25,
      0.50, 0.50, 0.50, 0.50, 0.50,
      1.0, 1.0, 1.0, 1.0, 1.0,
      2.0, 2.0, 2.0, 2.0, 2.0,
      5.0, 5.0, 5.0, 5.0, 5.0,
      10.0, 10.0, 10.0, 10.0, 10.0);
  private static final DoubleArray TENORS_20150320 = DoubleArray.of(
      1.0, 2.0, 5.0, 10.0, 30.0,
      1.0, 2.0, 5.0, 10.0, 30.0,
      1.0, 2.0, 5.0, 10.0, 30.0,
      1.0, 2.0, 5.0, 10.0, 30.0,
      1.0, 2.0, 5.0, 10.0, 30.0,
      1.0, 2.0, 5.0, 10.0, 30.0);
  private static final DoubleArray NORMAL_VOL_20150320_BP = DoubleArray.of(
      43.6, 65.3, 88, 87.5, 88, // 3M
      55.5, 72.2, 90.3, 89.3, 88.6, // 6M
      72.6, 82.7, 91.6, 89.8, 87.3, // 1Y
      90.4, 91.9, 93.4, 84.7, 93.5, // 2Y
      99.3, 96.8, 94.3, 88.6, 77.3,  // 5Y
      88.4, 85.9, 82.2, 76.7, 65.1); // 10Y
  private static final DoubleArray NORMAL_VOL_20150320 = NORMAL_VOL_20150320_BP.map(v -> v * BP1);
  private static final NodalSurface SURFACE_20150320 =
      InterpolatedNodalSurface.of(METADATA, TIMES_20150320, TENORS_20150320, NORMAL_VOL_20150320, INTERPOLATOR_2D);

  private static final LocalDate VAL_DATE_20150320 = LocalDate.of(2015, 3, 20);
  private static final LocalTime VAL_TIME_20150320 = LocalTime.of(18, 00);
  private static final ZoneId VAL_ZONE_20150320 = ZoneId.of("Europe/London");

  public static final NormalSwaptionExpiryTenorVolatilities NORMAL_VOL_SWAPTION_PROVIDER_USD_20150320 =
      NormalSwaptionExpiryTenorVolatilities.of(
          SURFACE_20150320, USD_1Y_LIBOR3M, VAL_DATE_20150320, VAL_TIME_20150320, VAL_ZONE_20150320, ACT_365F);

}
