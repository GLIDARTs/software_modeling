package sparebnb.model;

import java.util.List;

/**
 * A villa listing that may include a pool and a one-off cleaning fee.
 */
public class Villa extends Accommodation {

  private static final double CLEANING_FEE = 75.0;

  private final boolean hasPool;

  /**
   * Creates a villa.
   *
   * @param id            unique id
   * @param name          display name
   * @param location      city or area
   * @param pricePerNight nightly price
   * @param maxGuests     maximum guests
   * @param facilities    facilities
   * @param hasPool       whether the villa has a pool
   */
  public Villa(String id, String name, String location, double pricePerNight,
      int maxGuests, List<Facility> facilities, boolean hasPool) {
    super(id, name, location, pricePerNight, maxGuests, facilities);
    this.hasPool = hasPool;
  }

  @Override
  public double calculateCost(int nights) {
    requirePositiveNights(nights);
    return getPricePerNight() * nights + CLEANING_FEE;
  }

  @Override
  public String getTypeName() {
    return "Villa";
  }

  @Override
  public String describe() {
    return super.describe()
        + (hasPool ? " (pool, +GBP 75 cleaning fee)"
            : " (no pool, +GBP 75 cleaning fee)");
  }

  /** Returns true if the villa has a pool. */
  public boolean hasPool() {
    return hasPool;
  }
}
