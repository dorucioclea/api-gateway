package com.t1t.digipolis.apim.mail;

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
     * @param bean
     */
    void approveRequestMembership(MembershipRequestMailBean bean)throws MailServiceException;

    /**
     * Send reject for request membership.
     * @param bean
     */
    void rejectRequestMembership(MembershipRequestMailBean bean)throws MailServiceException;

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
     * @param contractMailBean
     */
    void sendContractRequest(ContractMailBean contractMailBean)throws MailServiceException;

    /**
     * Send mail for contract request approval.
     * @param contractMailBean
     */
    void approveContractRequest(ContractMailBean contractMailBean)throws MailServiceException;

    /**
     * Send reject for contract request.
     * @param contractMailBean
     */
    void rejectContractRequest(ContractMailBean contractMailBean)throws MailServiceException;

    /**
     * Send cancellation mail for membership request
     * @param bean
     * @throws MailServiceException
     */
    void cancelMembershipRequest(MembershipRequestMailBean bean) throws MailServiceException;

    /**
     * Send cancellation mail for contract request
     * @param bean
     * @throws MailServiceException
     */
    void cancelContractRequest(ContractMailBean bean) throws MailServiceException;

}
