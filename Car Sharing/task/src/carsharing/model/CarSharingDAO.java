package carsharing.model;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

// fixme for testing remove IF NOT EXISTS from table creation queries
public class CarSharingDAO {
    private static final String CREATE_CUSTOMER_TABLE = "CREATE TABLE IF NOT EXISTS customer (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) UNIQUE NOT NULL, " +
            "rented_car_id INT, CONSTRAINT fk_car FOREIGN KEY (rented_car_id) " +
            "REFERENCES car(id))";
    private static final String CREATE_COMPANY_TABLE = "CREATE TABLE IF NOT EXISTS company (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) UNIQUE NOT NULL)";
    private static final String CREATE_CAR_TABLE = "CREATE TABLE IF NOT EXISTS car (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) UNIQUE NOT NULL, " +
            "company_id INT NOT NULL, customer_id INT DEFAULT NULL, " +
            "CONSTRAINT fk_company FOREIGN KEY (company_id) REFERENCES company(id))";
    private static final String UPD_CUSTOMER_CAR_ID = "UPDATE customer SET rented_car_id = ? WHERE id = ?";
    private static final String UPD_CAR_CUSTOMER_ID = "UPDATE car SET customer_id = ? WHERE id = ?";
    private static final String CREATE_CAR = "INSERT INTO car(name, company_id) VALUES(?, ?)";
    private static final String ALL_CARS_BY_COMP_ID = "SELECT * FROM car WHERE company_id = ? ORDER BY id";
    private static final String AVAIL_CARS_BY_COMP_ID = "SELECT * FROM car WHERE company_id = ? " +
            "AND customer_id IS NULL ORDER BY id";
    private static final String CREATE_COMPANY = "INSERT INTO company(name) VALUES(?)";
    private static final String ALL_COMPANIES = "SELECT id, name FROM company ORDER BY id";
    private static final String CREATE_CUSTOMER = "INSERT INTO customer(name) VALUES(?)";
    private static final String ALL_CUSTOMERS = "SELECT * FROM customer ORDER BY id";
    private static final String CAR_BY_CUSTOMER_ID = "SELECT * FROM car WHERE customer_id = ?";
    private static final String SQL_ERROR = "SQL query failed.";
    private static final String FIND_COMPANY = "SELECT id, name FROM company WHERE id = ?";
    private static final String GET_CAR_FROM_CUSTOMER = "SELECT rented_car_id FROM customer WHERE id = ?";
    private static final String GET_CUSTOMER_FROM_CAR = "SELECT customer_id FROM car WHERE id = ?";

    private static final Logger LOGGER = Logger.getLogger(CarSharingDAO.class.getName());

    private final DataSource dataSource;

