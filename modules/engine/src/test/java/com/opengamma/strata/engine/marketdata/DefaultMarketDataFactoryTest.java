package com.opengamma.strata.engine.marketdata;

import static com.opengamma.strata.collect.Guavate.toImmutableMap;
import static com.opengamma.strata.collect.TestHelper.date;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Set;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;
import com.opengamma.analytics.financial.model.interestrate.curve.YieldCurve;
import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.index.IborIndex;
import com.opengamma.strata.basics.index.IborIndices;
import com.opengamma.strata.collect.TestHelper;
import com.opengamma.strata.collect.id.StandardId;
import com.opengamma.strata.collect.result.FailureReason;
import com.opengamma.strata.collect.result.Result;
import com.opengamma.strata.collect.timeseries.LocalDateDoubleTimeSeries;
import com.opengamma.strata.engine.marketdata.builders.DiscountingCurveMarketDataBuilder;
import com.opengamma.strata.engine.marketdata.builders.IndexCurveMarketDataBuilder;
import com.opengamma.strata.engine.marketdata.builders.ObservableMarketDataBuilder;
import com.opengamma.strata.engine.marketdata.builders.TimeSeriesProvider;
import com.opengamma.strata.engine.marketdata.mapping.VendorIdMapping;
import com.opengamma.strata.marketdata.curve.CurveGroup;
import com.opengamma.strata.marketdata.id.CurveGroupId;
import com.opengamma.strata.marketdata.id.DiscountingCurveId;
import com.opengamma.strata.marketdata.id.IndexCurveId;
import com.opengamma.strata.marketdata.id.IndexRateId;
import com.opengamma.strata.marketdata.id.ObservableId;
import com.opengamma.strata.marketdata.id.QuoteId;

@Test
public class DefaultMarketDataFactoryTest {

  /**
   * Tests building time series from requirements
   */
  public void buildTimeSeries() {
    IborIndex chfIndex = IborIndices.CHF_LIBOR_1M;
    IborIndex eurIndex = IborIndices.EUR_LIBOR_1M;
    IndexRateId chfId = IndexRateId.of(chfIndex);
    IndexRateId eurId = IndexRateId.of(eurIndex);
    LocalDateDoubleTimeSeries chfTimeSeries =
        LocalDateDoubleTimeSeries.builder()
            .put(date(2011, 3, 8), 1)
            .put(date(2011, 3, 9), 2)
            .put(date(2011, 3, 10), 3)
            .build();
    LocalDateDoubleTimeSeries eurTimeSeries =
        LocalDateDoubleTimeSeries.builder()
            .put(date(2012, 4, 8), 10)
            .put(date(2012, 4, 9), 20)
            .put(date(2012, 4, 10), 30)
            .build();
    Map<IndexRateId, LocalDateDoubleTimeSeries> timeSeries = ImmutableMap.of(chfId, chfTimeSeries, eurId, eurTimeSeries);
    DefaultMarketDataFactory marketDataFactory =
        new DefaultMarketDataFactory(
            new TestTimeSeriesProvider(timeSeries),
            ObservableMarketDataBuilder.none(),
            VendorIdMapping.identity());

    MarketDataRequirements requirements =
        MarketDataRequirements.builder()
            .timeSeries(chfId, eurId)
            .build();
    BaseMarketData marketData =
        marketDataFactory.buildBaseMarketData(requirements, BaseMarketData.empty(date(2015, 3, 25))).getMarketData();

    assertThat(marketData.getTimeSeries(chfId)).isEqualTo(chfTimeSeries);
    assertThat(marketData.getTimeSeries(eurId)).isEqualTo(eurTimeSeries);
  }

