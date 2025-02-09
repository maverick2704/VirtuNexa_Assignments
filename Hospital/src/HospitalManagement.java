import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class HospitalManagement extends JFrame {
    private JTabbedPane tabbedPane;
    private Connection conn;

    public HospitalManagement() {
        setTitle("Hospital Management System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set the background color of the frame
        getContentPane().setBackground(new Color(12, 12, 12)); // #0C0C0C

        // Establish the database connection before panel creation
        connectDatabase();

        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(72, 30, 20)); // #481E14
        tabbedPane.setForeground(Color.WHITE); // Text color for tabs

        // Pass the established connection to the panels
        tabbedPane.addTab("Patient Registration", new RegistrationPanel(conn));
        tabbedPane.addTab("Doctor Appointment", new AppointmentPanel(conn));
        tabbedPane.addTab("Billing", new BillingPanel(conn));

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void connectDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital?useSSL=false&serverTimezone=UTC", "root", "mohit");
            Statement stmt = conn.createStatement();

            // Create necessary tables
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS patients (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100), age INT, gender VARCHAR(10), address TEXT)");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS doctors (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100), fee DECIMAL(10,2))");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS appointments (id INT AUTO_INCREMENT PRIMARY KEY, patient_id INT, doctor_id INT, time_slot VARCHAR(50), FOREIGN KEY (patient_id) REFERENCES patients(id), FOREIGN KEY (doctor_id) REFERENCES doctors(id))");

            // Insert default doctors if not exists
            stmt.executeUpdate("INSERT IGNORE INTO doctors (id, name, fee) VALUES (1, 'Dr. Rajesh Sharma', 500), (2, 'Dr. Priya Mehta', 700), (3, 'Dr. Anil Verma', 600), (4, 'Dr. Neha Kapoor', 750)");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "MySQL Driver not found. Ensure the correct driver is added: " + e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Connection Failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HospitalManagement().setVisible(true));
    }

    public void refreshAppointmentPanel() {
        // Find the AppointmentPanel and refresh its patient dropdown
        for (Component comp : tabbedPane.getComponents()) {
            if (comp instanceof AppointmentPanel) {
                ((AppointmentPanel) comp).refreshPatientDropdown();
                break;
            }
        }
    }

    public void refreshBillingPanel() {
        // Find the BillingPanel and refresh its patient dropdown
        for (Component comp : tabbedPane.getComponents()) {
            if (comp instanceof BillingPanel) {
                ((BillingPanel) comp).refreshPatientDropdown();
                break;
            }
        }
    }
}

class RegistrationPanel extends JPanel {
    private Connection conn;
    private JTextField nameField, ageField, addressField;
    private JRadioButton maleRadioButton, femaleRadioButton;
    private ButtonGroup genderGroup;


    public RegistrationPanel(Connection conn) {
        this.conn = conn;
        setLayout(new GridBagLayout());
        setBackground(new Color(12, 12, 12)); // #0C0C0C
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Patient Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(242, 97, 63)); // #F2613F
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        add(createLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        nameField.setBackground(new Color(72, 30, 20)); // #481E14
        nameField.setForeground(Color.WHITE);
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(createLabel("Age:"), gbc);
        gbc.gridx = 1;
        ageField = new JTextField(15);
        ageField.setBackground(new Color(72, 30, 20)); // #481E14
        ageField.setForeground(Color.WHITE);
        add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(createLabel("Gender:"), gbc);
        gbc.gridx = 1;
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.setBackground(new Color(12, 12, 12)); // #0C0C0C
        maleRadioButton = new JRadioButton("Male");
        maleRadioButton.setBackground(new Color(12, 12, 12)); // #0C0C0C
        maleRadioButton.setForeground(Color.WHITE);
        femaleRadioButton = new JRadioButton("Female");
        femaleRadioButton.setBackground(new Color(12, 12, 12)); // #0C0C0C
        femaleRadioButton.setForeground(Color.WHITE);
        genderGroup = new ButtonGroup();
        genderGroup.add(maleRadioButton);
        genderGroup.add(femaleRadioButton);
        genderPanel.add(maleRadioButton);
        genderPanel.add(femaleRadioButton);
        add(genderPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(createLabel("Address:"), gbc);
        gbc.gridx = 1;
        addressField = new JTextField(15);
        addressField.setBackground(new Color(72, 30, 20)); // #481E14
        addressField.setForeground(Color.WHITE);
        add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton registerBtn = new JButton("Register");
        registerBtn.setBackground(new Color(155, 57, 34)); // #9B3922
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.addActionListener(e -> registerPatient());
        add(registerBtn, gbc);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(242, 97, 63)); // #F2613F
        return label;
    }

