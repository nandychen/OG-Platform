/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.marketdata.manipulator.dsl;

import java.io.StringReader;
import java.util.Map;

import org.fudgemsg.FudgeMsg;
import org.fudgemsg.MutableFudgeMsg;
import org.fudgemsg.mapping.FudgeDeserializer;
import org.fudgemsg.mapping.FudgeSerializer;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.ImmutableConstructor;
import org.joda.beans.PropertyDefinition;

import com.opengamma.core.config.Config;
import com.opengamma.engine.marketdata.manipulator.ScenarioDefinition;
import com.opengamma.engine.marketdata.manipulator.ScenarioDefinitionFactory;
import com.opengamma.util.ArgumentChecker;
import java.util.NoSuchElementException;
import java.util.Set;
import org.joda.beans.Bean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.BasicImmutableBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

/**
 * Configuration object containing a script to define a scenario.
 * The script is written in the Groovy-based scenario DSL.
 */
@Config(description = "Scenario DSL script")
@BeanDefinition
public final class ScenarioDslScript implements ImmutableBean, ScenarioDefinitionFactory {

  /** Field name for Fudge message */
  private static final String SCRIPT = "script";

  /**
   * The script text.
   */
  @PropertyDefinition(get = "private")
  private final String _script;

  //-------------------------------------------------------------------------
  /**
   * Obtains an instance of {@code ScenarioDslScript}.
   * 
   * @param script  the script, not null
   * @return the script, not null
   */
  public static ScenarioDslScript of(String script) {
    return new ScenarioDslScript(script);
  }

  //-------------------------------------------------------------------------
  /**
   * Creates an instance.
   * 
   * @param script  the script, not null
   */
  @ImmutableConstructor
  private ScenarioDslScript(String script) {
    ArgumentChecker.notEmpty(script, "script");
    _script = script;
  }

  //-------------------------------------------------------------------------
  @Override
  public ScenarioDefinition create(Map<String, Object> parameters) {
    return SimulationUtils.createScenarioFromDsl(new StringReader(_script), parameters).createDefinition();
  }

  //-------------------------------------------------------------------------
  public MutableFudgeMsg toFudgeMsg(final FudgeSerializer serializer) {
    MutableFudgeMsg msg = serializer.newMessage();
    serializer.addToMessage(msg, SCRIPT, null, _script);
    return msg;
  }

  public static ScenarioDslScript fromFudgeMsg(final FudgeDeserializer deserializer, final FudgeMsg msg) {
    String script = deserializer.fieldValueToObject(String.class, msg.getByName(SCRIPT));
    return new ScenarioDslScript(script);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code ScenarioDslScript}.
   * @return the meta-bean, not null
   */
  public static ScenarioDslScript.Meta meta() {
    return ScenarioDslScript.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(ScenarioDslScript.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   *
   * @return the builder, not null
   */
  public static ScenarioDslScript.Builder builder() {
    return new ScenarioDslScript.Builder();
  }

  @Override
  public ScenarioDslScript.Meta metaBean() {
    return ScenarioDslScript.Meta.INSTANCE;
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
   * Gets the script text.
   * @return the value of the property
   */
  private String getScript() {
    return _script;
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
  public ScenarioDslScript clone() {
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      ScenarioDslScript other = (ScenarioDslScript) obj;
      return JodaBeanUtils.equal(getScript(), other.getScript());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash += hash * 31 + JodaBeanUtils.hashCode(getScript());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("ScenarioDslScript{");
    buf.append("script").append('=').append(getScript());
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code ScenarioDslScript}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code script} property.
     */
    private final MetaProperty<String> _script = DirectMetaProperty.ofImmutable(
        this, "script", ScenarioDslScript.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "script");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -907685685:  // script
          return _script;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public ScenarioDslScript.Builder builder() {
      return new ScenarioDslScript.Builder();
    }

    @Override
    public Class<? extends ScenarioDslScript> beanType() {
      return ScenarioDslScript.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code script} property.
     * @return the meta-property, not null
     */
    public MetaProperty<String> script() {
      return _script;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -907685685:  // script
          return ((ScenarioDslScript) bean).getScript();
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
   * The bean-builder for {@code ScenarioDslScript}.
   */
  public static final class Builder extends BasicImmutableBeanBuilder<ScenarioDslScript> {

    private String _script;

    /**
     * Restricted constructor.
     */
    private Builder() {
      super(ScenarioDslScript.Meta.INSTANCE);
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(ScenarioDslScript beanToCopy) {
      super(ScenarioDslScript.Meta.INSTANCE);
      this._script = beanToCopy.getScript();
    }

    //-----------------------------------------------------------------------
    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case -907685685:  // script
          this._script = (String) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public ScenarioDslScript build() {
      return new ScenarioDslScript(
          _script);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the {@code script} property in the builder.
     * @param script  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder script(String script) {
      this._script = script;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(64);
      buf.append("ScenarioDslScript.Builder{");
      buf.append("script").append('=').append(_script);
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}

