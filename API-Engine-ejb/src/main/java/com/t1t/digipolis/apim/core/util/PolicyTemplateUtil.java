package com.t1t.digipolis.apim.core.util;

import com.t1t.digipolis.apim.beans.policies.PolicyBean;
import com.t1t.digipolis.apim.beans.policies.PolicyDefinitionBean;
import com.t1t.digipolis.apim.beans.policies.PolicyDefinitionTemplateBean;
import com.t1t.digipolis.apim.common.util.AesEncrypter;
import com.t1t.digipolis.apim.core.i18n.Messages;
import org.codehaus.jackson.map.ObjectMapper;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Utility for dealing with policy templates.  Policy descriptions are
 * generated dynamically using MVEL 2.0 templates configured on the policy
 * definition.  This utility applies those templates to policy instances
 * using the policy instance's configuration.
 *
 */
public class PolicyTemplateUtil {

    private static final ObjectMapper mapper = new ObjectMapper();
    // Cache a MVEL 2.0 compiled template - the key is PolicyDefId::language
    private static final Map<String, CompiledTemplate> templateCache = new HashMap<>();

    /**
     * Clears out the template cache.
     */
    public static void clearCache() {
        templateCache.clear();
    }

    /**
     * Generates a dynamic description for the given policy and stores the
     * result on the policy bean instance.  This should be done prior
     * to returning the policybean back to the user for a REST call to the
     * management API.
     * @param policy the policy
     * @throws Exception any exception
     */
    public static void generatePolicyDescription(PolicyBean policy) throws Exception {
        PolicyDefinitionBean def = policy.getDefinition();
        PolicyDefinitionTemplateBean templateBean = getTemplateBean(def);
        if (templateBean == null) {
            return;
        }
        String cacheKey = def.getId() + "::" + templateBean.getLanguage(); //$NON-NLS-1$
        CompiledTemplate template = templateCache.get(cacheKey);
        if (template == null) {
            template = TemplateCompiler.compileTemplate(templateBean.getTemplate());
            templateCache.put(cacheKey, template);
        }
        try {
            // TODO hack to fix broken descriptions - this util should probably not know about encrypted data
            String jsonConfig = AesEncrypter.decrypt(policy.getConfiguration());
            Map<String, Object> configMap = mapper.readValue(jsonConfig, Map.class);
            configMap = new PolicyConfigMap(configMap);
            String desc = (String) TemplateRuntime.execute(template, configMap);
            policy.setDescription(desc);
        } catch (Exception e) {
            // TODO log the error
            policy.setDescription(templateBean.getTemplate());
        }
    }

    /**
     * Determines the appropriate template bean to use given the current locale.
     * @param def
     */
    private static PolicyDefinitionTemplateBean getTemplateBean(PolicyDefinitionBean def) {
        Locale currentLocale = Messages.i18n.getLocale();
        String lang = currentLocale.getLanguage();
        String country = lang + "_" + currentLocale.getCountry(); //$NON-NLS-1$

        PolicyDefinitionTemplateBean nullBean = null;
        PolicyDefinitionTemplateBean langBean = null;
        PolicyDefinitionTemplateBean countryBean = null;
        for (PolicyDefinitionTemplateBean pdtb : def.getTemplates()) {
            if (pdtb.getLanguage() == null) {
                nullBean = pdtb;
            } else if (pdtb.getLanguage().equals(country)) {
                countryBean = pdtb;
                break;
            } else if (pdtb.getLanguage().equals(lang)) {
                langBean = pdtb;
            }
        }
        if (countryBean != null) {
            return countryBean;
        }
        if (langBean != null) {
            return langBean;
        }
        if (nullBean != null) {
            return nullBean;
        }
        return null;
    }
}
