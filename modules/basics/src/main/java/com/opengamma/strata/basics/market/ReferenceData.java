/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.basics.market;

import java.util.Map;
import java.util.Optional;

import com.opengamma.strata.basics.date.HolidayCalendar;
import com.opengamma.strata.basics.date.HolidayCalendarId;
import com.opengamma.strata.collect.Messages;

/**
 * Provides access to reference data, such as holiday calendars and securities.
 * <p>
 * Reference data is looked up using implementations of {@link ReferenceDataId}.
 * The identifier is parameterized with the type of the reference data to be returned.
 * <p>
 * The standard implementation is {@link ImmutableReferenceData}.
 */
public interface ReferenceData {

  /**
   * Obtains an instance from a map of reference data.
   * <p>
   * Each entry in the map is a unit of reference data, keyed by the matching identifier.
   * For example, a {@link HolidayCalendarId} associated with a {@link HolidayCalendar}.
   * The caller must ensure that the each entry in the map corresponds with the parameterized
   * type on the identifier.
   *
   * @param values  the reference data values
   * @return the reference data instance
   * @throws ClassCastException if a value does not match the parameterized type associated with the key
   */
  public static ReferenceData of(Map<? extends ReferenceDataId<?>, ?> values) {
    return ImmutableReferenceData.of(values);
  }

  /**
   * Obtains an instance of standard reference data.
   * <p>
   * Standard reference data is built into Strata and provides common holiday calendars and indices.
   * In most cases, production usage of Strata will not rely on this source of reference data.
   *
   * @return standard reference data
   */
  public static ReferenceData standard() {
    return StandardReferenceData.INSTANCE;
  }

  /**
   * Obtains an instance containing no reference data.
   *
   * @return empty reference data
   */
  public static ReferenceData empty() {
    return ImmutableReferenceData.empty();
  }

  //-------------------------------------------------------------------------
  /**
   * Checks if this reference data contains a value for the specified identifier.
   *
   * @param id  the identifier to find
   * @return true if the reference data contains a value for the identifier
   */
  public default boolean containsValue(ReferenceDataId<?> id) {
    return findValue(id).isPresent();
  }

  /**
   * Gets the reference data value associated with the specified identifier.
   * <p>
   * If this reference data instance contains the identifier, the value will be returned.
   * Otherwise, an exception will be thrown.
   *
   * @param <T>  the type of the reference data
   * @param id  the identifier to find
   * @return the reference data value
   * @throws ReferenceDataNotFoundException if the identifier is not found
   */
  public default <T> T getValue(ReferenceDataId<T> id) {
    return findValue(id)
        .orElseThrow(() -> new ReferenceDataNotFoundException(Messages.format(
            "Reference data not found for '{}' of type '{}'", id, id.getClass().getSimpleName())));
  }

  /**
   * Finds the reference data value associated with the specified identifier.
   * <p>
   * If this reference data instance contains the identifier, the value will be returned.
   * Otherwise, an empty optional will be returned.
   *
   * @param <T>  the type of the reference data
   * @param id  the identifier to find
   * @return the reference data value, empty if not found
   */
  public abstract <T> Optional<T> findValue(ReferenceDataId<T> id);

  //-------------------------------------------------------------------------
  /**
   * Combines this reference data with another.
   * <p>
   * The result combines both sets reference data.
   * Values are taken from this set of reference data if available, otherwise they are looked
   * up in the other set.
   * <p>
   * Therefore if the same ID is present in both sets of reference data the value in this object
   * will be returned.
   *
   * @param other  the other reference data
   * @return the combined reference data
   */
  public default ReferenceData combinedWith(ReferenceData other) {
    return new CombinedReferenceData(this, other);
  }

}
