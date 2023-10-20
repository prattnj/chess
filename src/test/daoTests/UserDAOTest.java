package daoTests;

import dao.UserDAO;
import dao.mysql.MySQLUserDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
import model.bean.UserBean;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {

    private Database db;
    private UserDAO dao;

    private final UserBean dummy1 = new UserBean(123, "njpratt", "fake", "email");

    @BeforeEach
    public void setup() throws DataAccessException {
        db = Database.getInstance();
        try {
            dao = new MySQLUserDAO(db.getConnection());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        dao.clear();
    }

    @AfterEach
    public void cleanup() {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        dao.insert(dummy1);
        UserBean test = dao.find(dummy1.getUserID());
        assertNotNull(test);
    }

    @Test
    public void insertFail() throws DataAccessException {
        dao.insert(dummy1);
        assertThrows(DataAccessException.class, () -> dao.insert(dummy1));
    }

    @Test
    public void findPass() throws DataAccessException {
        dao.insert(dummy1);
        assertNotNull(dao.find(dummy1.getUserID()));
        assertNotNull(dao.find(dummy1.getUsername()));
    }

    @Test
    public void findFail() throws DataAccessException {
        assertNull(dao.find(dummy1.getUsername()));
        assertNull(dao.find(dummy1.getUserID()));
    }

}
