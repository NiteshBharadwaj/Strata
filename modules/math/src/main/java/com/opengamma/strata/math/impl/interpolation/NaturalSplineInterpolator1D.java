/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.math.impl.interpolation;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.ImmutableConstructor;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

/**
 * Interpolator which uses {@link NaturalSplineInterpolator} to perform the interpolation.
 */
@BeanDefinition
public final class NaturalSplineInterpolator1D extends PiecewisePolynomialInterpolator1D implements ImmutableBean {

  /**
   * Default constructor where the interpolation method is fixed to be {@link NaturalSplineInterpolator}.
   */
  @ImmutableConstructor
  public NaturalSplineInterpolator1D() {
    super(new NaturalSplineInterpolator());
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code NaturalSplineInterpolator1D}.
   * @return the meta-bean, not null
   */
  public static NaturalSplineInterpolator1D.Meta meta() {
    return NaturalSplineInterpolator1D.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(NaturalSplineInterpolator1D.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static NaturalSplineInterpolator1D.Builder builder() {
    return new NaturalSplineInterpolator1D.Builder();
  }

  @Override
  public NaturalSplineInterpolator1D.Meta metaBean() {
    return NaturalSplineInterpolator1D.Meta.INSTANCE;
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
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      return true;
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(32);
    buf.append("NaturalSplineInterpolator1D{");
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code NaturalSplineInterpolator1D}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null);

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    public NaturalSplineInterpolator1D.Builder builder() {
      return new NaturalSplineInterpolator1D.Builder();
    }

    @Override
    public Class<? extends NaturalSplineInterpolator1D> beanType() {
      return NaturalSplineInterpolator1D.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code NaturalSplineInterpolator1D}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<NaturalSplineInterpolator1D> {

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      throw new NoSuchElementException("Unknown property: " + propertyName);
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      throw new NoSuchElementException("Unknown property: " + propertyName);
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
    public NaturalSplineInterpolator1D build() {
      return new NaturalSplineInterpolator1D();
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      return "NaturalSplineInterpolator1D.Builder{}";
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
