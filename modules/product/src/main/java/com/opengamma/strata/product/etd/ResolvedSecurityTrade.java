/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.etd;

import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.product.TradeInfo;

/**
 * A trade in a security that has been resolved for pricing.
 * <p>
 * Resolved trades are the primary input to pricers.
 * <p>
 * Resolved objects may be bound to data that changes over time, such as holiday calendars.
 * If the data changes, such as the addition of a new holiday, the resolved form will not be updated.
 * Care must be taken when placing the resolved form in a cache or persistence layer.
 */
public interface ResolvedSecurityTrade {

  /**
   * The additional trade information.
   * <p>
   * This allows additional information to be attached to the trade.
   * 
   * @return the additional trade info
   */
  public abstract TradeInfo getTradeInfo();

  /**
   * Gets the security that has been traded.
   * <p>
   * The security is the fungible instrument.
   * 
   * @return the security
   */
  public abstract ResolvedSecurity getSecurity();

  /**
   * Gets the currency of the trade.
   * 
   * @return the currency
   */
  public default Currency getCurrency() {
    return getSecurity().getCurrency();
  }

}
