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

    public ServerFacade(String host, String port) {
        this.host = host;
        this.port = port;
    }

    public BaseResponse login(BaseRequest request) {
        return execute("/session", request, null, "POST", LoginResponse.class);
    }

    public BaseResponse register(BaseRequest request) {
        return execute("/user", request, null, "POST", LoginResponse.class);
    }

    public BaseResponse logout(String authToken) {
        return execute("/session", null, authToken, "DELETE", BaseResponse.class);
    }

    public BaseResponse create(BaseRequest request, String authToken) {
        return execute("/game", request, authToken, "POST", CreateGameResponse.class);
    }

    public BaseResponse list(String authToken) {
        return execute("/game", null, authToken, "GET", ListGamesResponse.class);
    }

    public BaseResponse join(BaseRequest request, String authToken) {
        return execute("/game", request, authToken, "PUT", BaseResponse.class);
    }

    public BaseResponse clear() {
        return execute("/", null, null, "DELETE", BaseResponse.class);
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

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
