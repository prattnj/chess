package dao;

import dataAccess.DataAccessException;

public interface Transaction {

    /**
     * Starts a transaction
     * @throws DataAccessException
     */
    void openTransaction() throws DataAccessException;

    /**
     * Closes a transaction
     * @param commit A boolean representing whether to commit the changes
     * @throws DataAccessException
     */
    void closeTransaction(boolean commit) throws DataAccessException;

}
