package StorageManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class TestDatabase {
    public static void main( String args[] ) {
        String ip = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 5432;
        try {
            DatabaseManager dm = new DatabaseManager(new Service() {}, ip, port);
            GUIPresenter.databaseManager = dm;
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        GUIPresenter.launch(GUIPresenter.class);
    }
}
