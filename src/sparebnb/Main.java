package sparebnb;

import java.util.Arrays;
import java.util.List;
import sparebnb.factory.AccommodationFactory;
import sparebnb.model.Accommodation;
import sparebnb.model.AccommodationUnavailableException;
import sparebnb.model.Booking;
import sparebnb.model.Facility;
import sparebnb.model.Host;
import sparebnb.model.User;
import sparebnb.model.BookingService;

/**
 * Console demo for SpareB&amp;B Sprint 1-2.
 * Uses fictitious data only.
 */
public class Main {

  /**
   * Runs the SpareB&amp;B booking story demo.
   *
   * @param args unused
   */
  public static void main(String[] args) {
    System.out.println("========================================");
    System.out.println("       SpareB&B - Sprint 1+2 Demo");
    System.out.println("========================================\n");

    Facility wifi = new Facility("Wi-Fi");
    Facility parking = new Facility("Parking");
    Facility kitchen = new Facility("Kitchen");
    Facility pool = new Facility("Pool");

    AccommodationFactory factory = new AccommodationFactory();
    Accommodation riversideApt = factory.createApartment("Riverside Flat", "London", 95.0, 3, Arrays.asList(wifi, kitchen), 4);
    Accommodation seaViewVilla = factory.createVilla("Sea View Villa", "Brighton", 180.0, 6, Arrays.asList(wifi, parking, pool), true);
    Accommodation cityHotel = factory.createHotelRoom("City Centre Room", "Manchester", 110.0, 2, Arrays.asList(wifi), true);
    Accommodation hostelBunk = factory.createSharedRoom("Canal Hostel Bunk", "Birmingham", 40.0, 1, Arrays.asList(wifi), 6);

    List<Accommodation> catalogue = Arrays.asList(
        riversideApt, seaViewVilla, cityHotel, hostelBunk);

    User priya = new User("U-001", "Priya Shah", "priya@example.test");
    User marcus = new User("U-002", "Marcus Lee", "marcus@example.test");
    User aisha = new User("U-003", "Aisha Khan", "aisha@example.test");

    Host jordan = new Host("H-001", "Jordan Blake");
    jordan.addListing(riversideApt);
    jordan.addListing(seaViewVilla);
    jordan.addListing(cityHotel);
    jordan.addListing(hostelBunk);

    BookingService bookingService = new BookingService();

    printSection("1) Catalogue (US1, US2, US6, US8)");
    printCatalogue(catalogue);

    printSection("2) Priya books Sea View Villa for 3 nights (US3, US10)");
    Booking priyaBooking = null;
    try {
      priyaBooking = bookingService.book(priya, seaViewVilla, 3);
      System.out.println("OK: " + priyaBooking);
      System.out.printf("   Loyalty points for %s: %d%n",
          priya.getName(), priya.getLoyaltyPoints());
    } catch (AccommodationUnavailableException ex) {
      System.out.println("FAIL: " + ex.getMessage());
    }
    printStatusTable(catalogue);

    printSection("3) Marcus tries the same villa (US4)");
    try {
      bookingService.book(marcus, seaViewVilla, 2);
      System.out.println("Unexpected: Marcus booking succeeded");
    } catch (AccommodationUnavailableException ex) {
      System.out.println("Informed appropriately: " + ex.getMessage());
    }
    printStatusTable(catalogue);

    printSection("4) Priya cancels - villa moves to Cleaning (US5, US9 setup)");
    if (priyaBooking != null) {
      bookingService.release(priyaBooking);
      System.out.println("Released: " + priyaBooking);
      System.out.printf(
          "   Loyalty points for %s after cancel: %d (points removed)%n",
          priya.getName(), priya.getLoyaltyPoints());
    }
    printStatusTable(catalogue);

    printSection("5) Marcus tries while Cleaning (US9)");
    try {
      bookingService.book(marcus, seaViewVilla, 2);
      System.out.println("Unexpected: Marcus booking succeeded while cleaning");
    } catch (AccommodationUnavailableException ex) {
      System.out.println("Informed appropriately: " + ex.getMessage());
    }

    printSection("6) Host Jordan finishes cleaning (US11, US12)");
    System.out.print(jordan.viewListingStatuses());
    jordan.finishCleaning(seaViewVilla);
    System.out.println("Jordan marked cleaning finished for Sea View Villa.");
    System.out.print(jordan.viewListingStatuses());

    printSection("7) Marcus books successfully after cleaning (US3, US11)");
    try {
      Booking marcusBooking = bookingService.book(marcus, seaViewVilla, 2);
      System.out.println("OK: " + marcusBooking);
      System.out.printf("   Loyalty points for %s: %d%n",
          marcus.getName(), marcus.getLoyaltyPoints());
    } catch (AccommodationUnavailableException ex) {
      System.out.println("FAIL: " + ex.getMessage());
    }
    printStatusTable(catalogue);

    printSection("8) Aisha books Riverside Flat (extra guest + polymorphism)");
    try {
      Booking aishaBooking = bookingService.book(aisha, riversideApt, 4);
      System.out.println("OK: " + aishaBooking);
      System.out.printf("   Cost used Apartment.calculateCost -> GBP %.2f%n",
          aishaBooking.getTotalCost());
      System.out.printf("   Loyalty points for %s: %d%n",
          aisha.getName(), aisha.getLoyaltyPoints());
    } catch (AccommodationUnavailableException ex) {
      System.out.println("FAIL: " + ex.getMessage());
    }

    printSection("9) My bookings (US7)");
    printUserBookings(priya);
    printUserBookings(marcus);
    printUserBookings(aisha);

    printSection("10) Polymorphism - describe() and calculateCost() per place");
    for (int i = 0; i < catalogue.size(); i++) {
      Accommodation place = catalogue.get(i);
      System.out.printf("%d) %s%n", i + 1, place.describe());
      System.out.println("   Facilities: " + place.getFacilities());
      double sampleCost = place.calculateCost(2);
      System.out.printf("   Sample 2-night cost for %s: GBP %.2f%n",
          place.getName(), sampleCost);
    }

    System.out.println("\n========================================");
    System.out.println("              Demo complete");
    System.out.println("========================================");
  }

  private static void printSection(String title) {
    System.out.println("\n--- " + title + " ---");
  }

  private static void printCatalogue(List<Accommodation> catalogue) {
    System.out.printf("%-10s %-22s %-12s %10s %6s %-10s %-28s%n",
        "ID", "Name", "Type", "GBP/night", "Max", "Status", "Facilities");
    System.out.println("-".repeat(102));
    for (Accommodation place : catalogue) {
      System.out.printf("%-10s %-22s %-12s %10.2f %6d %-10s %-28s%n",
          place.getId(), place.getName(), place.getTypeName(),
          place.getPricePerNight(), place.getMaxGuests(), place.getStatusName(),
          place.getFacilities().toString());
    }
  }

  private static void printStatusTable(List<Accommodation> catalogue) {
    System.out.println("Current status:");
    for (Accommodation place : catalogue) {
      System.out.printf("  %-22s -> %s%n", place.getName(), place.getStatusName());
    }
  }

  private static void printUserBookings(User user) {
    System.out.println(user.getName() + " (points: " + user.getLoyaltyPoints() + "):");
    if (user.getBookings().isEmpty()) {
      System.out.println("  (no bookings)");
      return;
    }
    for (Booking booking : user.getBookings()) {
      System.out.println("  " + booking);
    }
  }
}
