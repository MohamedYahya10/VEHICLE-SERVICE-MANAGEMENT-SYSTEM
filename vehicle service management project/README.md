# Vehicle Service Management System (Java + Swing + JDBC + MySQL)

A simple, beginner-friendly desktop application for a second-year college mini-project.
It stores vehicle service records in a MySQL database and lets you manage them through a
Swing window.

---

## 1. Features

| Button                | What it does                                              |
|-----------------------|-----------------------------------------------------------|
| **Add Vehicle**       | Adds a new vehicle / service record to the database.      |
| **View All Records**  | Shows every record in the table.                          |
| **Search Vehicle**    | Finds records by vehicle number or owner name.            |
| **Update Service Status** | Changes the status of the selected record.           |
| **Delete Record**     | Deletes the selected record (asks for confirmation).      |

---

## 2. Project Structure

```
vehicle service management project/
├── lib/
│   └── mysql-connector-j-8.3.0.jar   # MySQL JDBC driver (already included)
├── sql/
│   └── vehicle_service.sql           # Creates the database + table + sample rows
├── src/
│   ├── DBConnection.java             # Opens the connection to MySQL
│   ├── ServiceRecord.java            # One record (a simple model class)
│   ├── ServiceRecordDAO.java         # All the SQL (add/view/search/update/delete)
│   └── VehicleServiceApp.java        # The Swing window + main()
├── out/                              # Compiled .class files (created by compile.bat)
├── compile.bat                       # Compiles the program
├── run.bat                           # Runs the program
└── README.md                         # This file
```

---

## 3. What You Need Installed

1. **JDK** (version 8 or newer). ✔ Already installed on this machine (JDK 26).
2. **MySQL Server** (version 8 recommended). ✔ Already installed and running on this machine.
3. **MySQL JDBC driver** (Connector/J). ✔ Already copied into the `lib` folder, so you do NOT
   need to download anything else.

---

## 4. ONE-TIME Database Setup (you must do this)

Before running the program, create the database and table.

### Easiest way — MySQL Workbench
1. Open **MySQL Workbench** and connect to your local MySQL server.
2. Open the file `sql/vehicle_service.sql`.
3. Click the **Execute** (lightning bolt) button.
4. You should see the `service_records` table with two sample rows.

### Alternative way — Command line
Open a terminal in this project folder and run:
```
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p < sql\vehicle_service.sql
```
(It will ask for your MySQL root password.)

---

## 5. Set Your MySQL Username / Password (IMPORTANT)

Open `src/DBConnection.java` and look at these three lines near the top:

```java
private static final String URL      = "jdbc:mysql://localhost:3306/vehicle_service?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
private static final String USER     = "root";
private static final String PASSWORD = "";
```

- If your MySQL `root` account has **no password**, leave `PASSWORD` as `""`.
- If it **has a password**, type it between the quotes, e.g. `"root1234"`.
- After editing, run **compile.bat** again so the change takes effect.

---

## 6. Compile and Run

Double-click (or run from a terminal):

```
compile.bat      # compiles the code -> creates the "out" folder
run.bat          # starts the application
```

Or, to do it manually from a terminal in this folder:
```
javac -cp "lib\mysql-connector-j-8.3.0.jar" -d out src\*.java
java  -cp "out;lib\mysql-connector-j-8.3.0.jar" VehicleServiceApp
```

---

## 7. Troubleshooting

- **"MySQL JDBC Driver not found"** → Make sure `lib\mysql-connector-j-8.3.0.jar` exists and is
  on the classpath (the .bat files handle this for you).
- **"Communications link failure" / "Access denied"** → MySQL is not running, or the
  username/password in `DBConnection.java` is wrong.
- **"Unknown database 'vehicle_service'"** → You skipped step 4. Run the SQL script first.
- **Table loads but is empty** → That is normal until you add vehicles. Click **Add Vehicle**.
