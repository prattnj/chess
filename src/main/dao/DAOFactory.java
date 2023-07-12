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

    public static UserDAO getNewUserDAO() throws DataAccessException {
        if (Util.CURRENT_DAO_TYPE.equals("mysql")) return new MySQLUserDAO(MySQLTransaction.getMySQLConnection());
        else return RAMUserDAO.getInstance();
    }

    public static GameDAO getNewGameDAO() throws DataAccessException {
        if (Util.CURRENT_DAO_TYPE.equals("mysql")) return new MySQLGameDAO(MySQLTransaction.getMySQLConnection());
        else return RAMGameDAO.getInstance();
    }

    public static AuthTokenDAO getNewAuthTokenDAO() throws DataAccessException {
        if (Util.CURRENT_DAO_TYPE.equals("mysql")) return new MySQLAuthTokenDAO(MySQLTransaction.getMySQLConnection());
        else return RAMAuthTokenDAO.getInstance();
    }

    public static Transaction getNewTransaction() {
        if (Util.CURRENT_DAO_TYPE.equals("mysql")) return MySQLTransaction.getInstance();
        else return RAMTransaction.getInstance();
    }

}
