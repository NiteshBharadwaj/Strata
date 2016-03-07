/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product;

import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.market.ReferenceData;
import com.opengamma.strata.product.etd.SecurityId;

/**
 * A single security that can be traded.
 * <p>
 * A security is one of the building blocks of finance, representing a fungible instrument that can be traded.
 * This is intended to cover instruments such as listed equities and futures.
 * It is intended that Over-The-Counter (OTC) instruments, such as an interest rate swap,
 * are embedded directly within the trade, rather than handled as one-off securities.
 * <p>
 * When referring to a security from another object, such as a trade, the reference
 * should normally be made using {@link SecurityId}.
 * When pricing, the identifier should be resolved using {@link ReferenceData}.
 * <p>
 * Implementations of this interface must be immutable beans.
 */
public interface SecurityProduct
    extends Product {

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
   * 
   * @return the security identifier
   */
  public abstract SecurityId getSecurityId();

  /**
   * Gets the currency that the security is traded in.
   * 
   * @return the trading currency
   */
  public abstract Currency getCurrency();

}
