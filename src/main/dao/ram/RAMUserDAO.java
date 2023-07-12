package dao.ram;

import dao.UserDAO;
import model.bean.UserBean;

import java.util.HashMap;
import java.util.Map;

public class RAMUserDAO implements UserDAO {

    private static final UserDAO instance = new RAMUserDAO();

    private final Map<Integer, UserBean> table = new HashMap<>();

    public static UserDAO getInstance() {
        return instance;
    }

    @Override
    public void insert(UserBean bean) {
        table.put(bean.getUserID(), bean);
    }

    @Override
    public UserBean find(int userID) {
        return table.get(userID);
    }

    @Override
    public UserBean find(String username) {
        for (UserBean bean : table.values()) if (bean.getUsername().equals(username)) return bean;
        return null;
    }

    @Override
    public void update(UserBean bean) {
        insert(bean);
    }

    @Override
    public void delete(int userID) {
        table.remove(userID);
    }

    @Override
    public void clear() {
        table.clear();
    }
}
