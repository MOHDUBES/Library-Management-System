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

        setTitle("Library Login");
        setSize(350, 200);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Username:"));
        userField = new JTextField();
        add(userField);

        add(new JLabel("Password:"));
        passField = new JPasswordField();
        add(passField);

        loginBtn = new JButton("Login");
        add(loginBtn);

        loginBtn.addActionListener(this);

        connectDB();

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    void connectDB() {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library_db?useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "0786"
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {

        try {

            if (con == null) {
                JOptionPane.showMessageDialog(this, "Database Connection Failed");
                return;
            }

            String user = userField.getText();
            String pass = new String(passField.getPassword());

            PreparedStatement pst = con.prepareStatement(
                    "SELECT * FROM users WHERE username=? AND password=?"
            );

            pst.setString(1, user);
            pst.setString(2, pass);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                new Dashboard();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Login!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}