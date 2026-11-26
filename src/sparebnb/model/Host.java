package sparebnb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A host who manages listings and can finish cleaning (US11, US12).
 * Kept small on purpose - no create/edit listing behaviour.
 */
public class Host {

  private final String id;
  private final String name;
  private final List<Accommodation> listings;

  /**
   * Creates a host with fictitious demo details.
   *
   * @param id unique id
   * @param name display name
   */
  public Host(String id, String name) {
    if (id == null || id.isBlank()) {
      throw new IllegalArgumentException("Host id must not be empty");
    }
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Host name must not be empty");
    }
    this.id = id.trim();
    this.name = name.trim();
    this.listings = new ArrayList<>();
  }

  /**
   * Assigns a listing for this host to manage.
   *
   * @param accommodation listing to manage
   */
  public void addListing(Accommodation accommodation) {
    if (accommodation == null) {
      throw new IllegalArgumentException("Accommodation must not be null");
    }
    if (listings.contains(accommodation)) {
      return;
    }
    listings.add(accommodation);
  }

  /**
   * Marks cleaning finished on a managed listing (US11).
   *
   * @param accommodation listing that is being cleaned
   * @throws IllegalArgumentException if this host does not manage the listing
   */
  public void finishCleaning(Accommodation accommodation) {
    if (accommodation == null) {
      throw new IllegalArgumentException("Accommodation must not be null");
    }
    if (!listings.contains(accommodation)) {
      throw new IllegalArgumentException(
          name + " does not manage " + accommodation.getName());
    }
    accommodation.finishCleaning();
  }

  /**
   * Builds a status summary of every managed listing (US12).
   *
   * @return formatted status lines
   */
  public String viewListingStatuses() {
    StringBuilder sb = new StringBuilder();
    sb.append("Listings managed by ").append(name).append(":\n");
    for (Accommodation place : listings) {
      sb.append(String.format("  %-24s %-12s %s%n",
          place.getName(), place.getTypeName(), place.getStatusName()));
    }
    return sb.toString();
  }

  /** Returns the unique id. */
  public String getId() {
    return id;
  }

  /** Returns the host name. */
  public String getName() {
    return name;
  }

  /** Returns an unmodifiable view of managed listings. */
  public List<Accommodation> getListings() {
    return Collections.unmodifiableList(listings);
  }
}