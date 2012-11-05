/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.cds;

/**
 * Types of legacy CDS.
 */
public enum CDSType {
  /** Vanilla */
  VANILLA,
  /** Lock */
  RECOVERY_LOCK,
  /** Fixed recovery */
  FIXED_RECOVERY
}
