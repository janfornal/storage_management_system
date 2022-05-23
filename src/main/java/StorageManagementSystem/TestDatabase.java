package StorageManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class TestDatabase {
    public static void main( String args[] ) {
        try {
            DatabaseManager dm = new DatabaseManager(new Service() {});
            System.out.println(dm.getProductsNameFromCategoryId(10));

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
    }
}
