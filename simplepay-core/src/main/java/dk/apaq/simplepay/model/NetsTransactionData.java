/*
 * Copyright by Apaq 2011-2013
 */

package dk.apaq.simplepay.model;

import dk.apaq.nets.payment.ActionCode;
import dk.apaq.nets.payment.Card;
import dk.apaq.nets.payment.ITransactionData;
import org.joda.money.Money;

/**
 * Javadoc
 */
public class NetsTransactionData implements ITransactionData {

    private String id;
    private String ode;
    private String approvalCode;
    private String processingCode;
    private Money approvedAmount;
    private ActionCode actionCode;
    private Card card;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public ActionCode getActionCode() {
        return actionCode;
    }

    @Override
    public void setActionCode(ActionCode actionCode) {
        this.actionCode = actionCode;
    }

    @Override
    public String getApprovalCode() {
        return approvalCode;
    }

    @Override
    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    @Override
    public String getProcessingCode() {
        return processingCode;
    }

    @Override
    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }
    
    @Override
    public Money getApprovedAmount() {
        return approvedAmount;
    }

    @Override
    public void setApprovedAmount(Money approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    @Override
    public String getOde() {
        return ode;
    }

    @Override
    public void setOde(String ode) {
        this.ode = ode;
    }

    @Override
    public Card getCard() {
        return card;
    }

    @Override
    public void setCard(Card card) {
        this.card = card;
    }
    

}
