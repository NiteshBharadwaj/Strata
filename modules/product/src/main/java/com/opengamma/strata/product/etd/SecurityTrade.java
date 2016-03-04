/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.etd;

import com.opengamma.strata.basics.Trade;
import com.opengamma.strata.basics.market.ReferenceData;
import com.opengamma.strata.basics.market.ReferenceDataNotFoundException;
import com.opengamma.strata.product.FinanceTrade;

/**
 * A trade that is based on a security.
 * <p>
 * A security trade is a {@link Trade} that contains a security identifier.
 * The identifier can be resolved using {@link ReferenceData} to a {@link Security}.
 * <p>
 * Implementations of this interface must be immutable beans.
 */
public interface SecurityTrade
    extends FinanceTrade {

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
   * Resolves the security using the specified reference data.
   * <p>
   * This uses the reference data to resolve the security.
   * Implementations should check and cast the result from the {@link ReferenceData} lookup.
   * 
   * @param refData  the reference data to use when resolving
   * @return the resolved instance
   * @throws ReferenceDataNotFoundException if an identifier cannot be resolved in the reference data
   * @throws ClassCastException if the security identifier resolves to the wrong type,
   *  which may occur if implementations override this method
   */
  public abstract Security resolveSecurity(ReferenceData refData);

}
