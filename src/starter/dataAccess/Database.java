package dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class responsible for creating connections to the database
 */
public class Database {

    /**
     * The connection to the database
     */
    private Connection conn;

    private static final Database instance = new Database();

    private Database() {}

    public static Database getInstance() {
        return instance;
    }

    /**
     * Initiates a connection to the database
     * @return new Connection object
     * @throws DataAccessException
     */
    public Connection openConnection() throws DataAccessException {
        try {
            //shouldn't try to open an active connection
            if (conn != null) throw new DataAccessException("Database connection already open");

            //The Structure for this Connection is driver:language:path
            //The path assumes you start in the root of your project unless given a non-relative path
            final String CONNECTION_URL = "jdbc:mysql://localhost:3306/chess";

            // Open a database connection to the file given in the path
            conn = DriverManager.getConnection(CONNECTION_URL, "pratt", System.getenv("MYSQL_PASSWORD"));

            // Start a transaction
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }

        return conn;
    }

    /**
     * If a connection exists, opens a connection. Otherwise, just uses current connection
     * @return Connection to database
     * @throws DataAccessException
     */
    public Connection getConnection() throws DataAccessException {
        if (conn == null) return openConnection();
        else return conn;
    }

    /**
     * Closes connection to database.
     * @param commit If we want to commit changes to the database. If false, no changes will be made
     * @throws DataAccessException
     */
    public void closeConnection(boolean commit) {
        if (conn == null) return;

        try {
            if (conn.isClosed()) return;

            if (commit) conn.commit();
            else conn.rollback();

            conn.close();
            conn = null;

        } catch (SQLException ignored) {
        }
    }
}
