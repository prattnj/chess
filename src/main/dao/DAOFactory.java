package dao;

import dao.mysql.MySQLAuthTokenDAO;
import dao.mysql.MySQLGameDAO;
import dao.mysql.MySQLTransaction;
import dao.mysql.MySQLUserDAO;
import dao.ram.RAMAuthTokenDAO;
import dao.ram.RAMGameDAO;
import dao.ram.RAMTransaction;
import dao.ram.RAMUserDAO;
import dataAccess.DataAccessException;
import util.Util;

public class DAOFactory {

    public static UserDAO getNewUserDAO(Transaction transaction) throws DataAccessException {
        if (Util.CURRENT_DAO_TYPE.equals("mysql")) return new MySQLUserDAO(((MySQLTransaction) transaction).getMySQLConnection());
        else return RAMUserDAO.getInstance();
    }

    public static GameDAO getNewGameDAO(Transaction transaction) throws DataAccessException {
        if (Util.CURRENT_DAO_TYPE.equals("mysql")) return new MySQLGameDAO(((MySQLTransaction) transaction).getMySQLConnection());
        else return RAMGameDAO.getInstance();
    }

    public static AuthTokenDAO getNewAuthTokenDAO(Transaction transaction) throws DataAccessException {
        if (Util.CURRENT_DAO_TYPE.equals("mysql")) return new MySQLAuthTokenDAO(((MySQLTransaction) transaction).getMySQLConnection());
        else return RAMAuthTokenDAO.getInstance();
    }

    public static Transaction getNewTransaction() {
        if (Util.CURRENT_DAO_TYPE.equals("mysql")) return new MySQLTransaction();
        else return RAMTransaction.getInstance();
    }

}
