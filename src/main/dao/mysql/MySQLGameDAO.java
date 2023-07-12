package dao.mysql;

import chess.ChessGame;
import dao.GameDAO;
import dataAccess.DataAccessException;
import model.bean.GameBean;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MySQLGameDAO implements GameDAO {

    private final Connection conn;

    public MySQLGameDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(GameBean bean) throws DataAccessException {
        String sql = "INSERT INTO game (gameID, whitePlayerID, blackPlayerID, gameName, game) VALUES (?, ?, ?, ?, ?);";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bean.getGameID());
            if (bean.getWhitePlayerID() == null) stmt.setNull(2, Types.INTEGER);
            else stmt.setInt(2, bean.getWhitePlayerID());
            if (bean.getBlackPlayerID() == null) stmt.setNull(3, Types.INTEGER);
            else stmt.setInt(3, bean.getBlackPlayerID());
            stmt.setString(4, bean.getGameName());
            stmt.setString(5, bean.getGame());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public GameBean find(int gameID) throws DataAccessException {
        String sql = "SELECT * FROM game WHERE gameID = ?;";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, gameID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Integer white = rs.getInt(2);
                Integer black = rs.getInt(3);
                if (white == 0) white = null;
                if (black == 0) black = null;
                return new GameBean(rs.getInt(1), white, black, rs.getString(4), rs.getString(5));
            }
            else return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public Collection<GameBean> findAll() throws DataAccessException {
        String sql = "SELECT * FROM game ORDER BY gameName;";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            Set<GameBean> allGames = new HashSet<>();
            while (rs.next()) {
                Integer white = rs.getInt(2);
                Integer black = rs.getInt(3);
                if (white == 0) white = null;
                if (black == 0) black = null;
                allGames.add(new GameBean(rs.getInt(1), white, black, rs.getString(4), rs.getString(5)));
            }
            return allGames;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void update(GameBean bean) throws DataAccessException {
        String sql = "UPDATE game SET game = ? WHERE gameID = ?";
        try {
            // If this game does not exist, insert it
            if (find(bean.getGameID()) == null) {
                insert(bean);
                return;
            }
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, bean.getGame());
            stmt.setInt(2, bean.getGameID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void delete(int gameID) throws DataAccessException {
        String sql = "DELETE FROM game WHERE gameID = ?;";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, gameID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM game;";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void claimSpot(int gameID, ChessGame.TeamColor color, int playerID) throws DataAccessException {
        if (color == null) return;
        String sql = color == ChessGame.TeamColor.WHITE ?
                "UPDATE game SET whitePlayerID = ? WHERE gameID = ?" :
                "UPDATE game SET blackPlayerID = ? WHERE gameID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, playerID);
            stmt.setInt(2, gameID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }
}
