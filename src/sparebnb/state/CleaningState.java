package sparebnb.state;

import sparebnb.model.Accommodation;
import sparebnb.model.AccommodationUnavailableException;
import sparebnb.model.User;

/**
 * State after a booking is released, before the host finishes cleaning.
 */
public class CleaningState implements AccommodationState {

  @Override
  public void book(Accommodation accommodation, User user, int nights) {
    String guestName = user == null ? "Guest" : user.getName();
    throw new AccommodationUnavailableException(
        "Sorry " + guestName + ", " + accommodation.getName()
            + " is being cleaned and is not ready to book yet.");
  }

  @Override
  public void release(Accommodation accommodation) {
    throw new IllegalStateException(
        accommodation.getName() + " is already being cleaned.");
  }

  @Override
  public void finishCleaning(Accommodation accommodation) {
    accommodation.changeState(new AvailableState());
  }

  @Override
  public boolean isAvailable() {
    return false;
  }

  @Override
  public String getName() {
    return "Cleaning";
  }
}
