/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.curve;

import java.io.Serializable;
import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectBean;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.id.UniqueId;

/**
 * Configuration object that contains information about how a curve is to be used (e.g. as a discounting curve,
 * as the curve for an overnight index).
 */
@BeanDefinition
public abstract class CurveTypeConfiguration extends DirectBean implements Serializable {

  /** Serialization version */
  private static final long serialVersionUID = 1L;

  /**
   * Constructor for the fudge builder
   */
  /* package */CurveTypeConfiguration() {
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code CurveTypeConfiguration}.
   * @return the meta-bean, not null
   */
  public static CurveTypeConfiguration.Meta meta() {
    return CurveTypeConfiguration.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(CurveTypeConfiguration.Meta.INSTANCE);
  }

  @Override
  public CurveTypeConfiguration.Meta metaBean() {
    return CurveTypeConfiguration.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    super.propertySet(propertyName, newValue, quiet);
  }

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

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code CurveTypeConfiguration}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null);

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    public BeanBuilder<? extends CurveTypeConfiguration> builder() {
      throw new UnsupportedOperationException("CurveTypeConfiguration is an abstract class");
    }

    @Override
    public Class<? extends CurveTypeConfiguration> beanType() {
      return CurveTypeConfiguration.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
