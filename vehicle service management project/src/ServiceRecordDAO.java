import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ServiceRecordDAO  (DAO = Data Access Object)
 * ---------------------------------------------
 * This is the ONLY class that talks to the database for records.
 * It has one method per feature:
 *
 *   addRecord()            -> Add Vehicle
 *   getAllRecords()        -> View All Records
 *   searchRecords(keyword) -> Search Vehicle
 *   updateStatus(...)      -> Update Service Status
 *   deleteRecord(id)       -> Delete Record
 *
 * Every method opens a connection, runs a query and closes everything.
 * We use PreparedStatement (the '?' placeholders) because it is SAFE and
 * is the correct way beginners should learn to write SQL from Java.
 */
public class ServiceRecordDAO {

    /** Add a new vehicle service record. */
    public void addRecord(ServiceRecord r) throws SQLException {
        String sql = "INSERT INTO service_records "
                   + "(vehicle_number, owner_name, vehicle_model, service_type, service_date, cost, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getVehicleNumber());
            ps.setString(2, r.getOwnerName());
            ps.setString(3, r.getVehicleModel());
            ps.setString(4, r.getServiceType());
            ps.setString(5, r.getServiceDate());
            ps.setDouble(6, r.getCost());
            ps.setString(7, r.getStatus());

            ps.executeUpdate();
        }
    }

    /** Return every record in the table (used by "View All"). */
    public List<ServiceRecord> getAllRecords() throws SQLException {
        List<ServiceRecord> list = new ArrayList<>();
        String sql = "SELECT id, vehicle_number, owner_name, vehicle_model, "
                   + "service_type, service_date, cost, status "
                   + "FROM service_records ORDER BY id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractRecord(rs));
            }
        }
        return list;
    }

    /** Search by vehicle number OR owner name (partial match allowed). */
    public List<ServiceRecord> searchRecords(String keyword) throws SQLException {
        List<ServiceRecord> list = new ArrayList<>();
        String sql = "SELECT id, vehicle_number, owner_name, vehicle_model, "
                   + "service_type, service_date, cost, status "
                   + "FROM service_records "
                   + "WHERE vehicle_number LIKE ? OR owner_name LIKE ? "
                   + "ORDER BY id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractRecord(rs));
                }
            }
        }
        return list;
    }

    /** Update ONLY the service status of one record (chosen by its id). */
    public boolean updateStatus(int id, String newStatus) throws SQLException {
        String sql = "UPDATE service_records SET status = ? WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    /** Delete one record (chosen by its id). */
    public boolean deleteRecord(int id) throws SQLException {
        String sql = "DELETE FROM service_records WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /** Small helper: turn the current row of a ResultSet into a ServiceRecord. */
    private ServiceRecord extractRecord(ResultSet rs) throws SQLException {
        int id            = rs.getInt("id");
        String vNumber    = rs.getString("vehicle_number");
        String owner      = rs.getString("owner_name");
        String model      = rs.getString("vehicle_model");
        String sType      = rs.getString("service_type");
        String sDate      = rs.getString("service_date");
        double cost       = rs.getDouble("cost");
        String status     = rs.getString("status");
        return new ServiceRecord(id, vNumber, owner, model, sType, sDate, cost, status);
    }
}
