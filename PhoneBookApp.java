import java.util.*;

public class PhoneBookApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PhoneBook phoneBook = new PhoneBook();

        boolean running = true;
        while (running) {
            System.out.println("\n1. Add Contact\n2. View Contacts\n3. Search Contact\n4. Delete Contact\n5. Edit Contact\n6. Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine().trim();
                    System.out.print("Enter phone: ");
                    String phone = scanner.nextLine().trim();
                    phoneBook.addContact(new Contact(name, phone));
                    break;
                case "2":
                    List<Contact> contacts = phoneBook.getAllContacts();
                    if (contacts.isEmpty()) {
                        System.out.println("(No contacts found)");
                    } else {
                        int i = 1;
                        for (Contact c : contacts) {
                            System.out.println(i++ + ". " + c);
                        }
                    }
                    break;
                case "3":
                    System.out.print("Enter keyword: ");
                    String keyword = scanner.nextLine();
                    List<Contact> results = phoneBook.search(keyword);
                    if (results.isEmpty()) {
                        System.out.println("No contacts found.");
                    } else {
                        int i = 1;
                        for (Contact c : results) {
                            System.out.println(i++ + ". " + c);
                        }
                    }
                    break;
                case "4":
                    System.out.print("Enter number to delete: ");
                    int idx = Integer.parseInt(scanner.nextLine());
                    phoneBook.deleteContact(idx);
                    break;
                case "5":
                    List<Contact> contactsToEdit = phoneBook.getAllContacts();
                    if (contactsToEdit.isEmpty()) {
                        System.out.println("(No contacts to edit)");
                        break;
                    }
                    int i = 1;
                    for (Contact c : contactsToEdit) {
                        System.out.println(i++ + ". " + c);
                    }
                    System.out.print("Enter number to edit: ");
                    int editIdx = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine().trim();
                    System.out.print("Enter new phone: ");
                    String newPhone = scanner.nextLine().trim();
                    phoneBook.editContact(editIdx, newName, newPhone);
                    break;
                case "6":
                    running = false;
                    System.out.println("Goodbye!");
                    break;

            }
        }
        scanner.close();
    }
}
