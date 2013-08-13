/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.component.factory.infrastructure;

import com.opengamma.component.ComponentInfo;
import com.opengamma.component.ComponentRepository;
import com.opengamma.component.factory.AbstractComponentFactory;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.management.ManagementService;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import org.springframework.context.Lifecycle;
import org.springframework.jmx.support.MBeanServerFactoryBean;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanServer;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Component Factory for a singleton MBeanServer.
 *
 * Because the MBeanServer is expected to run for the life of the VM and because
 * CacheManagers can come and go, there is proper lifecycle handling to clean up
 * instances of ManagementService associated with the CacheManagers to prevent
 * memory leaks.
 */
@BeanDefinition
public class MBeanServerComponentFactory extends AbstractComponentFactory {

  @PropertyDefinition(validate = "notNull")
  private String _classifier;

  @PropertyDefinition
  private CacheManager _cacheManager;

  @Override
  public void init(ComponentRepository repo, LinkedHashMap<String, String> configuration) throws Exception {

    final ComponentInfo info = new ComponentInfo(MBeanServer.class, getClassifier());
    final MBeanServer component = initMBeanServerAndRegisterCacheMananager(repo);
    repo.registerComponent(info, component);

  }

  protected final MBeanServer initMBeanServerAndRegisterCacheMananager(ComponentRepository repo) {

    MBeanServer mBeanServer = initMBeanServer();
    CacheManager cacheManager = getCacheManager();

    if (cacheManager != null) {

      ManagementService jmxService = new ManagementService(cacheManager, mBeanServer, true, true, true, true);
      repo.registerLifecycle(new CacheManagerLifecycle(jmxService));

    }

    return mBeanServer;

  }

  protected final MBeanServer initMBeanServer() {

    MBeanServerFactoryBean factoryBean = new MBeanServerFactoryBean();
    factoryBean.setLocateExistingServerIfPossible(true);
    factoryBean.afterPropertiesSet();

    return factoryBean.getObject();

  }

  static final class CacheManagerLifecycle implements Lifecycle {
    private ManagementService _jmxService;

    CacheManagerLifecycle(ManagementService jmxService) {
      _jmxService = jmxService;
    }

    @Override
    public void start() {
      try {
        _jmxService.init();
      } catch (CacheException ex) {
        if (ex.getCause() instanceof InstanceAlreadyExistsException == false) {
          throw ex;
        }
      }
    }

    @Override
    public void stop() {
      _jmxService.dispose();
      _jmxService = null;
    }

    @Override
    public boolean isRunning() {
      return _jmxService != null;
    }
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code MBeanServerComponentFactory}.
   * @return the meta-bean, not null
   */
  public static MBeanServerComponentFactory.Meta meta() {
    return MBeanServerComponentFactory.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(MBeanServerComponentFactory.Meta.INSTANCE);
  }

  @Override
  public MBeanServerComponentFactory.Meta metaBean() {
    return MBeanServerComponentFactory.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -281470431:  // classifier
        return getClassifier();
      case -1452875317:  // cacheManager
        return getCacheManager();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -281470431:  // classifier
        setClassifier((String) newValue);
        return;
      case -1452875317:  // cacheManager
        setCacheManager((CacheManager) newValue);
        return;
    }
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  protected void validate() {
    JodaBeanUtils.notNull(_classifier, "classifier");
    super.validate();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      MBeanServerComponentFactory other = (MBeanServerComponentFactory) obj;
      return JodaBeanUtils.equal(getClassifier(), other.getClassifier()) &&
          JodaBeanUtils.equal(getCacheManager(), other.getCacheManager()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getClassifier());
    hash += hash * 31 + JodaBeanUtils.hashCode(getCacheManager());
    return hash ^ super.hashCode();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the classifier.
   * @return the value of the property, not null
   */
  public String getClassifier() {
    return _classifier;
  }

  /**
   * Sets the classifier.
   * @param classifier  the new value of the property, not null
   */
  public void setClassifier(String classifier) {
    JodaBeanUtils.notNull(classifier, "classifier");
    this._classifier = classifier;
  }

  /**
   * Gets the the {@code classifier} property.
   * @return the property, not null
   */
  public final Property<String> classifier() {
    return metaBean().classifier().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the cacheManager.
   * @return the value of the property
   */
  public CacheManager getCacheManager() {
    return _cacheManager;
  }

  /**
   * Sets the cacheManager.
   * @param cacheManager  the new value of the property
   */
  public void setCacheManager(CacheManager cacheManager) {
    this._cacheManager = cacheManager;
  }

  /**
   * Gets the the {@code cacheManager} property.
   * @return the property, not null
   */
  public final Property<CacheManager> cacheManager() {
    return metaBean().cacheManager().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code MBeanServerComponentFactory}.
   */
  public static class Meta extends AbstractComponentFactory.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code classifier} property.
     */
    private final MetaProperty<String> _classifier = DirectMetaProperty.ofReadWrite(
        this, "classifier", MBeanServerComponentFactory.class, String.class);
    /**
     * The meta-property for the {@code cacheManager} property.
     */
    private final MetaProperty<CacheManager> _cacheManager = DirectMetaProperty.ofReadWrite(
        this, "cacheManager", MBeanServerComponentFactory.class, CacheManager.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "classifier",
        "cacheManager");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -281470431:  // classifier
          return _classifier;
        case -1452875317:  // cacheManager
          return _cacheManager;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends MBeanServerComponentFactory> builder() {
      return new DirectBeanBuilder<MBeanServerComponentFactory>(new MBeanServerComponentFactory());
    }

    @Override
    public Class<? extends MBeanServerComponentFactory> beanType() {
      return MBeanServerComponentFactory.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code classifier} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> classifier() {
      return _classifier;
    }

    /**
     * The meta-property for the {@code cacheManager} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<CacheManager> cacheManager() {
      return _cacheManager;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}