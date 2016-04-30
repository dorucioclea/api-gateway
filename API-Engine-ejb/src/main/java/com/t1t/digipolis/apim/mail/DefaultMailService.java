package com.t1t.digipolis.apim.mail;

import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.events.ContractRequest;
import com.t1t.digipolis.apim.beans.mail.*;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.MailServiceException;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Simple mailprovider, no templating and no dynamic substitutions.
 * For a straight forward impl, only text messages supported, with inline replacements.
 * For this first implementation the mail beans, doesn't carry content with them. The content is provided hard-coded in
 * the provided methods.
 * TODO: introduce string replacement, templating etc.
 */
@ApplicationScoped
@Default
public class DefaultMailService implements MailService {
    private final static Logger _LOG = LoggerFactory.getLogger(DefaultMailService.class.getName());
    @Inject private AppConfig config;
    @Inject @Default private MailProvider mailProvider;
    @Inject private IStorage storage;
    private static final String KEY_START = "{";
    private static final String KEY_END = "}";

    public void sendTestMail() throws MailServiceException {
        try{
            //get the mail template
            MailTemplateBean mailTemplate = storage.getMailTemplate(MailTopic.TEST);
            //prepare map
            TestMailBean testMailBean = new TestMailBean();
            testMailBean.setEnvironment(config.getEnvironment());
            Map<String, String> keymap = BeanUtilsBean.getInstance().describe(testMailBean);
            final StrSubstitutor sub = new StrSubstitutor(keymap,KEY_START,KEY_END);
            prepAndSendMail(sub,mailTemplate,config.getNotificationStartupMail());
        }catch(StorageException|NoSuchMethodException|IllegalAccessException|InvocationTargetException ex){
            throw new MailServiceException(ex.getMessage());
        }
    }

    @Override
    public void sendStatusMail(StatusMailBean statusMailBean) throws MailServiceException {
        try{
            MailTemplateBean mailTemplate = storage.getMailTemplate(MailTopic.STATUS);
            TestMailBean testMailBean = new TestMailBean();
            testMailBean.setEnvironment(config.getEnvironment());
            Map<String, String> keymap = BeanUtilsBean.getInstance().describe(testMailBean);
            final StrSubstitutor sub = new StrSubstitutor(keymap,KEY_START,KEY_END);
            prepAndSendMail(sub,mailTemplate,statusMailBean.getTo());
        }catch(StorageException|NoSuchMethodException|IllegalAccessException|InvocationTargetException ex){
            throw new MailServiceException(ex.getMessage());
        }
    }

    @Override
    public void sendRequestMembership(MembershipRequestMailBean membershipRequestMailBean) throws MailServiceException {
        try{
            MailTemplateBean mailTemplate = storage.getMailTemplate(MailTopic.MEMBERSHIP_REQUEST);
            Map<String, String> keymap = BeanUtilsBean.getInstance().describe(membershipRequestMailBean);
            final StrSubstitutor sub = new StrSubstitutor(keymap,KEY_START,KEY_END);
            prepAndSendMail(sub,mailTemplate, membershipRequestMailBean.getTo());
        }catch(StorageException|NoSuchMethodException|IllegalAccessException|InvocationTargetException ex){
            throw new MailServiceException(ex.getMessage());
        }
    }

    @Override
    public void approveRequestMembership(MembershipApproveMailBean membershipApproveMailBean) throws MailServiceException {
        membershipApproveMailBean.setEnvironment(config.getEnvironment());
        try{
            MailTemplateBean mailTemplate = storage.getMailTemplate(MailTopic.MEMBERSHIP_APPROVE);
            Map<String, String> keymap = BeanUtilsBean.getInstance().describe(membershipApproveMailBean);
            final StrSubstitutor sub = new StrSubstitutor(keymap,KEY_START,KEY_END);
            prepAndSendMail(sub,mailTemplate, membershipApproveMailBean.getTo());
        }catch(StorageException|NoSuchMethodException|IllegalAccessException|InvocationTargetException ex){
            throw new MailServiceException(ex.getMessage());
        }
    }

