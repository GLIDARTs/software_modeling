package sparebnb.model;

/**
 * Booking contract separate from what an accommodation is.
 * Other bookable things (for example a parking space) could implement this later
 * without extending {@link Accommodation}.
 */
public interface Bookable {

  /**
   * Attempts to book this item for the given guest and nights.
   *
   * @param user   the guest making the booking
   * @param nights number of nights (must be positive)
   * @throws AccommodationUnavailableException if booking is not allowed
   * @throws IllegalArgumentException if arguments are invalid
   */
  void book(User user, int nights);

  /**
   * Releases a previous booking so the item can move toward becoming available again.
   *
   * @throws IllegalStateException if release is not allowed in the current state
   */
  void release();

  /** @return true if a guest may book this item now */
  boolean isAvailable();
}
