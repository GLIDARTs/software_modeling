package sparebnb.model;

/**
 * Links one guest to one accommodation for a stay.
 * Stores real objects, not ids.
 */
public class Booking {

  private final String id;
  private final User user;
  private final Accommodation accommodation;
  private final int nights;
  private final double totalCost;
  private boolean active;

  /**
   * Creates a booking record.
   *
   * @param id unique booking id
   * @param user guest
   * @param accommodation place booked
   * @param nights number of nights
   * @param totalCost cost calculated via polymorphism
   */
  public Booking(String id, User user, Accommodation accommodation,
      int nights, double totalCost) {
    if (id == null || id.isBlank()) {
      throw new IllegalArgumentException("Booking id must not be empty");
    }
    if (user == null) {
      throw new IllegalArgumentException("User must not be null");
    }
    if (accommodation == null) {
      throw new IllegalArgumentException("Accommodation must not be null");
    }
    if (nights < 1) {
      throw new IllegalArgumentException("Nights must be at least 1");
    }
    if (totalCost < 0) {
      throw new IllegalArgumentException("Total cost must not be negative");
    }
    this.id = id.trim();
    this.user = user;
    this.accommodation = accommodation;
    this.nights = nights;
    this.totalCost = totalCost;
    this.active = true;
  }

  /**
   * Marks this booking cancelled. Package-private so only BookingService
   * can cancel as part of a coordinated release.
   */
  void cancel() {
    if (!active) {
      throw new IllegalStateException("Booking " + id + " is already cancelled");
    }
    active = false;
  }

  /** Returns the booking id. */
  public String getId() {
    return id;
  }

  /** Returns the guest. */
  public User getUser() {
    return user;
  }

  /** Returns the booked place. */
  public Accommodation getAccommodation() {
    return accommodation;
  }

  /** Returns the number of nights. */
  public int getNights() {
    return nights;
  }

  /** Returns the total cost. */
  public double getTotalCost() {
    return totalCost;
  }

  /** Returns true if the booking is still active. */
  public boolean isActive() {
    return active;
  }

  @Override
  public String toString() {
    return String.format("%s: %s booked %s for %d night(s) at GBP %.2f [%s]",
        id, user.getName(), accommodation.getName(), nights, totalCost,
        active ? "active" : "cancelled");
  }
}