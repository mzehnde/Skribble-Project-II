package UseCase2;



import Documents.DocumentToSign;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Documents.DocumentToSign.convertFileToBase64;


public class AllSigners {

    private ArrayList<Signer> signerList;


    public ArrayList<Signer> getSignerList() {
        return signerList;
    }


    public void setSignerList(ArrayList<Signer> signerList) {
        this.signerList = signerList;
    }

    //convert the csvFile-List to a list with Signer Entities (with E-Mail and DocumentToSign Entity)
    public ArrayList<Signer> populateSignerList(List <String> csvFileList, String filesToBeSignedPath) throws IOException {
        ArrayList<Signer> signerList = new ArrayList<>();

        for (int i = 0; i < csvFileList.size(); i++) {
            String[] parts = csvFileList.get(i).split(";");
            DocumentToSign documentToSign = convertFileToBase64(parts[0], filesToBeSignedPath);
            if (documentToSign != null) {
                signerList.add(new Signer(documentToSign, parts[1]));
            }
            else{
                System.out.println(String.format("The Signature Request couldn't be sent to %s because the file %s doesn't exist in the provided directory",
                        parts[1], parts[0]));
            }
        }

        return signerList;
    }

}
