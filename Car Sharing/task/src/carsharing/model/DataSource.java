package carsharing.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {
    private static final String DEFAULT_DB_NAME = "default_car_sharing_db";
    private static final String PATH = "jdbc:h2:./src/carsharing/db/";
    private static final String DRIVER = "org.h2.Driver";

    private final String url;

    public DataSource(String url) throws ClassNotFoundException {
        Class.forName(DRIVER);
        this.url = PATH + url;
    }

    public DataSource() throws ClassNotFoundException {
        this(DEFAULT_DB_NAME);
    }

    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url);
        connection.setAutoCommit(true);
        return connection;
    }
}
