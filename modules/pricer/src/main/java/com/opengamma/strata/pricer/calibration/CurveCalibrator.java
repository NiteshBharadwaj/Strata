/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.pricer.calibration;

import static com.opengamma.strata.collect.Guavate.toImmutableList;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.opengamma.strata.basics.index.Index;
import com.opengamma.strata.basics.market.MarketData;
import com.opengamma.strata.basics.market.ReferenceData;
import com.opengamma.strata.collect.Messages;
import com.opengamma.strata.collect.array.DoubleArray;
import com.opengamma.strata.collect.array.DoubleMatrix;
import com.opengamma.strata.collect.timeseries.LocalDateDoubleTimeSeries;
import com.opengamma.strata.market.curve.CurveGroupDefinition;
import com.opengamma.strata.market.curve.CurveName;
import com.opengamma.strata.market.curve.CurveNode;
import com.opengamma.strata.market.curve.CurveParameterSize;
import com.opengamma.strata.market.curve.JacobianCalibrationMatrix;
import com.opengamma.strata.math.impl.linearalgebra.DecompositionFactory;
import com.opengamma.strata.math.impl.matrix.CommonsMatrixAlgebra;
import com.opengamma.strata.math.impl.matrix.MatrixAlgebra;
import com.opengamma.strata.math.impl.rootfinding.newton.BroydenVectorRootFinder;
import com.opengamma.strata.pricer.rate.ImmutableRatesProvider;
import com.opengamma.strata.product.ResolvedTrade;

/**
 * Curve calibrator.
 * <p>
 * This calibrator takes an abstract curve definition and produces real curves.
 * <p>
 * Curves are calibrated in groups or one or more curves.
 * In addition, more than one group may be calibrated together.
 * <p>
 * Each curve is defined using two or more {@linkplain CurveNode nodes}.
 * Each node primarily defines enough information to produce a reference trade.
 * Calibration involves pricing, and re-pricing, these trades to find the best fit
 * using a root finder.
 * <p>
 * Once calibrated, the curves are then available for use.
 * Each node in the curve definition becomes a parameter in the matching output curve.
 */
public final class CurveCalibrator {

  /**
   * The standard curve calibrator.
   */
  private static final CurveCalibrator STANDARD = CurveCalibrator.of(1e-9, 1e-9, 1000, CalibrationMeasures.PAR_SPREAD);
  /**
   * The matrix algebra used for matrix inversion.
   */
  private static final MatrixAlgebra MATRIX_ALGEBRA = new CommonsMatrixAlgebra();

  /**
   * The root finder used for curve calibration.
   */
  private final BroydenVectorRootFinder rootFinder;
  /**
   * The calibration measures.
   * This is used to compute the function for which the root is found.
   */
  private final CalibrationMeasures measures;

  //-------------------------------------------------------------------------
  /**
   * The standard curve calibrator.
   * <p>
   * This uses the standard tolerance of 1e-9, a maximum of 1000 steps.
   * The default {@link CalibrationMeasures#PAR_SPREAD} measures are used.
   *
   * @return the standard curve calibrator
   */
  public static CurveCalibrator standard() {
    return CurveCalibrator.STANDARD;
  }

  /**
   * Obtains an instance specifying tolerances to use.
   * <p>
   * The standard {@link CalibrationMeasures#PAR_SPREAD} measures are used.
   *
   * @param toleranceAbs  the absolute tolerance
   * @param toleranceRel  the relative tolerance
   * @param stepMaximum  the maximum steps
   * @return the curve calibrator
   */
  public static CurveCalibrator of(
      double toleranceAbs,
      double toleranceRel,
      int stepMaximum) {

    return of(toleranceAbs, toleranceRel, stepMaximum, CalibrationMeasures.PAR_SPREAD);
  }

  /**
   * Obtains an instance specifying tolerances and measures to use.
   *
   * @param toleranceAbs  the absolute tolerance
   * @param toleranceRel  the relative tolerance
   * @param stepMaximum  the maximum steps
   * @param measures  the calibration measures, used to compute the function for which the root is found
   * @return the curve calibrator
   */
  public static CurveCalibrator of(
      double toleranceAbs,
      double toleranceRel,
      int stepMaximum,
      CalibrationMeasures measures) {

    return new CurveCalibrator(toleranceAbs, toleranceRel, stepMaximum, measures);
  }

