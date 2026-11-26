package sparebnb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import sparebnb.state.AccommodationState;
import sparebnb.state.AvailableState;

/**
 * Base type for every kind of place a guest can book.
 * Abstract because a generic accommodation should never be created.
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
   * @param id            unique id
   * @param name          display name
   * @param location      city or area
   * @param pricePerNight price greater than zero
   * @param maxGuests     maximum guests (at least 1)
   * @param facilities    facilities to aggregate (may be empty)
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

  @Override
  public void book(User user, int nights) {
    if (nights < 1) {
      throw new IllegalArgumentException("Nights must be at least 1");
    }
    state.book(this, user, nights);
  }

  @Override
  public void release() {
    state.release(this);
  }

  @Override
  public boolean isAvailable() {
    return state.isAvailable();
  }

  /**
   * Called by the host after cleaning is complete.
   */
  public void finishCleaning() {
    state.finishCleaning(this);
  }

  /**
   * Used by state classes to replace the current state (composition lifecycle).
   *
   * @param newState next state instance owned by this accommodation
   */
  public void changeState(AccommodationState newState) {
    if (newState == null) {
      throw new IllegalArgumentException("State must not be null");
    }
    this.state = newState;
  }

  /** @return unique id */
  public String getId() {
    return id;
  }

  /** @return display name */
  public String getName() {
    return name;
  }

  /** @return location */
  public String getLocation() {
    return location;
  }

  /** @return price per night */
  public double getPricePerNight() {
    return pricePerNight;
  }

  /** @return maximum guests */
  public int getMaxGuests() {
    return maxGuests;
  }

  /** @return unmodifiable view of facilities */
  public List<Facility> getFacilities() {
    return Collections.unmodifiableList(facilities);
  }

  /** @return current status name (Available, Booked, Cleaning) */
  public String getStatusName() {
    return state.getName();
  }

  /** @return current state object */
  public AccommodationState getState() {
    return state;
  }

  /**
   * Adds a facility to this accommodation.
   *
   * @param facility facility to aggregate
   */
  public void addFacility(Facility facility) {
    if (facility == null) {
      throw new IllegalArgumentException("Facility must not be null");
    }
    facilities.add(facility);
  }
}
