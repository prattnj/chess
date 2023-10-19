package service;

import dao.*;
import dataAccess.DataAccessException;
import model.request.BaseRequest;
import model.response.BaseResponse;
import server.ServerErrorException;
import server.UnauthorizedException;
import util.Util;

/**
 * The starting point for all Service classes
 */
public abstract class Service {

    protected UserDAO udao;
    protected GameDAO gdao;
    protected AuthTokenDAO adao;
    private Transaction transaction;

    /**
     * Handles all common service functionality including DAOs, validating tokens, and committing changes if necessary
     * @param request The request to be processed by each service
     * @param authToken The user's authToken, can be null
     * @return A response object, different for each service
     * @throws Exception Corresponds to the exceptions in this package so the Handler can set the HTTP status code
     */
    public BaseResponse execute(BaseRequest request, String authToken) throws Exception {

        try {
            // Initialize DAOs and DB transaction
            transaction = DAOFactory.getNewTransaction();
            transaction.openTransaction();

            udao = DAOFactory.getNewUserDAO(transaction);
            gdao = DAOFactory.getNewGameDAO(transaction);
            adao = DAOFactory.getNewAuthTokenDAO(transaction);

            // Validate authToken
            if (authToken != null) {
                if (adao.find(authToken) == null && !authToken.equals(System.getenv("MYSQL_PASSWORD"))) {
                    throw new UnauthorizedException(Util.INVALID_TOKEN);
                }
            }

            // Execute service logic
            BaseResponse response = doService(request, authToken);

            // Commit changes
            transaction.closeTransaction(true);

            return response;
        } catch (ServerErrorException | DataAccessException e) {
            // Rollback changes
            transaction.closeTransaction(false);
            e.printStackTrace();
            throw new ServerErrorException();
        }
    }

    /**
     * The specific implementations of each service so all common code can be abstracted out
     * @param request The request to be processed by each service
     * @param authToken The user's authToken, can be null
     * @return A response object, different for each service
     * @throws Exception
     */
    protected abstract BaseResponse doService(BaseRequest request, String authToken) throws Exception;
}
