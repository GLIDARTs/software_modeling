package sparebnb.model;

import java.util.List;

/**
 * A shared room with a discount because the space is shared.
 */
public class SharedRoom extends Accommodation {

  private static final double SHARED_DISCOUNT = 0.85;

  private final int bedsInRoom;

  /**
   * Creates a shared room.
   *
   * @param id            unique id
   * @param name          display name
   * @param location      city or area
   * @param pricePerNight nightly price before discount
   * @param maxGuests     maximum guests
   * @param facilities    facilities
   * @param bedsInRoom    number of beds in the room (at least 1)
   */
  public SharedRoom(String id, String name, String location, double pricePerNight,
      int maxGuests, List<Facility> facilities, int bedsInRoom) {
    super(id, name, location, pricePerNight, maxGuests, facilities);
    if (bedsInRoom < 1) {
      throw new IllegalArgumentException("Beds in room must be at least 1");
    }
    this.bedsInRoom = bedsInRoom;
  }

  @Override
  public double calculateCost(int nights) {
    requirePositiveNights(nights);
    return getPricePerNight() * nights * SHARED_DISCOUNT;
  }

  @Override
  public String getTypeName() {
    return "SharedRoom";
  }

  @Override
  public String describe() {
    return super.describe()
        + String.format(" (%d beds, 15%% shared discount)", bedsInRoom);
  }

  /** Returns the number of beds in the room. */
  public int getBedsInRoom() {
    return bedsInRoom;
  }
}
