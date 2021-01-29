package carsharing.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// fixme change method parameters to properties instead of objects(??)
public class CarSharingDAO {
    private static final String CREATE_CUSTOMER_TABLE = "CREATE TABLE IF NOT EXISTS customer (" +
            "id INTEGER AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) UNIQUE NOT NULL, " +
            "rented_car_id INTEGER DEFAULT NULL, CONSTRAINT fk_car_id FOREIGN KEY (rented_car_id) " +
            "REFERENCES car(id))";
    private static final String CREATE_COMPANY_TABLE = "CREATE TABLE IF NOT EXISTS company (" +
            "id INTEGER AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) UNIQUE NOT NULL)";
    private static final String CREATE_CAR_TABLE = "CREATE TABLE IF NOT EXISTS car (" +
            "id INTEGER AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) UNIQUE NOT NULL, " +
            "company_id INT NOT NULL, customer_id INTEGER DEFAULT NULL, " +
            "CONSTRAINT fk_company FOREIGN KEY (company_id) REFERENCES company(id))";
    private static final String UPD_CUSTOMER_CAR_ID = "UPDATE customer SET rented_car_id = ? WHERE id = ?";
    private static final String UPD_CAR_CUSTOMER_ID = "UPDATE car SET customer_id = ? WHERE id = ?";
    private static final String CREATE_CAR = "INSERT INTO car(name, company_id) VALUES(?, ?)";
    private static final String ALL_CARS_BY_COMP_ID = "SELECT * FROM car WHERE company_id = ? ORDER BY id";
    private static final String AVAIL_CARS_BY_COMP_ID = "SELECT * FROM car WHERE company_id = ? " +
            "AND customer_id IS NULL ORDER BY id";
    private static final String CREATE_COMPANY = "INSERT INTO company(name) VALUES(?)";
    private static final String ALL_COMPANIES = "SELECT * FROM company ORDER BY id";
    private static final String CREATE_CUSTOMER = "INSERT INTO customer(name) VALUES(?)";
    private static final String ALL_CUSTOMERS = "SELECT * FROM customer ORDER BY id";
    private static final String CAR_BY_CUSTOMER_ID = "SELECT * FROM car WHERE customer_id = ?";

    private final DataSource dataSource;

    public CarSharingDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createCarTable() {
        createTable(CREATE_CAR_TABLE);
    }

    public void createCompanyTable() {
        createTable(CREATE_COMPANY_TABLE);
    }

    public void createCustomerTable() {
        createTable(CREATE_CUSTOMER_TABLE);
    }

    private void createTable(String sql) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void createCar(String name, Integer companyId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_CAR)) {

            stmt.setString(1, name);
            stmt.setObject(2, companyId);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Car> findAllCarsByCompanyId(Integer companyId) {
        // fixme rented car not showing up??
        List<Car> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(ALL_CARS_BY_COMP_ID)) {

            stmt.setObject(1, companyId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                list.add(mapCar(resultSet));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // todo merge into one method with parameter boolean 'all' ==|!= true
    public List<Car> findAvailableCarsByCompanyId(Integer companyId) {
        List<Car> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(AVAIL_CARS_BY_COMP_ID)) {

            stmt.setObject(1, companyId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                list.add(mapCar(resultSet));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public void createCompany(String name) {
        // todo catch unique value violation exception and return success or failure

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_COMPANY)) {

            stmt.setString(1, name);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // todo merge findAll into a single generic(?) method that returns a list of Listables
    //  depending on sql string as the argument
    public List<Company> findAllCompanies() {
        List<Company> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(ALL_COMPANIES)) {

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                // todo do company mapping in a method
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                list.add(new Company(id, name));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    private <T extends Listable> List<T> findAll(Class<T> cl, String sql) {
        // fixme problems with mapping: unknown entity constructor arguments,
        //  so choose mapping method according to class and take it as an arg?
        List<T> list = new ArrayList<>();
        return list;
    }

    // fixme check for unique name violation and return success or failure
    public void createCustomer(String name) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_CUSTOMER)) {

            stmt.setString(1, name);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Customer> findAllCustomers() {
        List<Customer> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(ALL_CUSTOMERS)) {

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                // todo do customer mapping in a method
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

    public void addRentedCar(Integer customerId, Integer carId) {
        // todo prevent renting if customer with id=customerId has not null rented_car_id
        setRentedCar(customerId, carId, false);
    }

    public void removeRentedCar(Integer customerId, Integer carId) {
        // todo prevent returning if customer with id=customerId has null rented_car_id
        setRentedCar(customerId, carId, true);
    }

    private void setRentedCar(Integer customerId, Integer carId, boolean isRented) {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt1 = conn.prepareStatement(UPD_CUSTOMER_CAR_ID);
                 PreparedStatement stmt2 = conn.prepareStatement(UPD_CAR_CUSTOMER_ID) ) {

                conn.setAutoCommit(false);

                stmt1.setObject(1, isRented ? null : carId);
                stmt1.setObject(2, customerId);
                stmt2.setObject(1, isRented ? null : customerId);
                stmt2.setObject(2, carId);

                int rows = stmt1.executeUpdate() + stmt2.executeUpdate();
                if (rows != 2) {
                    throw new SQLException("Failed to update either of the records.");
                }

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw new DAOException("Transaction failed: ", e);
            }
        } catch (SQLException | NullPointerException ex) {
            throw new DAOException("Database connection failed: ", ex);
        }
    }

    public Car findRentedCar(Integer customerId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CAR_BY_CUSTOMER_ID)) {

            stmt.setObject(1, customerId);
            ResultSet rs = stmt.executeQuery();

            return rs.next() ? mapCar(rs) : null;

        } catch (SQLException ex) {
            throw new DAOException("Failed to receive data: ", ex);
        }
    }

    private Car mapCar(ResultSet rs) throws SQLException {
        Integer car_id = (Integer) rs.getObject("id");
        String name = rs.getString("name");
        Integer company_id = (Integer) rs.getObject("company_id");
        Integer customer_id = (Integer) rs.getObject("customer_id");

        return new Car(car_id, name, company_id, customer_id);
    }
}
