import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Dashboard extends JFrame implements ActionListener {

    JTextField titleField, authorField, subjectField, idField;
    JTextArea displayArea;

    JButton addBtn, viewBtn, issueBtn, returnBtn, deleteBtn;

    Connection con;

    Dashboard() {

        setTitle("📚 Smart Library Dashboard");
        setSize(950, 600);
        setLayout(new BorderLayout());

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(240, 248, 255));
        topPanel.setLayout(new FlowLayout());

        titleField = new JTextField(10);
        authorField = new JTextField(10);
        subjectField = new JTextField(10);
        idField = new JTextField(5);

        topPanel.add(new JLabel("Title"));
        topPanel.add(titleField);

        topPanel.add(new JLabel("Author"));
        topPanel.add(authorField);

        topPanel.add(new JLabel("Subject"));
        topPanel.add(subjectField);

        topPanel.add(new JLabel("ID"));
        topPanel.add(idField);

        // ===== BUTTONS =====
        addBtn = new JButton("Add");
        viewBtn = new JButton("View");
        issueBtn = new JButton("Issue");
        returnBtn = new JButton("Return");
        deleteBtn = new JButton("Delete");

        JButton[] btns = {addBtn, viewBtn, issueBtn, returnBtn, deleteBtn};

        for (JButton b : btns) {
            b.setBackground(new Color(70, 130, 180));
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            topPanel.add(b);
        }

        add(topPanel, BorderLayout.NORTH);

        // ===== DISPLAY AREA =====
        displayArea = new JTextArea();
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        displayArea.setBackground(Color.WHITE);
        displayArea.setForeground(Color.BLACK);
        displayArea.setEditable(false);

        JScrollPane sp = new JScrollPane(displayArea);
        add(sp, BorderLayout.CENTER);

        // ===== EVENTS =====
        addBtn.addActionListener(this);
        viewBtn.addActionListener(this);
        issueBtn.addActionListener(this);
        returnBtn.addActionListener(this);
        deleteBtn.addActionListener(this);

        connectDB();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ================= DATABASE =================
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

    // ================= VIEW =================
    void viewBooks() {
        try {
            displayArea.setText("");

            displayArea.append("=====================================================================\n");
            displayArea.append(" ID  | TITLE              | AUTHOR        | SUBJECT   | STATUS\n");
            displayArea.append("=====================================================================\n");

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT * FROM books WHERE is_deleted = FALSE"
            );

            while (rs.next()) {
                displayArea.append(
                        String.format(" %-3d | %-18s | %-13s | %-9s | %s\n",
                                rs.getInt("id"),
                                rs.getString("title"),
                                rs.getString("author"),
                                rs.getString("subject"),
                                rs.getString("status"))
                );
            }

            // ===== SUBJECT REPORT =====
            displayArea.append("\n📚 SUBJECT WISE REPORT\n");
            displayArea.append("----------------------------\n");

            Statement st2 = con.createStatement();
            ResultSet rs2 = st2.executeQuery(
                    "SELECT subject, COUNT(*) as total " +
                            "FROM books WHERE is_deleted = FALSE AND subject != '' " +
                            "GROUP BY subject"
            );

            while (rs2.next()) {
                displayArea.append(
                        rs2.getString("subject") + " : " + rs2.getInt("total") + "\n"
                );
            }

            // ===== OVERALL SUMMARY =====
            Statement st3 = con.createStatement();
            ResultSet rs3 = st3.executeQuery(
                    "SELECT COUNT(*) AS total, " +
                            "SUM(CASE WHEN status='Available' AND is_deleted=FALSE THEN 1 ELSE 0 END) AS available, " +
                            "SUM(CASE WHEN status='Issued' AND is_deleted=FALSE THEN 1 ELSE 0 END) AS issued, " +
                            "SUM(CASE WHEN is_deleted=TRUE THEN 1 ELSE 0 END) AS deleted, " +
                            "SUM(CASE WHEN is_deleted=FALSE THEN 1 ELSE 0 END) AS active " +
                            "FROM books"
            );

            if (rs3.next()) {
                displayArea.append("\n📊 OVERALL SUMMARY\n");
                displayArea.append("----------------------------\n");

                displayArea.append("Total Books     : " + rs3.getInt("total") + "\n");
                displayArea.append("Available Books : " + rs3.getInt("available") + "\n");
                displayArea.append("Issued Books    : " + rs3.getInt("issued") + "\n");
                displayArea.append("Active Books    : " + rs3.getInt("active") + "\n");
                displayArea.append("Deleted Books   : " + rs3.getInt("deleted") + "\n");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.toString());
        }
    }

    // ================= ACTION =================
    public void actionPerformed(ActionEvent e) {

        try {

            // ADD
            if (e.getSource() == addBtn) {

                if (titleField.getText().trim().isEmpty() ||
                        authorField.getText().trim().isEmpty() ||
                        subjectField.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(this, "❌ Fill all fields!");
                    return;
                }

                PreparedStatement pst = con.prepareStatement(
                        "INSERT INTO books(title, author, subject, status) VALUES (?, ?, ?, 'Available')"
                );

                pst.setString(1, titleField.getText());
                pst.setString(2, authorField.getText());
                pst.setString(3, subjectField.getText());

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "✅ Book Added");

                titleField.setText("");
                authorField.setText("");
                subjectField.setText("");

                viewBooks();
            }

            // VIEW
            if (e.getSource() == viewBtn) {
                viewBooks();
            }

            // ISSUE
            if (e.getSource() == issueBtn) {

                PreparedStatement pst = con.prepareStatement(
                        "UPDATE books SET status='Issued' WHERE id=? AND is_deleted=FALSE"
                );

                pst.setInt(1, Integer.parseInt(idField.getText()));
                pst.executeUpdate();

                viewBooks();
            }

            // RETURN
            if (e.getSource() == returnBtn) {

                PreparedStatement pst = con.prepareStatement(
                        "UPDATE books SET status='Available' WHERE id=? AND is_deleted=FALSE"
                );

                pst.setInt(1, Integer.parseInt(idField.getText()));
                pst.executeUpdate();

                viewBooks();
            }

            // DELETE
            if (e.getSource() == deleteBtn) {

                PreparedStatement pst = con.prepareStatement(
                        "UPDATE books SET is_deleted=TRUE WHERE id=?"
                );

                pst.setInt(1, Integer.parseInt(idField.getText()));
                pst.executeUpdate();

                viewBooks();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.toString());
        }
    }

    public static void main(String[] args) {
        new Dashboard();
    }
}
