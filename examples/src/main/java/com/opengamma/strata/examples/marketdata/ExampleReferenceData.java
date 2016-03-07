/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.strata.examples.marketdata;

import org.joda.beans.ser.JodaBeanSer;

import com.opengamma.strata.basics.market.ImmutableReferenceData;
import com.opengamma.strata.basics.market.ReferenceData;
import com.opengamma.strata.collect.Unchecked;
import com.opengamma.strata.collect.io.ResourceLocator;

/**
 * Contains utilities for using example reference data.
 */
public final class ExampleReferenceData {

  /**
   * Reference data file.
   */
  private static final String EXAMPLE_REF_DATA = "example-refdata/refData.xml";

  /**
   * Restricted constructor.
   */
  private ExampleReferenceData() {
  }

  //-------------------------------------------------------------------------
  /**
   * Obtains example reference data, including holidays and securities.
   * 
   * @return the reference data
   */
  public static ReferenceData refData() {
    return Unchecked.wrap(() -> {
      ResourceLocator locator = ResourceLocator.ofClasspath(EXAMPLE_REF_DATA);
      String content = locator.getCharSource().read();
      ReferenceData securities = JodaBeanSer.PRETTY.xmlReader().read(content, ImmutableReferenceData.class);
      return ReferenceData.standard().combinedWith(securities);
    });
  }

}
