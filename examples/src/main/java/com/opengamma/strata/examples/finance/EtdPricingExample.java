/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.strata.examples.finance;

import static com.opengamma.strata.basics.currency.Currency.EUR;
import static com.opengamma.strata.basics.currency.Currency.USD;

import java.time.LocalDate;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.opengamma.strata.basics.Trade;
import com.opengamma.strata.basics.currency.CurrencyAmount;
import com.opengamma.strata.basics.market.ReferenceData;
import com.opengamma.strata.calc.CalculationRules;
import com.opengamma.strata.calc.CalculationRunner;
import com.opengamma.strata.calc.Column;
import com.opengamma.strata.calc.config.Measures;
import com.opengamma.strata.calc.marketdata.MarketEnvironment;
import com.opengamma.strata.calc.runner.Results;
import com.opengamma.strata.collect.id.StandardId;
import com.opengamma.strata.examples.data.ExampleData;
import com.opengamma.strata.examples.marketdata.ExampleMarketData;
import com.opengamma.strata.examples.marketdata.ExampleMarketDataBuilder;
import com.opengamma.strata.function.StandardComponents;
import com.opengamma.strata.product.TradeInfo;
import com.opengamma.strata.product.etd.EtdTrade;
import com.opengamma.strata.product.etd.GenericEtd;
import com.opengamma.strata.product.etd.SecurityId;
import com.opengamma.strata.report.ReportCalculationResults;
import com.opengamma.strata.report.trade.TradeReport;
import com.opengamma.strata.report.trade.TradeReportTemplate;

/**
 * Example to illustrate using the engine to price generic ETDs.
 * <p>
 * This makes use of the example engine and the example market data environment.
 */
public class EtdPricingExample {

  private static final SecurityId FUTURE_ID1 = SecurityId.of("OG-Future", "Eurex-FGBL-Mar14");
  private static final GenericEtd FUTURE1 = GenericEtd.builder()
      .securityId(FUTURE_ID1)
      .productId(StandardId.of("Eurex", "FGBL"))
      .tickSize(0.01)
      .tickValue(CurrencyAmount.of(EUR, 10))
      .build();
  private static final SecurityId FUTURE_ID2 = SecurityId.of("OG-Future", "CME-ED-Mar14");
  private static final GenericEtd FUTURE2 = GenericEtd.builder()
      .securityId(FUTURE_ID2)
      .productId(StandardId.of("CME", "ED"))
      .tickSize(0.005)
      .tickValue(CurrencyAmount.of(USD, 12.5))
      .build();
  private static final SecurityId OPTION_ID1 = SecurityId.of("OG-FutOpt", "Eurex-OGBL-Mar14-C150");
  private static final GenericEtd OPTION1 = GenericEtd.builder()
      .securityId(OPTION_ID1)
      .productId(StandardId.of("Eurex", "OGBL"))
      .tickSize(0.01)
      .tickValue(CurrencyAmount.of(EUR, 10))
      .build();

  /**
   * Runs the example, pricing the instruments, producing the output as an ASCII table.
   * 
   * @param args  ignored
   */
  public static void main(String[] args) {
    // setup calculation runner component, which needs life-cycle management
    // a typical application might use dependency injection to obtain the instance
    try (CalculationRunner runner = CalculationRunner.ofMultiThreaded()) {
      calculate(runner);
    }
  }

  // obtains the data and calculates the grid of results
  private static void calculate(CalculationRunner runner) {
    // the trades that will have measures calculated
    List<Trade> trades = ImmutableList.of(createFutureTrade1(), createFutureTrade2(), createOptionTrade1());

    // the columns, specifying the measures to be calculated
    List<Column> columns = ImmutableList.of(
        Column.of(Measures.PRESENT_VALUE));

    // use the built-in example market data
    ExampleMarketDataBuilder marketDataBuilder = ExampleMarketData.builder();

    // the complete set of rules for calculating measures
    CalculationRules rules = CalculationRules.builder()
        .pricingRules(StandardComponents.pricingRules())
        .marketDataRules(marketDataBuilder.rules())
        .build();

    // build a market data snapshot for the valuation date
    LocalDate valuationDate = LocalDate.of(2014, 1, 22);
    MarketEnvironment marketSnapshot = marketDataBuilder.buildSnapshot(valuationDate);

    // the reference data, such as holidays and securities
    ReferenceData refData = referenceData();
    //.combinedWith(ExampleReferenceData.refData());

    // calculate the results
    Results results = runner.calculateSingleScenario(rules, trades, columns, marketSnapshot, refData);

    // use the report runner to transform the engine results into a trade report
    ReportCalculationResults calculationResults =
        ReportCalculationResults.of(valuationDate, trades, columns, results, refData);

    TradeReportTemplate reportTemplate = ExampleData.loadTradeReportTemplate("etd-report-template");
    TradeReport tradeReport = TradeReport.of(calculationResults, reportTemplate);
    tradeReport.writeAsciiTable(System.out);
  }

  //-----------------------------------------------------------------------
  private static ReferenceData referenceData() {
    ReferenceData securities = ReferenceData.of(ImmutableMap.of(
        FUTURE_ID1, FUTURE1, FUTURE_ID2, FUTURE2, OPTION_ID1, OPTION1));
    return ReferenceData.standard().combinedWith(securities);
  }

  // create an ETD trade
  private static Trade createFutureTrade1() {
    return EtdTrade.builder()
        .securityId(FUTURE_ID1)
        .tradeInfo(TradeInfo.builder()
            .attributes(ImmutableMap.of("description", "Euro-Bund Mar14"))
            .counterparty(StandardId.of("mn", "Dealer G"))
            .settlementDate(LocalDate.of(2013, 12, 15))
            .build())
        .quantity(20)
        .initialPrice(99.550)
        .build();
  }

  // create an ETD trade
  private static Trade createFutureTrade2() {
    return EtdTrade.builder()
        .securityId(FUTURE_ID2)
        .tradeInfo(TradeInfo.builder()
            .attributes(ImmutableMap.of("description", "EuroDollar Mar14"))
            .counterparty(StandardId.of("mn", "Dealer G"))
            .settlementDate(LocalDate.of(2013, 12, 18))
            .build())
        .quantity(50)
        .initialPrice(99.550)
        .build();
  }

  // create an ETD trade
  private static Trade createOptionTrade1() {
    return EtdTrade.builder()
        .securityId(OPTION_ID1)
        .tradeInfo(TradeInfo.builder()
            .attributes(ImmutableMap.of("description", "Call on Euro-Bund Mar14"))
            .counterparty(StandardId.of("mn", "Dealer G"))
            .settlementDate(LocalDate.of(2013, 1, 15))
            .build())
        .quantity(20)
        .initialPrice(1.6)
        .build();
  }

}
