package org.osmorc.inspection;

import javax.annotation.Nonnull;

/**
 * Result of a class/package availability check.
 *
 * @author <a href="janthomae@janthomae.de">Jan Thom&auml;</a>
 * @version $Id:$
 */
public class AvailabilityCheckResult {
  /**
   * Static result for OK checks, to avoid creating lots of objects.
   */
  public static final AvailabilityCheckResult OK = new AvailabilityCheckResult();

  private final ResultType myResult;
  private final String myDescription;

  public AvailabilityCheckResult() {
    this(ResultType.Ok, "");
  }

  public AvailabilityCheckResult(@Nonnull ResultType result, @Nonnull String description) {
    myResult = result;
    myDescription = description;
  }

  public boolean isOk() {
    return myResult == ResultType.Ok;
  }

  @Nonnull
  public String getDescription() {
    return myDescription;
  }

  @Nonnull
  public ResultType getResult() {
    return myResult;
  }

  public enum ResultType {
    SymbolIsNotImported,
    SymbolIsNotExported,
    Ok
  }
}
