package UseCase2;



import JsonEntities.SignatureRequestResponse;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.System.exit;

public class SignatureRequestIdFile {

    private ArrayList<SignatureRequestResponse> responseList;
    private String filePath;


    public SignatureRequestIdFile(ArrayList<SignatureRequestResponse> responseList, String filePath) {
        this.responseList = responseList;
        this.filePath = filePath;
    }


    public ArrayList<SignatureRequestResponse> getResponseList() {
        return responseList;
    }

    public String getFilePath() {
        return filePath;
    }


    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setResponseList(ArrayList<SignatureRequestResponse> responseList) {
        this.responseList = responseList;
    }

    //create a new file and write Signature-Request Id's with corresponding E-Mail to it
    public void writeIdToFile() throws IOException {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePath);
        } catch (Exception e) {
            System.out.println("Your path where you want to save your Id File is invalid");
            exit(0);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        handleFileFormat(byteArrayOutputStream);

        //get email and Signature-Request Id's from response-List and write them to the file
        for (int i = 0; i < responseList.size(); i++) {
            String eMail = responseList.get(i).getSignatures().get(0).getSigner_email_address();
            String signatureRequestId = responseList.get(i).getId();

            byteArrayOutputStream.write(String.format("%s\t \t \t %s", eMail, signatureRequestId).getBytes());
            if (i != responseList.size() - 1) {
                byteArrayOutputStream.write("\r\n".getBytes());
            }
        }

        byte[] idToBytes = byteArrayOutputStream.toByteArray();
        fileOutputStream.write(idToBytes);
    }


    private void handleFileFormat(ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        byteArrayOutputStream.write(String.format("%s\t \t \t \t \t %s", "E-Mail", "Signature-Request-ID").getBytes());
        byteArrayOutputStream.write("\r\n".getBytes());
        byteArrayOutputStream.write("\r\n".getBytes());
    }


}
