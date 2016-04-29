package com.t1t.digipolis.apim.mail;

import com.t1t.digipolis.apim.beans.events.ContractRequest;
import com.t1t.digipolis.apim.beans.mail.RequestMembershipMailBean;
import com.t1t.digipolis.apim.beans.mail.StatusMailBean;
import com.t1t.digipolis.apim.beans.mail.UpdateAdminMailBean;
import com.t1t.digipolis.apim.beans.mail.UpdateMemberMailBean;

/**
 * Created by michallispashidis on 8/04/16.
 */
public interface MailService {
    /**
     * Default endpoint to send email
     */
    void sendTestMail();

    /**
     * Send status mail.
     * @param statusMailBean
     */
    void sendStatusMail(StatusMailBean statusMailBean);

    /**
     * Send request membership information.
     * @param requestMembershipMailBean
     */
    void sendRequestMembership (RequestMembershipMailBean requestMembershipMailBean);

    /**
     * Send approval for request membership.
     * @param requestMembershipMailBean
     */
    void approveRequestMembership(RequestMembershipMailBean requestMembershipMailBean);

    /**
     * Send reject for request membership.
     * @param requestMembershipMailBean
     */
    void rejectRequestMembership(RequestMembershipMailBean requestMembershipMailBean);

    /**
     * Send update membership information.
     * @param updateMemberMailBean
     */
    void sendUpdateMember(UpdateMemberMailBean updateMemberMailBean);

    /**
     * Send update admin membership information.
     * @param updateAdminMailBean
     */
    void sendUpdateAdmin(UpdateAdminMailBean updateAdminMailBean);

    /**
     * Send contract request.
     * @param request
     */
    void sendContractRequest(ContractRequest request);

    /**
     * Send mail for contract request approval.
     * @param request
     */
    void approveContractRequest(ContractRequest request);

    /**
     * Send reject for contract request.
     * @param request
     */
    void rejectContractRequest(ContractRequest request);

}
