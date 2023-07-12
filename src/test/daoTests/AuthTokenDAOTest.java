package daoTests;

import dao.AuthTokenDAO;
import dao.mysql.MySQLAuthTokenDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
import model.bean.AuthTokenBean;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenDAOTest {

    private Database db;
    private AuthTokenDAO dao;

    private final AuthTokenBean dummy1 = new AuthTokenBean("test-token", 123);

    @BeforeEach
    public void setup() throws DataAccessException {
        db = new Database();
        try {
            dao = new MySQLAuthTokenDAO(db.getConnection());
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
        assertNotNull(dao.find(dummy1.getUserID()));
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
    }

    @Test
    public void findFail() throws DataAccessException {
        assertNull(dao.find(dummy1.getUserID()));
    }

}
