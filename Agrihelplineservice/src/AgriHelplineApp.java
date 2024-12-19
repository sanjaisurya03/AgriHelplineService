
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class AgriHelplineApp {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/agri_helpline";
    private static final String DB_USER = "root";  // Replace with your MySQL username
    private static final String DB_PASS = "1234";  // Replace with your MySQL password

    public static void main(String[] args) {
        JFrame frame = new JFrame("Agriculture Service Helpline");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JLabel titleLabel = new JLabel("Agriculture Service Helpline", SwingConstants.CENTER);
        titleLabel.setBounds(50, 30, 300, 30);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(100, 100, 200, 30);

        JButton signupButton = new JButton("Sign Up");
        signupButton.setBounds(100, 150, 200, 30);

        loginButton.addActionListener(e -> showLoginForm(frame));

        signupButton.addActionListener(e -> showSignupForm(frame));

        frame.setLayout(null);
        frame.add(titleLabel);
        frame.add(loginButton);
        frame.add(signupButton);
        frame.setVisible(true);
    }

    private static void showLoginForm(JFrame frame) {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(400, 300);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 50, 100, 30);
        JTextField usernameField = new JTextField();
        usernameField.setBounds(150, 50, 150, 30);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 100, 100, 30);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 150, 30);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 150, 100, 30);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            String role = login(username, password);
            if (role != null) {
                JOptionPane.showMessageDialog(loginFrame, "Login successful as " + role + "!");
                loginFrame.dispose();
                showPostLoginDialog(role, username);
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid credentials, try again!");
            }
        });

        loginFrame.setLayout(null);
        loginFrame.add(userLabel);
        loginFrame.add(usernameField);
        loginFrame.add(passLabel);
        loginFrame.add(passwordField);
        loginFrame.add(loginButton);
        loginFrame.setVisible(true);
    }

    private static void showSignupForm(JFrame frame) {
        JFrame signupFrame = new JFrame("Sign Up");
        signupFrame.setSize(400, 400);
        signupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 50, 100, 30);
        JTextField usernameField = new JTextField();
        usernameField.setBounds(150, 50, 150, 30);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 100, 100, 30);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 150, 30);

        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        confirmPassLabel.setBounds(50, 150, 100, 30);
        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(150, 150, 150, 30);

        JLabel roleLabel = new JLabel("Role (user/admin):");
        roleLabel.setBounds(50, 200, 100, 30);
        JTextField roleField = new JTextField();
        roleField.setBounds(150, 200, 150, 30);

        JButton signupButton = new JButton("Sign Up");
        signupButton.setBounds(150, 250, 100, 30);

        signupButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String role = roleField.getText().toLowerCase();

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(signupFrame, "Passwords do not match.");
                return;
            }

            if (!isValidPassword(password)) {
                JOptionPane.showMessageDialog(signupFrame, "Password must be at least 8 characters, contain a mix of uppercase, lowercase, digits, and special characters.");
                return;
            }

            if (signup(username, password, role)) {
                JOptionPane.showMessageDialog(signupFrame, "Sign up successful!");
                signupFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(signupFrame, "Sign up failed. Try again.");
            }
        });

        signupFrame.setLayout(null);
        signupFrame.add(userLabel);
        signupFrame.add(usernameField);
        signupFrame.add(passLabel);
        signupFrame.add(passwordField);
        signupFrame.add(confirmPassLabel);
        signupFrame.add(confirmPasswordField);
        signupFrame.add(roleLabel);
        signupFrame.add(roleField);
        signupFrame.add(signupButton);
        signupFrame.setVisible(true);
    }

    private static boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasUpper = true;
            } else if (Character.isLowerCase(ch)) {
                hasLower = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(ch)) {
                hasSpecial = true;
            }
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    private static boolean signup(String username, String password, String role) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String login(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String query = "SELECT role FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void showPostLoginDialog(String role, String username) {
        JFrame postLoginFrame = new JFrame("Welcome");
        postLoginFrame.setSize(400, 300);
        postLoginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel welcomeLabel = new JLabel("Welcome " + role + "!", SwingConstants.CENTER);
        welcomeLabel.setBounds(50, 50, 300, 30);

        if (role.equals("user")) {
            JButton submitQueryButton = new JButton("Submit Query");
            submitQueryButton.setBounds(100, 100, 200, 30);
            submitQueryButton.addActionListener(e -> showFarmerQueryForm(username));
            postLoginFrame.add(submitQueryButton);

            JButton viewQueriesButton = new JButton("View Queries");
            viewQueriesButton.setBounds(100, 150, 200, 30);
            viewQueriesButton.addActionListener(e -> showFarmerQueries(username));
            postLoginFrame.add(viewQueriesButton);
        }

        if (role.equals("admin")) {
            JButton manageUsersButton = new JButton("Manage Users");
            manageUsersButton.setBounds(100, 100, 200, 30);
            manageUsersButton.addActionListener(e -> showAdminUserManagement());
            postLoginFrame.add(manageUsersButton);

            JButton respondToQueriesButton = new JButton("Respond to Queries");
            respondToQueriesButton.setBounds(100, 150, 200, 30);
            respondToQueriesButton.addActionListener(e -> showAdminQueryResponse());
            postLoginFrame.add(respondToQueriesButton);
        }

        postLoginFrame.setLayout(null);
        postLoginFrame.add(welcomeLabel);
        postLoginFrame.setVisible(true);
    }

    private static void showFarmerQueryForm(String username) {
        JFrame queryFrame = new JFrame("Submit Query");
        queryFrame.setSize(400, 300);
        queryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea queryTextArea = new JTextArea();
        queryTextArea.setBounds(50, 50, 300, 100);

        JButton submitQueryButton = new JButton("Submit");
        submitQueryButton.setBounds(150, 200, 100, 30);
        submitQueryButton.addActionListener(e -> {
            String queryText = queryTextArea.getText();
            if (submitQuery(username, queryText)) {
                JOptionPane.showMessageDialog(queryFrame, "Query submitted successfully!");
            } else {
                JOptionPane.showMessageDialog(queryFrame, "Failed to submit query.");
            }
        });

        queryFrame.setLayout(null);
        queryFrame.add(queryTextArea);
        queryFrame.add(submitQueryButton);
        queryFrame.setVisible(true);
    }

    private static void showFarmerQueries(String username) {
        JFrame queriesFrame = new JFrame("Your Queries");
        queriesFrame.setSize(400, 300);
        queriesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea queriesTextArea = new JTextArea();
        queriesTextArea.setBounds(50, 50, 300, 150);
        queriesTextArea.setEditable(false);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String query = "SELECT query_text, response_text FROM queries WHERE user_id = (SELECT id FROM users WHERE username = ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            StringBuilder queries = new StringBuilder();
            while (rs.next()) {
                queries.append("Query: ").append(rs.getString("query_text")).append("\n");
                String response = rs.getString("response_text");
                if (response != null) {
                    queries.append("Response: ").append(response).append("\n");
                }
                queries.append("\n");
            }
            queriesTextArea.setText(queries.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        queriesFrame.setLayout(null);
        queriesFrame.add(queriesTextArea);
        queriesFrame.setVisible(true);
    }

    private static void showAdminUserManagement() {
        JFrame userManagementFrame = new JFrame("Admin User Management");
        userManagementFrame.setSize(500, 500);
        userManagementFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea usersTextArea = new JTextArea();
        usersTextArea.setBounds(50, 50, 400, 200);
        usersTextArea.setEditable(false);

        JTextField userIdField = new JTextField();
        userIdField.setBounds(50, 300, 50, 30);
        JLabel userIdLabel = new JLabel("User ID:");
        userIdLabel.setBounds(50, 270, 100, 30);

        JTextField roleField = new JTextField();
        roleField.setBounds(120, 300, 100, 30);
        JLabel roleLabel = new JLabel("New Role:");
        roleLabel.setBounds(120, 270, 100, 30);

        JButton updateRoleButton = new JButton("Update Role");
        updateRoleButton.setBounds(250, 300, 150, 30);

        JButton deleteUserButton = new JButton("Delete User");
        deleteUserButton.setBounds(50, 350, 150, 30);

        // Fetch and display users
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String query = "SELECT id, username, role FROM users";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            StringBuilder users = new StringBuilder();
            while (rs.next()) {
                users.append("ID: ").append(rs.getInt("id"))
                     .append(", Username: ").append(rs.getString("username"))
                     .append(", Role: ").append(rs.getString("role"))
                     .append("\n");
            }
            usersTextArea.setText(users.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Update user role
        updateRoleButton.addActionListener(e -> {
            int userId;
            try {
                userId = Integer.parseInt(userIdField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(userManagementFrame, "Invalid User ID.");
                return;
            }

            String newRole = roleField.getText().toLowerCase();
            if (!(newRole.equals("user") || newRole.equals("admin"))) {
                JOptionPane.showMessageDialog(userManagementFrame, "Role must be 'user' or 'admin'.");
                return;
            }

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                String query = "UPDATE users SET role = ? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, newRole);
                stmt.setInt(2, userId);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(userManagementFrame, "User role updated successfully!");
                    userManagementFrame.dispose();
                    showAdminUserManagement(); // Refresh the user management frame
                } else {
                    JOptionPane.showMessageDialog(userManagementFrame, "Failed to update role. User ID may be incorrect.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(userManagementFrame, "Database error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // Delete user
        deleteUserButton.addActionListener(e -> {
            int userId;
            try {
                userId = Integer.parseInt(userIdField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(userManagementFrame, "Invalid User ID.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(userManagementFrame, "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                String query = "DELETE FROM users WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, userId);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(userManagementFrame, "User deleted successfully!");
                    userManagementFrame.dispose();
                    showAdminUserManagement(); // Refresh the user management frame
                } else {
                    JOptionPane.showMessageDialog(userManagementFrame, "Failed to delete user. User ID may be incorrect.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(userManagementFrame, "Database error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        userManagementFrame.setLayout(null);
        userManagementFrame.add(usersTextArea);
        userManagementFrame.add(userIdLabel);
        userManagementFrame.add(userIdField);
        userManagementFrame.add(roleLabel);
        userManagementFrame.add(roleField);
        userManagementFrame.add(updateRoleButton);
        userManagementFrame.add(deleteUserButton);
        userManagementFrame.setVisible(true);
    }

    private static void showAdminQueryResponse() {
        JFrame responseFrame = new JFrame("Respond to Queries");
        responseFrame.setSize(500, 400);
        responseFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea queriesTextArea = new JTextArea();
        queriesTextArea.setBounds(50, 50, 300, 150);
        queriesTextArea.setEditable(false);

        JTextField queryIdField = new JTextField();
        queryIdField.setBounds(50, 220, 50, 30);
        JLabel queryIdLabel = new JLabel("Query ID:");
        queryIdLabel.setBounds(50, 190, 100, 30);

        JTextArea responseTextArea = new JTextArea();
        responseTextArea.setBounds(120, 220, 230, 100);
        JLabel responseLabel = new JLabel("Response:");
        responseLabel.setBounds(120, 190, 100, 30);

        JButton submitResponseButton = new JButton("Submit Response");
        submitResponseButton.setBounds(370, 290, 150, 30);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String query = "SELECT q.id, u.username, q.query_text FROM queries q JOIN users u ON q.user_id = u.id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            StringBuilder queries = new StringBuilder();
            while (rs.next()) {
                queries.append("ID: ").append(rs.getInt("id")).append(", User: ").append(rs.getString("username")).append(", Query: ").append(rs.getString("query_text")).append("\n");
            }
            queriesTextArea.setText(queries.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        submitResponseButton.addActionListener(e -> {
            int queryId;
            try {
                queryId = Integer.parseInt(queryIdField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(responseFrame, "Invalid Query ID.");
                return;
            }

            String responseText = responseTextArea.getText();
            if (responseText.isEmpty()) {
                JOptionPane.showMessageDialog(responseFrame, "Response cannot be empty.");
                return;
            }

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                String query = "UPDATE queries SET response_text = ? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, responseText);
                stmt.setInt(2, queryId);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(responseFrame, "Response submitted successfully!");
                } else {
                    JOptionPane.showMessageDialog(responseFrame, "Failed to submit response. Query ID may be incorrect.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(responseFrame, "Database error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        responseFrame.setLayout(null);
        responseFrame.add(queriesTextArea);
        responseFrame.add(queryIdLabel);
        responseFrame.add(queryIdField);
        responseFrame.add(responseLabel);
        responseFrame.add(responseTextArea);
        responseFrame.add(submitResponseButton);
        responseFrame.setVisible(true);
    }

    private static boolean submitQuery(String username, String queryText) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String query = "INSERT INTO queries (user_id, query_text) VALUES ((SELECT id FROM users WHERE username = ?), ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, queryText);

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}