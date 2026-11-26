package sparebnb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Control class that runs booking, release, and loyalty awarding.
 * Same package as the model so it can call package-private mutators.
 * Same role as the lecturer's EnrollmentControl example.
 *
 * <p>Money amounts are {@code double} for this educational prototype only.
 * Totals are rounded to two decimal places when a booking is created.
 */
public class BookingService {

  private static final int POINTS_PER_NIGHT = 10;

  private final List<Booking> bookings;
  private int nextBookingNumber;

  /** Creates an empty booking service. */
  public BookingService() {
    this.bookings = new ArrayList<>();
    this.nextBookingNumber = 1;
  }

  /**
   * Books an available place for a guest and awards loyalty points.
   *
   * <p>Cost is calculated before the state change. If anything fails after
   * the place becomes Booked, the place is rolled back to Available so the
   * catalogue is not left half-updated.
   *
   * @param user guest
   * @param accommodation place to book
   * @param nights number of nights
   * @return the new booking
   * @throws AccommodationUnavailableException if the place cannot be booked
   * @throws IllegalArgumentException if arguments are invalid
   */
  public Booking book(User user, Accommodation accommodation, int nights) {
    if (user == null) {
      throw new IllegalArgumentException("User must not be null");
    }
    if (accommodation == null) {
      throw new IllegalArgumentException("Accommodation must not be null");
    }
    Accommodation.requirePositiveNights(nights);

    // Fail fast on cost before touching availability state.
    double cost = roundMoney(accommodation.calculateCost(nights));
    accommodation.book(user, nights);

    try {
      Booking booking = new Booking(nextBookingId(), user, accommodation, nights, cost);
      bookings.add(booking);
      user.addBooking(booking);
      user.addPoints(POINTS_PER_NIGHT * nights);
      return booking;
    } catch (RuntimeException ex) {
      // Roll back Booked → Available if the booking record could not be completed.
      if ("Booked".equals(accommodation.getStatusName())) {
        accommodation.changeState(new AvailableState());
      }
      throw ex;
    }
  }

  /**
   * Cancels an active booking, releases the accommodation (to Cleaning),
   * and removes the loyalty points that were awarded for that booking.
   *
   * <p>Place release runs first. Cancel and point reclaim run only after a
   * successful release. Point reclaim never throws for a non-negative amount,
   * so an active booking is not left cancelled without reclaiming points.
   *
   * @param booking booking to cancel
   * @throws IllegalArgumentException if booking is null
   * @throws IllegalStateException if already cancelled or release not allowed
   */
  public void release(Booking booking) {
    if (booking == null) {
      throw new IllegalArgumentException("Booking must not be null");
    }
    if (!booking.isActive()) {
      throw new IllegalStateException(
          "Booking " + booking.getId() + " is already cancelled");
    }
    int pointsToRemove = POINTS_PER_NIGHT * booking.getNights();
    // Release place first so a failure leaves the booking still active.
    booking.getAccommodation().release();
    booking.cancel();
    booking.getUser().removePoints(pointsToRemove);
  }

  /** Returns an unmodifiable list of all bookings known to this service. */
  public List<Booking> getBookings() {
    return Collections.unmodifiableList(bookings);
  }

  private String nextBookingId() {
    return String.format("BK-%03d", nextBookingNumber++);
  }

  /** Rounds to two decimal places for display-stable GBP totals. */
  private static double roundMoney(double amount) {
    return Math.round(amount * 100.0) / 100.0;
  }
}
