package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {
        if (args.length == 2 && "-databaseFileName".equals(args[0])) {
            new DBManager(args[1]).run();
        } else {
            new DBManager().run();
        }
    }
}

class DBManager {
    private static final String DEFAULT_DB_NAME = "default_car_sharing_db";
    private static final String PATH = "jdbc:h2:./src/carsharing/db/";
    private static final String DRIVER = "org.h2.Driver";
    private static final String SQL_CREATE_TABLE = "CREATE TABLE COMPANY (" +
            "ID INTEGER AUTO_INCREMENT PRIMARY KEY, " +
            "NAME VARCHAR(255))";

    private final String dbFilename;

    public DBManager(String dbFileName) {
        this.dbFilename = PATH + dbFileName;
    }

    public DBManager() {
        this.dbFilename = PATH + DEFAULT_DB_NAME;
    }

    public void run() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try (Connection conn = DriverManager.getConnection(dbFilename);
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);
            stmt.executeUpdate(SQL_CREATE_TABLE);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}