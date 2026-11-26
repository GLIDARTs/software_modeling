package sparebnb.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import sparebnb.model.Accommodation;
import sparebnb.model.AccommodationUnavailableException;
import sparebnb.model.Booking;
import sparebnb.model.User;

/**
 * Control class that runs booking, release, and loyalty awarding.
 * Same role as the lecturer's EnrollmentControl example.
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
   * @param user          guest
   * @param accommodation place to book
   * @param nights        number of nights
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
    if (nights < 1) {
      throw new IllegalArgumentException("Nights must be at least 1");
    }

    // May throw AccommodationUnavailableException (already booked / cleaning)
    accommodation.book(user, nights);

    double cost = accommodation.calculateCost(nights);
    Booking booking = new Booking(nextBookingId(), user, accommodation, nights, cost);
    bookings.add(booking);
    user.addBooking(booking);
    user.addPoints(POINTS_PER_NIGHT * nights);
    return booking;
  }

  /**
   * Cancels an active booking, releases the accommodation (to Cleaning),
   * and removes the loyalty points that were awarded for that booking.
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
    booking.cancel();
    booking.getAccommodation().release();
    booking.getUser().removePoints(POINTS_PER_NIGHT * booking.getNights());
  }

  /** @return unmodifiable list of all bookings known to this service */
  public List<Booking> getBookings() {
    return Collections.unmodifiableList(bookings);
  }

  private String nextBookingId() {
    return String.format("BK-%03d", nextBookingNumber++);
  }
}
