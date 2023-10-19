package service;

import model.bean.AuthTokenBean;
import model.bean.UserBean;
import model.request.BaseRequest;
import model.response.BaseResponse;
import model.response.LoginResponse;
import util.Util;

public class RefreshTokenService extends Service {

    @Override
    protected BaseResponse doService(BaseRequest request, String authToken) throws Exception {
        // request is null

        AuthTokenBean authBean = adao.find(authToken);
        UserBean userBean = udao.find(authBean.getUserID());
        AuthTokenBean newBean = new AuthTokenBean(Util.getNewAuthToken(), authBean.getUserID());
        adao.update(newBean);
        return new LoginResponse(newBean.getAuthToken(), userBean.getUsername());
    }
}
