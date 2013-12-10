/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.util.result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.opengamma.util.ArgumentChecker;

/**
 * {@link FunctionResult} indicating a failure with multiple underlying causes.
 * An example of this might be a function calling several other functions when there is no market data available,
 * causing them all to fail. If all underlying results have the same status this result will use that status.
 * If they have different statues the status of this result will be {@link FailureStatus#MULTIPLE}.
 */
@BeanDefinition
public class MultipleFailureFunctionResult<T> implements FunctionResult<T>, ImmutableBean {

  @PropertyDefinition(validate = "notNull")
  private final List<FunctionResult<?>> _failures;

  @PropertyDefinition(validate = "notNull")
  private final String _failureMessage;

  @PropertyDefinition(validate = "notNull")
  private final FailureStatus _status;

  /* package */ MultipleFailureFunctionResult(List<FunctionResult<?>> failures) {
    _failures = ImmutableList.copyOf(ArgumentChecker.notEmpty(failures, "failures"));
    List<String> messages = Lists.newArrayListWithCapacity(failures.size());
    FailureStatus compositeStatus = null;
    for (FunctionResult<?> failure : failures) {
      FailureStatus status = (FailureStatus) failure.getStatus();
      if (compositeStatus == null) {
        compositeStatus = status;
      } else if (compositeStatus != status) {
        compositeStatus = FailureStatus.MULTIPLE;
      }
      messages.add(failure.getFailureMessage());
    }
    _status = compositeStatus;
    _failureMessage = StringUtils.join(messages, "\n");
  }

  @Override
  public T getResult() {
    throw new IllegalStateException("Unable to get a value from a failure result");
  }

  @Override
  public boolean isResultAvailable() {
    return false;
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code MultipleFailureFunctionResult}.
   * @return the meta-bean, not null
   */
  @SuppressWarnings("rawtypes")
  public static MultipleFailureFunctionResult.Meta meta() {
    return MultipleFailureFunctionResult.Meta.INSTANCE;
  }

