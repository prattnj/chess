package service;

import model.request.BaseRequest;
import model.response.BaseResponse;

/**
 * Clears the database without authorization
 */
public class ClearService extends Service {

    @Override
    public BaseResponse doService(BaseRequest request, String authToken) throws Exception {
        // request and authToken are null

        // Clear all tables
        udao.clear();
        gdao.clear();
        adao.clear();

        return new BaseResponse();
    }
}
