package com.t1t.digipolis.apim.mail;

import com.t1t.digipolis.apim.beans.events.ContractRequest;
import com.t1t.digipolis.apim.beans.mail.*;
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
     * @param membershipRequestMailBean
     */
    void sendRequestMembership (MembershipRequestMailBean membershipRequestMailBean)throws MailServiceException;

    /**
     * Send approval for request membership.
     * @param membershipApproveMailBean
     */
    void approveRequestMembership(MembershipApproveMailBean membershipApproveMailBean)throws MailServiceException;

    /**
     * Send reject for request membership.
     * @param membershipRejectMailBean
     */
    void rejectRequestMembership(MembershipRejectMailBean membershipRejectMailBean)throws MailServiceException;

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
     * @param contractRequestMailBean
     */
    void sendContractRequest(ContractRequestMailBean contractRequestMailBean)throws MailServiceException;

    /**
     * Send mail for contract request approval.
     * @param contractApprovedMailBean
     */
    void approveContractRequest(ContractApprovedMailBean contractApprovedMailBean)throws MailServiceException;

    /**
     * Send reject for contract request.
     * @param contractRejectedMailBean
     */
    void rejectContractRequest(ContractRejectedMailBean contractRejectedMailBean)throws MailServiceException;

}
