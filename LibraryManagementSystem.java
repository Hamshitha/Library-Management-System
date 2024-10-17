import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.List;

public class LibraryManagementSystem {
    private static final int PAGE_SIZE = 10;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        // Load books from CSV file
        loadBooksFromCSV("books.csv", library);

        System.out.println("Welcome to the Library Management System");

        boolean exit = false;
        while (!exit) {
            System.out.println("\n1. List all books");
            System.out.println("2. Search books");
            System.out.println("3. Borrow a book");
            System.out.println("4. Return a book");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            try {
                String input = scanner.nextLine();
                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1:
                        // List books with pagination
                        listBooksInPages(library, PAGE_SIZE, scanner);
                        break;

                    case 2:
                        // Search for books
                        searchBooks(library, scanner);
                        break;

                    case 3:
                        // Borrow a book
                        borrowBook(library, scanner);
                        break;

                    case 4:
                        // Return a book
                        returnBook(library, scanner);
                        break;

                    case 5:
                        // Exit the program
                        exit = true;
                        System.out.println("Exiting the Library Management System. Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid choice, please try again.");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        scanner.close();
    }

    // Load books from CSV file
    private static void loadBooksFromCSV(String filePath, Library library) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip the header row
            br.readLine();

            // Read the file line by line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String title = values[0].trim();
                String author = values[1].trim();
                library.addBook(new Book(title, author, true));
            }
            System.out.println("Books loaded successfully from " + filePath);
        } catch (IOException e) {
            System.out.println("Error loading books from CSV file: " + e.getMessage());
        }
    }

    private static void listBooksInPages(Library library, int pageSize, Scanner scanner) {
        List<Book> books = library.getBooks();
        int totalBooks = books.size();
        int totalPages = (int) Math.ceil((double) totalBooks / pageSize);

        int currentPage = 1;

        while (true) {
            int start = (currentPage - 1) * pageSize;
            int end = Math.min(start + pageSize, totalBooks);

            System.out.println("\nBooks (Page " + currentPage + " of " + totalPages + "):");
            for (int i = start; i < end; i++) {
                System.out.println(books.get(i));
            }

            System.out.println("\n1. Next Page");
            System.out.println("2. Previous Page");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");
            String input = scanner.nextLine();

            if (input.equals("1") && currentPage < totalPages) {
                currentPage++;
            } else if (input.equals("2") && currentPage > 1) {
                currentPage--;
            } else if (input.equals("3")) {
                break;
            } else {
                System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void searchBooks(Library library, Scanner scanner) {
        System.out.print("\nEnter search term (title/author): ");
        String searchTerm = scanner.nextLine().toLowerCase();

        List<Book> books = library.getBooks();
        System.out.println("\nSearch Results:");
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(searchTerm) ||
                book.getAuthor().toLowerCase().contains(searchTerm)) {
                System.out.println(book);
            }
        }
    }

    private static void borrowBook(Library library, Scanner scanner) {
        System.out.print("\nEnter the title of the book you want to borrow: ");
        String borrowTitle = scanner.nextLine();
        Book borrowBook = library.findBookByTitle(borrowTitle);
        if (borrowBook != null && borrowBook.isAvailable()) {
            borrowBook.borrowBook();
            System.out.println("You have successfully borrowed the book: " + borrowTitle);
        } else if (borrowBook != null) {
            System.out.println("Sorry, the book is already borrowed.");
        } else {
            System.out.println("Book not found.");
        }
    }

    private static void returnBook(Library library, Scanner scanner) {
        System.out.print("\nEnter the title of the book you want to return: ");
        String returnTitle = scanner.nextLine();
        Book returnBook = library.findBookByTitle(returnTitle);
        if (returnBook != null && !returnBook.isAvailable()) {
            returnBook.returnBook();
            System.out.println("You have successfully returned the book: " + returnTitle);
        } else if (returnBook != null) {
            System.out.println("This book wasn't borrowed.");
        } else {
            System.out.println("Book not found.");
        }
    }
}
