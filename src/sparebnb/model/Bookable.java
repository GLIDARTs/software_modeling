package sparebnb.model;

/**
 * Marks something that can report whether it is free to book.
 * Booking and release stay package-private on Accommodation so only
 * BookingService (same package) can drive a full booking transaction.
 */
public interface Bookable {

  /** Returns true if a guest may book this item now. */
  boolean isAvailable();
}