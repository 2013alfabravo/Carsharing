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
            "company_id INT NOT NULL, available BOOL NOT NULL DEFAULT TRUE, " +
            "CONSTRAINT fk_company FOREIGN KEY (company_id) REFERENCES company(id))";
    private static final String UPD_CUSTOMER_CAR_ID = "UPDATE customer SET rented_car_id = ? WHERE id = ?";
    private static final String UPD_CAR_AVAIL = "UPDATE car SET available = FALSE WHERE id = ?";
    private static final String CREATE_CAR = "INSERT INTO car(name, company_id) VALUES(?, ?)";
    private static final String ALL_CARS_BY_COMP_ID = "SELECT * FROM car WHERE company_id = ? ORDER BY id";
    private static final String AVAIL_CARS_BY_COMP_ID = "SELECT * FROM car WHERE company_id = ? " +
            "AND available IS TRUE ORDER BY id";
    private static final String CREATE_COMPANY = "INSERT INTO company(name) VALUES(?))";
    private static final String ALL_COMPANIES = "SELECT * FROM company ORDER BY id";
    private static final String CREATE_CUSTOMER = "INSERT INTO customer(name) VALUES(?)";
    private static final String ALL_CUSTOMERS = "SELECT * FROM customer ORDER BY id";
    private static final String CAR_BY_CUSTOMER_ID = "SELECT b.id, b.name, b.company_id, b.available " +
            "FROM customer AS a JOIN car AS b ON a.rented_car_id = b.id WHERE a.id = ?";

    private final DataSource dataSource;

    public CarSharingDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // todo merge table creation to a single method and do it in a batch
    public void createCarTable() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_CAR_TABLE)) {

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
        List<Car> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(ALL_CARS_BY_COMP_ID)) {

            stmt.setObject(1, companyId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                // todo move to mapCar method
                int carId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int carCompanyId = resultSet.getInt("company_id");
                Boolean available = (Boolean) resultSet.getObject("available");
                list.add(new Car(carId, name, carCompanyId, available));
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
                // todo move to mapCar method
                int carId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int carCompanyId = resultSet.getInt("company_id");
                Boolean available = (Boolean) resultSet.getObject("available");
                list.add(new Car(carId, name, carCompanyId, available));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // todo merge table creation to a single method and do it in a batch
    public void createCompanyTable() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_COMPANY_TABLE)) {

            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
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

    // fixme combine the table creation methods into a single transaction
    public void createCustomerTable() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_CUSTOMER_TABLE)) {

            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
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

    public void addRentedCar(Customer customer) {
        setRentedCar(customer.getId(), customer.getRentedCarId(), true);
    }

    public void removeRentedCar(Customer customer) {
        setRentedCar(customer.getId(), customer.getRentedCarId(), false);
    }

    private void setRentedCar(Integer customerId, Integer carId, boolean rented) {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt1 = conn.prepareStatement(UPD_CUSTOMER_CAR_ID);
                 PreparedStatement stmt2 = conn.prepareStatement(UPD_CAR_AVAIL) ) {

                conn.setAutoCommit(false);

                stmt1.setObject(1, carId);
                stmt1.setObject(2, customerId);
                stmt2.setBoolean(1, rented);

                int rows = stmt1.executeUpdate();
                rows += stmt2.executeUpdate();

                if (rows == 0) {
                    throw new SQLException("No rows were affected, rolling back the transaction.");
                }

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw new DAOException(e);
            }
        } catch (SQLException | NullPointerException ex) {
            throw new DAOException("Database connection failure: ", ex);
        }
    }

    public Car findRentedCar(Integer customerId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CAR_BY_CUSTOMER_ID)) {

            stmt.setObject(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // todo do car mapping in a method
                Integer cid = (Integer) rs.getObject("id");
                String name = rs.getString("name");
                Integer companyId = (Integer) rs.getObject("company_id");
                Boolean available = (Boolean) rs.getObject("available");

                return new Car(cid, name, companyId, available);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