    @Override
    public void rejectRequestMembership(MembershipRejectMailBean membershipRejectMailBean) throws MailServiceException {
        membershipRejectMailBean.setEnvironment(config.getEnvironment());
        try{
            MailTemplateBean mailTemplate = storage.getMailTemplate(MailTopic.MEMBERSHIP_REJECT);
            Map<String, String> keymap = BeanUtilsBean.getInstance().describe(membershipRejectMailBean);
            final StrSubstitutor sub = new StrSubstitutor(keymap,KEY_START,KEY_END);
            prepAndSendMail(sub,mailTemplate, membershipRejectMailBean.getTo());
        }catch(StorageException|NoSuchMethodException|IllegalAccessException|InvocationTargetException ex){
            throw new MailServiceException(ex.getMessage());
        }
    }

    @Override
    public void sendUpdateMember(UpdateMemberMailBean updateMemberMailBean) throws MailServiceException {
        updateMemberMailBean.setEnvironment(config.getEnvironment());
        switch (updateMemberMailBean.getMembershipAction()) {
            case NEW_MEMBERSHIP: {
                sendMembershipNew(updateMemberMailBean);
                break;
            }
            case TRANSFER: {
                sendMembershipTransfer(updateMemberMailBean);
                break;
            }
            case DELETE_MEMBERSHIP: {
                sendMembershipDelete(updateMemberMailBean);
                break;
            }
            case UPDATE_ROLE: {
                sendMembershipUpdateRole(updateMemberMailBean);
                break;
            }
        }
    }

    @Override
    public void sendUpdateAdmin(UpdateAdminMailBean updateAdminMailBean) throws MailServiceException {
        updateAdminMailBean.setEnvironment(config.getEnvironment());
        switch (updateAdminMailBean.getMembershipAction()) {
            case NEW_MEMBERSHIP: {
                sendMembershipAdminNew(updateAdminMailBean);
                break;
            }
            case DELETE_MEMBERSHIP: {
                sendMembershipAdminDelete(updateAdminMailBean);
                break;
            }
        }
    }

    @Override
    public void sendContractRequest(ContractRequestMailBean mailBean) throws MailServiceException {
        mailBean.setEnvironment(config.getEnvironment());
        try{
            MailTemplateBean mailTemplate = storage.getMailTemplate(MailTopic.CONTRACT_REQUEST);
            Map<String, String> keymap = BeanUtilsBean.getInstance().describe(mailBean);
            final StrSubstitutor sub = new StrSubstitutor(keymap,KEY_START,KEY_END);
            prepAndSendMail(sub,mailTemplate, mailBean.getTo());
        }catch(StorageException|NoSuchMethodException|IllegalAccessException|InvocationTargetException ex){
            throw new MailServiceException(ex.getMessage());
        }
    }

    @Override
    public void approveContractRequest(ContractApprovedMailBean mailBean) throws MailServiceException {
        mailBean.setEnvironment(config.getEnvironment());
        try{
            MailTemplateBean mailTemplate = storage.getMailTemplate(MailTopic.CONTRACT_APPROVE);
            Map<String, String> keymap = BeanUtilsBean.getInstance().describe(mailBean);
            final StrSubstitutor sub = new StrSubstitutor(keymap,KEY_START,KEY_END);
            prepAndSendMail(sub,mailTemplate, mailBean.getTo());
        }catch(StorageException|NoSuchMethodException|IllegalAccessException|InvocationTargetException ex){
            throw new MailServiceException(ex.getMessage());
        }
    }

    @Override
    public void rejectContractRequest(ContractRejectedMailBean mailBean) throws MailServiceException {
        mailBean.setEnvironment(config.getEnvironment());
        try{
            MailTemplateBean mailTemplate = storage.getMailTemplate(MailTopic.CONTRACT_REJECT);
            Map<String, String> keymap = BeanUtilsBean.getInstance().describe(mailBean);
            final StrSubstitutor sub = new StrSubstitutor(keymap,KEY_START,KEY_END);
            prepAndSendMail(sub,mailTemplate, mailBean.getTo());
        }catch(StorageException|NoSuchMethodException|IllegalAccessException|InvocationTargetException ex){
            throw new MailServiceException(ex.getMessage());
        }
    }

    /**
     * Utility endpoint to prepare and send generic mail.
     * @param sub
     * @param mailTemplate
     * @param to
     * @throws StorageException
     */
    private void prepAndSendMail(StrSubstitutor sub,MailTemplateBean mailTemplate,String to) throws StorageException {
        BaseMailBean mailBean = new BaseMailBean();
        mailBean.setSubject(sub.replace(mailTemplate.getSubject()));
        mailBean.setContent(sub.replace(mailTemplate.getTemplate()));
        //mailBean.setContent(mailTemplate.getTemplate());
        mailBean.setTo(to);
        mailProvider.sendMail(mailProvider.composeMessage(mailBean));
    }

