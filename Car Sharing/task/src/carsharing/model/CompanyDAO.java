package carsharing.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO {
    private static final String DEFAULT_DB_NAME = "default_car_sharing_db";
    private static final String PATH = "jdbc:h2:./src/carsharing/db/";
    private static final String DRIVER = "org.h2.Driver";
    private static final String SQL_CREATE_COMPANY_TABLE = "CREATE TABLE IF NOT EXISTS company (" +
            "id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
            "name VARCHAR(255) UNIQUE NOT NULL" +
            ")";

    private final String dbFilename;

    public CompanyDAO(String dbFilename) throws ClassNotFoundException {
        this.dbFilename = PATH + dbFilename;
        Class.forName(DRIVER);
    }

    public CompanyDAO() throws ClassNotFoundException {
       this(DEFAULT_DB_NAME);
    }

    public void createCompanyTable() {
        try (Connection conn = DriverManager.getConnection(dbFilename);
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(true);
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

//        if (findCompanyByName(name) != null) {
//            return;
//        }

        try (Connection conn = DriverManager.getConnection(dbFilename);
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(true);
            stmt.executeUpdate(sql);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // todo return either Error or Success with payload
    public Company findCompanyByName(String name) {
        String sql = "SELECT * FROM company WHERE name = '" + name + "'";
        try (Connection conn = DriverManager.getConnection(dbFilename);
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(true);
            ResultSet resultSet = stmt.executeQuery(sql);

            int companyId = resultSet.getInt("id");
            String companyName = resultSet.getString("name");
            return new Company(companyId, companyName);

        } catch (SQLException ex) {
            return null;
        }
    }

    public List<Company> findAllCompanies() {
        String sql = "SELECT * FROM company ORDER BY id";
        List<Company> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbFilename);
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(true);
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
}
