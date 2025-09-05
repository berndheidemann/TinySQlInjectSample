package de.szut;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class ExampleUI {
    private static final String DB_URL = "jdbc:sqlite:sample.db";
    private JFrame frame;
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton loginButton;

    public ExampleUI() {
        createUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ExampleUI();
            }
        });
    }

    private void createUI() {
        // JFrame erstellen
        frame = new JFrame("Login Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 200);
        frame.setLocationRelativeTo(null); // Zentriert das Fenster
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username Label und Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        frame.add(new JLabel("Benutzername:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        usernameField = new JTextField(15);
        frame.add(usernameField, gbc);

        // Password Label und Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        frame.add(new JLabel("Passwort:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        passwordField = new JTextField(15);
        frame.add(passwordField, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        loginButton = new JButton("Anmelden");

        // Event Listener für den Button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = passwordField.getText();

                // Datenbankverbindung und Login-Prüfung
                if (authenticateUser(username, password)) {
                    JOptionPane.showMessageDialog(frame, "Eingeloggt", "Login erfolgreich", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Falsche Logindaten!", "Login fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.add(loginButton, gbc);

        // Frame sichtbar machen
        frame.setVisible(true);
    }

    private boolean authenticateUser(String username, String password) {
        String query = "SELECT User.username, User.email, Gehaelter.position, Gehaelter.abteilung FROM User JOIN Gehaelter on User.id = Gehaelter.user_id WHERE username = '" + username + "' AND password = '" + password + "'";

        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Willkommen " + rs.getString(1) + "!\n" +
                                "Email: " + rs.getString(2) + "\n" +
                                "Position: " + rs.getString(3) + "\n" +
                                "Abteilung: " + rs.getString(4), "\n" +
                                "Login erfolgreich", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        } catch (Exception e) {
            System.err.println("Datenbankfehler: " + e.getMessage());
            JOptionPane.showMessageDialog(frame, "Datenbankfehler aufgetreten!", "Fehler", JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }
}
