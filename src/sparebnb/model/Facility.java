package sparebnb.model;

/**
 * A single facility that an accommodation may offer, such as Wi-Fi or parking.
 * Facilities are aggregated by accommodations and can exist independently.
 */
public class Facility {

  private final String name;

  /**
   * Creates a facility with the given display name.
   *
   * @param name non-empty facility name
   * @throws IllegalArgumentException if name is null or blank
   */
  public Facility(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Facility name must not be empty");
    }
    this.name = name.trim();
  }

  /** @return the facility name */
  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }
}
