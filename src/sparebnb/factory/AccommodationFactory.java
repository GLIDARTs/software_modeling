package sparebnb.factory;

import java.util.List;
import sparebnb.model.Accommodation;
import sparebnb.model.Apartment;
import sparebnb.model.Facility;
import sparebnb.model.HotelRoom;
import sparebnb.model.SharedRoom;
import sparebnb.model.Villa;

/**
 * Creates accommodation subtypes in one place so callers depend only on
 * {@link Accommodation}.
 */
public final class AccommodationFactory {

  private static int nextId = 1;

  private AccommodationFactory() {
    // utility class
  }

  /**
   * Creates an accommodation of the requested type with a generated id.
   *
   * @param type          accommodation kind
   * @param name          display name
   * @param location      city or area
   * @param pricePerNight nightly price
   * @param maxGuests     maximum guests
   * @param facilities    facilities to attach
   * @param typeSpecific  floor / hasPool / breakfast / beds depending on type
   * @return new accommodation as the parent type
   */
  public static Accommodation create(AccommodationType type, String name,
      String location, double pricePerNight, int maxGuests,
      List<Facility> facilities, Object typeSpecific) {
    if (type == null) {
      throw new IllegalArgumentException("Type must not be null");
    }
    String id = nextId();
    switch (type) {
      case APARTMENT:
        return new Apartment(id, name, location, pricePerNight, maxGuests,
            facilities, toInt(typeSpecific, "floorNumber"));
      case VILLA:
        return new Villa(id, name, location, pricePerNight, maxGuests,
            facilities, toBoolean(typeSpecific, "hasPool"));
      case HOTEL_ROOM:
        return new HotelRoom(id, name, location, pricePerNight, maxGuests,
            facilities, toBoolean(typeSpecific, "breakfastIncluded"));
      case SHARED_ROOM:
        return new SharedRoom(id, name, location, pricePerNight, maxGuests,
            facilities, toInt(typeSpecific, "bedsInRoom"));
      default:
        throw new IllegalArgumentException("Unknown type: " + type);
    }
  }

  private static String nextId() {
    return String.format("ACC-%03d", nextId++);
  }

  private static int toInt(Object value, String fieldName) {
    if (!(value instanceof Integer)) {
      throw new IllegalArgumentException(fieldName + " must be an Integer");
    }
    return (Integer) value;
  }

  private static boolean toBoolean(Object value, String fieldName) {
    if (!(value instanceof Boolean)) {
      throw new IllegalArgumentException(fieldName + " must be a Boolean");
    }
    return (Boolean) value;
  }
}
