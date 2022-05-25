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

    public DatabaseManager(Service s, String ip, Integer port) {
        this(new DatabaseConnectionFactory(s, ip, port));
    }

    // these variables should either be all nulls or all non-nulls
    private Connection conn;
    private PreparedStatement getPriceOfProductFromId, getGrossOfProductFromId;
    private PreparedStatement getProductNameFromId, getIdFromProductName;
    private PreparedStatement getProductsIdFromCategoryId, getProductsNameFromCategoryId;
    private PreparedStatement getProductPropertiesFromId;
    private PreparedStatement addNewDelivery;
    private PreparedStatement checkLoginExist, checkPasswordCorrect, getFirstName, getSurname;
    private PreparedStatement getTableOfProducts;

    public void close() {
        if (conn == null)
            return;
        try {
            getPriceOfProductFromId.close();
            getGrossOfProductFromId.close();
            getProductNameFromId.close();
            getIdFromProductName.close();
            getProductsIdFromCategoryId.close();
            getProductsNameFromCategoryId.close();
            getProductPropertiesFromId.close();
            addNewDelivery.close();
            checkLoginExist.close();
            checkPasswordCorrect.close();
            getFirstName.close();
            getSurname.close();
            getTableOfProducts.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace(Service.ERROR_STREAM);
        } finally {
            conn = null;
            getPriceOfProductFromId = getGrossOfProductFromId = null;
            getProductNameFromId = getIdFromProductName = null;
            getProductsIdFromCategoryId = getProductsNameFromCategoryId = null;
            getProductPropertiesFromId = null;
            addNewDelivery = null;
            checkLoginExist = checkPasswordCorrect = getFirstName = getSurname = null;
            getTableOfProducts = null;
        }
    }

    private void openConnection() {
        close();

        try {
            conn = connectionSupplier.get();
            initSchema();

            getPriceOfProductFromId = conn.prepareStatement("SELECT * FROM get_current_price(?)");
            getGrossOfProductFromId = conn.prepareStatement("SELECT * FROM get_gross_price(?)");

            getProductNameFromId = conn.prepareStatement("SELECT name FROM PRODUCTS WHERE id = ?");
            getIdFromProductName = conn.prepareStatement("SELECT id FROM PRODUCTS WHERE name = ?");

            getProductsIdFromCategoryId = conn.prepareStatement("SELECT id FROM PRODUCTS WHERE id_category = ?");
            getProductsNameFromCategoryId = conn.prepareStatement("SELECT name FROM PRODUCTS WHERE id_category = ?");

            getProductPropertiesFromId = conn.prepareStatement("SELECT " +
                    "(SELECT name FROM parameters WHERE parameters.id_parameter = pp.id_parameter), " +
                    "quantity FROM parameter_products pp WHERE id_product = ?");

            addNewDelivery = conn.prepareStatement("INSERT INTO deliveries (id_supplier, date_delivery) VALUES (?, ?)");

            checkLoginExist = conn.prepareStatement("SELECT * FROM COALESCE((SELECT 'TRUE' FROM employees WHERE login = ?),'FALSE')");
            checkPasswordCorrect = conn.prepareStatement("SELECT * FROM COALESCE((SELECT 'TRUE' FROM employees WHERE login = ? AND password = ?),'FALSE')");
            getFirstName = conn.prepareStatement("SELECT first_name FROM employees WHERE login = ?");
            getSurname = conn.prepareStatement("SELECT last_name FROM employees WHERE login = ?");

            getTableOfProducts = conn.prepareStatement("SELECT * FROM nice_repr_of_products()");

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

    private String queryString(PreparedStatement st) throws SQLException {
        try (ResultSet rs = st.executeQuery()) {
            Service.DB_QUERY_CALL_STREAM.println(st);
            String res = rs.next() ? rs.getString(1) : null;
            Service.DB_QUERY_RESULT_STREAM.println(st + "\tresult: " + res);
            return res;
        }
    }

    private boolean queryBoolean(PreparedStatement st) throws SQLException {
        try (ResultSet rs = st.executeQuery()) {
            Service.DB_QUERY_CALL_STREAM.println(st);
            Boolean res = rs.next() ? rs.getBoolean(1) : null;
            Service.DB_QUERY_RESULT_STREAM.println(st + "\tresult: " + res);
            return res;
        }
    }

    private ArrayList<String> queryStringList(PreparedStatement st) throws SQLException {
        try (ResultSet rs = st.executeQuery()) {
            Service.DB_QUERY_CALL_STREAM.println(st);
            ArrayList<String> returnList = new ArrayList<>();
            int colcnt = rs.getMetaData().getColumnCount();
            StringBuilder returnBuilder = new StringBuilder();
            while(rs.next()) {
                returnBuilder.setLength(0);
                for(int i=1; i<=colcnt; i++) {
                    returnBuilder.append(rs.getString(i));
                    if(i < colcnt) returnBuilder.append(" ");
                }
                returnList.add(returnBuilder.toString());
            }
            Service.DB_QUERY_RESULT_STREAM.println(st + "\tresult: " + returnList);
            return returnList;
        }
    }

    private ArrayList<ProductRepr> queryProductList(PreparedStatement st) throws SQLException {
        try (ResultSet rs = st.executeQuery()) {
            Service.DB_QUERY_CALL_STREAM.println(st);
            ArrayList<ProductRepr> returnList = new ArrayList<>();
            while(rs.next()) {
                returnList.add(new ProductRepr(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getDouble(6)));
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

    public Double getGrossOfProductFromId(int w) {
        try {
            getGrossOfProductFromId.setInt(1, w);
            return queryDouble(getGrossOfProductFromId);
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

    public void addNewDelivery(int id_supplier, String data) {   /// class date
        try {
            addNewDelivery.setInt(1, id_supplier);
            addNewDelivery.setString(2, data);
            update(addNewDelivery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> getProductPropertiesFromId(int id) {
        try {
            getProductPropertiesFromId.setInt(1, id);
            return queryStringList(getProductPropertiesFromId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean checkLoginExist(String login) {
        try {
            checkLoginExist.setString(1, login);
            return queryBoolean(checkLoginExist);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean checkPasswordCorrect(String login, Integer password) {
        try {
            checkPasswordCorrect.setString(1, login);
            checkPasswordCorrect.setInt(2, password);
            return queryBoolean(checkPasswordCorrect);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFirstName(String name) {
        try {
            getFirstName.setString(1, name);
            return queryString(getFirstName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSurname(String name) {
        try {
            getSurname.setString(1, name);
            return queryString(getSurname);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ProductRepr> getTableOfProducts() {
        try {
            return queryProductList(getTableOfProducts);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

