package net;

import com.google.gson.Gson;
import model.request.BaseRequest;
import model.response.BaseResponse;
import model.response.CreateGameResponse;
import model.response.ListGamesResponse;
import model.response.LoginResponse;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import static util.Util.SERVER_ERROR;

public class ServerFacade {

    private final String host;
    private final String port;

    /**
     * Creates a facade to send requests to the server
     * @param host The IP address of the server
     * @param port The port to connect to
     */
    public ServerFacade(String host, String port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Sends a login request to the server
     * @param request The login request containing username and password
     * @return The login result that contains an authToken if successful
     */
    public BaseResponse login(BaseRequest request) {
        return execute("/session", request, null, "POST", LoginResponse.class);
    }

    /**
     * Sends a register request to the server
     * @param request The register request containing username, password, and email
     * @return The login result that contains an authToken if successful
     */
    public BaseResponse register(BaseRequest request) {
        return execute("/user", request, null, "POST", LoginResponse.class);
    }

    /**
     * Sends a logout request to the server
     * @param authToken The authToken of the user to logout
     * @return A basic response indicating success or failure
     */
    public BaseResponse logout(String authToken) {
        return execute("/session", null, authToken, "DELETE", BaseResponse.class);
    }

    /**
     * Sends a create game request to the server
     * @param request The create game request containing the game name
     * @param authToken The authToken of the user creating the game
     * @return The creation result that contains the game ID if successful
     */
    public BaseResponse create(BaseRequest request, String authToken) {
        return execute("/game", request, authToken, "POST", CreateGameResponse.class);
    }

    /**
     * Sends a list games request to the server
     * @param authToken The authToken of the user requesting the list
     * @return The list result that contains a list of games if successful
     */
    public BaseResponse list(String authToken) {
        return execute("/game", null, authToken, "GET", ListGamesResponse.class);
    }

    /**
     * Sends a join game request to the server
     * @param request The join game request containing the game ID
     * @param authToken The authToken of the user joining the game
     * @return A basic response indicating success or failure
     */
    public BaseResponse join(BaseRequest request, String authToken) {
        return execute("/game", request, authToken, "PUT", BaseResponse.class);
    }

    /**
     * Sends a start game request to the server
     * @return A basic response indicating success or failure
     */
    public BaseResponse clear() {
        return execute("/db", null, System.getenv("MYSQL_PASSWORD"), "DELETE", BaseResponse.class);
    }

    private BaseResponse execute(String endpoint, BaseRequest request, String authToken, String verb, Type successResp) {

        Gson gson = new Gson();

        try {
            // Set up request
            URL url = new URI("http://" + host + ":" + port + endpoint).toURL();
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod(verb);
            http.setDoOutput(true);
            http.addRequestProperty("Accept", "application/json");
            // Add authToken if applicable
            if (authToken != null) http.addRequestProperty("Authorization", authToken);
            // Add request if applicable
            if (request != null) {
                String reqData = gson.toJson(request);
                OutputStream reqBody = http.getOutputStream();
                writeString(reqData, reqBody);
                reqBody.close();
            }
            http.connect();

            // Handle response
            InputStream respBody;
            Type responseType = BaseResponse.class;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                respBody = http.getInputStream();
                responseType = successResp;
            }
            else respBody = http.getErrorStream();
            String respData = readString(respBody);
            BaseResponse response = gson.fromJson(respData, responseType);
            respBody.close();
            return response;

        } catch (Exception e) {
            return new BaseResponse(SERVER_ERROR);
        }
    }

    // Given an InputStream, returns a string stored therein
    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) sb.append(buf, 0, len);
        return sb.toString();
    }

    // Writes a given String to a given OutputStream
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
