package sparebnb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A guest who can book accommodation and earn loyalty points.
 */
public class User {

  private final String id;
  private final String name;
  private final String email;
  private final List<Booking> bookings;
  private int loyaltyPoints;

  /**
   * Creates a guest with fictitious demo details.
   *
   * @param id unique id
   * @param name display name
   * @param email contact email
   */
  public User(String id, String name, String email) {
    if (id == null || id.isBlank()) {
      throw new IllegalArgumentException("User id must not be empty");
    }
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("User name must not be empty");
    }
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("User email must not be empty");
    }
    this.id = id.trim();
    this.name = name.trim();
    this.email = email.trim();
    this.bookings = new ArrayList<>();
    this.loyaltyPoints = 0;
  }

  /**
   * Adds a booking to this guest's list. Package-private for BookingService.
   *
   * @param booking booking to attach
   */
  void addBooking(Booking booking) {
    if (booking == null) {
      throw new IllegalArgumentException("Booking must not be null");
    }
    bookings.add(booking);
  }

  /**
   * Stores loyalty points awarded by BookingService. Package-private.
   *
   * @param points points to add (must be non-negative)
   */
  void addPoints(int points) {
    if (points < 0) {
      throw new IllegalArgumentException("Points to add must not be negative");
    }
    loyaltyPoints += points;
  }

  /**
   * Removes loyalty points when a booking is cancelled. Package-private.
   *
   * @param points points to remove (must be non-negative)
   */
  void removePoints(int points) {
    if (points < 0) {
      throw new IllegalArgumentException("Points to remove must not be negative");
    }
    loyaltyPoints -= points;
    if (loyaltyPoints < 0) {
      loyaltyPoints = 0;
    }
  }

  /** Returns the unique id. */
  public String getId() {
    return id;
  }

  /** Returns the guest name. */
  public String getName() {
    return name;
  }

  /** Returns the email address. */
  public String getEmail() {
    return email;
  }

  /** Returns an unmodifiable view of this guest's bookings. */
  public List<Booking> getBookings() {
    return Collections.unmodifiableList(bookings);
  }

  /** Returns the current loyalty points. */
  public int getLoyaltyPoints() {
    return loyaltyPoints;
  }
}