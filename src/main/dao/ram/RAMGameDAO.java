package dao.ram;

import chess.ChessGame;
import dao.GameDAO;
import model.bean.GameBean;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RAMGameDAO implements GameDAO {

    private static final GameDAO instance = new RAMGameDAO();
    private final Map<Integer, GameBean> table = new HashMap<>();

    public static GameDAO getInstance() {
        return instance;
    }

    @Override
    public void insert(GameBean bean) {
        table.put(bean.getGameID(), bean);
    }

    @Override
    public GameBean find(int gameID) {
        return table.get(gameID);
    }

    @Override
    public Collection<GameBean> findAll() {
        return table.values();
    }

    @Override
    public void update(GameBean game) {
        insert(game);
    }

    @Override
    public void delete(int gameID) {
        table.remove(gameID);
    }

    @Override
    public void clear() {
        table.clear();
    }

    @Override
    public void claimSpot(int gameID, ChessGame.TeamColor color, int playerID) {
        GameBean bean = table.get(gameID);
        if (color == ChessGame.TeamColor.WHITE) bean.setWhitePlayerID(playerID);
        else bean.setBlackPlayerID(playerID);
    }
}
