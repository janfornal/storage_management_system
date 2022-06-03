package StorageManagementSystem;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExecuteOrderList {

    public double netPrice;
    public double grossPrice;
    public String createTransaction;
    public String orderList = "";

    public void testTransaction() {
        String exactProcedure = "BEGIN;"
                + createTransaction
                + "RETURNING id_sale AS special_id;"
                + orderList
                + "SELECT * FROM get_current_price(special_id);"
                + "SELECT * FROM get_gross_price(special_id);"
                + "ROLLBACK;"
                + "COMMIT;";
        System.out.println(exactProcedure);
        try {
            ResultSet rs = GUIPresenter.databaseManager.queryAnything(exactProcedure);
            netPrice = rs.getDouble(1);
            grossPrice = rs.getDouble(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
