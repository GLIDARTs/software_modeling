from pathlib import Path
from docx import Document
from docx.shared import Inches, Pt, Cm, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from PIL import Image, ImageDraw, ImageFont
import re

ROOT = Path(r"d:\software modeling\SpareBnB")
REPORT = ROOT / "report"
DIAG = ROOT / "diagrams"
OUT = REPORT / "A00087120_AbdulRahmanMohammed_Spirit1&2_report.docx"
DEMO_TXT = REPORT / "main-demo-output.txt"
DEMO_PNG = REPORT / "main-demo-screenshot.png"
COVER = REPORT / "cover-page.png"

def fnt(sz, bold=False):
    windir = Path(r"C:\Windows\Fonts")
    names = ["arialbd.ttf","calibrib.ttf"] if bold else ["arial.ttf","calibri.ttf"]
    for n in names:
        p = windir/n
        if p.exists():
            return ImageFont.truetype(str(p), sz)
    return ImageFont.load_default()

# cover
w,h=1240,1754
img=Image.new("RGB",(w,h),(28,28,28)); d=ImageDraw.Draw(img)
def center(t,y,font,fill):
    b=d.textbbox((0,0),t,font=font); tw=b[2]-b[0]
    d.text(((w-tw)/2,y),t,font=font,fill=fill); return y+(b[3]-b[1])+18
y=280
y=center("Software Modelling",y,fnt(54,True),(255,255,255))
y=center("Module code: Autumn - CMPN222",y+8,fnt(28),(255,255,255)); y+=90
y=center("Coursework Portfolio",y,fnt(44,True),(176,196,222)); y+=36
y=center("Sprint 1",y,fnt(30),(176,196,222))
y=center("Sprint 2",y,fnt(30),(176,196,222)); y=1180
y=center("Abdul Rahman Mohammed",y,fnt(34,True),(255,255,255))
y=center("Student ID: A00087120",y+6,fnt(26),(255,255,255))
y=center("University of Roehampton",y,fnt(26),(255,255,255))
center("November 2026",y,fnt(26),(255,255,255))
img.save(COVER)

# demo screenshot
text=DEMO_TXT.read_text(encoding="utf-8",errors="replace").replace("\r\n","\n")
lines=text.split("\n"); mono=fnt(15); pad=24; lh=20
probe=ImageDraw.Draw(Image.new("RGB",(10,10)))
mw=max(probe.textbbox((0,0),ln or " ",font=mono)[2] for ln in lines)
im=Image.new("RGB",(max(900,mw+2*pad), pad*2+lh*len(lines)),(12,12,12)); dr=ImageDraw.Draw(im)
yy=pad
for ln in lines:
    dr.text((pad,yy),ln,font=mono,fill=(220,220,220)); yy+=lh
im.save(DEMO_PNG)

doc=Document()
for s in doc.sections:
    s.top_margin=Cm(1.8); s.bottom_margin=Cm(1.8); s.left_margin=Cm(2.0); s.right_margin=Cm(2.0)
doc.add_picture(str(COVER), width=Cm(17)); doc.paragraphs[-1].alignment=WD_ALIGN_PARAGRAPH.CENTER
doc.add_page_break()
p=doc.add_paragraph(); p.alignment=WD_ALIGN_PARAGRAPH.CENTER
r=p.add_run("SpareB&B — Sprint 1 & 2 Report"); r.bold=True; r.font.size=Pt(16)
p=doc.add_paragraph(); p.alignment=WD_ALIGN_PARAGRAPH.CENTER
r=p.add_run("CMPN222 Software Modelling | Abdul Rahman Mohammed (A00087120)"); r.font.size=Pt(10)

def H(t): doc.add_heading(t,1)
def P(t):
    para=doc.add_paragraph(t)
    para.paragraph_format.first_line_indent=Cm(0.5)
    para.paragraph_format.space_after=Pt(8)
def CAP(t):
    para=doc.add_paragraph(t); para.alignment=WD_ALIGN_PARAGRAPH.CENTER
    for run in para.runs: run.font.size=Pt(9); run.font.color.rgb=RGBColor(80,80,80)
def PIC(path, wi=5.9):
    doc.add_picture(str(path), width=Inches(wi)); doc.paragraphs[-1].alignment=WD_ALIGN_PARAGRAPH.CENTER

H("1. Introduction and scope")
P("SpareB&B is a console prototype of a small accommodation booking system. Guests can browse listings, book an available place, cancel when plans change, see facilities, and earn loyalty points. Hosts are kept small: finish cleaning and view listing status, which gives Cleaning to Available a clear owner.")
P("Out of scope for Sprint 1 and 2: GUI (Sprint 3), database, payments, calendars/dates, interactive input, and host create/edit listings. The Main demo uses fictitious data only.")