    private void sendMembershipAdminDelete(UpdateAdminMailBean mailBean) throws MailServiceException {
        try{
            MailTemplateBean mailTemplate = storage.getMailTemplate(MailTopic.MEMBERSHIP_ADMIN_DELETED);
            Map<String, String> keymap = BeanUtilsBean.getInstance().describe(mailBean);
            final StrSubstitutor sub = new StrSubstitutor(keymap,KEY_START,KEY_END);
            prepAndSendMail(sub,mailTemplate, mailBean.getTo());
        }catch(StorageException|NoSuchMethodException|IllegalAccessException|InvocationTargetException ex){
            throw new MailServiceException(ex.getMessage());
        }
    }

    private void sendMembershipAdminNew(UpdateAdminMailBean mailBean) throws MailServiceException {
        try{
            MailTemplateBean mailTemplate = storage.getMailTemplate(MailTopic.MEMBERSHIP_ADMIN_NEW);
            Map<String, String> keymap = BeanUtilsBean.getInstance().describe(mailBean);
            final StrSubstitutor sub = new StrSubstitutor(keymap,KEY_START,KEY_END);
            prepAndSendMail(sub,mailTemplate, mailBean.getTo());
        }catch(StorageException|NoSuchMethodException|IllegalAccessException|InvocationTargetException ex){
            throw new MailServiceException(ex.getMessage());
        }
    }

    private void sendMembershipUpdateRole(UpdateMemberMailBean mailBean) throws MailServiceException {
        try{
            MailTemplateBean mailTemplate = storage.getMailTemplate(MailTopic.MEMBERSHIP_UPDATE_ROLE);
            Map<String, String> keymap = BeanUtilsBean.getInstance().describe(mailBean);
            final StrSubstitutor sub = new StrSubstitutor(keymap,KEY_START,KEY_END);
            prepAndSendMail(sub,mailTemplate, mailBean.getTo());
        }catch(StorageException|NoSuchMethodException|IllegalAccessException|InvocationTargetException ex){
            throw new MailServiceException(ex.getMessage());
        }
    }

    private void sendMembershipDelete(UpdateMemberMailBean mailBean) throws MailServiceException {
        try{
            MailTemplateBean mailTemplate = storage.getMailTemplate(MailTopic.MEMBERSHIP_DELETED);
            Map<String, String> keymap = BeanUtilsBean.getInstance().describe(mailBean);
            final StrSubstitutor sub = new StrSubstitutor(keymap,KEY_START,KEY_END);
            prepAndSendMail(sub,mailTemplate, mailBean.getTo());
        }catch(StorageException|NoSuchMethodException|IllegalAccessException|InvocationTargetException ex){
            throw new MailServiceException(ex.getMessage());
        }
    }

    private void sendMembershipTransfer(UpdateMemberMailBean mailBean) throws MailServiceException {
        try{
            MailTemplateBean mailTemplate = storage.getMailTemplate(MailTopic.ORGANIZATION_TRANSFER);
            Map<String, String> keymap = BeanUtilsBean.getInstance().describe(mailBean);
            final StrSubstitutor sub = new StrSubstitutor(keymap,KEY_START,KEY_END);
            prepAndSendMail(sub,mailTemplate, mailBean.getTo());
        }catch(StorageException|NoSuchMethodException|IllegalAccessException|InvocationTargetException ex){
            throw new MailServiceException(ex.getMessage());
        }
    }

    private void sendMembershipNew(UpdateMemberMailBean mailBean) throws MailServiceException {
        try{
            MailTemplateBean mailTemplate = storage.getMailTemplate(MailTopic.MEMBERSHIP_NEW);
            Map<String, String> keymap = BeanUtilsBean.getInstance().describe(mailBean);
            final StrSubstitutor sub = new StrSubstitutor(keymap,KEY_START,KEY_END);
            prepAndSendMail(sub,mailTemplate, mailBean.getTo());
        }catch(StorageException|NoSuchMethodException|IllegalAccessException|InvocationTargetException ex){
            throw new MailServiceException(ex.getMessage());
        }
    }
}
