package com.t1t.apim.mail;

import com.t1t.apim.AppConfigBean;
import com.t1t.apim.T1G;
import com.t1t.apim.beans.mail.*;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.MailServiceException;
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
    private static final String KEY_START = "{";
    private static final String KEY_END = "}";
    @Inject
    @T1G
    private AppConfigBean config;
    @Inject
    @Default
    private MailProvider mailProvider;
    @Inject
    private IStorage storage;

    public void sendTestMail() throws MailServiceException {
        BaseMailBean mail = new BaseMailBean();
        mail.setTo(config.getNotificationStartupMail());
        createAndSendMail(MailTopic.TEST, mail);
    }

    @Override
    public void sendStatusMail(StatusMailBean statusMailBean) throws MailServiceException {
        createAndSendMail(MailTopic.STATUS, statusMailBean);
    }

    @Override
    public void sendRequestMembership(MembershipRequestMailBean membershipRequestMailBean) throws MailServiceException {
        createAndSendMail(MailTopic.MEMBERSHIP_REQUEST, membershipRequestMailBean);
    }

    @Override
    public void approveRequestMembership(MembershipRequestMailBean bean) throws MailServiceException {
        createAndSendMail(MailTopic.MEMBERSHIP_APPROVE, bean);
    }

    @Override
    public void rejectRequestMembership(MembershipRequestMailBean bean) throws MailServiceException {
        createAndSendMail(MailTopic.MEMBERSHIP_REJECT, bean);
    }

    @Override
    public void cancelMembershipRequest(MembershipRequestMailBean bean) throws MailServiceException {
        createAndSendMail(MailTopic.MEMBERSHIP_REQUEST_CANCEL, bean);
    }

    @Override
    public void sendUpdateMember(UpdateMemberMailBean updateMemberMailBean) throws MailServiceException {
        MailTopic topic = null;
        switch (updateMemberMailBean.getMembershipAction()) {
            case NEW_MEMBERSHIP: {
                topic = MailTopic.MEMBERSHIP_NEW;
                break;
            }
            case TRANSFER: {
                topic = MailTopic.ORGANIZATION_TRANSFER;
                break;
            }
            case DELETE_MEMBERSHIP: {
                topic = MailTopic.MEMBERSHIP_DELETED;
                break;
            }
            case UPDATE_ROLE: {
                topic = MailTopic.MEMBERSHIP_UPDATE_ROLE;
                break;
            }
            default:
                break;
        }
        if (topic != null) {
            createAndSendMail(topic, updateMemberMailBean);
        }
    }

    @Override
    public void sendUpdateAdmin(UpdateAdminMailBean updateAdminMailBean) throws MailServiceException {
        MailTopic topic = null;
        switch (updateAdminMailBean.getMembershipAction()) {
            case NEW_MEMBERSHIP: {
                topic = MailTopic.MEMBERSHIP_ADMIN_NEW;
                break;
            }
            case DELETE_MEMBERSHIP: {
                topic = MailTopic.MEMBERSHIP_ADMIN_DELETED;
                break;
            }
            default:
                break;
        }
        if (topic != null) {
            createAndSendMail(topic, updateAdminMailBean);
        }
    }

    @Override
    public void sendContractRequest(ContractMailBean mailBean) throws MailServiceException {
        createAndSendMail(MailTopic.CONTRACT_REQUEST, mailBean);
    }

    @Override
    public void approveContractRequest(ContractMailBean mailBean) throws MailServiceException {
        createAndSendMail(MailTopic.CONTRACT_APPROVE, mailBean);
    }

    @Override
    public void rejectContractRequest(ContractMailBean mailBean) throws MailServiceException {
        createAndSendMail(MailTopic.CONTRACT_REJECT, mailBean);
    }

    @Override
    public void cancelContractRequest(ContractMailBean bean) throws MailServiceException {
        createAndSendMail(MailTopic.CONTRACT_REQUEST_CANCEL, bean);
    }

    private void createAndSendMail(MailTopic topic, BaseMailBean bean) {
        try {
            bean.setEnvironment(config.getEnvironment());
            //get the mail template
            MailTemplateBean mailTemplate = storage.getMailTemplate(topic);
            //prepare map
            Map<String, String> keymap = BeanUtilsBean.getInstance().describe(bean);
            final StrSubstitutor sub = new StrSubstitutor(keymap, KEY_START, KEY_END);
            prepAndSendMail(sub, mailTemplate, bean.getTo());
        } catch (StorageException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            throw new MailServiceException(ex.getMessage());
        }
    }

    /**
     * Utility endpoint to prepare and send generic mail.
     *
     * @param sub
     * @param mailTemplate
     * @param to
     * @throws StorageException
     */
    private void prepAndSendMail(StrSubstitutor sub, MailTemplateBean mailTemplate, String to) throws StorageException {
        BaseMailBean mailBean = new BaseMailBean();
        mailBean.setSubject(sub.replace(mailTemplate.getSubject()));
        mailBean.setContent(sub.replace(mailTemplate.getTemplate()));
        mailBean.setTo(to);
        _LOG.debug("Sending mail: {}", mailBean);
        mailProvider.sendMail(mailProvider.composeMessage(mailBean));
    }
}
