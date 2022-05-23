package StorageManagementSystem;

import java.sql.*;
import java.util.function.Supplier;

/**
 * This class is not thread-safe
 */
public class DatabaseConnectionFactory implements Supplier<Connection> {
    private static final String PSQL_DB_DRIVER = "org.postgresql.Driver";
    private static final String PSQL_DB_CONNECTION = "jdbc:postgresql://20.203.217.128:5432/storage";
    private static final String PSQL_DB_USER = "storagemanagementsystem";
    private static final String PSQL_DB_PASSWORD = "systempassword";

    static {
        try {
            Class.forName(PSQL_DB_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(Service.DB_DRIVER_ERROR_STREAM);
        }
    }

    private final Service service;
    public DatabaseConnectionFactory(Service s) {
        service = s;
    }

    private Connection get(String url, String user, String password) {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace(Service.DB_CONNECTION_ERROR_STREAM);
            return null;
        }
    }

    @Override
    public Connection get() {
        Connection psql = get(PSQL_DB_CONNECTION, PSQL_DB_USER, PSQL_DB_PASSWORD);

        if (psql != null) {
            Service.DB_CONNECTION_INFO_STREAM.println("connected to postgres server");
            return psql;
        }

        throw new RuntimeException("unable to connect to database");
    }
}
