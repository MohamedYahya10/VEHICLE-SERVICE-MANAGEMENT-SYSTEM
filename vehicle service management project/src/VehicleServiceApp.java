import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * VehicleServiceApp - the main Swing window of the Vehicle Service
 * Management System.  Five features: Add Vehicle, View All Records,
 * Search Vehicle, Update Service Status, Delete Record.
 *
 * The GUI never talks to the database directly; it calls ServiceRecordDAO.
 */
public class VehicleServiceApp extends JFrame {

    private final ServiceRecordDAO dao = new ServiceRecordDAO();

    // Input fields for the "Add Vehicle" form
    private final JTextField txtVehicleNumber = new JTextField(15);
    private final JTextField txtOwnerName     = new JTextField(15);
    private final JTextField txtVehicleModel  = new JTextField(15);
    private final JTextField txtServiceType   = new JTextField(15);
    private final JTextField txtServiceDate   = new JTextField(15);
    private final JTextField txtCost          = new JTextField(10);
    private final JComboBox<String> cmbStatus = new JComboBox<>(
            new String[]{"Pending", "In Progress", "Completed"});

    private final JTextField txtSearch = new JTextField(15);

    // The results table (read-only)
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Vehicle No", "Owner", "Model", "Service Type",
                         "Date", "Cost", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);

    public VehicleServiceApp() {
        setTitle("Vehicle Service Management System");
        setSize(900, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(buildFormPanel(),  BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);

        loadAllRecords();
    }

    // TOP PANEL : the "Add Vehicle" input form + search box
    private JPanel buildFormPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Add New Vehicle / Service Record"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 6, 4, 6);
        gc.anchor = GridBagConstraints.LINE_START;

        gc.gridx = 0; gc.gridy = 0; form.add(new JLabel("Vehicle Number:"), gc);
        gc.gridx = 1;           form.add(txtVehicleNumber, gc);
        gc.gridx = 2;           form.add(new JLabel("Owner Name:"), gc);
        gc.gridx = 3;           form.add(txtOwnerName, gc);
        gc.gridx = 4;           form.add(new JLabel("Vehicle Model:"), gc);
        gc.gridx = 5;           form.add(txtVehicleModel, gc);

        gc.gridx = 0; gc.gridy = 1; form.add(new JLabel("Service Type:"), gc);
        gc.gridx = 1;           form.add(txtServiceType, gc);
        gc.gridx = 2;           form.add(new JLabel("Service Date (yyyy-MM-dd):"), gc);
        gc.gridx = 3;           form.add(txtServiceDate, gc);
        gc.gridx = 4;           form.add(new JLabel("Cost:"), gc);
        gc.gridx = 5;           form.add(txtCost, gc);

        gc.gridx = 0; gc.gridy = 2; form.add(new JLabel("Status:"), gc);
        gc.gridx = 1;           form.add(cmbStatus, gc);
        gc.gridx = 2;           form.add(new JLabel("Search (Veh No / Owner):"), gc);
        gc.gridx = 3;           form.add(txtSearch, gc);

        return form;
    }

    // CENTER PANEL : the table that shows records
    private JScrollPane buildTablePanel() {
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Service Records"));
        return scroll;
    }

    // BOTTOM PANEL : the 5 feature buttons
    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));

        JButton btnAdd          = new JButton("Add Vehicle");
        JButton btnViewAll      = new JButton("View All Records");
        JButton btnSearch       = new JButton("Search Vehicle");
        JButton btnUpdateStatus = new JButton("Update Service Status");
        JButton btnDelete       = new JButton("Delete Record");
        JButton btnClear        = new JButton("Clear Form");

        btnAdd.addActionListener(this::onAdd);
        btnViewAll.addActionListener(e -> loadAllRecords());
        btnSearch.addActionListener(this::onSearch);
        btnUpdateStatus.addActionListener(this::onUpdateStatus);
        btnDelete.addActionListener(this::onDelete);
        btnClear.addActionListener(e -> clearForm());

        panel.add(btnAdd);
        panel.add(btnViewAll);
        panel.add(btnSearch);
        panel.add(btnUpdateStatus);
        panel.add(btnDelete);
        panel.add(btnClear);

        return panel;
    }

    // ================================================================
    //  ACTION HANDLERS (one per feature)
    // ================================================================

    /** 1) Add Vehicle */
    private void onAdd(ActionEvent e) {
        if (txtVehicleNumber.getText().trim().isEmpty()
                || txtOwnerName.getText().trim().isEmpty()) {
            showError("Vehicle Number and Owner Name are required.");
            return;
        }

        double cost;
        try {
            String costText = txtCost.getText().trim();
            cost = costText.isEmpty() ? 0.0 : Double.parseDouble(costText);
        } catch (NumberFormatException ex) {
            showError("Cost must be a number (for example 2500 or 2500.50).");
            return;
        }

        ServiceRecord r = new ServiceRecord(
                txtVehicleNumber.getText().trim(),
                txtOwnerName.getText().trim(),
                txtVehicleModel.getText().trim(),
                txtServiceType.getText().trim(),
                txtServiceDate.getText().trim(),
                cost,
                (String) cmbStatus.getSelectedItem());

        try {
            dao.addRecord(r);
            showInfo("Vehicle added successfully.");
            clearForm();
            loadAllRecords();
        } catch (Exception ex) {
            showError("Could not add record:\n" + ex.getMessage());
        }
    }

    /** 3) Search Vehicle */
    private void onSearch(ActionEvent e) {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            showError("Type something to search (vehicle number or owner name).");
            return;
        }
        try {
            List<ServiceRecord> results = dao.searchRecords(keyword);
            fillTable(results);
            showInfo(results.size() + " record(s) found.");
        } catch (Exception ex) {
            showError("Search failed:\n" + ex.getMessage());
        }
    }

    /** 4) Update Service Status */
    private void onUpdateStatus(ActionEvent e) {
        int selectedId = getSelectedId();
        if (selectedId == -1) {
            showError("Please select a record in the table first.");
            return;
        }

        String newStatus = (String) JOptionPane.showInputDialog(
                this,
                "Enter the new status for record ID " + selectedId + ":",
                "Update Service Status",
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"Pending", "In Progress", "Completed"},
                "Completed");

        if (newStatus == null) {
            return; // user pressed Cancel
        }

        try {
            boolean ok = dao.updateStatus(selectedId, newStatus);
            if (ok) {
                showInfo("Status updated to '" + newStatus + "'.");
                loadAllRecords();
            } else {
                showError("Update failed: record not found.");
            }
        } catch (Exception ex) {
            showError("Update failed:\n" + ex.getMessage());
        }
    }

    /** 5) Delete Record */
    private void onDelete(ActionEvent e) {
        int selectedId = getSelectedId();
        if (selectedId == -1) {
            showError("Please select a record in the table first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete record ID " + selectedId + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean ok = dao.deleteRecord(selectedId);
                if (ok) {
                    showInfo("Record deleted.");
                    loadAllRecords();
                } else {
                    showError("Delete failed: record not found.");
                }
            } catch (Exception ex) {
                showError("Delete failed:\n" + ex.getMessage());
            }
        }
    }


    // ================================================================
    //  HELPER METHODS
    // ================================================================

    /** 2) View All Records : reload every row from the database. */
    private void loadAllRecords() {
        try {
            fillTable(dao.getAllRecords());
        } catch (Exception ex) {
            showError("Could not load records from the database.\n"
                    + "Is MySQL running and does the 'vehicle_service' database exist?\n\n"
                    + ex.getMessage());
        }
    }

    /** Replace the table contents with the given list of records. */
    private void fillTable(List<ServiceRecord> records) {
        tableModel.setRowCount(0); // clear old rows
        for (ServiceRecord r : records) {
            tableModel.addRow(new Object[]{
                    r.getId(),
                    r.getVehicleNumber(),
                    r.getOwnerName(),
                    r.getVehicleModel(),
                    r.getServiceType(),
                    r.getServiceDate(),
                    r.getCost(),
                    r.getStatus()
            });
        }
    }

    /** Return the id in the first column of the selected row, or -1. */
    private int getSelectedId() {
        int row = table.getSelectedRow();
        if (row == -1) {
            return -1;
        }
        return (int) tableModel.getValueAt(row, 0);
    }

    /** Empty all the input fields and reset the status dropdown. */
    private void clearForm() {
        txtVehicleNumber.setText("");
        txtOwnerName.setText("");
        txtVehicleModel.setText("");
        txtServiceType.setText("");
        txtServiceDate.setText("");
        txtCost.setText("");
        cmbStatus.setSelectedIndex(0);
        txtVehicleNumber.requestFocus();
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    // ================================================================
    //  MAIN : start the application
    // ================================================================
    public static void main(String[] args) {
        // Use the OS look-and-feel so the window looks like a normal app
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // If this fails we keep the default Java look; not a problem.
        }

        // Show the window on the Swing event thread (best practice)
        SwingUtilities.invokeLater(() -> new VehicleServiceApp().setVisible(true));
    }
}

