
package dk.apaq.simplepay.model;

import dk.apaq.nets.payment.ActionCode;

/**
 * Javadoc
 */
public class NetsMeta {

    private String id;
    private Merchant merchant;
    private String refId;
    private String ode;
    private ActionCode actionCode;
    private String approvalCode;
    private String processingCode;

    protected NetsMeta() {
    }

    public NetsMeta(Merchant merchant, String refId, String ode, ActionCode actionCode, String approvalCode, String processingCode) {
        this.merchant = merchant;
        this.refId = refId;
        this.ode = ode;
        this.actionCode = actionCode;
        this.approvalCode = approvalCode;
        this.processingCode = processingCode;
    }
    
    public String getId() {
        return id;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public String getRefId() {
        return refId;
    }

    public String getOde() {
        return ode;
    }

    public ActionCode getActionCode() {
        return actionCode;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public String getProcessingCode() {
        return processingCode;
    }
    
    
    
}
