package carsharing.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {
    private static final String DEFAULT_DB_NAME = "default_car_sharing_db";
    private static final String PATH = "jdbc:h2:./src/carsharing/db/";
    private static final String DRIVER = "org.h2.Driver";
    private static final String SQL_CREATE_CAR_TABLE = "CREATE TABLE IF NOT EXISTS car (" +
            "id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
            "name VARCHAR(255) UNIQUE NOT NULL," +
            "company_id INT NOT NULL, " +
            "available BOOL NOT NULL DEFAULT TRUE, " +
            "CONSTRAINT fk_company FOREIGN KEY (company_id) REFERENCES company(id)" +
            ")";

    private final String dbFilename;

    public CarDAO(String dbFilename) throws ClassNotFoundException {
        this.dbFilename = PATH + dbFilename;
        Class.forName(DRIVER);
    }

    public CarDAO() throws ClassNotFoundException {
        this(DEFAULT_DB_NAME);
    }

    public void createCarTable() {
        try (Connection conn = DriverManager.getConnection(dbFilename);
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(true);
            stmt.executeUpdate(SQL_CREATE_CAR_TABLE);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addCar(String name, int companyId) {
        String sql = "INSERT INTO car(" +
                "id, name, company_id) " +
                "VALUES(NULL, '" + name + "', " + companyId + ")";

        try (Connection conn = DriverManager.getConnection(dbFilename);
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(true);
            stmt.executeUpdate(sql);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Car> findCarsByCompanyId(int companyId) {
        String sql = "SELECT * FROM car WHERE company_id = " + companyId + " ORDER BY id";
        List<Car> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbFilename);
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(true);
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
        try (Connection conn = DriverManager.getConnection(dbFilename);
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(true);
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
        try (Connection conn = DriverManager.getConnection(dbFilename);
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(true);
            stmt.executeUpdate(sql);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
