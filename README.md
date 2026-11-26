# SpareB&B (CMPN222 Software Modelling)

Console prototype for Sprint 1-2: guests book accommodation; hosts finish cleaning and view listing status. Fictitious data only.

## Layout

- `src/sparebnb/` - `Main.java`
- `src/sparebnb/model/` - domain types, State classes, `BookingService` (control)
- `src/sparebnb/factory/` - Factory pattern (typed create methods)
- `diagrams/` - PlantUML (use case, class, sequence, state machine)
- `report/` - Sprint 2 report draft

## Compile and run (PowerShell)

```powershell
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out sparebnb.Main
```

## Coding standard

Google Java Style: 2-space indent, Javadoc on public types/methods.