H("2. User stories")
P("Stories describe what users need, not how code is written. IDs support traceability into diagrams and the viva.")
tbl=doc.add_table(rows=1,cols=2); tbl.style="Table Grid"
tbl.rows[0].cells[0].text="ID"; tbl.rows[0].cells[1].text="User story"
stories=[
("US1","As a guest, I want to view a list of all accommodation so that I can compare options."),
("US2","As a guest, I want to see details of one place (type, price, max guests) so that I can decide if it suits me."),
("US3","As a guest, I want to book an available place so that I can secure my stay."),
("US4","As a guest, I want a clear message when a place is already booked so that I can choose another."),
("US5","As a guest, I want to cancel my booking when plans change."),
("US6","As a guest, I want to see whether each place is available or booked."),
("US7","As a guest, I want to see my own bookings."),
("US8","As a guest, I want to see facilities such as Wi-Fi, parking or kitchen."),
("US9","As a guest, I want to know when a place is being cleaned and will be bookable after the host finishes cleaning."),
("US10","As a guest, I want to earn loyalty points when I book."),
("US11","As a host, I want to mark cleaning as finished so the listing becomes available again."),
("US12","As a host, I want to view the status of my listings."),
]
for a,b in stories:
    row=tbl.add_row().cells; row[0].text=a; row[1].text=b
CAP("Table 1. SpareB&B user stories (US1–US12).")
P("Acceptance checks: US4 blocks a second guest with a clear message; US9 refuses booking while Cleaning; US11 makes the place Available after host finishCleaning.")

H("3. Use case diagram")
P("Guest is primary. Host is secondary for Finish Cleaning and View Listing Status. Book includes Check Availability. Earn Loyalty Points extends Book because points follow a successful booking.")
PIC(DIAG/"use-case.png",5.8); CAP("Figure 1. Use case diagram for SpareB&B.")

H("4. Class design and OOP principles")
P("Diagrams are split for readability. Objects are stored as objects, not ids. Abstraction: abstract Accommodation. Encapsulation: private fields; package-private book/release/finishCleaning/changeState; unmodifiable list getters; constructor validation. Inheritance: four subtypes. Polymorphism: calculateCost/describe overrides and state.book(this). Bookable exposes isAvailable; BookingService is the control class in model. Facility is aggregation; current state is composition.")
PIC(DIAG/"class-model.png",5.9); CAP("Figure 2. Class diagram — domain model.")
PIC(DIAG/"class-patterns.png",5.9); CAP("Figure 3. Class diagram — Factory, State, control and Main.")

H("5. Sequence diagram")
P("For US3 I followed the seminar six-step method. Participants: Main, BookingService, Accommodation, AccommodationState, Booking, User. The alt shows success versus already booked/cleaning. Message names match real methods.")
PIC(DIAG/"sequence-book.png",5.9); CAP("Figure 4. Sequence diagram for Guest books accommodation.")

H("6. Design patterns")
P("Factory: typed create methods in AccommodationFactory keep creation in one place and preserve compile-time safety. Callers depend on Accommodation.")
P("State: Available, Booked and Cleaning each own the rules for book/release/finishCleaning, matching the state machine and avoiding a large if-else block.")
PIC(DIAG/"state-machine.png",5.5); CAP("Figure 5. State machine for accommodation lifecycle.")

H("7. Own feature and demo")
P("Loyalty points: User stores points; BookingService awards 10 per night on success and removes them on cancel. The Main demo shows four types, three guests, one host, blocked booking, cleaning cycle, facilities and points. Costs such as 615 and 435 come from polymorphic calculateCost.")
PIC(DEMO_PNG,5.9); CAP("Figure 6. Console output from sparebnb.Main (fictitious demo data).")

H("8. AI use declaration")
P("I used Claude (Anthropic) as a tutor to explain concepts and plan the design, and Cursor Agent as an assistant. I reviewed the code, diagrams and wording and can explain them in the viva.")

H("References")
for ref in [
"[1] E. Gamma, R. Helm, R. Johnson, and J. Vlissides, Design Patterns: Elements of Reusable Object-Oriented Software. Addison-Wesley, 1994.",
"[2] University of Roehampton, CMPN222 Software Modelling coursework brief, 2026.",
"[3] Google, “Google Java Style Guide.” [Online]. Available: https://google.github.io/styleguide/javaguide.html",
]:
    para=doc.add_paragraph(ref); para.paragraph_format.left_indent=Cm(0.75); para.paragraph_format.first_line_indent=Cm(-0.75)

words=0
for para in doc.paragraphs:
    t=para.text.strip()
    if not t or t.startswith("Figure ") or t.startswith("Table "):
        continue
    words += len(re.findall(r"\b[\w']+\b", t))
doc.save(OUT)
print("Wrote", OUT, "words~", words)