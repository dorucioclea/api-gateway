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
            //map
            TestMailBean testMailBean = new TestMailBean();
            testMailBean.setEnvironment(config.getEnvironment());

            Map<String, String> keymap = BeanUtilsBean.getInstance().describe(testMailBean);
            final StrSubstitutor sub = new StrSubstitutor(keymap,KEY_START,KEY_END);

            BaseMailBean mailBean = new BaseMailBean();
            mailBean.setSubject(sub.replace(mailTemplate.getSubject()));
            mailBean.setContent(sub.replace(mailTemplate.getTemplate()));
            mailBean.setTo(config.getNotificationStartupMail());
            mailProvider.sendMail(mailProvider.composeMessage(mailBean));
        }catch(StorageException|NoSuchMethodException|IllegalAccessException|InvocationTargetException ex){
            throw new MailServiceException(ex.getMessage());
        }
    }

    @Override
    public void sendStatusMail(StatusMailBean statusMailBean) throws MailServiceException {
        BaseMailBean mailBean = new BaseMailBean();
        mailBean.setSubject("API Engine - status mail (" + config.getEnvironment() + ")");
        mailBean.setContent("Not yet implemented");
        mailBean.setTo(statusMailBean.getTo());
        mailProvider.sendMail(mailProvider.composeMessage(mailBean));
    }

    @Override
    public void sendRequestMembership(RequestMembershipMailBean requestMembershipMailBean) throws MailServiceException {
        requestMembershipMailBean.setSubject("API Engine (" + config.getEnvironment() + ") - request membership for " + requestMembershipMailBean.getOrgFriendlyName());
        //TODO: templating
        StringBuilder sContent = new StringBuilder("");
        sContent.append("The following user requests membership for your organization: ");
        sContent.append(requestMembershipMailBean.getOrgFriendlyName());
        sContent.append("(" + requestMembershipMailBean.getOrgName() + ")");
        sContent.append("\n- Username: " + requestMembershipMailBean.getUserId());
        sContent.append("\n- Email   : " + requestMembershipMailBean.getUserMail());
        sContent.append("\n\nYou can add the user in the 'Members'-tab of your organization.");
        sContent.append(getMailSignature());
        //set content
        requestMembershipMailBean.setContent(sContent.toString());
        //TODO
        mailProvider.sendMail(mailProvider.composeMessage(requestMembershipMailBean));
    }

    @Override
    public void approveRequestMembership(RequestMembershipMailBean requestMembershipMailBean) throws MailServiceException {

    }

    @Override
    public void rejectRequestMembership(RequestMembershipMailBean requestMembershipMailBean) throws MailServiceException {

    }

    @Override
    public void sendUpdateMember(UpdateMemberMailBean updateMemberMailBean) throws MailServiceException {
        //TODO: templating
        StringBuilder sContent = new StringBuilder("");
        sContent.append("Your organization profile has been updated for organization: ");
        sContent.append(updateMemberMailBean.getOrgFriendlyName());
        sContent.append(" (" + updateMemberMailBean.getOrgName() + ")");
        switch (updateMemberMailBean.getMembershipAction()) {
            case NEW_MEMBERSHIP: {
                updateMemberMailBean.setSubject("API Engine (" + config.getEnvironment() + ") - welcome to " + updateMemberMailBean.getOrgFriendlyName());
                sContent.append("\nYou have been added as a new member for the organization.");
                sContent.append("\nYou have been assigned with the role '" + updateMemberMailBean.getRole() + "'");
                break;
            }
            case TRANSFER: {
                updateMemberMailBean.setSubject("API Engine (" + config.getEnvironment() + ") - transfer ownership " + updateMemberMailBean.getOrgFriendlyName());
                sContent.append("\nYou have been assigned as owner of the organization.");
                break;
            }
            case DELETE_MEMBERSHIP: {
                updateMemberMailBean.setSubject("API Engine (" + config.getEnvironment() + ") - membership deleted");
                sContent.append("\nYou have been removed from the organization.");
                break;
            }
            case UPDATE_ROLE: {
                updateMemberMailBean.setSubject("API Engine (" + config.getEnvironment() + ") - updated role");
                sContent.append("\nYour role has been changed to '" + updateMemberMailBean.getRole() + "'.");
                break;
            }
            default: {
                updateMemberMailBean.setSubject("API Engine (" + config.getEnvironment() + ") - verify account");
                sContent.append("Verify your account.");
            }
        }
        sContent.append(getMailSignature());
        //set content
        updateMemberMailBean.setContent(sContent.toString());
        //TODO
        mailProvider.sendMail(mailProvider.composeMessage(updateMemberMailBean));
    }

    @Override
    public void sendUpdateAdmin(UpdateAdminMailBean updateAdminMailBean) throws MailServiceException {
        //TODO: templating
        StringBuilder sContent = new StringBuilder("");
        sContent.append("Your admin profile has been updated: ");
        switch (updateAdminMailBean.getMembershipAction()) {
            case NEW_MEMBERSHIP: {
                updateAdminMailBean.setSubject("API Engine (" + config.getEnvironment() + ") - admin rights assigned");
                sContent.append("\nYou have been added as administrator for the API Engine.");
                break;
            }
            case DELETE_MEMBERSHIP: {
                updateAdminMailBean.setSubject("API Engine (" + config.getEnvironment() + ") - admin rights revoked");
                sContent.append("\nYou have been removed as administrator for the API Engine.");
                break;
            }
            default: {
                updateAdminMailBean.setSubject("API Engine (" + config.getEnvironment() + ") - verify account");
                sContent.append("Verify your account.");
            }
        }
        sContent.append(getMailSignature());

        //set content
        updateAdminMailBean.setContent(sContent.toString());
        mailProvider.sendMail(mailProvider.composeMessage(updateAdminMailBean));
    }

    @Override
    public void sendContractRequest(ContractRequest request) throws MailServiceException {

    }

    @Override
    public void approveContractRequest(ContractRequest request) throws MailServiceException {

    }

    @Override
    public void rejectContractRequest(ContractRequest request) throws MailServiceException {

    }

    private String getMailSignature() {
        StringBuilder sContent = new StringBuilder();
        sContent.append("\n\n\nGreetings from the APIe Team.");
        sContent.append("\n\n");
        sContent.append("\n  /~\\");
        sContent.append("\n C oo");
        sContent.append("\n _( ^)");
        sContent.append("\n/   ~\\");
        return sContent.toString();
    }
}
