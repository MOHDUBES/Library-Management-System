import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Dashboard extends JFrame implements ActionListener {

    JTextField titleField, authorField, idField;
    JTextArea displayArea;
    JButton addBtn, viewBtn, issueBtn, returnBtn, deleteBtn;
    Connection con;

    Dashboard() {

        setTitle("Library Dashboard");
        setSize(600, 500);
        setLayout(new FlowLayout());

        add(new JLabel("Title:"));
        titleField = new JTextField(15);
        add(titleField);

        add(new JLabel("Author:"));
        authorField = new JTextField(15);
        add(authorField);

        addBtn = new JButton("Add Book");
        viewBtn = new JButton("View Books");

        add(addBtn);
        add(viewBtn);

        add(new JLabel("Book ID:"));
        idField = new JTextField(5);
        add(idField);

        issueBtn = new JButton("Issue");
        returnBtn = new JButton("Return");
        deleteBtn = new JButton("Delete");

        add(issueBtn);
        add(returnBtn);
        add(deleteBtn);

        displayArea = new JTextArea(15, 50);
        add(new JScrollPane(displayArea));

        addBtn.addActionListener(this);
        viewBtn.addActionListener(this);
        issueBtn.addActionListener(this);
        returnBtn.addActionListener(this);
        deleteBtn.addActionListener(this);

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

            if(con == null) return;

            // ADD BOOK
            if (e.getSource() == addBtn) {

                PreparedStatement pst = con.prepareStatement(
                        "INSERT INTO books(title, author, status) VALUES (?, ?, 'Available')"
                );

                pst.setString(1, titleField.getText());
                pst.setString(2, authorField.getText());
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this,"Book Added!");

            }

            // VIEW BOOKS
            if (e.getSource() == viewBtn) {

                displayArea.setText("");

                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM books");

                while (rs.next()) {

                    displayArea.append(
                            rs.getInt("id") + " | " +
                                    rs.getString("title") + " | " +
                                    rs.getString("author") + " | " +
                                    rs.getString("status") + "\n"
                    );
                }
            }

            // ISSUE BOOK
            if (e.getSource() == issueBtn) {

                int id = Integer.parseInt(idField.getText());

                PreparedStatement pst = con.prepareStatement(
                        "UPDATE books SET status='Issued' WHERE id=?"
                );

                pst.setInt(1, id);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this,"Book Issued!");

            }

            // RETURN BOOK
            if (e.getSource() == returnBtn) {

                int id = Integer.parseInt(idField.getText());

                PreparedStatement pst = con.prepareStatement(
                        "UPDATE books SET status='Available' WHERE id=?"
                );

                pst.setInt(1, id);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this,"Book Returned!");

            }

            // DELETE BOOK
            if (e.getSource() == deleteBtn) {

                int id = Integer.parseInt(idField.getText());

                PreparedStatement pst = con.prepareStatement(
                        "DELETE FROM books WHERE id=?"
                );

                pst.setInt(1, id);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this,"Book Deleted!");

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new Dashboard();
    }
}