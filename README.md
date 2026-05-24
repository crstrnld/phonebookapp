# PhoneBook — Java Console Contact Manager

A simple CLI phone book application written in pure Java, storing contacts persistently in a plain-text file (`phone.txt`) using only the `java.io` package.

---

## Features

| # | Feature | Description |
|---|---------|-------------|
| 1 | **Add Contact** | Save a name + phone number to `phone.txt` |
| 2 | **View All Contacts** | Display every saved contact in a neat table |
| 3 | **Search Contact** | Find contacts by name or phone number (partial match) |
| 4 | **Delete Contact** | Remove a contact; file is rewritten immediately |

### Bonus features included
-  Duplicate phone number prevention
-  Neat formatted table output
-  Input validation (empty fields, invalid phone characters)
-  Delete confirmation prompt
-  Graceful handling of malformed lines in the file

---

## Technical Constraints

- **No external libraries** — only `java.io` (`File`, `FileWriter`, `BufferedWriter`, `FileReader`, `BufferedReader`)
- **No database** — plain `phone.txt` for persistence
- **Console-only (CLI)** application

---

## How to Run

### Prerequisites
- Java JDK 8 or later installed

### Compile
```bash
javac PhoneBook.java
```

### Run
```bash
java PhoneBook
```

---

## File Format (`phone.txt`)

Each line stores one contact in the format:
```
Name - PhoneNumber
```

Example:
```
John Doe - 08123456789
Jane Smith - 08987654321
Budi Santoso - 081234567890
```

---

## Project Structure

```
PhoneBook/
├── PhoneBook.java   ← Single-file source code
├── phone.txt        ← Persistent contact storage (auto-created if missing)
└── README.md        ← This file
```

---

## Sample Session

```
╔══════════════════════════════╗
║          PHONE BOOK          ║
╚══════════════════════════════╝
┌──────────────────────────────┐
│           MAIN MENU          │
├──────────────────────────────┤
│  1. Add Contact              │
│  2. View All Contacts        │
│  3. Search Contact           │
│  4. Delete Contact           │
│  5. Exit                     │
└──────────────────────────────┘
Choose an option: 2

── All Contacts ─────────────────
  No.   Name                      Phone Number        
  ────────────────────────────────────────────────────
  1     John Doe                  08123456789         
  2     Jane Smith                08987654321         
  3     Budi Santoso              081234567890        
```
