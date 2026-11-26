package sparebnb.model;

import java.util.List;

/**
 * A hotel room that may include breakfast charged per night.
 */
public class HotelRoom extends Accommodation {

  private static final double BREAKFAST_PER_NIGHT = 12.0;

  private final boolean breakfastIncluded;

  /**
   * Creates a hotel room.
   *
   * @param id                unique id
   * @param name              display name
   * @param location          city or area
   * @param pricePerNight     nightly price
   * @param maxGuests         maximum guests
   * @param facilities        facilities
   * @param breakfastIncluded whether breakfast is included
   */
  public HotelRoom(String id, String name, String location, double pricePerNight,
      int maxGuests, List<Facility> facilities, boolean breakfastIncluded) {
    super(id, name, location, pricePerNight, maxGuests, facilities);
    this.breakfastIncluded = breakfastIncluded;
  }

  @Override
  public double calculateCost(int nights) {
    if (nights < 1) {
      throw new IllegalArgumentException("Nights must be at least 1");
    }
    double total = getPricePerNight() * nights;
    if (breakfastIncluded) {
      total += BREAKFAST_PER_NIGHT * nights;
    }
    return total;
  }

  @Override
  public String getTypeName() {
    return "HotelRoom";
  }

  @Override
  public String describe() {
    return super.describe()
        + (breakfastIncluded
            ? " (breakfast +GBP 12/night)"
            : " (breakfast not included)");
  }

  /** Returns true if breakfast is included. */
  public boolean isBreakfastIncluded() {
    return breakfastIncluded;
  }
}