  //-------------------------------------------------------------------------
  // restricted constructor
  private CurveCalibrator(
      double toleranceAbs,
      double toleranceRel,
      int stepMaximum,
      CalibrationMeasures measures) {

    this.rootFinder = new BroydenVectorRootFinder(
        toleranceAbs,
        toleranceRel,
        stepMaximum,
        DecompositionFactory.getDecomposition(DecompositionFactory.SV_COMMONS_NAME));
    this.measures = measures;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the measures.
   * 
   * @return the measures
   */
  public CalibrationMeasures getMeasures() {
    return measures;
  }

  //-------------------------------------------------------------------------
  /**
   * Calibrates a single curve group, containing one or more curves.
   * <p>
   * The calibration is defined using {@link CurveGroupDefinition}.
   * Observable market data, time-series and FX are also needed to complete the calibration.
   *
   * @param curveGroupDefn  the curve group definition
   * @param valuationDate  the validation date
   * @param marketData  the market data required to build a trade for the instrument
   * @param refData  the reference data, used to resolve the trades
   * @param timeSeries  the time-series
   * @return the rates provider resulting from the calibration
   */
  public ImmutableRatesProvider calibrate(
      CurveGroupDefinition curveGroupDefn,
      LocalDate valuationDate,
      MarketData marketData,
      ReferenceData refData,
      Map<Index, LocalDateDoubleTimeSeries> timeSeries) {

    ImmutableRatesProvider knownData = ImmutableRatesProvider.builder(valuationDate)
        .fxRateProvider(new MarketDataFxRateProvider(marketData))
        .timeSeries(timeSeries)
        .build();
    return calibrate(ImmutableList.of(curveGroupDefn), knownData, marketData, refData);
  }

  /**
   * Calibrates a list of curve groups, each containing one or more curves.
   * <p>
   * The calibration is defined using a list of {@link CurveGroupDefinition}.
   * Observable market data and existing known data are also needed to complete the calibration.
   * <p>
   * A curve must only exist in one group.
   *
   * @param allGroupsDefn  the curve group definitions
   * @param knownData  the starting data for the calibration
   * @param marketData  the market data required to build a trade for the instrument
   * @param refData  the reference data, used to resolve the trades
   * @return the rates provider resulting from the calibration
   */
  public ImmutableRatesProvider calibrate(
      List<CurveGroupDefinition> allGroupsDefn,
      ImmutableRatesProvider knownData,
      MarketData marketData,
      ReferenceData refData) {

    // perform calibration one group at a time, building up the result by mutating these variables
    ImmutableRatesProvider providerCombined = knownData;
    ImmutableList<CurveParameterSize> orderPrev = ImmutableList.of();
    ImmutableMap<CurveName, JacobianCalibrationMatrix> jacobians = ImmutableMap.of();
    for (CurveGroupDefinition groupDefn : allGroupsDefn) {
      // combine all data in the group into flat lists
      ImmutableList<ResolvedTrade> trades = groupDefn.resolvedTrades(knownData.getValuationDate(), marketData, refData);
      ImmutableList<Double> initialGuesses = groupDefn.initialGuesses(knownData.getValuationDate(), marketData);
      ImmutableList<CurveParameterSize> orderGroup = toOrder(groupDefn);
      ImmutableList<CurveParameterSize> orderPrevAndGroup = ImmutableList.<CurveParameterSize>builder()
          .addAll(orderPrev)
          .addAll(orderGroup)
          .build();

      // calibrate
      RatesProviderGenerator providerGenerator = ImmutableRatesProviderGenerator.of(providerCombined, groupDefn, refData);
      DoubleArray calibratedGroupParams = calibrateGroup(providerGenerator, trades, initialGuesses, orderGroup);
      ImmutableRatesProvider calibratedProvider = providerGenerator.generate(calibratedGroupParams);

      // use calibration to build Jacobian matrices
      jacobians = updateJacobiansForGroup(
          calibratedProvider, trades, orderGroup, orderPrev, orderPrevAndGroup, jacobians);
      orderPrev = orderPrevAndGroup;

      // use Jacobians to build output curves
      providerCombined = providerGenerator.generate(calibratedGroupParams, jacobians);
    }
    // return the calibrated provider
    return providerCombined;
  }

  // converts a definition to the curve order list
  private static ImmutableList<CurveParameterSize> toOrder(CurveGroupDefinition groupDefn) {
    return groupDefn.getCurveDefinitions().stream().map(def -> def.toCurveParameterSize()).collect(toImmutableList());
  }

  //-------------------------------------------------------------------------
  // calibrates a single group
  private DoubleArray calibrateGroup(
      RatesProviderGenerator providerGenerator,
      ImmutableList<ResolvedTrade> trades,
      ImmutableList<Double> initialGuesses,
      ImmutableList<CurveParameterSize> curveOrder) {

    // setup for calibration
    Function<DoubleArray, DoubleArray> valueCalculator = new CalibrationValue(trades, measures, providerGenerator);
    Function<DoubleArray, DoubleMatrix> derivativeCalculator =
        new CalibrationDerivative(trades, measures, providerGenerator, curveOrder);

    // calibrate
    DoubleArray initGuessMatrix = DoubleArray.copyOf(initialGuesses);
    return rootFinder.getRoot(valueCalculator, derivativeCalculator, initGuessMatrix);
  }

  //-------------------------------------------------------------------------
  // calculates the Jacobian and builds the result, called once per group
  // this uses, but does not alter, data from previous groups
  private ImmutableMap<CurveName, JacobianCalibrationMatrix> updateJacobiansForGroup(
      ImmutableRatesProvider provider,
      ImmutableList<ResolvedTrade> trades,
      ImmutableList<CurveParameterSize> orderGroup,
      ImmutableList<CurveParameterSize> orderPrev,
      ImmutableList<CurveParameterSize> orderAll,
      ImmutableMap<CurveName, JacobianCalibrationMatrix> jacobians) {

    // sensitivity to all parameters in the stated order
    int totalParamsAll = orderAll.stream().mapToInt(e -> e.getParameterCount()).sum();
    DoubleMatrix res = derivatives(trades, provider, orderAll, totalParamsAll);

    // jacobian direct
    int nbTrades = trades.size();
    int totalParamsGroup = orderGroup.stream().mapToInt(e -> e.getParameterCount()).sum();
    int totalParamsPrevious = totalParamsAll - totalParamsGroup;
    DoubleMatrix pDmCurrentMatrix = jacobianDirect(res, nbTrades, totalParamsGroup, totalParamsPrevious);

    // jacobian indirect: when totalParamsPrevious > 0
    DoubleMatrix pDmPrevious = jacobianIndirect(
        res, pDmCurrentMatrix, nbTrades, totalParamsGroup, totalParamsPrevious, orderPrev, jacobians);

    // add to the map of jacobians, one entry for each curve in this group
    ImmutableMap.Builder<CurveName, JacobianCalibrationMatrix> jacobianBuilder = ImmutableMap.builder();
    jacobianBuilder.putAll(jacobians);
    int startIndex = 0;
    for (CurveParameterSize order : orderGroup) {
      int paramCount = order.getParameterCount();
      double[][] pDmCurveArray = new double[paramCount][totalParamsAll];
      // copy data for previous groups
      if (totalParamsPrevious > 0) {
        for (int p = 0; p < paramCount; p++) {
          System.arraycopy(pDmPrevious.rowArray(startIndex + p), 0, pDmCurveArray[p], 0, totalParamsPrevious);
        }
      }
      // copy data for this group
      for (int p = 0; p < paramCount; p++) {
        System.arraycopy(pDmCurrentMatrix.rowArray(startIndex + p), 0, pDmCurveArray[p], totalParamsPrevious, totalParamsGroup);
      }
      // build final Jacobian matrix
      DoubleMatrix pDmCurveMatrix = DoubleMatrix.ofUnsafe(pDmCurveArray);
      jacobianBuilder.put(order.getName(), JacobianCalibrationMatrix.of(orderAll, pDmCurveMatrix));
      startIndex += paramCount;
    }
    return jacobianBuilder.build();
  }

  // calculate the derivatives
  private DoubleMatrix derivatives(
      ImmutableList<ResolvedTrade> trades,
      ImmutableRatesProvider provider,
      ImmutableList<CurveParameterSize> orderAll,
      int totalParamsAll) {

    return DoubleMatrix.ofArrayObjects(
        trades.size(),
        totalParamsAll,
        i -> measures.derivative(trades.get(i), provider, orderAll));
  }

  // jacobian direct, for the current group
  private static DoubleMatrix jacobianDirect(
      DoubleMatrix res,
      int nbTrades,
      int totalParamsGroup,
      int totalParamsPrevious) {

    double[][] direct = new double[totalParamsGroup][totalParamsGroup];
    for (int i = 0; i < nbTrades; i++) {
      System.arraycopy(res.rowArray(i), totalParamsPrevious, direct[i], 0, totalParamsGroup);
    }
    return MATRIX_ALGEBRA.getInverse(DoubleMatrix.copyOf(direct));
  }

  // jacobian indirect, merging groups
  private static DoubleMatrix jacobianIndirect(
      DoubleMatrix res,
      DoubleMatrix pDmCurrentMatrix,
      int nbTrades,
      int totalParamsGroup,
      int totalParamsPrevious,
      ImmutableList<CurveParameterSize> orderPrevious,
      ImmutableMap<CurveName, JacobianCalibrationMatrix> jacobiansPrevious) {

    if (totalParamsPrevious == 0) {
      return DoubleMatrix.EMPTY;
    }
    double[][] nonDirect = new double[totalParamsGroup][totalParamsPrevious];
    for (int i = 0; i < nbTrades; i++) {
      System.arraycopy(res.rowArray(i), 0, nonDirect[i], 0, totalParamsPrevious);
    }
    DoubleMatrix pDpPreviousMatrix = (DoubleMatrix) MATRIX_ALGEBRA.scale(
        MATRIX_ALGEBRA.multiply(pDmCurrentMatrix, DoubleMatrix.copyOf(nonDirect)), -1d);
    // all curves: order and size
    int[] startIndexBefore = new int[orderPrevious.size()];
    for (int i = 1; i < orderPrevious.size(); i++) {
      startIndexBefore[i] = startIndexBefore[i - 1] + orderPrevious.get(i - 1).getParameterCount();
    }
    // transition Matrix: all curves from previous groups
    double[][] transition = new double[totalParamsPrevious][totalParamsPrevious];
    for (int i = 0; i < orderPrevious.size(); i++) {
      int paramCountOuter = orderPrevious.get(i).getParameterCount();
      JacobianCalibrationMatrix thisInfo = jacobiansPrevious.get(orderPrevious.get(i).getName());
      DoubleMatrix thisMatrix = thisInfo.getJacobianMatrix();
      int startIndexInner = 0;
      for (int j = 0; j < orderPrevious.size(); j++) {
        int paramCountInner = orderPrevious.get(j).getParameterCount();
        if (thisInfo.containsCurve(orderPrevious.get(j).getName())) { // If not, the matrix stay with 0
          for (int k = 0; k < paramCountOuter; k++) {
            System.arraycopy(
                thisMatrix.rowArray(k),
                startIndexInner,
                transition[startIndexBefore[i] + k],
                startIndexBefore[j],
                paramCountInner);
          }
        }
        startIndexInner += paramCountInner;
      }
    }
    DoubleMatrix transitionMatrix = DoubleMatrix.copyOf(transition);
    return (DoubleMatrix) MATRIX_ALGEBRA.multiply(pDpPreviousMatrix, transitionMatrix);
  }

  //-------------------------------------------------------------------------
  @Override
  public String toString() {
    return Messages.format("CurveCalibrator[{}]", measures);
  }

}
