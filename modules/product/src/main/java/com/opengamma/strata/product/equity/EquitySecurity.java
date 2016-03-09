/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.equity;

import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.DerivedProperty;
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
import com.google.common.collect.ImmutableSet;
import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.market.ReferenceData;
import com.opengamma.strata.product.ReferenceSecurity;
import com.opengamma.strata.product.SecurityId;
import com.opengamma.strata.product.SecurityInfoType;
import com.opengamma.strata.product.TradeInfo;

/**
 * A security representing an equity share of a company.
 * <p>
 * An equity represents the concept of a single equity share of a company.
 * For example, a single share of OpenGamma.
 */
@BeanDefinition
public final class EquitySecurity
    implements ReferenceSecurity, ImmutableBean, Serializable {

  /**
   * The product, capturing the financial details of the contract.
   */
  @PropertyDefinition(validate = "notNull")
  private final Equity product;
  /**
   * The additional security information.
   * <p>
   * This stores additional information for the security.
   */
  @PropertyDefinition(validate = "notNull")
  private final ImmutableMap<SecurityInfoType<?>, Object> info;

  //-------------------------------------------------------------------------
  @Override
  @DerivedProperty
  public SecurityId getSecurityId() {
    return product.getSecurityId();
  }

  @Override
  @DerivedProperty
  public Currency getCurrency() {
    return product.getCurrency();
  }

  @Override
  public ImmutableSet<SecurityId> getUnderlyingIds() {
    return ImmutableSet.of();
  }

  //-------------------------------------------------------------------------
  @SuppressWarnings("unchecked")
  @Override
  public <T> Optional<T> findInfo(SecurityInfoType<T> type) {
    return Optional.ofNullable((T) info.get(type));
  }

  @Override
  public Equity createProduct(ReferenceData refData) {
    return product;
  }

  @Override
  public EquityTrade createTrade(TradeInfo tradeInfo, long quantity, double tradePrice, ReferenceData refData) {
    return new EquityTrade(tradeInfo, product, quantity, tradePrice);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code EquitySecurity}.
   * @return the meta-bean, not null
   */
  public static EquitySecurity.Meta meta() {
    return EquitySecurity.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(EquitySecurity.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static EquitySecurity.Builder builder() {
    return new EquitySecurity.Builder();
  }

  private EquitySecurity(
      Equity product,
      Map<SecurityInfoType<?>, Object> info) {
    JodaBeanUtils.notNull(product, "product");
    JodaBeanUtils.notNull(info, "info");
    this.product = product;
    this.info = ImmutableMap.copyOf(info);
  }

  @Override
  public EquitySecurity.Meta metaBean() {
    return EquitySecurity.Meta.INSTANCE;
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
   * Gets the product, capturing the financial details of the contract.
   * @return the value of the property, not null
   */
  public Equity getProduct() {
    return product;
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
      EquitySecurity other = (EquitySecurity) obj;
      return JodaBeanUtils.equal(product, other.product) &&
          JodaBeanUtils.equal(info, other.info);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(product);
    hash = hash * 31 + JodaBeanUtils.hashCode(info);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(160);
    buf.append("EquitySecurity{");
    buf.append("product").append('=').append(product).append(',').append(' ');
    buf.append("info").append('=').append(info).append(',').append(' ');
    buf.append("securityId").append('=').append(getSecurityId()).append(',').append(' ');
    buf.append("currency").append('=').append(JodaBeanUtils.toString(getCurrency()));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code EquitySecurity}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code product} property.
     */
    private final MetaProperty<Equity> product = DirectMetaProperty.ofImmutable(
        this, "product", EquitySecurity.class, Equity.class);
    /**
     * The meta-property for the {@code info} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<ImmutableMap<SecurityInfoType<?>, Object>> info = DirectMetaProperty.ofImmutable(
        this, "info", EquitySecurity.class, (Class) ImmutableMap.class);
    /**
     * The meta-property for the {@code securityId} property.
     */
    private final MetaProperty<SecurityId> securityId = DirectMetaProperty.ofDerived(
        this, "securityId", EquitySecurity.class, SecurityId.class);
    /**
     * The meta-property for the {@code currency} property.
     */
    private final MetaProperty<Currency> currency = DirectMetaProperty.ofDerived(
        this, "currency", EquitySecurity.class, Currency.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "product",
        "info",
        "securityId",
        "currency");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -309474065:  // product
          return product;
        case 3237038:  // info
          return info;
        case 1574023291:  // securityId
          return securityId;
        case 575402001:  // currency
          return currency;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public EquitySecurity.Builder builder() {
      return new EquitySecurity.Builder();
    }

    @Override
    public Class<? extends EquitySecurity> beanType() {
      return EquitySecurity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code product} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Equity> product() {
      return product;
    }

    /**
     * The meta-property for the {@code info} property.
     * @return the meta-property, not null
     */
    public MetaProperty<ImmutableMap<SecurityInfoType<?>, Object>> info() {
      return info;
    }

    /**
     * The meta-property for the {@code securityId} property.
     * @return the meta-property, not null
     */
    public MetaProperty<SecurityId> securityId() {
      return securityId;
    }

    /**
     * The meta-property for the {@code currency} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Currency> currency() {
      return currency;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -309474065:  // product
          return ((EquitySecurity) bean).getProduct();
        case 3237038:  // info
          return ((EquitySecurity) bean).getInfo();
        case 1574023291:  // securityId
          return ((EquitySecurity) bean).getSecurityId();
        case 575402001:  // currency
          return ((EquitySecurity) bean).getCurrency();
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
   * The bean-builder for {@code EquitySecurity}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<EquitySecurity> {

    private Equity product;
    private Map<SecurityInfoType<?>, Object> info = ImmutableMap.of();

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(EquitySecurity beanToCopy) {
      this.product = beanToCopy.getProduct();
      this.info = beanToCopy.getInfo();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case -309474065:  // product
          return product;
        case 3237038:  // info
          return info;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case -309474065:  // product
          this.product = (Equity) newValue;
          break;
        case 3237038:  // info
          this.info = (Map<SecurityInfoType<?>, Object>) newValue;
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
    public EquitySecurity build() {
      return new EquitySecurity(
          product,
          info);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the product, capturing the financial details of the contract.
     * @param product  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder product(Equity product) {
      JodaBeanUtils.notNull(product, "product");
      this.product = product;
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

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(96);
      buf.append("EquitySecurity.Builder{");
      buf.append("product").append('=').append(JodaBeanUtils.toString(product)).append(',').append(' ');
      buf.append("info").append('=').append(JodaBeanUtils.toString(info));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