    public CarSharingDAO(DataSource dataSource) {
        this.dataSource = dataSource;

        LOGGER.setLevel(Level.INFO);

        Handler handler;
        try {
            handler = new FileHandler("log.txt", true);
        } catch (IOException e) {
            handler = new ConsoleHandler();
        }

        handler.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(handler);
        LOGGER.info("Initiated.");
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

            LOGGER.info(stmt.toString());
            stmt.executeUpdate();

        } catch (SQLException ex) {
            LOGGER.info(ex.getSQLState());
            throw new DAOException(SQL_ERROR, ex);
        }
    }

    public void dropTables() {
        try (Connection conn = dataSource.getConnection()) {

            try {
                Statement statement = conn.createStatement();
                statement.executeUpdate("DROP TABLE IF EXISTS customer");
                statement.executeUpdate("DROP TABLE IF EXISTS car");
                statement.executeUpdate("DROP TABLE IF EXISTS company");
            } catch (SQLException ex) {
                LOGGER.info(ex.getSQLState());
            }

        } catch (SQLException e) {
            LOGGER.info(e.getSQLState());
            throw new DAOException(SQL_ERROR);
        }
    }

    public void createCar(String name, Integer companyId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_CAR)) {

            stmt.setString(1, name);
            stmt.setObject(2, companyId);
            LOGGER.info(stmt.toString());
            stmt.executeUpdate();

        } catch (SQLException ex) {
            LOGGER.info(ex.getSQLState());
            throw new DAOException(SQL_ERROR, ex);
        }
    }

    public List<Car> findAllCarsByCompanyId(Integer companyId) {
        return findCars(companyId, true);
    }

    public List<Car> findAvailableCarsByCompanyId(Integer companyId) {
        return findCars(companyId, false);
    }

    private List<Car> findCars(Integer id, boolean all) {
        String sql = all ? ALL_CARS_BY_COMP_ID : AVAIL_CARS_BY_COMP_ID;
        List<Car> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            LOGGER.info(stmt.toString());
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                list.add(mapCar(resultSet));
            }

        } catch (SQLException ex) {
            LOGGER.info(ex.getSQLState());
            throw new DAOException(SQL_ERROR, ex);
        }
        return list;
    }

    public void createCompany(String name) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_COMPANY)) {

            stmt.setString(1, name);
            LOGGER.info(stmt.toString());
            stmt.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DAOException("Integrity constraint violated", e);
        } catch (SQLException ex) {
            LOGGER.info(ex.getSQLState());
            throw new DAOException(SQL_ERROR, ex);
        }
    }

    public List<Company> findAllCompanies() {
        List<Company> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(ALL_COMPANIES)) {

            LOGGER.info(stmt.toString());
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                list.add(mapCompany(resultSet));
            }

        } catch (SQLException ex) {
            LOGGER.info(ex.getSQLState());
            throw new DAOException(SQL_ERROR, ex);
        }
        return list;
    }

    public void createCustomer(String name) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_CUSTOMER)) {

            stmt.setString(1, name);
            LOGGER.info(stmt.toString());
            stmt.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DAOException("Integrity constraint violated", e);
        } catch (SQLException ex) {
            LOGGER.info(ex.getSQLState());
            throw new DAOException(SQL_ERROR, ex);
        }
    }

    public List<Customer> findAllCustomers() {
        List<Customer> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(ALL_CUSTOMERS)) {

            LOGGER.info(stmt.toString());
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                list.add(mapCustomer(resultSet));
            }

        } catch (SQLException ex) {
            LOGGER.info(ex.getSQLState());
            throw new DAOException(SQL_ERROR, ex);
        }
        return list;
    }

    public void addRentedCar(Integer customerId, Integer carId) {
        if (hasRented(customerId)) {
            throw new DAOException(String.format("id=%d already has a rented car", customerId));
        }
        setRentedCar(customerId, carId, false);
    }

    public void removeRentedCar(Integer customerId, Integer carId) {
        if (isNotRented(carId)) {
            throw new DAOException(String.format("id=%d is not rented", carId));
        }
        setRentedCar(customerId, carId, true);
    }

    private boolean isNotRented(Integer carId) {
        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(GET_CUSTOMER_FROM_CAR)) {

            stmt.setObject(1, carId);
            LOGGER.info(stmt.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getObject("customer_id") == null;
            }
        } catch (SQLException e) {
            LOGGER.info(e.getSQLState());
            throw new DAOException(SQL_ERROR, e);
        }

        return false;
    }

    private boolean hasRented(Integer customerId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_CAR_FROM_CUSTOMER)) {

            stmt.setObject(1, customerId);
            LOGGER.info(stmt.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getObject("rented_car_id") != null;
            }
        } catch (SQLException e) {
            LOGGER.info(e.getSQLState());
            throw new DAOException(SQL_ERROR, e);
        }

        return false;
    }

    private void setRentedCar(Integer customerId, Integer carId, boolean isRented) {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt1 = conn.prepareStatement(UPD_CUSTOMER_CAR_ID);
                 PreparedStatement stmt2 = conn.prepareStatement(UPD_CAR_CUSTOMER_ID) ) {

                conn.setAutoCommit(false);

                stmt1.setObject(1, isRented ? null : carId);
                stmt1.setObject(2, customerId);
                LOGGER.info(stmt1.toString());

                stmt2.setObject(1, isRented ? null : customerId);
                stmt2.setObject(2, carId);
                LOGGER.info(stmt2.toString());

                int rows = stmt1.executeUpdate() + stmt2.executeUpdate();
                if (rows != 2) {
                    throw new SQLException(SQL_ERROR);
                }

                conn.commit();
                conn.setAutoCommit(true);

            } catch (SQLException e) {
                conn.rollback();
                throw new DAOException("Transaction failed.", e);
            }
        } catch (SQLException | NullPointerException ex) {
            LOGGER.info(ex.getMessage());
            throw new DAOException(SQL_ERROR, ex);
        }
    }

    public Car findRentedCar(Integer customerId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CAR_BY_CUSTOMER_ID)) {

            stmt.setObject(1, customerId);
            LOGGER.info(stmt.toString());
            ResultSet rs = stmt.executeQuery();

            return rs.next() ? mapCar(rs) : null;

        } catch (SQLException ex) {
            LOGGER.info(ex.getSQLState());
            throw new DAOException(SQL_ERROR, ex);
        }
    }

    public Company findCompanyById(Integer companyId) {
        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(FIND_COMPANY)) {

            stmt.setObject(1, companyId);
            LOGGER.info(stmt.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapCompany(rs);
            }
        } catch (SQLException e) {
            LOGGER.info(e.getSQLState());
            throw new DAOException(SQL_ERROR, e);
        }

        return null;
    }

    private Car mapCar(ResultSet rs) throws SQLException {
        Integer car_id = (Integer) rs.getObject("id");
        String name = rs.getString("name");
        Integer company_id = (Integer) rs.getObject("company_id");
        Integer customer_id = (Integer) rs.getObject("customer_id");

        return new Car(car_id, name, company_id, customer_id);
    }

    private Company mapCompany(ResultSet rs) throws SQLException {
        Integer id = (Integer) rs.getObject("id");
        String name = rs.getString("name");

        return new Company(id, name);
    }

    private Customer mapCustomer(ResultSet rs) throws SQLException {
        Integer id = (Integer) rs.getObject("id");
        String name = rs.getString("name");
        Integer carId = (Integer) rs.getObject("rented_car_id");

        return new Customer(id, name, carId);
    }
}
