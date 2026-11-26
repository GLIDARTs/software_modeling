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
 * {@link Accommodation}. Typed create methods keep compile-time safety.
 *
 * <p>Each factory instance owns its own id sequence so repeated demos in the
 * same JVM do not keep climbing forever from a shared static counter.
 */
public class AccommodationFactory {

  private int nextId = 1;

  /** Creates a factory with ids starting at ACC-001. */
  public AccommodationFactory() {
  }

  /**
   * Creates an apartment with a generated id.
   *
   * @param name display name
   * @param location city or area
   * @param pricePerNight nightly price
   * @param maxGuests maximum guests
   * @param facilities facilities to attach
   * @param floorNumber floor of the apartment
   * @return new apartment as Accommodation
   */
  public Accommodation createApartment(String name, String location,
      double pricePerNight, int maxGuests, List<Facility> facilities,
      int floorNumber) {
    return new Apartment(nextId(), name, location, pricePerNight, maxGuests,
        facilities, floorNumber);
  }

  /**
   * Creates a villa with a generated id.
   *
   * @param name display name
   * @param location city or area
   * @param pricePerNight nightly price
   * @param maxGuests maximum guests
   * @param facilities facilities to attach
   * @param hasPool whether the villa has a pool
   * @return new villa as Accommodation
   */
  public Accommodation createVilla(String name, String location,
      double pricePerNight, int maxGuests, List<Facility> facilities,
      boolean hasPool) {
    return new Villa(nextId(), name, location, pricePerNight, maxGuests,
        facilities, hasPool);
  }

  /**
   * Creates a hotel room with a generated id.
   *
   * @param name display name
   * @param location city or area
   * @param pricePerNight nightly price
   * @param maxGuests maximum guests
   * @param facilities facilities to attach
   * @param breakfastIncluded whether breakfast is included
   * @return new hotel room as Accommodation
   */
  public Accommodation createHotelRoom(String name, String location,
      double pricePerNight, int maxGuests, List<Facility> facilities,
      boolean breakfastIncluded) {
    return new HotelRoom(nextId(), name, location, pricePerNight, maxGuests,
        facilities, breakfastIncluded);
  }

  /**
   * Creates a shared room with a generated id.
   *
   * @param name display name
   * @param location city or area
   * @param pricePerNight nightly price
   * @param maxGuests maximum guests
   * @param facilities facilities to attach
   * @param bedsInRoom number of beds in the room
   * @return new shared room as Accommodation
   */
  public Accommodation createSharedRoom(String name, String location,
      double pricePerNight, int maxGuests, List<Facility> facilities,
      int bedsInRoom) {
    return new SharedRoom(nextId(), name, location, pricePerNight, maxGuests,
        facilities, bedsInRoom);
  }

  private String nextId() {
    return String.format("ACC-%03d", nextId++);
  }
}
