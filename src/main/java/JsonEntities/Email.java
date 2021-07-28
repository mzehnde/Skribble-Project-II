package JsonEntities;


import com.google.gson.Gson;

public class Email {

    public String signer_email_address;

    public Email(String signer_email_address) {
        this.signer_email_address = signer_email_address;
    }

    public String getSigner_email_address() {
        return signer_email_address;
    }

    public void setSigner_email_address(String signer_email_address) {
        this.signer_email_address = signer_email_address;
    }

    //Function to convert Json
    public static SignatureRequestResponse convertJsonToEntity(String json) {
        Gson gson = new Gson(); //
        return gson.fromJson(json, SignatureRequestResponse.class);
    }

}
