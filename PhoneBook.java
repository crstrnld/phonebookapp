import java.io.*;
import java.util.*;

public class PhoneBook {
    private static final String FILE_NAME = "phone.txt";
    private static final String SEPARATOR = " - ";

    public PhoneBook() {
        ensureFileExists();
    }

    // Add contact
    public boolean addContact(Contact contact) {
        List<Contact> contacts = getAllContacts();
        for (Contact c : contacts) {
            if (c.getPhone().equalsIgnoreCase(contact.getPhone())) {
                System.out.println("⚠ Phone number already exists for: " + c.getName());
                return false;
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(contact.toString());
            bw.newLine();
            System.out.println("✔ Contact saved: " + contact);
            return true;
        } catch (IOException e) {
            System.out.println("✖ Error saving contact: " + e.getMessage());
            return false;
        }
    }

    // Get all contacts
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(SEPARATOR);
                if (parts.length == 2) {
                    contacts.add(new Contact(parts[0].trim(), parts[1].trim()));
                }
            }
        } catch (IOException e) {
            System.out.println("✖ Error reading file: " + e.getMessage());
        }
        return contacts;
    }

    // Search contact
    public List<Contact> search(String keyword) {
        List<Contact> results = new ArrayList<>();
        for (Contact c : getAllContacts()) {
            if (c.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                c.getPhone().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(c);
            }
        }
        return results;
    }

    // Delete contact by index
    public boolean deleteContact(int index) {
        List<Contact> contacts = getAllContacts();
        if (index < 1 || index > contacts.size()) {
            System.out.println("⚠ Number out of range.");
            return false;
        }
        contacts.remove(index - 1);
        rewriteFile(contacts);
        System.out.println("✔ Contact deleted successfully.");
        return true;
    }

    // Helpers
    private void ensureFileExists() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("✖ Could not create file: " + e.getMessage());
            }
        }
    }

    private void rewriteFile(List<Contact> contacts) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, false))) {
            for (Contact c : contacts) {
                bw.write(c.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("✖ Error updating file: " + e.getMessage());
        }
    }
}
