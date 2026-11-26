package sparebnb.model;

/**
 * State in which a guest may book the accommodation.
 */
public class AvailableState implements AccommodationState {

  @Override
  public void book(Accommodation accommodation, User user, int nights) {
    accommodation.changeState(new BookedState());
  }

  @Override
  public void release(Accommodation accommodation) {
    throw new IllegalStateException(
        accommodation.getName() + " is available, so there is nothing to release.");
  }

  @Override
  public void finishCleaning(Accommodation accommodation) {
    throw new IllegalStateException(
        accommodation.getName() + " is available, so cleaning cannot be finished.");
  }

  @Override
  public boolean isAvailable() {
    return true;
  }

  @Override
  public String getName() {
    return "Available";
  }
}