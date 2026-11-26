# SpareB&B (CMPN222 Software Modelling)

Folder layout:
- src/sparebnb/            -> Main.java goes here
- src/sparebnb/model/      -> Accommodation + subclasses, User, Booking, Facility, Bookable
- src/sparebnb/factory/    -> Factory pattern
- src/sparebnb/state/      -> State pattern
- src/sparebnb/service/    -> BookingService
- diagrams/                -> .puml files + exported PNGs
- report/                  -> Sprint 2 report

Compile and run (PowerShell, from this folder):
  javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
  java -cp out sparebnb.Main
