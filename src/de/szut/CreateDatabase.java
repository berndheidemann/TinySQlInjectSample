package de.szut;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDatabase {
    private static final String DB_URL = "jdbc:sqlite:sample.db";

    public static void main(String[] args) {
        CreateDatabase creator = new CreateDatabase();
        creator.createDatabase();
    }

    public void createDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            System.out.println("Verbindung zur SQLite-Datenbank hergestellt.");

            // Tabellen erstellen
            createUserTable(conn);
            createGehaelterTable(conn);

            // Beispieldaten einfügen
            insertSampleUsers(conn);
            insertSampleSalaries(conn);

            System.out.println("Datenbank und Tabellen erfolgreich erstellt!");

        } catch (SQLException e) {
            System.err.println("Fehler beim Erstellen der Datenbank: " + e.getMessage());
        }
    }

    private void createUserTable(Connection conn) throws SQLException {
        String createUserTableSQL = """
            CREATE TABLE IF NOT EXISTS User (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                email TEXT,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
            )
            """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createUserTableSQL);
            System.out.println("User-Tabelle erstellt.");
        }
    }

    private void createGehaelterTable(Connection conn) throws SQLException {
        String createGehaelterTableSQL = """
            CREATE TABLE IF NOT EXISTS Gehaelter (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                gehalt DECIMAL(10,2) NOT NULL,
                position TEXT,
                abteilung TEXT,
                FOREIGN KEY (user_id) REFERENCES User(id)
            )
            """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createGehaelterTableSQL);
            System.out.println("Gehaelter-Tabelle erstellt.");
        }
    }

    private void insertSampleUsers(Connection conn) throws SQLException {
        String insertUserSQL = "INSERT INTO User (username, password, email) VALUES (?, ?, ?)";

        String[][] sampleUsers = {
            {"admin", "admin123", "admin@firma.de"},
            {"mmueller", "password", "m.mueller@firma.de"},
            {"aschmidt", "123456", "a.schmidt@firma.de"},
            {"jweber", "secret", "j.weber@firma.de"},
            {"bfischer", "qwerty", "b.fischer@firma.de"}
        };

        try (PreparedStatement pstmt = conn.prepareStatement(insertUserSQL)) {
            for (String[] user : sampleUsers) {
                pstmt.setString(1, user[0]);
                pstmt.setString(2, user[1]);
                pstmt.setString(3, user[2]);
                pstmt.executeUpdate();
            }
            System.out.println("5 Beispieluser eingefügt.");
        }
    }

    private void insertSampleSalaries(Connection conn) throws SQLException {
        String insertSalarySQL = "INSERT INTO Gehaelter (user_id, gehalt, position, abteilung) VALUES (?, ?, ?, ?)";

        Object[][] sampleSalaries = {
            {1, 75000.00, "Administrator", "IT"},
            {2, 52000.00, "Entwickler", "IT"},
            {3, 48000.00, "Sachbearbeiter", "Verwaltung"},
            {4, 65000.00, "Projektleiter", "IT"},
            {5, 55000.00, "Analyst", "Controlling"}
        };

        try (PreparedStatement pstmt = conn.prepareStatement(insertSalarySQL)) {
            for (Object[] salary : sampleSalaries) {
                pstmt.setInt(1, (Integer) salary[0]);
                pstmt.setDouble(2, (Double) salary[1]);
                pstmt.setString(3, (String) salary[2]);
                pstmt.setString(4, (String) salary[3]);
                pstmt.executeUpdate();
            }
            System.out.println("Gehaltsdaten für alle User eingefügt.");
        }
    }
}
