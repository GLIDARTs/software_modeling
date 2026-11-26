package sparebnb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base type for every kind of place a guest can book.
 * Abstract because a generic accommodation should never be created.
 *
 * <p>Prices use {@code double} for this educational prototype only.
 * Production code would use a money type (for example minor units or
 * {@code BigDecimal}). {@link BookingService} rounds totals to two decimals
 * when a booking is stored.
 */
public abstract class Accommodation implements Bookable {

  private final String id;
  private final String name;
  private final String location;
  private final double pricePerNight;
  private final int maxGuests;
  private final List<Facility> facilities;
  private AccommodationState state;

  /**
   * Creates an accommodation with shared fields.
   *
   * @param id unique id
   * @param name display name
   * @param location city or area
   * @param pricePerNight price greater than zero
   * @param maxGuests maximum guests (at least 1)
   * @param facilities facilities to aggregate (may be empty)
   */
  protected Accommodation(String id, String name, String location,
      double pricePerNight, int maxGuests, List<Facility> facilities) {
    if (id == null || id.isBlank()) {
      throw new IllegalArgumentException("Id must not be empty");
    }
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name must not be empty");
    }
    if (location == null || location.isBlank()) {
      throw new IllegalArgumentException("Location must not be empty");
    }
    if (pricePerNight <= 0) {
      throw new IllegalArgumentException("Price per night must be greater than 0");
    }
    if (maxGuests < 1) {
      throw new IllegalArgumentException("Max guests must be at least 1");
    }
    this.id = id.trim();
    this.name = name.trim();
    this.location = location.trim();
    this.pricePerNight = pricePerNight;
    this.maxGuests = maxGuests;
    this.facilities = new ArrayList<>();
    if (facilities != null) {
      this.facilities.addAll(facilities);
    }
    this.state = new AvailableState();
  }

  /**
   * Each type works out its cost differently - polymorphism.
   *
   * @param nights number of nights
   * @return total cost for the stay
   */
  public abstract double calculateCost(int nights);

  /**
   * Short label such as "Villa" - overridden by each subclass.
   *
   * @return type label
   */
  public abstract String getTypeName();

  /**
   * Human-readable summary used when listing places.
   *
   * @return description including type-specific details
   */
  public String describe() {
    String guestLabel = maxGuests == 1 ? "guest" : "guests";
    return String.format(
        "%s [%s] in %s - GBP %.2f/night, max %d %s, status: %s",
        name, getTypeName(), location, pricePerNight, maxGuests, guestLabel,
        getStatusName());
  }

  /**
   * Attempts to book this place. Package-private so only BookingService
   * in this package can start a booking transaction.
   *
   * @param user the guest making the booking
   * @param nights number of nights (must be positive)
   */
  void book(User user, int nights) {
    if (user == null) {
      throw new IllegalArgumentException("User must not be null");
    }
    requirePositiveNights(nights);
    state.book(this, user, nights);
  }

  /**
   * Shared nights validation used by cost calculations and booking paths.
   *
   * @param nights number of nights
   * @throws IllegalArgumentException if nights is less than 1
   */
  protected static void requirePositiveNights(int nights) {
    if (nights < 1) {
      throw new IllegalArgumentException("Nights must be at least 1");
    }
  }

  /**
   * Releases a booking so the place moves toward Cleaning.
   * Package-private for BookingService.
   */
  void release() {
    state.release(this);
  }

  @Override
  public boolean isAvailable() {
    return state.isAvailable();
  }

  /**
   * Called after cleaning is complete. Package-private so only Host
   * in this package can finish cleaning.
   */
  void finishCleaning() {
    state.finishCleaning(this);
  }

  /**
   * Replaces the current state. Package-private so only state classes
   * in this package can call it.
   *
   * @param newState next state instance owned by this accommodation
   */
  void changeState(AccommodationState newState) {
    if (newState == null) {
      throw new IllegalArgumentException("State must not be null");
    }
    this.state = newState;
  }

  /** Returns the unique id. */
  public String getId() {
    return id;
  }

  /** Returns the display name. */
  public String getName() {
    return name;
  }

  /** Returns the location. */
  public String getLocation() {
    return location;
  }

  /** Returns the price per night. */
  public double getPricePerNight() {
    return pricePerNight;
  }

  /** Returns the maximum number of guests. */
  public int getMaxGuests() {
    return maxGuests;
  }

  /** Returns an unmodifiable view of the facilities. */
  public List<Facility> getFacilities() {
    return Collections.unmodifiableList(facilities);
  }

  /** Returns the current status name (Available, Booked, or Cleaning). */
  public String getStatusName() {
    return state.getName();
  }

  /** Returns the current state object. */
  AccommodationState getState() {
    return state;
  }

  /**
   * Adds a facility to this accommodation. Package-private so only same-package
   * setup code can mutate facilities after construction; callers normally pass
   * facilities through the factory.
   *
   * @param facility facility to aggregate
   */
  void addFacility(Facility facility) {
    if (facility == null) {
      throw new IllegalArgumentException("Facility must not be null");
    }
    if (facilities.contains(facility)) {
      return;
    }
    facilities.add(facility);
  }
}