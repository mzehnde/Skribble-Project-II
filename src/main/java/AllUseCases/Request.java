package AllUseCases;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class Request {

    private String requestType;
    private String JsonInputString;
    private HttpURLConnection connection;
    private StringBuilder token;

    public Request(String requestType, String jsonInputString, HttpURLConnection connection, StringBuilder token) {
        this.requestType = requestType;
        this.JsonInputString = jsonInputString;
        this.connection = connection;
        this.token = token;
    }


    public String getRequestType() {
        return requestType;
    }

    public String getJsonInputString() {
        return JsonInputString;
    }
    public HttpURLConnection getConnection() {
        return connection;
    }


    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public void setJsonInputString(String jsonInputString) {
        JsonInputString = jsonInputString;
    }

    public void setConnection(HttpURLConnection connection) {
        this.connection = connection;
    }


    public String processRequest(boolean pdf) throws IOException {

        setRequestProperties();

        if (requestType == "POST") {
            setOutputStream();
        }
        if (pdf) {
            return null;
        }
        else {
            return getRequestResponse();
        }
    }


    //Helper Functions / setting Streams & Requests up
    public void setRequestProperties() throws IOException {

        if (token != null) {
            connection.setRequestProperty("Authorization", "Bearer " + token.toString());
        }

        connection.setRequestMethod(requestType);
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setDoOutput(true);
        connection.setConnectTimeout(6000);
        connection.setReadTimeout(6000);
    }

    public void setOutputStream() throws IOException {
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = JsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.close();
        }
    }

    public String getRequestResponse() throws IOException {
        String responseData = "";
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response1 = new StringBuilder();
            String responseLine1 = null;
            while ((responseLine1 = br.readLine()) != null) {
                response1.append(responseLine1.trim());
                responseData += responseLine1;
            }
        }
        return responseData;
    }

}