  /**
   * Tests building single values using market data builders.
   */
  public void buildNoObservableValues() {
    CurveGroup curveGroup = MarketDataTestUtils.curveGroup();
    YieldCurve discountingCurve = MarketDataTestUtils.discountingCurve(1, Currency.AUD, curveGroup);
    YieldCurve iborCurve = MarketDataTestUtils.iborIndexCurve(1, IborIndices.EUR_EURIBOR_12M, curveGroup);
    DiscountingCurveId discountingCurveId = DiscountingCurveId.of(Currency.AUD, MarketDataTestUtils.CURVE_GROUP_NAME);
    IndexCurveId iborCurveId = IndexCurveId.of(IborIndices.EUR_EURIBOR_12M, MarketDataTestUtils.CURVE_GROUP_NAME);
    CurveGroupId groupId = CurveGroupId.of(MarketDataTestUtils.CURVE_GROUP_NAME);
    BaseMarketData suppliedData = BaseMarketData.builder(date(2011, 3, 8)).addValue(groupId, curveGroup).build();
    DefaultMarketDataFactory marketDataFactory =
        new DefaultMarketDataFactory(
            new TestTimeSeriesProvider(ImmutableMap.of()),
            ObservableMarketDataBuilder.none(),
            VendorIdMapping.identity(),
            new DiscountingCurveMarketDataBuilder(),
            new IndexCurveMarketDataBuilder());

    MarketDataRequirements requirements =
        MarketDataRequirements.builder()
            .values(discountingCurveId, iborCurveId)
            .build();
    MarketDataResult result = marketDataFactory.buildBaseMarketData(requirements, suppliedData);
    BaseMarketData marketData = result.getMarketData();
    assertThat(marketData.getValue(discountingCurveId)).isEqualTo(discountingCurve);
    assertThat(marketData.getValue(iborCurveId)).isEqualTo(iborCurve);
  }

  /**
   * Tests building observable market data values.
   */
  public void buildObservableValues() {
    DefaultMarketDataFactory factory =
        new DefaultMarketDataFactory(
            new TestTimeSeriesProvider(ImmutableMap.of()),
            new TestObservableMarketDataBuilder(),
            new TestVendorIdMapping());

    BaseMarketData suppliedData = BaseMarketData.empty(TestHelper.date(2011, 3, 8));
    QuoteId id1 = QuoteId.of(StandardId.of("reqs", "a"));
    QuoteId id2 = QuoteId.of(StandardId.of("reqs", "b"));
    MarketDataRequirements requirements = MarketDataRequirements.builder().values(id1, id2).build();
    BaseMarketData marketData = factory.buildBaseMarketData(requirements, suppliedData).getMarketData();

    assertThat(marketData.getValue(id1)).isEqualTo(1d);
    assertThat(marketData.getValue(id2)).isEqualTo(2d);
  }

  /**
   * Simple time series provider backed by a map.
   */
  private static final class TestTimeSeriesProvider implements TimeSeriesProvider {

    private final Map<? extends ObservableId, LocalDateDoubleTimeSeries> timeSeries;

    private TestTimeSeriesProvider(Map<IndexRateId, LocalDateDoubleTimeSeries> timeSeries) {
      this.timeSeries = timeSeries;
    }

    @Override
    public Result<LocalDateDoubleTimeSeries> timeSeries(ObservableId id) {
      LocalDateDoubleTimeSeries series = timeSeries.get(id);
      return Result.ofNullable(series, FailureReason.MISSING_DATA, "No time series found for ID {}", id);
    }
  }

  /**
   * Builds observable data by parsing the value of the standard ID.
   */
  private static final class TestObservableMarketDataBuilder implements ObservableMarketDataBuilder {

    @Override
    public Map<ObservableId, Result<Double>> build(Set<? extends ObservableId> requirements) {
      return requirements.stream().collect(toImmutableMap(id -> id, this::buildResult));
    }

    private Result<Double> buildResult(ObservableId id) {
      return Result.success(Double.parseDouble(id.getStandardId().getValue()));
    }
  }

  /**
   * Simple ID mapping backed by a map.
   */
  private static final class TestVendorIdMapping implements VendorIdMapping {

    private final Map<ObservableId, ObservableId> idMap =
        ImmutableMap.of(
            QuoteId.of(StandardId.of("reqs", "a")), QuoteId.of(StandardId.of("vendor", "1")),
            QuoteId.of(StandardId.of("reqs", "b")), QuoteId.of(StandardId.of("vendor", "2")));

    @Override
    public ObservableId idForVendor(ObservableId id) {
      return idMap.get(id);
    }
  }
}