    private void registerPatient() {
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String gender = maleRadioButton.isSelected() ? "Male" : "Female";
        String address = addressField.getText();

        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO patients (name, age, gender, address) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, gender);
            stmt.setString(4, address);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Patient Registered Successfully!");

            // Clear fields after registration
            nameField.setText("");
            ageField.setText("");
            genderGroup.clearSelection();
            addressField.setText("");

            // Refresh the patient dropdown in both AppointmentPanel and BillingPanel
            refreshAppointmentPanel();
            refreshBillingPanel();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void refreshAppointmentPanel() {
        // Get the parent HospitalManagement frame
        HospitalManagement parentFrame = (HospitalManagement) SwingUtilities.getWindowAncestor(this);
        if (parentFrame != null) {
            // Refresh the patient dropdown in the AppointmentPanel
            parentFrame.refreshAppointmentPanel();
        }
    }

    private void refreshBillingPanel() {
        // Get the parent HospitalManagement frame
        HospitalManagement parentFrame = (HospitalManagement) SwingUtilities.getWindowAncestor(this);
        if (parentFrame != null) {
            // Refresh the patient dropdown in the BillingPanel
            parentFrame.refreshBillingPanel();
        }
    }


}

class AppointmentPanel extends JPanel {
    private Connection conn;
    private JComboBox<String> patientComboBox;
    private JComboBox<String> doctorComboBox;
    private JComboBox<String> timeSlotComboBox;

