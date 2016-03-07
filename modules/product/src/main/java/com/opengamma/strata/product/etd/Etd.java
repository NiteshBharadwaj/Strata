/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.etd;

import com.opengamma.strata.basics.currency.CurrencyAmount;
import com.opengamma.strata.collect.id.StandardId;

/**
 * An exchange-traded derivative contract (ETD).
 * <p>
 * An ETD is a security that is based on the value of another asset and that is traded on an exchange.
 * Derivatives are available on many different kinds of asset, such as equities, bonds, indices,
 * interest rates, currency and weather.
 * <p>
 * For example, the widely traded "CME Eurodollar futures contract" is based on the
 * USD Libor 3 month rate 'USD-LIBOR-3M' and is traded at CME.
 * <p>
 * Implementations of this interface must be immutable beans.
 */
public interface Etd
    extends Security {

  /**
   * Gets the base product identifier.
   * <p>
   * The identifier that is used for the base product, also known as the symbol.
   * Many derivatives, such as futures, expire monthly or quarterly, thus the product referred to here
   * is the base product of a series of contracts. A unique identifier for the contract is formed
   * by combining the base product with additional derivative-specific information.
   * For example, 'Eurex~FGBL' could be used to refer to the Euro-Bund base product at Eurex.
   * 
   * @return the product identifier
   */
  public abstract StandardId getProductId();

  /**
   * Gets the size of each contract.
   * <p>
   * The contract size is defined as a positive decimal number.
   * In many cases, the contract size will be one.
   * 
   * @return the contract size
   */
  public abstract double getContractSize();

  /**
   * Gets the size of each tick.
   * <p>
   * The tick size is defined as a positive decimal number.
   * If the tick size is 1/32, the tick size would be 0.03125.
   * 
   * @return the tick size
   */
  public abstract double getTickSize();

  /**
   * Gets the monetary value of each tick.
   * <p>
   * When the price changes by one tick, this amount is gained/lost.
   * 
   * @return the tick value
   */
  public abstract CurrencyAmount getTickValue();

  /**
   * Converts this ETD to the generic form.
   * <p>
   * A {@link GenericEtd} is the simplest form of ETD, suitable for mark-to-market pricing.
   * 
   * @return the equivalent generic ETD
   */
  public abstract GenericEtd toGenericEtd();

}
