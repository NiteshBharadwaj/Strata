/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.etd;

import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.collect.ImmutableMap;
import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.currency.CurrencyAmount;
import com.opengamma.strata.basics.market.ReferenceData;
import com.opengamma.strata.collect.ArgChecker;
import com.opengamma.strata.collect.id.StandardId;

/**
 * A generic exchange-traded derivative contract (ETD).
 * <p>
 * An ETD is a security that is based on the value of another asset and that is traded on an exchange.
 * Derivatives are available on many different kinds of asset, such as equities, bonds, indices,
 * interest rates, currency and weather. This implementation is the simplest definition of an ETD.
 */
@BeanDefinition
public final class GenericEtd
    implements Etd, ResolvedSecurity, ImmutableBean, Serializable {

  /**
   * The security identifier.
   * <p>
   * This identifier uniquely identifies the security within the system.
   * It is the key used to lookup the security in {@link ReferenceData}.
   * <p>
   * A real-world security will typically have multiple identifiers.
   * The only restriction placed on the identifier is that it is sufficiently
   * unique for the reference data lookup. As such, it is acceptable to use
   * an identifier from a well-known global or vendor symbology.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final SecurityId securityId;
  /**
   * The additional security information.
   * <p>
   * This stores additional information for the security.
   */
  @PropertyDefinition(validate = "notNull")
  private final ImmutableMap<SecurityInfoType<?>, Object> info;
  /**
   * The base product identifier.
   * <p>
   * The identifier that is used for the base product, also known as the symbol.
   * Many derivatives, such as futures, expire monthly or quarterly, thus the product referred to here
   * is the base product of a series of contracts. A unique identifier for the contract is formed
   * by combining the base product with additional derivative-specific information.
   * For example, 'Eurex~FGBL' could be used to refer to the Euro-Bund base product at Eurex.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final StandardId productId;
  /**
   * The size of each tick.
   * <p>
   * The tick size is defined as a positive decimal number.
   * If the tick size is 1/32, the tick size would be 0.03125.
   */
  @PropertyDefinition(validate = "ArgChecker.notNegativeOrZero", overrideGet = true)
  private final double tickSize;
  /**
   * The monetary value of one tick.
   * <p>
   * When the price changes by one tick, this amount is gained/lost.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final CurrencyAmount tickValue;

  //-----------------------------------------------------------------------
  /**
   * Gets the currency that the security is traded in.
   * <p>
   * The currency is derived from the tick value.
   * 
   * @return the currency
   */
  @Override
  public Currency getCurrency() {
    return tickValue.getCurrency();
  }

  @Override
  public <T> T getInfo(SecurityInfoType<T> type) {
    return ResolvedSecurity.super.getInfo(type);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> Optional<T> findInfo(SecurityInfoType<T> type) {
    return Optional.ofNullable((T) info.get(type));
  }

  @Override
  public GenericEtd toGenericEtd() {
    return this;
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code GenericEtd}.
   * @return the meta-bean, not null
   */
  public static GenericEtd.Meta meta() {
    return GenericEtd.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(GenericEtd.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static GenericEtd.Builder builder() {
    return new GenericEtd.Builder();
  }

  private GenericEtd(
      SecurityId securityId,
      Map<SecurityInfoType<?>, Object> info,
      StandardId productId,
      double tickSize,
      CurrencyAmount tickValue) {
    JodaBeanUtils.notNull(securityId, "securityId");
    JodaBeanUtils.notNull(info, "info");
    JodaBeanUtils.notNull(productId, "productId");
    ArgChecker.notNegativeOrZero(tickSize, "tickSize");
    JodaBeanUtils.notNull(tickValue, "tickValue");
    this.securityId = securityId;
    this.info = ImmutableMap.copyOf(info);
    this.productId = productId;
    this.tickSize = tickSize;
    this.tickValue = tickValue;
  }

  @Override
  public GenericEtd.Meta metaBean() {
    return GenericEtd.Meta.INSTANCE;
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
   * Gets the security identifier.
   * <p>
   * This identifier uniquely identifies the security within the system.
   * It is the key used to lookup the security in {@link ReferenceData}.
   * <p>
   * A real-world security will typically have multiple identifiers.
   * The only restriction placed on the identifier is that it is sufficiently
   * unique for the reference data lookup. As such, it is acceptable to use
   * an identifier from a well-known global or vendor symbology.
   * @return the value of the property, not null
   */
  @Override
  public SecurityId getSecurityId() {
    return securityId;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the additional security information.
   * <p>
   * This stores additional information for the security.
   * @return the value of the property, not null
   */
  public ImmutableMap<SecurityInfoType<?>, Object> getInfo() {
    return info;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the base product identifier.
   * <p>
   * The identifier that is used for the base product, also known as the symbol.
   * Many derivatives, such as futures, expire monthly or quarterly, thus the product referred to here
   * is the base product of a series of contracts. A unique identifier for the contract is formed
   * by combining the base product with additional derivative-specific information.
   * For example, 'Eurex~FGBL' could be used to refer to the Euro-Bund base product at Eurex.
   * @return the value of the property, not null
   */
  @Override
  public StandardId getProductId() {
    return productId;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the size of each tick.
   * <p>
   * The tick size is defined as a positive decimal number.
   * If the tick size is 1/32, the tick size would be 0.03125.
   * @return the value of the property
   */
  @Override
  public double getTickSize() {
    return tickSize;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the monetary value of one tick.
   * <p>
   * When the price changes by one tick, this amount is gained/lost.
   * @return the value of the property, not null
   */
  @Override
  public CurrencyAmount getTickValue() {
    return tickValue;
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
      GenericEtd other = (GenericEtd) obj;
      return JodaBeanUtils.equal(securityId, other.securityId) &&
          JodaBeanUtils.equal(info, other.info) &&
          JodaBeanUtils.equal(productId, other.productId) &&
          JodaBeanUtils.equal(tickSize, other.tickSize) &&
          JodaBeanUtils.equal(tickValue, other.tickValue);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(securityId);
    hash = hash * 31 + JodaBeanUtils.hashCode(info);
    hash = hash * 31 + JodaBeanUtils.hashCode(productId);
    hash = hash * 31 + JodaBeanUtils.hashCode(tickSize);
    hash = hash * 31 + JodaBeanUtils.hashCode(tickValue);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(192);
    buf.append("GenericEtd{");
    buf.append("securityId").append('=').append(securityId).append(',').append(' ');
    buf.append("info").append('=').append(info).append(',').append(' ');
    buf.append("productId").append('=').append(productId).append(',').append(' ');
    buf.append("tickSize").append('=').append(tickSize).append(',').append(' ');
    buf.append("tickValue").append('=').append(JodaBeanUtils.toString(tickValue));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code GenericEtd}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code securityId} property.
     */
    private final MetaProperty<SecurityId> securityId = DirectMetaProperty.ofImmutable(
        this, "securityId", GenericEtd.class, SecurityId.class);
    /**
     * The meta-property for the {@code info} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<ImmutableMap<SecurityInfoType<?>, Object>> info = DirectMetaProperty.ofImmutable(
        this, "info", GenericEtd.class, (Class) ImmutableMap.class);
    /**
     * The meta-property for the {@code productId} property.
     */
    private final MetaProperty<StandardId> productId = DirectMetaProperty.ofImmutable(
        this, "productId", GenericEtd.class, StandardId.class);
    /**
     * The meta-property for the {@code tickSize} property.
     */
    private final MetaProperty<Double> tickSize = DirectMetaProperty.ofImmutable(
        this, "tickSize", GenericEtd.class, Double.TYPE);
    /**
     * The meta-property for the {@code tickValue} property.
     */
    private final MetaProperty<CurrencyAmount> tickValue = DirectMetaProperty.ofImmutable(
        this, "tickValue", GenericEtd.class, CurrencyAmount.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "securityId",
        "info",
        "productId",
        "tickSize",
        "tickValue");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1574023291:  // securityId
          return securityId;
        case 3237038:  // info
          return info;
        case -1051830678:  // productId
          return productId;
        case 1936822078:  // tickSize
          return tickSize;
        case -85538348:  // tickValue
          return tickValue;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public GenericEtd.Builder builder() {
      return new GenericEtd.Builder();
    }

    @Override
    public Class<? extends GenericEtd> beanType() {
      return GenericEtd.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code securityId} property.
     * @return the meta-property, not null
     */
    public MetaProperty<SecurityId> securityId() {
      return securityId;
    }

    /**
     * The meta-property for the {@code info} property.
     * @return the meta-property, not null
     */
    public MetaProperty<ImmutableMap<SecurityInfoType<?>, Object>> info() {
      return info;
    }

    /**
     * The meta-property for the {@code productId} property.
     * @return the meta-property, not null
     */
    public MetaProperty<StandardId> productId() {
      return productId;
    }

    /**
     * The meta-property for the {@code tickSize} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Double> tickSize() {
      return tickSize;
    }

    /**
     * The meta-property for the {@code tickValue} property.
     * @return the meta-property, not null
     */
    public MetaProperty<CurrencyAmount> tickValue() {
      return tickValue;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1574023291:  // securityId
          return ((GenericEtd) bean).getSecurityId();
        case 3237038:  // info
          return ((GenericEtd) bean).getInfo();
        case -1051830678:  // productId
          return ((GenericEtd) bean).getProductId();
        case 1936822078:  // tickSize
          return ((GenericEtd) bean).getTickSize();
        case -85538348:  // tickValue
          return ((GenericEtd) bean).getTickValue();
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
   * The bean-builder for {@code GenericEtd}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<GenericEtd> {

    private SecurityId securityId;
    private Map<SecurityInfoType<?>, Object> info = ImmutableMap.of();
    private StandardId productId;
    private double tickSize;
    private CurrencyAmount tickValue;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(GenericEtd beanToCopy) {
      this.securityId = beanToCopy.getSecurityId();
      this.info = beanToCopy.getInfo();
      this.productId = beanToCopy.getProductId();
      this.tickSize = beanToCopy.getTickSize();
      this.tickValue = beanToCopy.getTickValue();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1574023291:  // securityId
          return securityId;
        case 3237038:  // info
          return info;
        case -1051830678:  // productId
          return productId;
        case 1936822078:  // tickSize
          return tickSize;
        case -85538348:  // tickValue
          return tickValue;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 1574023291:  // securityId
          this.securityId = (SecurityId) newValue;
          break;
        case 3237038:  // info
          this.info = (Map<SecurityInfoType<?>, Object>) newValue;
          break;
        case -1051830678:  // productId
          this.productId = (StandardId) newValue;
          break;
        case 1936822078:  // tickSize
          this.tickSize = (Double) newValue;
          break;
        case -85538348:  // tickValue
          this.tickValue = (CurrencyAmount) newValue;
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
    public GenericEtd build() {
      return new GenericEtd(
          securityId,
          info,
          productId,
          tickSize,
          tickValue);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the security identifier.
     * <p>
     * This identifier uniquely identifies the security within the system.
     * It is the key used to lookup the security in {@link ReferenceData}.
     * <p>
     * A real-world security will typically have multiple identifiers.
     * The only restriction placed on the identifier is that it is sufficiently
     * unique for the reference data lookup. As such, it is acceptable to use
     * an identifier from a well-known global or vendor symbology.
     * @param securityId  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder securityId(SecurityId securityId) {
      JodaBeanUtils.notNull(securityId, "securityId");
      this.securityId = securityId;
      return this;
    }

    /**
     * Sets the additional security information.
     * <p>
     * This stores additional information for the security.
     * @param info  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder info(Map<SecurityInfoType<?>, Object> info) {
      JodaBeanUtils.notNull(info, "info");
      this.info = info;
      return this;
    }

    /**
     * Sets the base product identifier.
     * <p>
     * The identifier that is used for the base product, also known as the symbol.
     * Many derivatives, such as futures, expire monthly or quarterly, thus the product referred to here
     * is the base product of a series of contracts. A unique identifier for the contract is formed
     * by combining the base product with additional derivative-specific information.
     * For example, 'Eurex~FGBL' could be used to refer to the Euro-Bund base product at Eurex.
     * @param productId  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder productId(StandardId productId) {
      JodaBeanUtils.notNull(productId, "productId");
      this.productId = productId;
      return this;
    }

    /**
     * Sets the size of each tick.
     * <p>
     * The tick size is defined as a positive decimal number.
     * If the tick size is 1/32, the tick size would be 0.03125.
     * @param tickSize  the new value
     * @return this, for chaining, not null
     */
    public Builder tickSize(double tickSize) {
      ArgChecker.notNegativeOrZero(tickSize, "tickSize");
      this.tickSize = tickSize;
      return this;
    }

    /**
     * Sets the monetary value of one tick.
     * <p>
     * When the price changes by one tick, this amount is gained/lost.
     * @param tickValue  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder tickValue(CurrencyAmount tickValue) {
      JodaBeanUtils.notNull(tickValue, "tickValue");
      this.tickValue = tickValue;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(192);
      buf.append("GenericEtd.Builder{");
      buf.append("securityId").append('=').append(JodaBeanUtils.toString(securityId)).append(',').append(' ');
      buf.append("info").append('=').append(JodaBeanUtils.toString(info)).append(',').append(' ');
      buf.append("productId").append('=').append(JodaBeanUtils.toString(productId)).append(',').append(' ');
      buf.append("tickSize").append('=').append(JodaBeanUtils.toString(tickSize)).append(',').append(' ');
      buf.append("tickValue").append('=').append(JodaBeanUtils.toString(tickValue));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
