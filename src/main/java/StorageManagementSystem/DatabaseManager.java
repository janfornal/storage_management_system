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
    private PreparedStatement getProductsIdFromCategoryId, getProductsNameFromCategoryId, getProductsFromCategoryName;
    private PreparedStatement getAllCategories;
    private PreparedStatement getProductPropertiesFromId;
    private PreparedStatement addNewDelivery, addNewDeliveryProduct;
    private PreparedStatement checkLoginExist, checkPasswordCorrect, getFirstName, getSurname;
    private PreparedStatement getTableOfProducts;
    private PreparedStatement registerNewEmployee;

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
            getProductsFromCategoryName.close();
            getAllCategories.close();
            getProductPropertiesFromId.close();
            addNewDelivery.close();
            addNewDeliveryProduct.close();
            checkLoginExist.close();
            checkPasswordCorrect.close();
            getFirstName.close();
            getSurname.close();
            getTableOfProducts.close();
            registerNewEmployee.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace(Service.ERROR_STREAM);
        } finally {
            conn = null;
            getPriceOfProductFromId = getGrossOfProductFromId = null;
            getProductNameFromId = getIdFromProductName = null;
            getProductsIdFromCategoryId = getProductsNameFromCategoryId = getProductsFromCategoryName = null;
            getAllCategories = null;
            getProductPropertiesFromId = null;
            addNewDelivery = addNewDeliveryProduct = null;
            checkLoginExist = checkPasswordCorrect = getFirstName = getSurname = null;
            getTableOfProducts = null;
            registerNewEmployee = null;
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
            getProductsFromCategoryName = conn.prepareStatement("SELECT * FROM nice_repr_of_products() WHERE category = ?");

            getAllCategories = conn.prepareStatement("SELECT * FROM CATEGORIES");

            getProductPropertiesFromId = conn.prepareStatement("SELECT " +
                    "(SELECT name FROM parameters WHERE parameters.id_parameter = pp.id_parameter), " +
                    "quantity FROM parameter_products pp WHERE id_product = ?");

            addNewDelivery = conn.prepareStatement("INSERT INTO deliveries (id_supplier, date_delivery) VALUES (?, ?) RETURNING id_delivery");
            addNewDeliveryProduct = conn.prepareStatement("INSERT INTO products_deliveries (id_delivery, id_product, quantity) VALUES (?, ?, ?)");

            checkLoginExist = conn.prepareStatement("SELECT * FROM COALESCE((SELECT 'TRUE' FROM employees WHERE login = ?),'FALSE')");
            checkPasswordCorrect = conn.prepareStatement("SELECT * FROM COALESCE((SELECT 'TRUE' FROM employees WHERE login = ? AND password = ?),'FALSE')");
            getFirstName = conn.prepareStatement("SELECT first_name FROM employees WHERE login = ?");
            getSurname = conn.prepareStatement("SELECT last_name FROM employees WHERE login = ?");

            getTableOfProducts = conn.prepareStatement("SELECT * FROM nice_repr_of_products()");

            registerNewEmployee = conn.prepareStatement("INSERT INTO employees (\"login\", \"password\", first_name, last_name) VALUES (?, ?, ?, ?)");

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

    private Integer queryInteger(PreparedStatement st) throws SQLException {
        try (ResultSet rs = st.executeQuery()) {
            Service.DB_QUERY_CALL_STREAM.println(st);
            Integer res = rs.next() ? rs.getInt(1) : null;
            Service.DB_QUERY_RESULT_STREAM.println(st + "\tresult: " + res);
            return res;
        }
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

    private ArrayList<CategoryRecord> queryCategoryList(PreparedStatement st) throws SQLException {
        try (ResultSet rs = st.executeQuery()) {
            Service.DB_QUERY_CALL_STREAM.println(st);
            ArrayList<CategoryRecord> returnList = new ArrayList<>();
            while(rs.next()) {
                returnList.add(new CategoryRecord(rs.getInt(1), rs.getString(2)));
            }
            Service.DB_QUERY_RESULT_STREAM.println(st + "\tresult: " + returnList);
            return returnList;
        }
    }

    public ResultSet queryAnything(String statement) throws SQLException {
        PreparedStatement st = conn.prepareStatement(statement);
        try (ResultSet rs = st.executeQuery()) {
            Service.DB_QUERY_CALL_STREAM.println(st);
            st.close();
            return rs;
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

    public int addNewDelivery(int id_supplier, Date data) {   /// class date
        try {
            addNewDelivery.setInt(1, id_supplier);
            addNewDelivery.setDate(2, data);
            return queryInteger(addNewDelivery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addNewDeliveryProduct(int id_delivery, int id_product, double quantity) {   /// class date
        try {
            addNewDeliveryProduct.setInt(1, id_delivery);
            addNewDeliveryProduct.setInt(2, id_product);
            addNewDeliveryProduct.setDouble(3, quantity);
            update(addNewDeliveryProduct);
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

    public Boolean checkPasswordCorrect(String login, String password) {
        try {
            checkPasswordCorrect.setString(1, login);
            checkPasswordCorrect.setString(2, password);
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

    public ArrayList<ProductRepr> getProductsFromCategoryName(String category) {
        try {
            getProductsFromCategoryName.setString(1, category);
            return queryProductList(getProductsFromCategoryName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<CategoryRecord> getAllCategories() {
        try {
            return queryCategoryList(getAllCategories);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerNewEmployee(String login, String password, String first_name, String last_name) {
        try {
            registerNewEmployee.setString(1, login);
            registerNewEmployee.setString(2, password);
            registerNewEmployee.setString(3, first_name);
            registerNewEmployee.setString(4, last_name);
            update(registerNewEmployee);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

