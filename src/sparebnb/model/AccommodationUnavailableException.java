package sparebnb.model;

/**
 * Thrown when a guest cannot book a place (already booked, being cleaned, etc.).
 */
public class AccommodationUnavailableException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Creates an exception with a guest-friendly message.
   *
   * @param message clear reason why booking failed
   */
  public AccommodationUnavailableException(String message) {
    super(message);
  }
}