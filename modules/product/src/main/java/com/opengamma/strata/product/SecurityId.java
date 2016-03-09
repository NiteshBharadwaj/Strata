/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.strata.product;

import java.io.Serializable;

import org.joda.beans.PropertyDefinition;
import org.joda.convert.FromString;
import org.joda.convert.ToString;

import com.opengamma.strata.basics.market.ReferenceData;
import com.opengamma.strata.basics.market.ReferenceDataId;
import com.opengamma.strata.basics.market.ReferenceDataNotFoundException;
import com.opengamma.strata.basics.market.Resolvable;
import com.opengamma.strata.collect.ArgChecker;
import com.opengamma.strata.collect.id.StandardId;

/**
 * An identifier for a security.
 * <p>
 * This identifier is used to obtain a {@link ReferenceSecurity} from {@link ReferenceData}.
 * <p>
 * A security identifier uniquely identifies a security within the system.
 * A real-world security will typically have multiple identifiers.
 * The only restriction placed on the identifier is that it is sufficiently
 * unique for the reference data lookup. As such, it is acceptable to use
 * an identifier from a well-known global or vendor symbology.
 */
public final class SecurityId
    implements ReferenceDataId<ReferenceSecurity>, Resolvable<ReferenceSecurity>, Serializable {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The identifier, expressed as a standard two-part identifier.
   */
  @PropertyDefinition(validate = "notNull")
  private final StandardId standardId;

  //-------------------------------------------------------------------------
  /**
   * Obtains an instance from a scheme and value.
   * <p>
   * The scheme must be non-empty and match the regular expression '{@code [A-Za-z0-9:/+.=_-]*}'.
   * If necessary, the scheme can be encoded using {@link StandardId#encodeScheme(String)}.
   * <p>
   * The value must be non-empty and match the regular expression '{@code [!-z][ -z]*}'.
   *
   * @param scheme  the scheme of the identifier, not empty
   * @param value  the value of the identifier, not empty
   * @return the security identifier
   * @throws IllegalArgumentException if the scheme or value is invalid
   */
  public static SecurityId of(String scheme, String value) {
    return of(StandardId.of(scheme, value));
  }

  /**
   * Creates an instance from a standard two-part identifier.
   * <p>
   * This parses the identifier from the form produced by {@code toString()}
   * which is '{@code $scheme~$value}'.
   *
   * @param standardId  the underlying standard two-part identifier
   * @return the security identifier
   */
  public static SecurityId of(StandardId standardId) {
    return new SecurityId(standardId);
  }

  /**
   * Parses an {@code StandardId} from a formatted scheme and value.
   * <p>
   * This parses the identifier from the form produced by {@code toString()}
   * which is '{@code $scheme~$value}'.
   *
   * @param str  the identifier to parse
   * @return the security identifier
   * @throws IllegalArgumentException if the identifier cannot be parsed
   */
  @FromString
  public static SecurityId parse(String str) {
    return new SecurityId(StandardId.parse(str));
  }

  // creates an identifier for a combined calendar
  private SecurityId(StandardId standardId) {
    this.standardId = ArgChecker.notNull(standardId, "standardId");
  }

  // resolve after deserialization
  private Object readResolve() {
    return of(standardId);
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the standard two-part identifier.
   * 
   * @return the standard two-part identifier
   */
  public StandardId getStandardId() {
    return standardId;
  }

  /**
   * Gets the type of data this identifier refers to.
   * <p>
   * A {@code SecurityId} refers to a {@code ReferenceSecurity}.
   *
   * @return the type of the reference data this identifier refers to
   */
  @Override
  public Class<ReferenceSecurity> getReferenceDataType() {
    return ReferenceSecurity.class;
  }

  //-------------------------------------------------------------------------
  /**
   * Resolves this identifier to a security using the specified reference data.
   * <p>
   * This returns an instance of {@link ReferenceSecurity}.
   * <p>
   * Resolved objects may be bound to data that changes over time.
   * If the data changes, the resolved form will not be updated.
   * Care must be taken when placing the resolved form in a cache or persistence layer.
   * 
   * @param refData  the reference data, used to resolve the reference
   * @return the resolved security
   * @throws ReferenceDataNotFoundException if the identifier is not found
   */
  @Override
  public ReferenceSecurity resolve(ReferenceData refData) {
    return refData.getValue(this);
  }

  /**
   * Resolves this identifier to a security using the specified reference data and type.
   * <p>
   * This returns an instance of {@link ReferenceSecurity} cast to the specified type.
   * <p>
   * Resolved objects may be bound to data that changes over time.
   * If the data changes, the resolved form will not be updated.
   * Care must be taken when placing the resolved form in a cache or persistence layer.
   * 
   * @param <T>  the type of the security
   * @param refData  the reference data, used to resolve the reference
   * @param securityType  the type of the security
   * @return the resolved security
   * @throws ReferenceDataNotFoundException if the identifier is not found
   * @throws ClassCastException if the identifier exists, but is not of the correct type
   */
  public <T extends ReferenceSecurity> T resolve(ReferenceData refData, Class<T> securityType) {
    return securityType.cast(refData.getValue(this));
  }

  //-------------------------------------------------------------------------
  /**
   * Checks if this identifier equals another identifier.
   * <p>
   * The comparison checks the name.
   * 
   * @param obj  the other identifier, null returns false
   * @return true if equal
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof SecurityId) {
      return standardId.equals(((SecurityId) obj).standardId);
    }
    return false;
  }

  /**
   * Returns a suitable hash code for the identifier.
   * 
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return standardId.hashCode() + 7;
  }

  /**
   * Returns the identifier in a standard string format.
   * <p>
   * The returned string is in the form '{@code $scheme~$value}'.
   * This is suitable for use with {@link #parse(String)}.
   * 
   * @return a parsable representation of the identifier
   */
  @ToString
  @Override
  public String toString() {
    return standardId.toString();
  }

}
