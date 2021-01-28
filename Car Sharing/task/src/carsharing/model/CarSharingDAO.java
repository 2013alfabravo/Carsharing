package carsharing.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarSharingDAO {
    private static final String DEFAULT_DB_NAME = "default_car_sharing_db";
    private static final String PATH = "jdbc:h2:./src/carsharing/db/";
    private static final String DRIVER = "org.h2.Driver";
    private static final String SQL_CREATE_CUSTOMER_TABLE = "CREATE TABLE IF NOT EXISTS customer (" +
            "id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
            "name VARCHAR(255) UNIQUE NOT NULL," +
            "rented_car_id INTEGER DEFAULT NULL," +
            "CONSTRAINT fk_car_id FOREIGN KEY (rented_car_id) REFERENCES car(id)" +
            ")";
    private static final String SQL_CREATE_COMPANY_TABLE = "CREATE TABLE IF NOT EXISTS company (" +
            "id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
            "name VARCHAR(255) UNIQUE NOT NULL" +
            ")";
    private static final String SQL_CREATE_CAR_TABLE = "CREATE TABLE IF NOT EXISTS car (" +
            "id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
            "name VARCHAR(255) UNIQUE NOT NULL," +
            "company_id INT NOT NULL, " +
            "available BOOL NOT NULL DEFAULT TRUE, " +
            "CONSTRAINT fk_company FOREIGN KEY (company_id) REFERENCES company(id)" +
            ")";

    private Connection conn;

    public CarSharingDAO(String dbFilename) throws ClassNotFoundException {
        Class.forName(DRIVER);
        try {
            conn =  DriverManager.getConnection(PATH + dbFilename);
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CarSharingDAO() throws ClassNotFoundException {
        this(DEFAULT_DB_NAME);
    }

    public void createCarTable() {
        try (Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(SQL_CREATE_CAR_TABLE);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addCar(String name, int companyId) {
        String sql = "INSERT INTO car(" +
                "id, name, company_id) " +
                "VALUES(NULL, '" + name + "', " + companyId + ")";

        try (Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Car> findCarsByCompanyId(int companyId) {
        String sql = "SELECT * FROM car WHERE company_id = " + companyId + " ORDER BY id";
        List<Car> list = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {

            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                int carId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int carCompanyId = resultSet.getInt("company_id");
                list.add(new Car(carId, name, carCompanyId));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public List<Car> findAvailableCarsByCompanyId(int companyId) {
        String sql = "SELECT * FROM car WHERE company_id = " + companyId + " AND available IS TRUE ORDER BY id";
        List<Car> list = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {

            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                int carId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int carCompanyId = resultSet.getInt("company_id");
                list.add(new Car(carId, name, carCompanyId));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public void setUnavailable(Car car) {
        String sql = "UPDATE car SET available = FALSE WHERE id = " + car.getId();
        try (Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void createCompanyTable() {
        try (Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(SQL_CREATE_COMPANY_TABLE);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // todo throw exception if something fails
    public void addCompany(String name) {
        // todo catch unique value violation exception and return success of failure
        String sql = "INSERT INTO company(name) SELECT '" + name + "' WHERE NOT EXISTS (" +
                "SELECT name FROM company WHERE name = '" + name + "')";

        try (Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Company> findAllCompanies() {
        String sql = "SELECT * FROM company ORDER BY id";
        List<Company> list = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {

            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                list.add(new Company(id, name));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public void createCustomerTable() {
        try (Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(SQL_CREATE_CUSTOMER_TABLE);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addCustomer(String name) {
        String sql = "INSERT INTO customer(name) VALUES('" + name + "')";

        try (Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Customer> findAllCustomers() {
        String sql = "SELECT * FROM customer ORDER BY id";
        List<Customer> list = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {

            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int nullableInt = resultSet.getInt("rented_car_id");
                Integer carId = nullableInt == 0 ? null : nullableInt;
                list.add(new Customer(id, name, carId));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public void addRentedCar(Customer customer) {
        // todo rework to add rented car and set the car unavailable as a batch transaction
        int customerId = customer.getId();
        int carId = customer.getRentedCarId();
        String sql = "UPDATE customer SET rented_car_id = " + carId + " WHERE id = " + customerId;
        System.out.println("updating car: " + sql);

        try (Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void removeRentedCar(Customer customer) {
        // todo the same as in addRentedCar, do a batch transaction with customer and car tables
        String sql = "UPDATE customer SET rented_car_id = NULL WHERE id = " + customer.getId();

        try (Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Car findRentedCar(Customer customer) {
        String sql = "SELECT id, name, company_id FROM car WHERE id = " + customer.getRentedCarId();

        try (Statement pst = conn.createStatement()) {

            ResultSet rs = pst.executeQuery(sql);
            if (rs.next()) {
                int id = rs.getInt("id");
                System.out.println(id);
                String name = rs.getString("name");
                System.out.println(name);
                int companyId = rs.getInt("company_id");
                System.out.println(companyId);
                return new Car(id, name, companyId);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
