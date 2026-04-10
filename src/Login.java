import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {

    JTextField userField;
    JPasswordField passField;
    JButton loginBtn;
    Connection con;

    Login() {

        setTitle("📚 Library Login");
        setSize(500, 350);
        setLayout(new BorderLayout());

        // ===== MAIN PANEL (BACKGROUND) =====
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(220, 235, 250));
        mainPanel.setLayout(new GridBagLayout());

        // ===== LOGIN CARD =====
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(300, 200));
        card.setBackground(Color.WHITE);
        card.setLayout(null);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // ===== TITLE =====
        JLabel title = new JLabel("Login");
        title.setBounds(120, 10, 100, 30);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        card.add(title);

        // ===== USERNAME =====
        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(30, 50, 100, 20);
        card.add(userLabel);

        userField = new JTextField();
        userField.setBounds(30, 70, 230, 25);
        card.add(userField);

        // ===== PASSWORD =====
        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(30, 100, 100, 20);
        card.add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(30, 120, 230, 25);
        card.add(passField);

        // ===== BUTTON =====
        loginBtn = new JButton("Login");
        loginBtn.setBounds(90, 155, 120, 30);
        loginBtn.setBackground(new Color(70, 130, 180));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        card.add(loginBtn);

        mainPanel.add(card);
        add(mainPanel, BorderLayout.CENTER);

        loginBtn.addActionListener(this);

        connectDB();

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    // ===== DATABASE CONNECTION =====
    void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library_db",
                    "root",
                    "0786"
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.toString());
        }
    }

    // ===== LOGIN ACTION =====
    public void actionPerformed(ActionEvent e) {

        try {

            if (con == null) {
                JOptionPane.showMessageDialog(this, "Database Connection Failed");
                return;
            }

            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();

            // INPUT VALIDATION
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "❌ Enter Username & Password");
                return;
            }

            PreparedStatement pst = con.prepareStatement(
                    "SELECT * FROM users WHERE username=? AND password=?"
            );

            pst.setString(1, user);
            pst.setString(2, pass);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "✅ Login Successful!");
                new Dashboard();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Invalid Login!");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.toString());
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
