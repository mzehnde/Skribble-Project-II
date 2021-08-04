package Documents;



import AllUseCases.Request;
import JsonEntities.SignatureRequestResponse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DocumentSigned {

    private String signedDocumentId;
    private StringBuilder token;


    public DocumentSigned(String signedDocumentId, StringBuilder token) {
        this.signedDocumentId = signedDocumentId;
        this.token = token;
    }


    public String getSignedDocumentId() {
        return signedDocumentId;
    }


    public void setSignedDocumentId(String signedDocumentId) {
        this.signedDocumentId = signedDocumentId;
    }


    public void downloadPDF(String savePath, SignatureRequestResponse signatureRequestResponse) throws IOException {

        //GET REQUEST TO RETRIEVE PDF
        URL url = new URL("https://api.scribital.com/v1/documents/" + signedDocumentId + "/content");

        Request request = new Request("GET", null, token, url);
        request.processRequest(true);

        getResponseData(request, savePath, signatureRequestResponse);
    }


    private void getResponseData(Request request, String savePath, SignatureRequestResponse signatureRequestResponse){
        try {
            InputStream inputStream = request.getConnection().getInputStream();
            FileOutputStream outputStream = new FileOutputStream(String.format(savePath + "/%s.pdf", signatureRequestResponse.getId()));
            int bytesRead = -1;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            System.out.println("Couldn't access Input Stream");
        }
    }

}