    public AppointmentPanel(Connection conn) {
        this.conn = conn;
        setLayout(new GridBagLayout());
        setBackground(new Color(12, 12, 12)); // #0C0C0C
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Doctor Appointment");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(242, 97, 63)); // #F2613F
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        add(createLabel("Select Patient:"), gbc);
        gbc.gridx = 1;
        patientComboBox = new JComboBox<>();
        patientComboBox.setBackground(new Color(72, 30, 20)); // #481E14
        patientComboBox.setForeground(Color.WHITE);
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT name FROM patients");
            while (rs.next()) {
                patientComboBox.addItem(rs.getString("name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        add(patientComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(createLabel("Select Doctor:"), gbc);
        gbc.gridx = 1;
        doctorComboBox = new JComboBox<>(new String[]{"Dr. Rajesh Sharma", "Dr. Priya Mehta", "Dr. Anil Verma", "Dr. Neha Kapoor"});
        doctorComboBox.setBackground(new Color(72, 30, 20)); // #481E14
        doctorComboBox.setForeground(Color.WHITE);
        add(doctorComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(createLabel("Select Time Slot:"), gbc);
        gbc.gridx = 1;
        timeSlotComboBox = new JComboBox<>(new String[]{"10:00 AM - 11:00 AM", "11:00 AM - 12:00 PM", "2:00 PM - 3:00 PM", "3:00 PM - 4:00 PM"});
        timeSlotComboBox.setBackground(new Color(72, 30, 20)); // #481E14
        timeSlotComboBox.setForeground(Color.WHITE);
        add(timeSlotComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton bookBtn = new JButton("Book Appointment");
        bookBtn.setBackground(new Color(155, 57, 34)); // #9B3922
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setFocusPainted(false);
        bookBtn.addActionListener(e -> bookAppointment());
        add(bookBtn, gbc);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(242, 97, 63)); // #F2613F
        return label;
    }

    private void bookAppointment() {
        String patientName = (String) patientComboBox.getSelectedItem();
        String doctorName = (String) doctorComboBox.getSelectedItem();
        String timeSlot = (String) timeSlotComboBox.getSelectedItem();

        try {
            int patientId = getPatientId(patientName);
            int doctorId = getDoctorId(doctorName);

            if (patientId != -1 && doctorId != -1) {
                try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO appointments (patient_id, doctor_id, time_slot) VALUES (?, ?, ?)")) {
                    stmt.setInt(1, patientId);
                    stmt.setInt(2, doctorId);
                    stmt.setString(3, timeSlot);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Appointment Booked Successfully!");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private int getPatientId(String name) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT id FROM patients WHERE name = ?")) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }

    private int getDoctorId(String name) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT id FROM doctors WHERE name = ?")) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }

    public void refreshPatientDropdown() {
        patientComboBox.removeAllItems();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT name FROM patients");
            while (rs.next()) {
                patientComboBox.addItem(rs.getString("name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

class BillingPanel extends JPanel {
    private Connection conn;
    private JComboBox<String> patientComboBox;
    private JTextArea billTextArea;

    public BillingPanel(Connection conn) {
        this.conn = conn;
        setLayout(new BorderLayout());
        setBackground(new Color(12, 12, 12)); // #0C0C0C

        // Create a panel for the top section (menu and button)
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(12, 12, 12)); // #0C0C0C
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Billing");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(242, 97, 63)); // #F2613F
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        topPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        topPanel.add(createLabel("Select Patient:"), gbc);
        gbc.gridx = 1;
        patientComboBox = new JComboBox<>();
        patientComboBox.setBackground(new Color(72, 30, 20)); // #481E14
        patientComboBox.setForeground(Color.WHITE);
        refreshPatientDropdown(); // Load patients initially
        topPanel.add(patientComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton generateBillBtn = new JButton("Generate Bill");
        generateBillBtn.setBackground(new Color(155, 57, 34)); // #9B3922
        generateBillBtn.setForeground(Color.WHITE);
        generateBillBtn.setFocusPainted(false);
        generateBillBtn.addActionListener(e -> generateBill());
        topPanel.add(generateBillBtn, gbc);

        // Add the top panel to the north of the BillingPanel
        add(topPanel, BorderLayout.NORTH);

        // Create a white canvas for the bill details
        billTextArea = new JTextArea();
        billTextArea.setEditable(false);
        billTextArea.setBackground(new Color(72, 30, 20)); // #481E14
        billTextArea.setForeground(Color.WHITE);
        billTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(billTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Bill Details"));

        // Add the scroll pane to the center of the BillingPanel
        add(scrollPane, BorderLayout.CENTER);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(242, 97, 63)); // #F2613F
        return label;
    }


    private void generateBill() {
        String patientName = (String) patientComboBox.getSelectedItem();
        if (patientName == null || patientName.isEmpty()) {
            billTextArea.setText("Please select a patient.");
            return;
        }

        try {
            int patientId = getPatientId(patientName);
            if (patientId == -1) {
                billTextArea.setText("Patient not found.");
                return;
            }

            // Fetch patient details
            String patientDetails = "";
            try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM patients WHERE id = ?")) {
                stmt.setInt(1, patientId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    patientDetails = "Patient Name: " + rs.getString("name") + "\n" +
                            "Age: " + rs.getInt("age") + "\n" +
                            "Gender: " + rs.getString("gender") + "\n" +
                            "Address: " + rs.getString("address") + "\n";
                }
            }

            // Fetch appointments and calculate bill
            StringBuilder billDetails = new StringBuilder();
            double totalBill = 0.0;
            try (PreparedStatement stmt = conn.prepareStatement("SELECT d.name, d.fee FROM appointments a JOIN doctors d ON a.doctor_id = d.id WHERE a.patient_id = ?")) {
                stmt.setInt(1, patientId);
                ResultSet rs = stmt.executeQuery();
                billDetails.append("Appointments:\n");
                while (rs.next()) {
                    String doctorName = rs.getString("name");
                    double fee = rs.getDouble("fee");
                    billDetails.append("Doctor: ").append(doctorName).append(", Fee: ").append(fee).append("\n");
                    totalBill += fee;
                }
            }

            // Display the bill
            billTextArea.setText(patientDetails + "\n" + billDetails.toString() + "\nTotal Bill: " + totalBill);
        } catch (SQLException ex) {
            billTextArea.setText("Error: " + ex.getMessage());
        }
    }

    private int getPatientId(String name) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT id FROM patients WHERE name = ?")) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }

    // Method to refresh the patient dropdown
    public void refreshPatientDropdown() {
        patientComboBox.removeAllItems();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT name FROM patients");
            while (rs.next()) {
                patientComboBox.addItem(rs.getString("name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
