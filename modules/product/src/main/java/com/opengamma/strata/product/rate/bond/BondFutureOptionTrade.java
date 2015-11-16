/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.rate.bond;

import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.OptionalDouble;
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

import com.opengamma.strata.collect.id.LinkResolver;
import com.opengamma.strata.collect.id.StandardId;
import com.opengamma.strata.product.SecurityLink;
import com.opengamma.strata.product.SecurityTrade;
import com.opengamma.strata.product.TradeInfo;

/**
 * A trade representing an option on a futures contract based on bonds.
 * <p>
 * A trade in an underlying {@link BondFutureOption}.
 * The option is American, exercised at any point up to the exercise time.
 * Both daily margin and upfront premium styles are handled.
 */
@BeanDefinition
public final class BondFutureOptionTrade
    implements SecurityTrade<BondFutureOption>, ImmutableBean, Serializable {

  /**
   * The additional trade information, defaulted to an empty instance.
   * <p>
   * This allows additional information to be attached to the trade.
   */
  @PropertyDefinition(overrideGet = true)
  private final TradeInfo tradeInfo;
  /**
   * The link to the option that was traded.
   * <p>
   * This property returns a link to the security via a {@link StandardId}.
   * See {@link #getSecurity()} and {@link SecurityLink} for more details.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final SecurityLink<BondFutureOption> securityLink;
  /**
   * The quantity, indicating the number of option contracts in the trade.
   * <p>
   * This will be positive if buying and negative if selling.
   */
  @PropertyDefinition
  private final long quantity;
  /**
   * The initial price of the option, represented in decimal form.
   * <p>
   * This is the price agreed when the trade occurred.
   * This must be represented in decimal form, {@code (1.0 - decimalRate)}. 
   * As such, the common market price of 99.3 for a 0.7% rate must be input as 0.993.
   * <p>
   * This property should be set if the option has daily margining.
   */
  @PropertyDefinition(get = "optional")
  private final Double initialPrice;

  //-------------------------------------------------------------------------
  @SuppressWarnings({"rawtypes", "unchecked"})
  @ImmutableDefaults
  private static void applyDefaults(Builder builder) {
    builder.tradeInfo = TradeInfo.EMPTY;
  }

  //-------------------------------------------------------------------------
  @Override
  public BondFutureOptionTrade resolveLinks(LinkResolver resolver) {
    return resolver.resolveLinksIn(this, securityLink, resolved -> toBuilder().securityLink(resolved).build());
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code BondFutureOptionTrade}.
   * @return the meta-bean, not null
   */
  public static BondFutureOptionTrade.Meta meta() {
    return BondFutureOptionTrade.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(BondFutureOptionTrade.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static BondFutureOptionTrade.Builder builder() {
    return new BondFutureOptionTrade.Builder();
  }

  private BondFutureOptionTrade(
      TradeInfo tradeInfo,
      SecurityLink<BondFutureOption> securityLink,
      long quantity,
      Double initialPrice) {
    JodaBeanUtils.notNull(securityLink, "securityLink");
    this.tradeInfo = tradeInfo;
    this.securityLink = securityLink;
    this.quantity = quantity;
    this.initialPrice = initialPrice;
  }

  @Override
  public BondFutureOptionTrade.Meta metaBean() {
    return BondFutureOptionTrade.Meta.INSTANCE;
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
   * Gets the link to the option that was traded.
   * <p>
   * This property returns a link to the security via a {@link StandardId}.
   * See {@link #getSecurity()} and {@link SecurityLink} for more details.
   * @return the value of the property, not null
   */
  @Override
  public SecurityLink<BondFutureOption> getSecurityLink() {
    return securityLink;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the quantity, indicating the number of option contracts in the trade.
   * <p>
   * This will be positive if buying and negative if selling.
   * @return the value of the property
   */
  public long getQuantity() {
    return quantity;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the initial price of the option, represented in decimal form.
   * <p>
   * This is the price agreed when the trade occurred.
   * This must be represented in decimal form, {@code (1.0 - decimalRate)}.
   * As such, the common market price of 99.3 for a 0.7% rate must be input as 0.993.
   * <p>
   * This property should be set if the option has daily margining.
   * @return the optional value of the property, not null
   */
  public OptionalDouble getInitialPrice() {
    return initialPrice != null ? OptionalDouble.of(initialPrice) : OptionalDouble.empty();
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
      BondFutureOptionTrade other = (BondFutureOptionTrade) obj;
      return JodaBeanUtils.equal(tradeInfo, other.tradeInfo) &&
          JodaBeanUtils.equal(securityLink, other.securityLink) &&
          (quantity == other.quantity) &&
          JodaBeanUtils.equal(initialPrice, other.initialPrice);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(tradeInfo);
    hash = hash * 31 + JodaBeanUtils.hashCode(securityLink);
    hash = hash * 31 + JodaBeanUtils.hashCode(quantity);
    hash = hash * 31 + JodaBeanUtils.hashCode(initialPrice);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(160);
    buf.append("BondFutureOptionTrade{");
    buf.append("tradeInfo").append('=').append(tradeInfo).append(',').append(' ');
    buf.append("securityLink").append('=').append(securityLink).append(',').append(' ');
    buf.append("quantity").append('=').append(quantity).append(',').append(' ');
    buf.append("initialPrice").append('=').append(JodaBeanUtils.toString(initialPrice));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code BondFutureOptionTrade}.
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
        this, "tradeInfo", BondFutureOptionTrade.class, TradeInfo.class);
    /**
     * The meta-property for the {@code securityLink} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<SecurityLink<BondFutureOption>> securityLink = DirectMetaProperty.ofImmutable(
        this, "securityLink", BondFutureOptionTrade.class, (Class) SecurityLink.class);
    /**
     * The meta-property for the {@code quantity} property.
     */
    private final MetaProperty<Long> quantity = DirectMetaProperty.ofImmutable(
        this, "quantity", BondFutureOptionTrade.class, Long.TYPE);
    /**
     * The meta-property for the {@code initialPrice} property.
     */
    private final MetaProperty<Double> initialPrice = DirectMetaProperty.ofImmutable(
        this, "initialPrice", BondFutureOptionTrade.class, Double.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "tradeInfo",
        "securityLink",
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
        case 807992154:  // securityLink
          return securityLink;
        case -1285004149:  // quantity
          return quantity;
        case -423406491:  // initialPrice
          return initialPrice;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BondFutureOptionTrade.Builder builder() {
      return new BondFutureOptionTrade.Builder();
    }

    @Override
    public Class<? extends BondFutureOptionTrade> beanType() {
      return BondFutureOptionTrade.class;
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
     * The meta-property for the {@code securityLink} property.
     * @return the meta-property, not null
     */
    public MetaProperty<SecurityLink<BondFutureOption>> securityLink() {
      return securityLink;
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
          return ((BondFutureOptionTrade) bean).getTradeInfo();
        case 807992154:  // securityLink
          return ((BondFutureOptionTrade) bean).getSecurityLink();
        case -1285004149:  // quantity
          return ((BondFutureOptionTrade) bean).getQuantity();
        case -423406491:  // initialPrice
          return ((BondFutureOptionTrade) bean).initialPrice;
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
   * The bean-builder for {@code BondFutureOptionTrade}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<BondFutureOptionTrade> {

    private TradeInfo tradeInfo;
    private SecurityLink<BondFutureOption> securityLink;
    private long quantity;
    private Double initialPrice;

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
    private Builder(BondFutureOptionTrade beanToCopy) {
      this.tradeInfo = beanToCopy.getTradeInfo();
      this.securityLink = beanToCopy.getSecurityLink();
      this.quantity = beanToCopy.getQuantity();
      this.initialPrice = beanToCopy.initialPrice;
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 752580658:  // tradeInfo
          return tradeInfo;
        case 807992154:  // securityLink
          return securityLink;
        case -1285004149:  // quantity
          return quantity;
        case -423406491:  // initialPrice
          return initialPrice;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 752580658:  // tradeInfo
          this.tradeInfo = (TradeInfo) newValue;
          break;
        case 807992154:  // securityLink
          this.securityLink = (SecurityLink<BondFutureOption>) newValue;
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
    public BondFutureOptionTrade build() {
      return new BondFutureOptionTrade(
          tradeInfo,
          securityLink,
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
     * Sets the link to the option that was traded.
     * <p>
     * This property returns a link to the security via a {@link StandardId}.
     * See {@link #getSecurity()} and {@link SecurityLink} for more details.
     * @param securityLink  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder securityLink(SecurityLink<BondFutureOption> securityLink) {
      JodaBeanUtils.notNull(securityLink, "securityLink");
      this.securityLink = securityLink;
      return this;
    }

    /**
     * Sets the quantity, indicating the number of option contracts in the trade.
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
     * Sets the initial price of the option, represented in decimal form.
     * <p>
     * This is the price agreed when the trade occurred.
     * This must be represented in decimal form, {@code (1.0 - decimalRate)}.
     * As such, the common market price of 99.3 for a 0.7% rate must be input as 0.993.
     * <p>
     * This property should be set if the option has daily margining.
     * @param initialPrice  the new value
     * @return this, for chaining, not null
     */
    public Builder initialPrice(Double initialPrice) {
      this.initialPrice = initialPrice;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(160);
      buf.append("BondFutureOptionTrade.Builder{");
      buf.append("tradeInfo").append('=').append(JodaBeanUtils.toString(tradeInfo)).append(',').append(' ');
      buf.append("securityLink").append('=').append(JodaBeanUtils.toString(securityLink)).append(',').append(' ');
      buf.append("quantity").append('=').append(JodaBeanUtils.toString(quantity)).append(',').append(' ');
      buf.append("initialPrice").append('=').append(JodaBeanUtils.toString(initialPrice));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}