package JsonEntities;

public class Signer {
    String sid;
    String signer_email_address;
    SignerIdentityData signer_identity_data;
    String status_code;
    int order;
    boolean notify;

    public Signer(String sid, String signer_email_address, SignerIdentityData signer_identity_data, int order, String status_code, boolean notify) {
        this.sid = sid;
        this.signer_email_address = signer_email_address;
        this.signer_identity_data = signer_identity_data;
        this.order = order;
        this.status_code = status_code;
        this.notify = notify;
    }

    public String getSigner_email_address() {
        return signer_email_address;
    }

    public void setSigner_email_address(String signer_email_address) {
        this.signer_email_address = signer_email_address;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public SignerIdentityData getSigner_identity_data() {
        return signer_identity_data;
    }

    public void setSigner_identity_data(SignerIdentityData signer_identity_data) {
        this.signer_identity_data = signer_identity_data;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }
}
