package carsharing.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private static final String DEFAULT_DB_NAME = "default_car_sharing_db";
    private static final String PATH = "jdbc:h2:./src/carsharing/db/";
    private static final String DRIVER = "org.h2.Driver";
    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS customer (" +
            "id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
            "name VARCHAR(255) UNIQUE NOT NULL," +
            "rented_car_id INTEGER DEFAULT NULL," +
            "CONSTRAINT fk_car_id FOREIGN KEY (rented_car_id) REFERENCES car(id)" +
            ")";

    private final String dbFilename;

    public CustomerDAO(String dbFilename) throws ClassNotFoundException {
        this.dbFilename = PATH + dbFilename;
        Class.forName(DRIVER);
    }

    public CustomerDAO() throws ClassNotFoundException {
        this(DEFAULT_DB_NAME);
    }

    public void createTable() {
        try (Connection conn = DriverManager.getConnection(dbFilename);
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(true);
            stmt.executeUpdate(SQL_CREATE_TABLE);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addCustomer(String name) {
        String sql = "INSERT INTO customer(name) VALUES('" + name + "')";

        try (Connection conn = DriverManager.getConnection(dbFilename);
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(true);
            stmt.executeUpdate(sql);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Customer findCustomerByName(String name) {
        String sql = "SELECT * FROM customer WHERE name = " + name;
        try (Connection conn = DriverManager.getConnection(dbFilename);
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(true);
            ResultSet resultSet = stmt.executeQuery(sql);

            int customerId = resultSet.getInt("id");
            String customerName = resultSet.getString("name");
            int hiredCarId = resultSet.getInt("rented_car_id");
            return new Customer(customerId, customerName, hiredCarId);

        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Customer> findAllCustomers() {
        String sql = "SELECT * FROM customer ORDER BY id";
        List<Customer> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbFilename);
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(true);
            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int carId = resultSet.getInt("rented_car_id");
                list.add(new Customer(id, name, carId));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }
}
