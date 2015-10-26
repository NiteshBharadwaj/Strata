/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.market.curve.definition;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaBean;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.light.LightMetaBean;

import com.opengamma.strata.basics.Trade;

/**
 * Dummy trade.
 * Based on a FRA.
 */
@BeanDefinition(style = "light")
public final class DummyFraTrade
    implements Trade, ImmutableBean, Serializable {

  @PropertyDefinition(validate = "notNull")
  private final LocalDate date;
  @PropertyDefinition
  private final double fixedRate;

  public static DummyFraTrade of(LocalDate date, double fixedRate) {
    return new DummyFraTrade(date, fixedRate);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code DummyFraTrade}.
   */
  private static MetaBean META_BEAN = LightMetaBean.of(DummyFraTrade.class);

  /**
   * The meta-bean for {@code DummyFraTrade}.
   * @return the meta-bean, not null
   */
  public static MetaBean meta() {
    return META_BEAN;
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  private DummyFraTrade(
      LocalDate date,
      double fixedRate) {
    JodaBeanUtils.notNull(date, "date");
    this.date = date;
    this.fixedRate = fixedRate;
  }

  @Override
  public MetaBean metaBean() {
    return META_BEAN;
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
   * Gets the date.
   * @return the value of the property, not null
   */
  public LocalDate getDate() {
    return date;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the fixedRate.
   * @return the value of the property
   */
  public double getFixedRate() {
    return fixedRate;
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      DummyFraTrade other = (DummyFraTrade) obj;
      return JodaBeanUtils.equal(getDate(), other.getDate()) &&
          JodaBeanUtils.equal(getFixedRate(), other.getFixedRate());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getDate());
    hash = hash * 31 + JodaBeanUtils.hashCode(getFixedRate());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("DummyFraTrade{");
    buf.append("date").append('=').append(getDate()).append(',').append(' ');
    buf.append("fixedRate").append('=').append(JodaBeanUtils.toString(getFixedRate()));
    buf.append('}');
    return buf.toString();
  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