  /**
   * The meta-bean for {@code MultipleFailureFunctionResult}.
   * @param <R>  the bean's generic type
   * @param cls  the bean's generic type
   * @return the meta-bean, not null
   */
  @SuppressWarnings("unchecked")
  public static <R> MultipleFailureFunctionResult.Meta<R> metaMultipleFailureFunctionResult(Class<R> cls) {
    return MultipleFailureFunctionResult.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(MultipleFailureFunctionResult.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @param <T>  the type
   * @return the builder, not null
   */
  public static <T> MultipleFailureFunctionResult.Builder<T> builder() {
    return new MultipleFailureFunctionResult.Builder<T>();
  }

  /**
   * Restricted constructor.
   * @param builder  the builder to copy from, not null
   */
  protected MultipleFailureFunctionResult(MultipleFailureFunctionResult.Builder<T> builder) {
    JodaBeanUtils.notNull(builder._failures, "failures");
    JodaBeanUtils.notNull(builder._failureMessage, "failureMessage");
    JodaBeanUtils.notNull(builder._status, "status");
    this._failures = ImmutableList.copyOf(builder._failures);
    this._failureMessage = builder._failureMessage;
    this._status = builder._status;
  }

  @SuppressWarnings("unchecked")
  @Override
  public MultipleFailureFunctionResult.Meta<T> metaBean() {
    return MultipleFailureFunctionResult.Meta.INSTANCE;
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
   * Gets the failures.
   * @return the value of the property, not null
   */
  public List<FunctionResult<?>> getFailures() {
    return _failures;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the failureMessage.
   * @return the value of the property, not null
   */
  public String getFailureMessage() {
    return _failureMessage;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the status.
   * @return the value of the property, not null
   */
  public FailureStatus getStatus() {
    return _status;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder<T> toBuilder() {
    return new Builder<T>(this);
  }

  @Override
  public MultipleFailureFunctionResult<T> clone() {
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      MultipleFailureFunctionResult<?> other = (MultipleFailureFunctionResult<?>) obj;
      return JodaBeanUtils.equal(getFailures(), other.getFailures()) &&
          JodaBeanUtils.equal(getFailureMessage(), other.getFailureMessage()) &&
          JodaBeanUtils.equal(getStatus(), other.getStatus());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash += hash * 31 + JodaBeanUtils.hashCode(getFailures());
    hash += hash * 31 + JodaBeanUtils.hashCode(getFailureMessage());
    hash += hash * 31 + JodaBeanUtils.hashCode(getStatus());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(128);
    buf.append("MultipleFailureFunctionResult{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("failures").append('=').append(JodaBeanUtils.toString(getFailures())).append(',').append(' ');
    buf.append("failureMessage").append('=').append(JodaBeanUtils.toString(getFailureMessage())).append(',').append(' ');
    buf.append("status").append('=').append(JodaBeanUtils.toString(getStatus())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code MultipleFailureFunctionResult}.
   */
  public static class Meta<T> extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    @SuppressWarnings("rawtypes")
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code failures} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<List<FunctionResult<?>>> _failures = DirectMetaProperty.ofImmutable(
        this, "failures", MultipleFailureFunctionResult.class, (Class) List.class);
    /**
     * The meta-property for the {@code failureMessage} property.
     */
    private final MetaProperty<String> _failureMessage = DirectMetaProperty.ofImmutable(
        this, "failureMessage", MultipleFailureFunctionResult.class, String.class);
    /**
     * The meta-property for the {@code status} property.
     */
    private final MetaProperty<FailureStatus> _status = DirectMetaProperty.ofImmutable(
        this, "status", MultipleFailureFunctionResult.class, FailureStatus.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "failures",
        "failureMessage",
        "status");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 675938345:  // failures
          return _failures;
        case -1704954083:  // failureMessage
          return _failureMessage;
        case -892481550:  // status
          return _status;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public MultipleFailureFunctionResult.Builder<T> builder() {
      return new MultipleFailureFunctionResult.Builder<T>();
    }

    @SuppressWarnings({"unchecked", "rawtypes" })
    @Override
    public Class<? extends MultipleFailureFunctionResult<T>> beanType() {
      return (Class) MultipleFailureFunctionResult.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code failures} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<List<FunctionResult<?>>> failures() {
      return _failures;
    }

    /**
     * The meta-property for the {@code failureMessage} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> failureMessage() {
      return _failureMessage;
    }

    /**
     * The meta-property for the {@code status} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<FailureStatus> status() {
      return _status;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 675938345:  // failures
          return ((MultipleFailureFunctionResult<?>) bean).getFailures();
        case -1704954083:  // failureMessage
          return ((MultipleFailureFunctionResult<?>) bean).getFailureMessage();
        case -892481550:  // status
          return ((MultipleFailureFunctionResult<?>) bean).getStatus();
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
   * The bean-builder for {@code MultipleFailureFunctionResult}.
   */
  public static class Builder<T> extends DirectFieldsBeanBuilder<MultipleFailureFunctionResult<T>> {

    private List<FunctionResult<?>> _failures = new ArrayList<FunctionResult<?>>();
    private String _failureMessage;
    private FailureStatus _status;

    /**
     * Restricted constructor.
     */
    protected Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    protected Builder(MultipleFailureFunctionResult<T> beanToCopy) {
      this._failures = new ArrayList<FunctionResult<?>>(beanToCopy.getFailures());
      this._failureMessage = beanToCopy.getFailureMessage();
      this._status = beanToCopy.getStatus();
    }

    //-----------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    @Override
    public Builder<T> set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 675938345:  // failures
          this._failures = (List<FunctionResult<?>>) newValue;
          break;
        case -1704954083:  // failureMessage
          this._failureMessage = (String) newValue;
          break;
        case -892481550:  // status
          this._status = (FailureStatus) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder<T> set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder<T> setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    @Override
    public Builder<T> setString(MetaProperty<?> property, String value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder<T> setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public MultipleFailureFunctionResult<T> build() {
      return new MultipleFailureFunctionResult<T>(this);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the {@code failures} property in the builder.
     * @param failures  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder<T> failures(List<FunctionResult<?>> failures) {
      JodaBeanUtils.notNull(failures, "failures");
      this._failures = failures;
      return this;
    }

    /**
     * Sets the {@code failureMessage} property in the builder.
     * @param failureMessage  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder<T> failureMessage(String failureMessage) {
      JodaBeanUtils.notNull(failureMessage, "failureMessage");
      this._failureMessage = failureMessage;
      return this;
    }

    /**
     * Sets the {@code status} property in the builder.
     * @param status  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder<T> status(FailureStatus status) {
      JodaBeanUtils.notNull(status, "status");
      this._status = status;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(128);
      buf.append("MultipleFailureFunctionResult.Builder{");
      int len = buf.length();
      toString(buf);
      if (buf.length() > len) {
        buf.setLength(buf.length() - 2);
      }
      buf.append('}');
      return buf.toString();
    }

    protected void toString(StringBuilder buf) {
      buf.append("failures").append('=').append(JodaBeanUtils.toString(_failures)).append(',').append(' ');
      buf.append("failureMessage").append('=').append(JodaBeanUtils.toString(_failureMessage)).append(',').append(' ');
      buf.append("status").append('=').append(JodaBeanUtils.toString(_status)).append(',').append(' ');
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
