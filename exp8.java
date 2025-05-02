import java.sql.*;
import java.util.Scanner;

public class LoginSignupSystem {
    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/javadbN";
    private static final String USER = "Anushka Singh";
    private static final String PASSWORD = "Anushk@27";

    public static void main(String[] args) {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish Connection
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL successfully!");

            // Ensure table exists
            createUsersTable(conn);

            // Scanner for user input
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n1. Sign Up\n2. Log In\n3. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        signUp(conn, scanner);
                        break;
                    case 2:
                        logIn(conn, scanner);
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        conn.close();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice! Try again.");
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found! Add JDBC Jar.");
        } catch (SQLException e) {
            System.out.println("Connection failed! Check database settings.");
            e.printStackTrace();
        }
    }

    // Method to create users table if it doesn't exist
    private static void createUsersTable(Connection conn) throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) UNIQUE NOT NULL, " +
                "password VARCHAR(255) NOT NULL)";
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(query);
        stmt.close();
    }

    // Sign Up Method
    // Sign Up Method
    private static void signUp(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter new username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            // Debug Statement
            System.out.println("User " + username + " is being inserted into the database...");

            // Insert into database
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password); // In real applications, use password hashing
            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("User registered successfully!");
            } else {
                System.out.println("User registration failed!");
            }

            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error: Username might already exist!");
            e.printStackTrace(); // Debugging the SQL error
        }
    }

    // Login Method
    private static void logIn(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            // Query database
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful! Welcome, " + username + "!");
            } else {
                System.out.println("Invalid username or password!");
            }

            pstmt.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error while logging in!");
        }
    }
}
