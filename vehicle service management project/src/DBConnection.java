import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection
 * ------------
 * A tiny helper class that opens a connection to the MySQL database.
 *
 * For a beginner project everything is kept in ONE place so it is easy to
 * change.  Look at the three constants below (URL, USER, PASSWORD) and edit
 * them so they match YOUR MySQL installation.
 */
public class DBConnection {

    // ------------------------------------------------------------------
    // EDIT THESE THREE VALUES TO MATCH YOUR MySQL SETUP
    // ------------------------------------------------------------------
    // URL  = jdbc:mysql://<host>:<port>/<database-name>
    //        - host  is usually "localhost"
    //        - port  is usually 3306
    //        - database name must be "vehicle_service" (see sql/vehicle_service.sql)
    private static final String URL =
            "jdbc:mysql://localhost:3306/vehicle_service"
          + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    private static final String USER = "root";

    // IMPORTANT: type your MySQL root password between the quotes "".
    // If your MySQL "root" account has NO password, leave it as "".
    private static final String PASSWORD = "20YAHYA06";
    // ------------------------------------------------------------------

    /**
     * Opens and returns a live connection to the database.
     * Throws SQLException if the database/server cannot be reached.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load the MySQL JDBC driver (Connector/J).
            // Modern drivers auto-register, but loading it explicitly is a
            // good habit and makes errors easier to understand.
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                "MySQL JDBC Driver not found. Make sure mysql-connector-j-*.jar "
              + "is inside the 'lib' folder and added to the classpath.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
