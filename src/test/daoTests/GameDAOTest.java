package daoTests;

import chess.ChessGame;
import dao.GameDAO;
import dao.mysql.MySQLGameDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
import model.bean.GameBean;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameDAOTest {

    private Database db;
    private GameDAO dao;

    private final GameBean dummy1 = new GameBean(68, 69, 70, "test", "gameNameTest");

    @BeforeEach
    public void setup() throws DataAccessException {
        db = Database.getInstance();
        try {
            dao = new MySQLGameDAO(db.getConnection());
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
        GameBean test = dao.find(dummy1.getGameID());
        assertNotNull(test);
        assertDoesNotThrow(() -> dao.insert(new GameBean(23, null, 2, "null", "name")));
    }

    @Test
    public void insertFail() throws DataAccessException {
        dao.insert(dummy1);
        assertThrows(DataAccessException.class, () -> dao.insert(dummy1));
    }

    @Test
    public void findPass() throws DataAccessException {
        dao.insert(dummy1);
        assertNotNull(dao.find(dummy1.getGameID()));
    }

    @Test
    public void findFail() throws DataAccessException {
        assertNull(dao.find(dummy1.getGameID()));
    }

    @Test
    public void testClaimSpot() throws DataAccessException {
        dao.insert(dummy1);
        dao.claimSpot(dummy1.getGameID(), ChessGame.TeamColor.WHITE, 0);
        GameBean bean = dao.find(dummy1.getGameID());
        assertNull(bean.getWhitePlayerID());
    }
}
