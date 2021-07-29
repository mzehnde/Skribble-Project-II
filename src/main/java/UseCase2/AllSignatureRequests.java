package UseCase2;



import AllUseCases.Request;
import JsonEntities.SignatureRequestResponse;



import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static JsonEntities.Email.convertJsonToEntity;


public class AllSignatureRequests {

    private ArrayList<SignatureRequestResponse> responseList;
    private ArrayList<Signer> signerList;
    private StringBuilder token;


    public AllSignatureRequests(ArrayList<Signer> signerList, StringBuilder token) {
        this.signerList = signerList;
        this.token = token;
    }


    public ArrayList<Signer> getSignerList() {
        return signerList;
    }

    public ArrayList<SignatureRequestResponse> getResponseList() {
        return responseList;
    }


    public void setSignerList(ArrayList<Signer> signerList) {
        this.signerList = signerList;
    }

    public void setResponseList(ArrayList<SignatureRequestResponse> responseList) {
        this.responseList = responseList;
    }


    //create SR for all files and signers in signer-list
    public ArrayList<SignatureRequestResponse> doRequests() throws IOException {
        ArrayList<SignatureRequestResponse> responseList = new ArrayList<>();

        if (signerList == null || signerList.size() == 0){
            System.out.println("Your List of signer is empty, can't send the signature Requests. PLease check yur CSV File");
        }

        for (Signer signer : signerList) {
            URL url = new URL("https://api.scribital.com/v1/signature-requests");

            String jsonInputString = "{\"title\": \"Example Contract X\"," +
                    "\"message\": \"Please sign this document\"," +
                    "\"content\":\"" + signer.getDocumentToSign().getBase64Content() + "\"," +
                    "\"signatures\":[{\"signer_email_address\" : \"" + signer.getE_mail() + "\"}]," +
                    "\"callback_success_url\": \"https://invulnerable-vin-64865.herokuapp.com/download/SKRIBBLE_DOCUMENT_ID\"}";

            //get the response of the request in Json and create response-list with entity (converted from json file)
            Request request = new Request("POST", jsonInputString, token, url);
            String response = request.processRequest(false);
            SignatureRequestResponse signatureRequestResponse = convertJsonToEntity(response);
            responseList.add(signatureRequestResponse);
        }
        return responseList;
    }
}
