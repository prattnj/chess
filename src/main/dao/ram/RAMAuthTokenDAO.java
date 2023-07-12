package dao.ram;

import dao.AuthTokenDAO;
import model.bean.AuthTokenBean;

import java.util.HashMap;
import java.util.Map;

public class RAMAuthTokenDAO implements AuthTokenDAO {

    private static final AuthTokenDAO instance = new RAMAuthTokenDAO();

    private final Map<String, AuthTokenBean> table = new HashMap<>();

    public static AuthTokenDAO getInstance() {
        return instance;
    }

    @Override
    public void insert(AuthTokenBean bean) {
        table.put(bean.getAuthToken(), bean);
    }

    @Override
    public AuthTokenBean find(int userID) {
        for (AuthTokenBean bean : table.values()) if (bean.getUserID() == userID) return bean;
        return null;
    }

    @Override
    public AuthTokenBean find(String authToken) {
        return table.get(authToken);
    }

    @Override
    public void update(AuthTokenBean bean) {
        insert(bean);
    }

    @Override
    public void delete(String authToken) {
        table.remove(authToken);
    }

    @Override
    public void clear() {
        table.clear();
    }
}
