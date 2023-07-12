package dao.mysql;

import dao.Transaction;
import dataAccess.DataAccessException;
import dataAccess.Database;

import java.sql.Connection;

public class MySQLTransaction implements Transaction {

    private static final Transaction instance = new MySQLTransaction();

    private static final Database db = new Database();

    public static Transaction getInstance() {
        return instance;
    }

    @Override
    public void openTransaction() throws DataAccessException {
        db.getConnection();
    }

    @Override
    public void closeTransaction(boolean commit) {
        db.closeConnection(commit);
    }

    public static Connection getMySQLConnection() throws DataAccessException {
        return db.getConnection();
    }
}
