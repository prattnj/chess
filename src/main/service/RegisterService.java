package service;

import model.bean.UserBean;
import model.request.BaseRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.response.BaseResponse;
import server.ForbiddenException;
import util.Util;

/**
 * Registers a new user and logs them in (see LoginService)
 */
public class RegisterService extends Service {

    @Override
    public BaseResponse doService(BaseRequest request, String authToken) throws Exception {
        // authToken is null

        RegisterRequest req = (RegisterRequest) request;

        UserBean user = udao.find(req.getUsername());
        if (user != null) throw new ForbiddenException("username is taken");
        if (udao.emailExists(req.getEmail())) throw new ForbiddenException("email is taken");

        // Everything is valid, we can create the user
        user = new UserBean(Util.getRandomID(6), req.getUsername(), req.getPassword(), req.getEmail());
        udao.insert(user);

        // Log the new user in
        Service loginService = new LoginService();
        return loginService.execute(new LoginRequest(req.getUsername(), req.getPassword()), null);
    }
}
