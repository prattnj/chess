package service;

import model.bean.AuthTokenBean;
import model.request.BaseRequest;
import model.response.BaseResponse;
import util.Util;

public class RefreshTokenService extends Service {

    @Override
    protected BaseResponse doService(BaseRequest request, String authToken) throws Exception {
        // request is null

        AuthTokenBean authBean = adao.find(authToken);
        adao.update(new AuthTokenBean(Util.getNewAuthToken(), authBean.getUserID()));
        return new BaseResponse();
    }
}
