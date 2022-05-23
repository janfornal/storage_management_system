package StorageManagementSystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * This class is not thread-safe
 */
public class DatabaseManager {
    private final Supplier<Connection> connectionSupplier;

    public DatabaseManager(Supplier<Connection> connectionFactory) {
        connectionSupplier = connectionFactory;

        openConnection();
    }

    public DatabaseManager(Service s) {
        this(new DatabaseConnectionFactory(s));
    }

    // these variables should either be all nulls or all non-nulls
    private Connection conn;
    private PreparedStatement getPriceOfProductFromId;
    private PreparedStatement getProductNameFromId, getIdFromProductName;
    private PreparedStatement getProductsIdFromCategoryId, getProductsNameFromCategoryId;

    public void close() {
        if (conn == null)
            return;
        try {
            getPriceOfProductFromId.close();
            getProductNameFromId.close();
            getIdFromProductName.close();
            getProductsIdFromCategoryId.close();
            getProductsNameFromCategoryId.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace(Service.ERROR_STREAM);
        } finally {
            conn = null;
            getPriceOfProductFromId = null;
            getProductNameFromId = getIdFromProductName = null;
            getProductsIdFromCategoryId = getProductsNameFromCategoryId = null;
        }
    }

    private void openConnection() {
        close();

        try {
            conn = connectionSupplier.get();
            initSchema();

            getPriceOfProductFromId = conn.prepareStatement("SELECT net_price" +
                    "    FROM PRICE_HISTORY" +
                    "    WHERE id_product = ?" +
                    "    ORDER BY launch_date DESC" +
                    "    LIMIT 1");

            getProductNameFromId = conn.prepareStatement("SELECT name FROM PRODUCTS WHERE id = ?");
            getIdFromProductName = conn.prepareStatement("SELECT id FROM PRODUCTS WHERE name = ?");

            getProductsIdFromCategoryId = conn.prepareStatement("SELECT id FROM PRODUCTS WHERE id_category = ?");
            getProductsNameFromCategoryId = conn.prepareStatement("SELECT name FROM PRODUCTS WHERE id_category = ?");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initSchema() throws SQLException {
    }

    private int update(PreparedStatement st) throws SQLException {
        Service.DB_QUERY_CALL_STREAM.println(st);
        int res = st.executeUpdate();
        Service.DB_QUERY_RESULT_STREAM.println(st + "\tresult: " + res);
        return res;
    }

    private int updateOnce(PreparedStatement st) throws SQLException {
        int res = update(st);
        st.close();
        return res;
    }

    private Double queryDouble(PreparedStatement st) throws SQLException {
        try (ResultSet rs = st.executeQuery()) {
            Service.DB_QUERY_CALL_STREAM.println(st);
            Double res = rs.next() ? rs.getDouble(1) : null;
            Service.DB_QUERY_RESULT_STREAM.println(st + "\tresult: " + res);
            return res;
        }
    }

    private ArrayList<String> queryStringList(PreparedStatement st) throws SQLException {
        try (ResultSet rs = st.executeQuery()) {
            Service.DB_QUERY_CALL_STREAM.println(st);
            ArrayList<String> returnList = new ArrayList<>();
            while(rs.next()) {
                returnList.add(rs.getString(1));
            }
            Service.DB_QUERY_RESULT_STREAM.println(st + "\tresult: " + returnList);
            return returnList;
        }
    }

    public Double getPriceOfProductFromId(int w) {
        try {
            getPriceOfProductFromId.setInt(1, w);
            return queryDouble(getPriceOfProductFromId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> getProductsNameFromCategoryId(int w) {
        try {
            getProductsNameFromCategoryId.setInt(1, w);
            return queryStringList(getProductsNameFromCategoryId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

