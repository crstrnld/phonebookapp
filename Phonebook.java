import java.io.*;
import java.util.*;

/**
 * PhoneBook - A simple console-based contact manager.
 * Stores contacts persistently in phone.txt using java.io.
 *
 * Format per line: Name - PhoneNumber
 * Example:         John Doe - 08123456789
 */
public class PhoneBook {

    // ---------------------------------------------------------------
    // CONSTANTS
    // ---------------------------------------------------------------

    /** Path to the persistent storage file. */
    private static final String FILE_NAME = "phone.txt";

    /** Separator between name and phone number inside the file. */
    private static final String SEPARATOR = " - ";

    // ---------------------------------------------------------------
    // ENTRY POINT
    // ---------------------------------------------------------------

    public static void main(String[] args) {
        ensureFileExists();          // Create phone.txt if it doesn't exist yet
        Scanner scanner = new Scanner(System.in);

        System.out.println("╔══════════════════════════════╗");
        System.out.println("║       📞  PHONE BOOK         ║");
        System.out.println("╚══════════════════════════════╝");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": addContact(scanner);    break;
                case "2": viewContacts();         break;
                case "3": searchContact(scanner); break;
                case "4": deleteContact(scanner); break;
                case "5":
                    System.out.println("\nGoodbye! 👋");
                    running = false;
                    break;
                default:
                    System.out.println("⚠  Invalid option. Please enter 1-5.\n");
            }
        }

        scanner.close();
    }

    // ---------------------------------------------------------------
    // MENU
    // ---------------------------------------------------------------

    /** Prints the main menu to the console. */
    private static void printMenu() {
        System.out.println("┌──────────────────────────────┐");
        System.out.println("│           MAIN MENU          │");
        System.out.println("├──────────────────────────────┤");
        System.out.println("│  1. Add Contact              │");
        System.out.println("│  2. View All Contacts        │");
        System.out.println("│  3. Search Contact           │");
        System.out.println("│  4. Delete Contact           │");
        System.out.println("│  5. Exit                     │");
        System.out.println("└──────────────────────────────┘");
        System.out.print("Choose an option: ");
    }

    // ---------------------------------------------------------------
    // FEATURE 1 – ADD CONTACT
    // ---------------------------------------------------------------

    /**
     * Prompts the user for a name and phone number, validates the input,
     * checks for duplicate phone numbers, then appends the new contact
     * to phone.txt.
     *
     * @param scanner shared Scanner reading from System.in
     */
    private static void addContact(Scanner scanner) {
        System.out.println("\n── Add New Contact ──────────────");

        // --- Read name ---
        System.out.print("Enter name        : ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("⚠  Name cannot be empty.\n");
            return;
        }

        // --- Read phone number ---
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine().trim();
        if (phone.isEmpty()) {
            System.out.println("⚠  Phone number cannot be empty.\n");
            return;
        }
        // Only allow digits, spaces, +, -, ()
        if (!phone.matches("[0-9+\\-() ]+")) {
            System.out.println("⚠  Invalid phone number format.\n");
            return;
        }

        // --- Duplicate check (BONUS) ---
        List<String> contacts = readAllLines();
        for (String line : contacts) {
            String[] parts = splitLine(line);
            if (parts != null && parts[1].equalsIgnoreCase(phone)) {
                System.out.println("⚠  Phone number already exists for: " + parts[0] + "\n");
                return;
            }
        }

        // --- Append to file ---
        String entry = name + SEPARATOR + phone;
        try (FileWriter fw = new FileWriter(FILE_NAME, true);         // true = append mode
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(entry);
            bw.newLine();   // OS-agnostic line ending
            System.out.println("✔  Contact saved: " + entry + "\n");

        } catch (IOException e) {
            System.out.println("✖  Error saving contact: " + e.getMessage() + "\n");
        }
    }

    // ---------------------------------------------------------------
    // FEATURE 2 – VIEW ALL CONTACTS
    // ---------------------------------------------------------------

    /**
     * Reads every line from phone.txt and displays contacts in a
     * formatted table. Skips blank or malformed lines gracefully.
     */
    private static void viewContacts() {
        System.out.println("\n── All Contacts ─────────────────");

        List<String> lines = readAllLines();
        if (lines.isEmpty()) {
            System.out.println("  (No contacts found)\n");
            return;
        }

        // Print formatted table
        System.out.printf("  %-5s %-25s %-20s%n", "No.", "Name", "Phone Number");
        System.out.println("  " + "─".repeat(52));

        int index = 1;
        for (String line : lines) {
            String[] parts = splitLine(line);
            if (parts != null) {
                System.out.printf("  %-5d %-25s %-20s%n", index++, parts[0], parts[1]);
            }
        }
        System.out.println();
    }

    // ---------------------------------------------------------------
    // FEATURE 3 – SEARCH CONTACT
    // ---------------------------------------------------------------

    /**
     * Searches all contacts whose name OR phone number contains the
     * user-supplied keyword (case-insensitive substring match).
     *
     * @param scanner shared Scanner reading from System.in
     */
    private static void searchContact(Scanner scanner) {
        System.out.println("\n── Search Contact ───────────────");
        System.out.print("Enter name or phone to search: ");
        String keyword = scanner.nextLine().trim().toLowerCase();

        if (keyword.isEmpty()) {
            System.out.println("⚠  Search keyword cannot be empty.\n");
            return;
        }

        List<String> lines = readAllLines();
        List<String[]> results = new ArrayList<>();

        for (String line : lines) {
            String[] parts = splitLine(line);
            if (parts != null) {
                // Match if keyword appears in name OR phone number
                if (parts[0].toLowerCase().contains(keyword) ||
                    parts[1].toLowerCase().contains(keyword)) {
                    results.add(parts);
                }
            }
        }

        if (results.isEmpty()) {
            System.out.println("  No contacts matching \"" + keyword + "\".\n");
        } else {
            System.out.println("  Found " + results.size() + " result(s):\n");
            System.out.printf("  %-5s %-25s %-20s%n", "No.", "Name", "Phone Number");
            System.out.println("  " + "─".repeat(52));
            int i = 1;
            for (String[] r : results) {
                System.out.printf("  %-5d %-25s %-20s%n", i++, r[0], r[1]);
            }
            System.out.println();
        }
    }

    // ---------------------------------------------------------------
    // FEATURE 4 – DELETE CONTACT
    // ---------------------------------------------------------------

    /**
     * Lists all contacts, asks the user to pick one by number, then
     * rewrites phone.txt without the deleted entry.
     *
     * @param scanner shared Scanner reading from System.in
     */
    private static void deleteContact(Scanner scanner) {
        System.out.println("\n── Delete Contact ───────────────");

        List<String> lines = readAllLines();
        if (lines.isEmpty()) {
            System.out.println("  (No contacts to delete)\n");
            return;
        }

        // Show numbered list so the user can pick
        System.out.printf("  %-5s %-25s %-20s%n", "No.", "Name", "Phone Number");
        System.out.println("  " + "─".repeat(52));

        List<String[]> valid = new ArrayList<>();
        for (String line : lines) {
            String[] parts = splitLine(line);
            if (parts != null) valid.add(parts);
        }

        for (int i = 0; i < valid.size(); i++) {
            System.out.printf("  %-5d %-25s %-20s%n", i + 1, valid.get(i)[0], valid.get(i)[1]);
        }

        System.out.print("\nEnter number to delete (0 to cancel): ");
        String input = scanner.nextLine().trim();
        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("⚠  Invalid number.\n");
            return;
        }

        if (choice == 0) {
            System.out.println("  Deletion cancelled.\n");
            return;
        }
        if (choice < 1 || choice > valid.size()) {
            System.out.println("⚠  Number out of range.\n");
            return;
        }

        // Confirmation prompt
        String[] toDelete = valid.get(choice - 1);
        System.out.println("  About to delete: " + toDelete[0] + SEPARATOR + toDelete[1]);
        System.out.print("  Confirm? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (!confirm.equals("y")) {
            System.out.println("  Deletion cancelled.\n");
            return;
        }

        // Rebuild the file without the deleted line
        valid.remove(choice - 1);
        rewriteFile(valid);
        System.out.println("✔  Contact deleted successfully.\n");
    }

    // ---------------------------------------------------------------
    // HELPER – FILE I/O UTILITIES
    // ---------------------------------------------------------------

    /**
     * Creates phone.txt if it does not already exist.
     * Uses java.io.File to check existence and create the file.
     */
    private static void ensureFileExists() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("ℹ  Created new file: " + FILE_NAME);
                }
            } catch (IOException e) {
                System.out.println("✖  Could not create " + FILE_NAME + ": " + e.getMessage());
            }
        }
    }

    /**
     * Reads every non-blank line from phone.txt and returns them as a List.
     *
     * @return list of raw lines from the file; empty list on any error
     */
    private static List<String> readAllLines() {
        List<String> lines = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) return lines;   // Nothing to read yet

        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {

            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {   // Skip blank lines
                    lines.add(line.trim());
                }
            }

        } catch (IOException e) {
            System.out.println("✖  Error reading file: " + e.getMessage());
        }

        return lines;
    }

    /**
     * Overwrites phone.txt with the contacts provided in the list.
     * Used by deleteContact() after removing an entry.
     *
     * @param contacts list of [name, phone] string arrays to persist
     */
    private static void rewriteFile(List<String[]> contacts) {
        try (FileWriter fw = new FileWriter(FILE_NAME, false);    // false = overwrite
             BufferedWriter bw = new BufferedWriter(fw)) {

            for (String[] parts : contacts) {
                bw.write(parts[0] + SEPARATOR + parts[1]);
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("✖  Error updating file: " + e.getMessage());
        }
    }

    /**
     * Splits a single line from phone.txt into a [name, phone] array.
     * Returns null if the line doesn't match the expected format.
     *
     * @param line raw line from phone.txt, e.g. "John Doe - 08123456789"
     * @return String[2] { name, phone } or null on parse failure
     */
    private static String[] splitLine(String line) {
        int idx = line.indexOf(SEPARATOR);
        if (idx < 0) return null;                      // Malformed line – skip

        String name  = line.substring(0, idx).trim();
        String phone = line.substring(idx + SEPARATOR.length()).trim();

        if (name.isEmpty() || phone.isEmpty()) return null;

        return new String[]{ name, phone };
    }
}
