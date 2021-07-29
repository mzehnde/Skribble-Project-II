package AllUseCases;



import Documents.DocumentToSign;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class User {

    private StringBuilder token;
    private String username;
    private String api_key;
    private DocumentToSign DocumentToSign;


    public User(String username, String api_key) {
        this.username = username;
        this.api_key = api_key;
    }

    public StringBuilder getToken() {
        return this.token;
    }

    public DocumentToSign getDocumentToSign() {
        return DocumentToSign;
    }

    public String getUsername() {
        return username;
    }

    public String getApi_key() {
        return api_key;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public void setToken(StringBuilder token) { this.token = token; }

    public void setDocumentToSign(DocumentToSign base64DocumentToSign) { this.DocumentToSign = base64DocumentToSign; }


    public StringBuilder loginUser() throws IOException {

        // build a connection to the API call
        URL url = new URL("https://api.scribital.com/v1/access/login");
        //HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        //Create JSonInput for Login Request
        String jsonInputString1 = "{\"username\": \"" + this.getUsername() + "\", \"api-key\":\"" + this.getApi_key() + "\"}";

        //Process the request and get response of request
        Request request = new Request("POST", jsonInputString1, null, url);
        String token = request.processRequest(false);

        //save response as our token
        saveToken(token);

        //disconnect
        //connection.disconnect();
        return this.token;
    }


    private void saveToken(String token) {
        StringBuilder responseData = new StringBuilder();
        responseData.append(token);
        this.token = responseData;
    }

}
