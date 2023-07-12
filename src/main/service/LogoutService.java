package service;

import model.request.BaseRequest;
import model.response.BaseResponse;

/**
 * Removes an authToken for a user (logs them out)
 */
public class LogoutService extends Service {

    @Override
    public BaseResponse doService(BaseRequest request, String authToken) throws Exception {
        // request is null

        adao.delete(authToken);
        return new BaseResponse();
    }
}
