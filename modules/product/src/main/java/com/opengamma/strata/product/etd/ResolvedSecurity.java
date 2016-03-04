/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.etd;

import java.util.Optional;

import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.market.ReferenceData;
import com.opengamma.strata.collect.Messages;

/**
 * A security that has been resolved for pricing.
 * <p>
 * This is the resolved form of {@link Security}. Applications will typically create
 * a {@code ResolvedSecurity} from a {@code Security} using {@link ReferenceData}.
 * Where there is no action to take, the security and resolved security will be the same class.
 * <p>
 * Resolved objects may be bound to data that changes over time, such as holiday calendars.
 * If the data changes, such as the addition of a new holiday, the resolved form will not be updated.
 * Care must be taken when placing the resolved form in a cache or persistence layer.
 */
public interface ResolvedSecurity {

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

  /**
   * Gets additional information about the security.
   * <p>
   * This method obtains the specified additional information.
   * This allows additional information about a security to be obtained if available.
   * <p>
   * If the info is not found, an exception is thrown.
   * 
   * @param <T>  the type of the info
   * @param type  the type to find
   * @return the security information
   * @throws IllegalArgumentException if the information is not found
   */
  public default <T> T getInfo(SecurityInfoType<T> type) {
    return findInfo(type).orElseThrow(() -> new IllegalArgumentException(
        Messages.format("Security info not found for type '{}'", type)));
  }

  /**
   * Finds additional information about the security.
   * <p>
   * This method obtains the specified additional information.
   * This allows additional information about a security to be obtained if available.
   * <p>
   * If the info is not found, optional empty is returned.
   * 
   * @param <T>  the type of the info
   * @param type  the type to find
   * @return the security information
   */
  public abstract <T> Optional<T> findInfo(SecurityInfoType<T> type);

}
