package sparebnb.model;

/**
 * State in which the accommodation is held by a guest.
 */
public class BookedState implements AccommodationState {

  @Override
  public void book(Accommodation accommodation, User user, int nights) {
    String guestName = user == null ? "Guest" : user.getName();
    throw new AccommodationUnavailableException(
        "Sorry " + guestName + ", " + accommodation.getName()
            + " is already booked.");
  }

  @Override
  public void release(Accommodation accommodation) {
    accommodation.changeState(new CleaningState());
  }

  @Override
  public void finishCleaning(Accommodation accommodation) {
    throw new IllegalStateException(
        accommodation.getName() + " is booked, so cleaning cannot be finished yet.");
  }

  @Override
  public boolean isAvailable() {
    return false;
  }

  @Override
  public String getName() {
    return "Booked";
  }
}