/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product;

import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.market.ReferenceData;

/**
 * The product details of a financial instrument that is traded as a security.
 * <p>
 * A securitized product contains the structure of a financial instrument that is traded as a {@link Security}.
 * The product of a security is distinct from the security itself.
 * A {@link Security} contains details about itself, with any underlying securities
 * referred to by {@linkplain SecurityId identifier}.
 * By contrast, the product contains the full model for pricing, including underlying products.
 * <p>
 * For example, the securitized product of a bond future option directly contains all
 * the details of the future and the basket of bonds. Whereas, a bond future security
 * only contains details of the option and an identifier referring to the future.
 * <p>
 * Implementations of this interface must be immutable beans.
 */
public interface SecuritizedProduct
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
