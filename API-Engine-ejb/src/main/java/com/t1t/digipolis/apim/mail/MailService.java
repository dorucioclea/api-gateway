package com.t1t.digipolis.apim.mail;

import com.t1t.digipolis.apim.beans.events.ContractRequest;
import com.t1t.digipolis.apim.beans.mail.RequestMembershipMailBean;
import com.t1t.digipolis.apim.beans.mail.StatusMailBean;
import com.t1t.digipolis.apim.beans.mail.UpdateAdminMailBean;
import com.t1t.digipolis.apim.beans.mail.UpdateMemberMailBean;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.MailServiceException;

/**
 * Created by michallispashidis on 8/04/16.
 */
public interface MailService {
    /**
     * Default endpoint to send email
     */
    void sendTestMail() throws StorageException;

    /**
     * Send status mail.
     * @param statusMailBean
     */
    void sendStatusMail(StatusMailBean statusMailBean) throws MailServiceException;

    /**
     * Send request membership information.
     * @param requestMembershipMailBean
     */
    void sendRequestMembership (RequestMembershipMailBean requestMembershipMailBean)throws MailServiceException;

    /**
     * Send approval for request membership.
     * @param requestMembershipMailBean
     */
    void approveRequestMembership(RequestMembershipMailBean requestMembershipMailBean)throws MailServiceException;

    /**
     * Send reject for request membership.
     * @param requestMembershipMailBean
     */
    void rejectRequestMembership(RequestMembershipMailBean requestMembershipMailBean)throws MailServiceException;

    /**
     * Send update membership information.
     * @param updateMemberMailBean
     */
    void sendUpdateMember(UpdateMemberMailBean updateMemberMailBean)throws MailServiceException;

    /**
     * Send update admin membership information.
     * @param updateAdminMailBean
     */
    void sendUpdateAdmin(UpdateAdminMailBean updateAdminMailBean)throws MailServiceException;

    /**
     * Send contract request.
     * @param request
     */
    void sendContractRequest(ContractRequest request)throws MailServiceException;

    /**
     * Send mail for contract request approval.
     * @param request
     */
    void approveContractRequest(ContractRequest request)throws MailServiceException;

    /**
     * Send reject for contract request.
     * @param request
     */
    void rejectContractRequest(ContractRequest request)throws MailServiceException;

}
