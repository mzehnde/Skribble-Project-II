package UseCase1;



import AllUseCases.Request;
import Documents.DocumentSigned;
import JsonEntities.SignatureRequestResponse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import static JsonEntities.Email.convertJsonToEntity;


public class Poller {

    private SignatureRequestResponse signatureRequestResponse;
    private StringBuilder token;


    public Poller(SignatureRequestResponse signatureRequestResponse, StringBuilder token) {
        this.signatureRequestResponse = signatureRequestResponse;
        this.token = token;
    }

    public SignatureRequestResponse getSignatureRequestResponse() {
        return signatureRequestResponse;
    }

    public void setSignatureRequestResponse(SignatureRequestResponse signatureRequestResponse) {
        this.signatureRequestResponse = signatureRequestResponse;
    }

    public void startPolling(SignatureRequestResponse signatureRequestResponse, String savePath) {
        //Start polling SR Get, every 10 seconds
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                SignatureRequestResponse signatureRequestResponse = null;
                try {

                    signatureRequestResponse = getSignatureRequestResponse1();

                } catch (MalformedURLException e) {

                    e.printStackTrace();

                }

                if (isSigned(signatureRequestResponse)) {
                    try {

                        DocumentSigned documentSigned = new DocumentSigned(signatureRequestResponse.getDocument_id(), token);
                        documentSigned.downloadPDF(savePath, signatureRequestResponse);
                        System.out.println("Your Document was signed and downloaded");

                    } catch (IOException e) {

                        e.printStackTrace();

                    }

                    //Stop polling
                    timer.cancel();
                }
            }
        }, 0, 10000);//wait 0 ms before doing the action and do it evry 1000ms (1second)
    }


    public SignatureRequestResponse getSignatureRequestResponse1() throws MalformedURLException {
        URL url = new URL("https://api.scribital.com/v1/signature-requests/" + signatureRequestResponse.getId() + "");
        String data = null;
        try {
            Request request = new Request("GET", null, token, url);
            data = request.processRequest(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //convert Response from json to entity to check status (signed or declined)
        return convertJsonToEntity(data);
    }


    public boolean isSigned(SignatureRequestResponse signatureRequestResponse) {
        return signatureRequestResponse.getStatus_overall().equals("SIGNED");
    }

}
