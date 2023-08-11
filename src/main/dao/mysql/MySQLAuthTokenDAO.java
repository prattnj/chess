package dao.mysql;

import dao.AuthTokenDAO;
import dataAccess.DataAccessException;
import model.bean.AuthTokenBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLAuthTokenDAO implements AuthTokenDAO {

    private final Connection conn;

    public MySQLAuthTokenDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(AuthTokenBean bean) throws DataAccessException {
        String sql = "INSERT INTO auth (authtoken, userID) VALUES (?, ?);";
        try {
            if (conn.isClosed()) return;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, bean.getAuthToken());
            stmt.setInt(2, bean.getUserID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public AuthTokenBean find(int userID) throws DataAccessException {
        String sql = "SELECT * FROM auth WHERE userID = ?;";
        try {
            if (conn.isClosed()) return null;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return new AuthTokenBean(rs.getString("authtoken"), rs.getInt("userID"));
            else return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public AuthTokenBean find(String authToken) throws DataAccessException {
        String sql = "SELECT * FROM auth WHERE authtoken = ?;";
        try {
            if (conn.isClosed()) return null;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, authToken);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return new AuthTokenBean(rs.getString("authtoken"), rs.getInt("userID"));
            else return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void update(AuthTokenBean bean) throws DataAccessException {
        String sql = "UPDATE auth SET authToken = ? WHERE userID = ?";
        try {
            if (conn.isClosed()) return;
            // If this authToken does not exist, insert it
            if (find(bean.getUserID()) == null) {
                insert(bean);
                return;
            }
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, bean.getAuthToken());
            stmt.setInt(2, bean.getUserID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void delete(String authToken) throws DataAccessException {
        String sql = "DELETE FROM auth WHERE authtoken = ?;";
        try {
            if (conn.isClosed()) return;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, authToken);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM auth;";
        try {
            if (conn.isClosed()) return;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }
}
