package sparebnb.model;

import java.util.List;

/**
 * An apartment listing with a floor number.
 */
public class Apartment extends Accommodation {

  private final int floorNumber;

  /**
   * Creates an apartment.
   *
   * @param id            unique id
   * @param name          display name
   * @param location      city or area
   * @param pricePerNight nightly price
   * @param maxGuests     maximum guests
   * @param facilities    facilities
   * @param floorNumber   floor of the apartment (0 or above)
   */
  public Apartment(String id, String name, String location, double pricePerNight,
      int maxGuests, List<Facility> facilities, int floorNumber) {
    super(id, name, location, pricePerNight, maxGuests, facilities);
    if (floorNumber < 0) {
      throw new IllegalArgumentException("Floor number must be 0 or greater");
    }
    this.floorNumber = floorNumber;
  }

  @Override
  public double calculateCost(int nights) {
    requirePositiveNights(nights);
    return getPricePerNight() * nights;
  }

  @Override
  public String getTypeName() {
    return "Apartment";
  }

  @Override
  public String describe() {
    return super.describe() + String.format(" (floor %d)", floorNumber);
  }

  /** Returns the floor number. */
  public int getFloorNumber() {
    return floorNumber;
  }
}
