package dao.mysql;

import dao.Transaction;
import dataAccess.DataAccessException;
import dataAccess.Database;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLTransaction implements Transaction {

    private final Database db = Database.getInstance();

    @Override
    public void openTransaction() throws DataAccessException {
        db.getConnection();
    }

    @Override
    public void closeTransaction(boolean commit) {
        db.closeConnection(commit);
    }

    @Override
    public boolean isOpen() throws DataAccessException {
        try {
            if (db.getConnection() == null) return false;
            return !db.getConnection().isClosed();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public Connection getMySQLConnection() throws DataAccessException {
        return db.getConnection();
    }
}
