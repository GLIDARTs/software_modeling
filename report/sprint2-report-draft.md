# SpareB&B — Sprint 1 & 2 Report (Draft)

**Module:** CMPN222 Software Modelling  
**Word limit:** max ~1500 words / 5 pages (diagrams as PNGs do not count toward words)  
**References:** IEEE style  
**Data note:** All names, emails, and listings used in the demo are fictitious.

---

## 1. Introduction and scope (~150 words)

SpareB&B is a console prototype of a spare-room booking system. Guests can view accommodation, book an available place, cancel a booking, see facilities, and earn loyalty points. Hosts have a small role: finish cleaning after a stay and view listing status. The system will not include a GUI (Sprint 3), a database, payments, calendars/dates, or interactive keyboard input in this sprint. Creation of new listings by hosts is also out of scope.

---

## 2. User stories (~200 words)

| ID | User story |
|-|-|
| US1 | As a guest, I want to view a list of all accommodation so that I can compare the options and find somewhere that suits my trip. |
| US2 | As a guest, I want to see the details of one place (type, price, and maximum guests) so that I can decide whether it suits my stay. |
| US3 | As a guest, I want to book an available place so that I can secure my stay. |
| US4 | As a guest, I want to be told clearly when a place is already booked so that I know why my booking failed and can choose another place. |
| US5 | As a guest, I want to cancel my booking so that I am no longer committed to accommodation I do not need when my plans change. |
| US6 | As a guest, I want to see whether each place is available or booked so that I only try to book places that are free. |
| US7 | As a guest, I want to see my own bookings so that I can keep track of what I have reserved. |
| US8 | As a guest, I want to see the facilities of a place (for example Wi-Fi, parking, or kitchen) so that I can choose accommodation that matches my needs. |
| US9 | As a guest, I want to know when a place is not ready because it is being cleaned, and it'll be bookable once the host finishes cleaning, so that I understand why I cannot book it and know when to try again. |
| US10 | As a guest, I want to earn loyalty points when I book so that I am rewarded for using SpareB&B again. |
| US11 | As a host, I want to mark cleaning as finished so that my listing becomes available for guests again. |
| US12 | As a host, I want to view the status of my listings so that I know which places are available, booked, or being cleaned. |

**Acceptance criteria**

- **US4:** Given a place is booked by guest A, when guest B tries to book it, then B sees a clear unavailable message and no new booking is created.
- **US9:** Given Cleaning, when a guest tries to book, then booking is refused with a clear "being cleaned" message and the place stays Cleaning.
- **US11:** Given Cleaning, when the host finishes cleaning, then the place becomes Available and a guest can book it.

---

## 3. Use case diagram

See `diagrams/use-case.puml` (export PNG for submission).

Guest is the primary actor. Host is a secondary actor for Finish Cleaning and View Listing Status. *Book Accommodation* **includes** *Check Availability* because availability is always checked. *Earn Loyalty Points* **extends** *Book Accommodation* because points are awarded only after a successful booking.

---

## 4. Class diagram and OOP (~350 words)

See `diagrams/class-model.puml` and `diagrams/class-patterns.puml` (split for readability). Facility is aggregation (hollow diamond); current state is composition (filled diamond).

**Abstraction:** `Accommodation` is abstract — a generic place has no real price rule.  
**Encapsulation:** private fields; `book` / `release` / `finishCleaning` / `changeState` / `Booking.cancel` / loyalty mutators are package-private in `model`, so only `BookingService` and `Host` can drive those rules; constructors validate; list getters return unmodifiable views. Prices use `double` for educational simplicity.
**Inheritance:** `Apartment`, `Villa`, `HotelRoom`, `SharedRoom` extend `Accommodation`.  
**Polymorphism:** (1) subclasses override `calculateCost` / `describe`; (2) `state.book(this)` behaves differently per state.  
**Interface:** `Bookable` exposes availability (`isAvailable`); full booking transactions go through `BookingService`.
**Relationships:** aggregation Facility; composition current state; associations for Booking–User–Accommodation and Host manages listings; Main *uses* service/factory (dependency). Objects are stored, not ids. `BookingService` is the control class and lives in `model` with the entities it coordinates.

---

## 5. Sequence diagram (~100 words)

See `diagrams/sequence-book.puml`, drawn for user story US3 following the six-step method: select story → scenario → participants → messages → draw with numbering/activation/returns → validate. The `alt` fragment shows success versus already-booked/cleaning failure.

---

## 6. Design patterns (~400 words)

**Factory**  
User story: As a system maintainer, I want new accommodation types added in one place so that client code does not need to change.  
Pattern: Factory (`AccommodationFactory` with typed methods such as `createVilla`).  
Motivation: callers depend on `Accommodation`; type-specific parameters stay compile-time safe; adding a type means adding one factory method in one class.

**State**  
User stories: US4, US9, US11.  
Pattern: State (`AvailableState`, `BookedState`, `CleaningState`).  
Motivation: Available / Booked / Cleaning need different rules for `book`, `release`, and `finishCleaning` without a large if-else. See `diagrams/state-machine.puml`.

---

## 7. Own feature and demo (~150 words)

Loyalty points: `User` stores points (`addPoints` / `removePoints`); `BookingService` awards 10 points per night after a successful booking and removes them on cancel so cancelled stays do not keep rewards. Paste a screenshot of `Main` output here after export.

---

## 8. AI use declaration (~50 words)

I used Claude (Anthropic) as a tutor to explain concepts and plan the design and Cursor Agent as an assistant; all code, diagrams and text are my own / reviewed and understood by me.

---

## References (IEEE)

[1] E. Gamma, R. Helm, R. Johnson, and J. Vlissides, *Design Patterns: Elements of Reusable Object-Oriented Software*. Addison-Wesley, 1994.  
[2] University of Roehampton, CMPN222 Software Modelling coursework brief.  
[3] Google, “Google Java Style Guide.” [Online]. Available: https://google.github.io/styleguide/javaguide.html

