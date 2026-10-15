/**
 * ServiceRecord
 * -------------
 * A simple "model" class.  It represents ONE row of the service_records table.
 * Think of it as a box that holds the data of a single vehicle service record
 * so we can pass it easily between the database code and the GUI.
 */
public class ServiceRecord {

    private int id;              // auto-increment primary key in the database
    private String vehicleNumber;
    private String ownerName;
    private String vehicleModel;
    private String serviceType;
    private String serviceDate;  // kept as text (yyyy-MM-dd) to stay beginner friendly
    private double cost;
    private String status;       // e.g. Pending / In Progress / Completed

    // Empty constructor
    public ServiceRecord() {
    }

    // Constructor WITHOUT id (used when adding a new record; the DB creates the id)
    public ServiceRecord(String vehicleNumber, String ownerName, String vehicleModel,
                         String serviceType, String serviceDate, double cost, String status) {
        this.vehicleNumber = vehicleNumber;
        this.ownerName = ownerName;
        this.vehicleModel = vehicleModel;
        this.serviceType = serviceType;
        this.serviceDate = serviceDate;
        this.cost = cost;
        this.status = status;
    }

    // Full constructor (used when reading rows back from the database)
    public ServiceRecord(int id, String vehicleNumber, String ownerName, String vehicleModel,
                         String serviceType, String serviceDate, double cost, String status) {
        this(vehicleNumber, ownerName, vehicleModel, serviceType, serviceDate, cost, status);
        this.id = id;
    }

    // -------- Getters and Setters --------
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
