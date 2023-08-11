package dao.mysql;

import dao.UserDAO;
import dataAccess.DataAccessException;
import model.bean.UserBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLUserDAO implements UserDAO {

    private final Connection conn;

    public MySQLUserDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(UserBean bean) throws DataAccessException {
        String sql = "INSERT INTO user (userID, username, password, email) VALUES (?, ?, ?, ?);";
        try {
            if (conn.isClosed()) return;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bean.getUserID());
            stmt.setString(2, bean.getUsername());
            stmt.setString(3, bean.getPassword());
            stmt.setString(4, bean.getEmail());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public UserBean find(int userID) throws DataAccessException {
        String sql = "SELECT * FROM user WHERE userID = ?;";
        try {
            if (conn.isClosed()) return null;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return new UserBean(rs.getInt("userID"), rs.getString("username"), rs.getString("password"), rs.getString("email"));
            else return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public UserBean find(String username) throws DataAccessException {
        String sql = "SELECT * FROM user WHERE username = ?;";
        try {
            if (conn.isClosed()) return null;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return new UserBean(rs.getInt("userID"), rs.getString("username"), rs.getString("password"), rs.getString("email"));
            else return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void update(UserBean bean) throws DataAccessException {
        insert(bean);
    }

    @Override
    public void delete(int userID) throws DataAccessException {
        String sql = "DELETE FROM user WHERE userID = ?";
        try {
            if (conn.isClosed()) return;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM user;";
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
