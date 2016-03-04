/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.etd;

import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.ImmutableDefaults;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.strata.basics.market.ReferenceData;
import com.opengamma.strata.product.TradeInfo;

/**
 * A trade in a generic exchange-traded derivative contract (ETD), resolved for pricing.
 * <p>
 * This is the resolved form of {@link EtdTrade} and is the primary input to the pricers.
 * Applications will typically create a {@code ResolvedEtdTrade} from a {@code EtdTrade}
 * using {@link EtdTrade#resolve(ReferenceData)}.
 * <p>
 * A {@code ResolvedEtdTrade} is bound to a specific security from reference data.
 * If the data changes, the resolved form will not be updated.
 * Care must be taken when placing the resolved form in a cache or persistence layer.
 */
@BeanDefinition(constructorScope = "package")
public final class ResolvedGenericEtdTrade
    implements ResolvedSecurityTrade, ImmutableBean, Serializable {

  /**
   * The additional trade information, defaulted to an empty instance.
   * <p>
   * This allows additional information to be attached to the trade.
   */
  @PropertyDefinition(overrideGet = true)
  private final TradeInfo tradeInfo;
  /**
   * The ETD security.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final GenericEtd security;
  /**
   * The quantity, indicating the number of contracts in the trade.
   * <p>
   * This will be positive if buying and negative if selling.
   */
  @PropertyDefinition
  private final long quantity;
  /**
   * The initial price of the ETD, represented in decimal form.
   * <p>
   * This is the price agreed when the trade occurred.
   * This must be represented in decimal form.
   * <p>
   * No indication is provided as to the meaning of one unit of this price.
   * It may be an amount in a currency, a percentage or something else entirely.
   */
  @PropertyDefinition
  private final double initialPrice;

  //-------------------------------------------------------------------------
  @ImmutableDefaults
  private static void applyDefaults(Builder builder) {
    builder.tradeInfo = TradeInfo.EMPTY;
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code ResolvedGenericEtdTrade}.
   * @return the meta-bean, not null
   */
  public static ResolvedGenericEtdTrade.Meta meta() {
    return ResolvedGenericEtdTrade.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(ResolvedGenericEtdTrade.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static ResolvedGenericEtdTrade.Builder builder() {
    return new ResolvedGenericEtdTrade.Builder();
  }

  /**
   * Creates an instance.
   * @param tradeInfo  the value of the property
   * @param security  the value of the property, not null
   * @param quantity  the value of the property
   * @param initialPrice  the value of the property
   */
  ResolvedGenericEtdTrade(
      TradeInfo tradeInfo,
      GenericEtd security,
      long quantity,
      double initialPrice) {
    JodaBeanUtils.notNull(security, "security");
    this.tradeInfo = tradeInfo;
    this.security = security;
    this.quantity = quantity;
    this.initialPrice = initialPrice;
  }

  @Override
  public ResolvedGenericEtdTrade.Meta metaBean() {
    return ResolvedGenericEtdTrade.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the additional trade information, defaulted to an empty instance.
   * <p>
   * This allows additional information to be attached to the trade.
   * @return the value of the property
   */
  @Override
  public TradeInfo getTradeInfo() {
    return tradeInfo;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the ETD security.
   * @return the value of the property, not null
   */
  @Override
  public GenericEtd getSecurity() {
    return security;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the quantity, indicating the number of contracts in the trade.
   * <p>
   * This will be positive if buying and negative if selling.
   * @return the value of the property
   */
  public long getQuantity() {
    return quantity;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the initial price of the ETD, represented in decimal form.
   * <p>
   * This is the price agreed when the trade occurred.
   * This must be represented in decimal form.
   * <p>
   * No indication is provided as to the meaning of one unit of this price.
   * It may be an amount in a currency, a percentage or something else entirely.
   * @return the value of the property
   */
  public double getInitialPrice() {
    return initialPrice;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      ResolvedGenericEtdTrade other = (ResolvedGenericEtdTrade) obj;
      return JodaBeanUtils.equal(tradeInfo, other.tradeInfo) &&
          JodaBeanUtils.equal(security, other.security) &&
          (quantity == other.quantity) &&
          JodaBeanUtils.equal(initialPrice, other.initialPrice);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(tradeInfo);
    hash = hash * 31 + JodaBeanUtils.hashCode(security);
    hash = hash * 31 + JodaBeanUtils.hashCode(quantity);
    hash = hash * 31 + JodaBeanUtils.hashCode(initialPrice);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(160);
    buf.append("ResolvedGenericEtdTrade{");
    buf.append("tradeInfo").append('=').append(tradeInfo).append(',').append(' ');
    buf.append("security").append('=').append(security).append(',').append(' ');
    buf.append("quantity").append('=').append(quantity).append(',').append(' ');
    buf.append("initialPrice").append('=').append(JodaBeanUtils.toString(initialPrice));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code ResolvedGenericEtdTrade}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code tradeInfo} property.
     */
    private final MetaProperty<TradeInfo> tradeInfo = DirectMetaProperty.ofImmutable(
        this, "tradeInfo", ResolvedGenericEtdTrade.class, TradeInfo.class);
    /**
     * The meta-property for the {@code security} property.
     */
    private final MetaProperty<GenericEtd> security = DirectMetaProperty.ofImmutable(
        this, "security", ResolvedGenericEtdTrade.class, GenericEtd.class);
    /**
     * The meta-property for the {@code quantity} property.
     */
    private final MetaProperty<Long> quantity = DirectMetaProperty.ofImmutable(
        this, "quantity", ResolvedGenericEtdTrade.class, Long.TYPE);
    /**
     * The meta-property for the {@code initialPrice} property.
     */
    private final MetaProperty<Double> initialPrice = DirectMetaProperty.ofImmutable(
        this, "initialPrice", ResolvedGenericEtdTrade.class, Double.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "tradeInfo",
        "security",
        "quantity",
        "initialPrice");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 752580658:  // tradeInfo
          return tradeInfo;
        case 949122880:  // security
          return security;
        case -1285004149:  // quantity
          return quantity;
        case -423406491:  // initialPrice
          return initialPrice;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public ResolvedGenericEtdTrade.Builder builder() {
      return new ResolvedGenericEtdTrade.Builder();
    }

    @Override
    public Class<? extends ResolvedGenericEtdTrade> beanType() {
      return ResolvedGenericEtdTrade.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code tradeInfo} property.
     * @return the meta-property, not null
     */
    public MetaProperty<TradeInfo> tradeInfo() {
      return tradeInfo;
    }

    /**
     * The meta-property for the {@code security} property.
     * @return the meta-property, not null
     */
    public MetaProperty<GenericEtd> security() {
      return security;
    }

    /**
     * The meta-property for the {@code quantity} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Long> quantity() {
      return quantity;
    }

    /**
     * The meta-property for the {@code initialPrice} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Double> initialPrice() {
      return initialPrice;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 752580658:  // tradeInfo
          return ((ResolvedGenericEtdTrade) bean).getTradeInfo();
        case 949122880:  // security
          return ((ResolvedGenericEtdTrade) bean).getSecurity();
        case -1285004149:  // quantity
          return ((ResolvedGenericEtdTrade) bean).getQuantity();
        case -423406491:  // initialPrice
          return ((ResolvedGenericEtdTrade) bean).getInitialPrice();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code ResolvedGenericEtdTrade}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<ResolvedGenericEtdTrade> {

    private TradeInfo tradeInfo;
    private GenericEtd security;
    private long quantity;
    private double initialPrice;

    /**
     * Restricted constructor.
     */
    private Builder() {
      applyDefaults(this);
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(ResolvedGenericEtdTrade beanToCopy) {
      this.tradeInfo = beanToCopy.getTradeInfo();
      this.security = beanToCopy.getSecurity();
      this.quantity = beanToCopy.getQuantity();
      this.initialPrice = beanToCopy.getInitialPrice();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 752580658:  // tradeInfo
          return tradeInfo;
        case 949122880:  // security
          return security;
        case -1285004149:  // quantity
          return quantity;
        case -423406491:  // initialPrice
          return initialPrice;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 752580658:  // tradeInfo
          this.tradeInfo = (TradeInfo) newValue;
          break;
        case 949122880:  // security
          this.security = (GenericEtd) newValue;
          break;
        case -1285004149:  // quantity
          this.quantity = (Long) newValue;
          break;
        case -423406491:  // initialPrice
          this.initialPrice = (Double) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    @Override
    public Builder setString(MetaProperty<?> property, String value) {
      super.setString(property, value);
      return this;
    }

    @Override
    public Builder setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public ResolvedGenericEtdTrade build() {
      return new ResolvedGenericEtdTrade(
          tradeInfo,
          security,
          quantity,
          initialPrice);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the additional trade information, defaulted to an empty instance.
     * <p>
     * This allows additional information to be attached to the trade.
     * @param tradeInfo  the new value
     * @return this, for chaining, not null
     */
    public Builder tradeInfo(TradeInfo tradeInfo) {
      this.tradeInfo = tradeInfo;
      return this;
    }

    /**
     * Sets the ETD security.
     * @param security  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder security(GenericEtd security) {
      JodaBeanUtils.notNull(security, "security");
      this.security = security;
      return this;
    }

    /**
     * Sets the quantity, indicating the number of contracts in the trade.
     * <p>
     * This will be positive if buying and negative if selling.
     * @param quantity  the new value
     * @return this, for chaining, not null
     */
    public Builder quantity(long quantity) {
      this.quantity = quantity;
      return this;
    }

    /**
     * Sets the initial price of the ETD, represented in decimal form.
     * <p>
     * This is the price agreed when the trade occurred.
     * This must be represented in decimal form.
     * <p>
     * No indication is provided as to the meaning of one unit of this price.
     * It may be an amount in a currency, a percentage or something else entirely.
     * @param initialPrice  the new value
     * @return this, for chaining, not null
     */
    public Builder initialPrice(double initialPrice) {
      this.initialPrice = initialPrice;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(160);
      buf.append("ResolvedGenericEtdTrade.Builder{");
      buf.append("tradeInfo").append('=').append(JodaBeanUtils.toString(tradeInfo)).append(',').append(' ');
      buf.append("security").append('=').append(JodaBeanUtils.toString(security)).append(',').append(' ');
      buf.append("quantity").append('=').append(JodaBeanUtils.toString(quantity)).append(',').append(' ');
      buf.append("initialPrice").append('=').append(JodaBeanUtils.toString(initialPrice));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}