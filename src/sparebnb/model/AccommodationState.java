package sparebnb.model;

/**
 * Behaviour of an accommodation in one lifecycle state
 * (Available, Booked, Cleaning).
 */
public interface AccommodationState {

  /**
   * Handles a book request for the given accommodation.
   *
   * @param accommodation the place whose state this is
   * @param user the guest trying to book
   * @param nights number of nights
   */
  void book(Accommodation accommodation, User user, int nights);

  /**
   * Handles a release/cancel request for the given accommodation.
   *
   * @param accommodation the place whose state this is
   */
  void release(Accommodation accommodation);

  /**
   * Handles finishing cleaning for the given accommodation.
   *
   * @param accommodation the place whose state this is
   */
  void finishCleaning(Accommodation accommodation);

  /** Returns true if guests may book in this state. */
  boolean isAvailable();

  /** Returns the display name such as Available, Booked, or Cleaning. */
  String getName();
}