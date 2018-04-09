package com.t1t.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.t1t.apim.beans.policies.Policies;
import com.t1t.apim.beans.policies.PolicyBean;
import com.t1t.apim.beans.policies.PolicyDefinitionBean;
import com.t1t.apim.beans.summary.EnrichedPolicySummaryBean;
import com.t1t.apim.beans.summary.PolicyDefinitionSummaryBean;
import com.t1t.kong.model.*;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by michallispashidis on 14/02/16.
 */
public class PolicyUtil {

    private static final Gson GSON = new GsonBuilder().create();
    private static final String KEY_START = "{{";
    private static final String KEY_END = "}}";
    private static final String NOT_CONFIGURED = "Not configured";

    public static EnrichedPolicySummaryBean createEnrichedPolicySummary(PolicyBean pb, Boolean scrubSensitiveInfo) {
        EnrichedPolicySummaryBean rval = null;
        Policies polDef = getPolicyDef(pb.getDefinition());
        if (polDef != null) {
            KongConfigValue config = getDeserializedConfig(pb, polDef, scrubSensitiveInfo);
            rval = new EnrichedPolicySummaryBean();
            rval.setId(pb.getId());
            rval.setType(pb.getType());
            rval.setOrganizationId(pb.getOrganizationId());
            rval.setEntityId(pb.getEntityId());
            rval.setEntityVersion(pb.getEntityVersion());
            rval.setDefinition(getPolicyDefinitionSummary(pb.getDefinition()));
            rval.setConfiguration(pb.getConfiguration());
            rval.setCreatedBy(pb.getCreatedBy());
            rval.setCreatedOn(pb.getCreatedOn());
            rval.setModifiedBy(pb.getModifiedBy());
            rval.setModifiedOn(pb.getModifiedOn());
            rval.setOrderIndex(pb.getOrderIndex());
            rval.setGatewayId(pb.getGatewayId());
            rval.setKongPluginId(pb.getKongPluginId());
            rval.setContractId(pb.getContractId());
            rval.setEnabled(pb.isEnabled());
            if (config != null && StringUtils.isNotEmpty(pb.getDefinition().getPopoverTemplate())) {
                try {
                    rval.setPopover(getPopover(pb, polDef, config));
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassCastException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return rval;
    }

    public static EnrichedPolicySummaryBean createEnrichedPolicySummary(PolicyBean pb) {
        return createEnrichedPolicySummary(pb, false);
    }

    public static PolicyDefinitionSummaryBean getPolicyDefinitionSummary(PolicyDefinitionBean pdb) {
        PolicyDefinitionSummaryBean rval = new PolicyDefinitionSummaryBean();
        rval.setId(pdb.getId());
        rval.setName(pdb.getName());
        rval.setDescription(pdb.getDescription());
        rval.setMarketplaceDescription(pdb.getMarketplaceDescription());
        rval.setIcon(pdb.getIcon());
        rval.setFormType(pdb.getFormType());
        rval.setForm(pdb.getForm());
        rval.setFormOverride(pdb.getFormOverride());
        rval.setScopeService(pdb.getScopeService());
        rval.setScopePlan(pdb.getScopePlan());
        rval.setScopeAuto(pdb.getScopeAuto());
        rval.setBase64Logo(pdb.getBase64logo());
        return rval;
    }

    public static Policies getPolicyDef(PolicyDefinitionBean pdb) {
        Policies polDef;
        try {
            polDef = Policies.valueOf(pdb.getId().toUpperCase());
        } catch (IllegalArgumentException ex) {
            polDef = null;
        }
        return polDef;
    }

    public static KongConfigValue getDeserializedConfig(PolicyBean pb, Policies polDef, Boolean scrubSensitiveInfo) {
        KongConfigValue rval = null;
        try {
            rval = (KongConfigValue) GSON.fromJson(pb.getConfiguration(), polDef.getClazz());
            if (scrubSensitiveInfo) {
                switch (polDef) {
                    case OAUTH2:
                        ((KongPluginOAuth) rval).setProvisionKey(null);
                        break;
                    case REQUESTTRANSFORMER:
                        rval = new KongPluginRequestTransformer();
                        break;
                    case RESPONSETRANSFORMER:
                        rval = new KongPluginResponseTransformer();
                        break;
                    case LDAPAUTHENTICATION:
                        rval = new KongPluginLDAP();
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rval;
    }

    private static String convertToCommaSeparatedString(Collection<String> collection) {
        StringBuilder rval = new StringBuilder("");
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            rval.append(iterator.next());
            if (iterator.hasNext()) rval.append(", ");
        }
        return rval.toString();
    }

    private static boolean isCollectionEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    private static String getPopover(PolicyBean pb, Policies polDef, KongConfigValue config) throws ClassCastException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, String> keyMap = BeanUtilsBean.getInstance().describe(config).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> StringUtils.isEmpty(entry.getValue()) ? NOT_CONFIGURED : entry.getValue()));
        switch (polDef) {
            case CORS:
                modifyKeyMapForCorsPopover(keyMap, config);
                break;
            case IPRESTRICTION:
                modifyKeyMapForIpRestrictionPopover(keyMap, config);
                break;
            case KEYAUTHENTICATION:
                modifyKeyMapForKeyAuthPopover(keyMap, config);
                break;
            case OAUTH2:
                modifyKeyMapForOAuthPopover(keyMap, config);
                break;
            case REQUESTTRANSFORMER:
                modifyKeyMapForRequestTransformerPopover(keyMap, config);
                break;
            case RESPONSETRANSFORMER:
                modifyKeyMapForResponseTransformerPopover(keyMap, config);
                break;
            case JWT:
                modifyKeyMapForJWTPopover(keyMap, config);
                break;
            default:
                //No further action required
                break;
        }
        StrSubstitutor sub = new StrSubstitutor(keyMap, KEY_START, KEY_END);
        return sub.replace(pb.getDefinition().getPopoverTemplate());
    }

    private static void modifyKeyMapForCorsPopover(Map<String, String> keyMap, KongConfigValue config) {
        KongPluginCors cors = (KongPluginCors) config;
        keyMap.put("methods", isCollectionEmpty(cors.getMethods()) ? NOT_CONFIGURED : convertToCommaSeparatedString(cors.getMethods().stream().map(method -> method.toString()).collect(Collectors.toList())));
        keyMap.put("origin", isCollectionEmpty(cors.getOrigins()) ? NOT_CONFIGURED : convertToCommaSeparatedString(cors.getOrigins().stream().map(method -> method.toString()).collect(Collectors.toList())));
    }

    private static void modifyKeyMapForIpRestrictionPopover(Map<String, String> keyMap, KongConfigValue config) {
        KongPluginIPRestriction ipRestriction = (KongPluginIPRestriction) config;
        keyMap.put("blacklist", isCollectionEmpty(ipRestriction.getBlacklist()) ? NOT_CONFIGURED : convertToCommaSeparatedString(ipRestriction.getBlacklist()));
        keyMap.put("whitelist", isCollectionEmpty(ipRestriction.getWhitelist()) ? NOT_CONFIGURED : convertToCommaSeparatedString(ipRestriction.getWhitelist()));
    }

    private static void modifyKeyMapForKeyAuthPopover(Map<String, String> keyMap, KongConfigValue config) {
        KongPluginKeyAuth keyAuth = (KongPluginKeyAuth) config;
        keyMap.put("keyNames", isCollectionEmpty(keyAuth.getKeyNames()) ? NOT_CONFIGURED : convertToCommaSeparatedString(keyAuth.getKeyNames()));
    }

    private static void modifyKeyMapForOAuthPopover(Map<String, String> keyMap, KongConfigValue config) {
        KongPluginOAuth oAuth = (KongPluginOAuth) config;
        keyMap.put("scopes", isCollectionEmpty(oAuth.getScopes()) ? NOT_CONFIGURED : convertToCommaSeparatedString(oAuth.getScopes().stream().map(scope -> scope.getScopeDesc()).collect(Collectors.toList())));
    }

    private static void modifyKeyMapForRequestTransformerPopover(Map<String, String> keyMap, KongConfigValue config) {
        KongPluginRequestTransformerModification reqRem = ((KongPluginRequestTransformer) config).getRemove();
        KongPluginRequestTransformerModification reqAdd = ((KongPluginRequestTransformer) config).getAdd();
        KongPluginRequestTransformerModification reqRep = ((KongPluginRequestTransformer) config).getReplace();
        KongPluginRequestTransformerModification reqApp = ((KongPluginRequestTransformer) config).getAppend();
        keyMap.put("removeQuerystrings", reqRem == null || isCollectionEmpty(reqRem.getQuerystring()) ? NOT_CONFIGURED : convertToCommaSeparatedString(reqRem.getQuerystring()));
        keyMap.put("removeBody", reqRem == null || isCollectionEmpty(reqRem.getBody()) ? NOT_CONFIGURED : convertToCommaSeparatedString(reqRem.getBody()));
        keyMap.put("removeHeaders", reqRem == null || isCollectionEmpty(reqRem.getHeaders()) ? NOT_CONFIGURED : convertToCommaSeparatedString(reqRem.getHeaders()));
        keyMap.put("addQuerystrings", reqAdd == null || isCollectionEmpty(reqAdd.getQuerystring()) ? NOT_CONFIGURED : convertToCommaSeparatedString(reqAdd.getQuerystring()));
        keyMap.put("addBody", reqAdd == null || isCollectionEmpty(reqAdd.getBody()) ? NOT_CONFIGURED : convertToCommaSeparatedString(reqAdd.getBody()));
        keyMap.put("addHeaders", reqAdd == null || isCollectionEmpty(reqAdd.getHeaders()) ? NOT_CONFIGURED : convertToCommaSeparatedString(reqAdd.getHeaders()));
        keyMap.put("replaceQuerystrings", reqRep == null || isCollectionEmpty(reqRep.getQuerystring()) ? NOT_CONFIGURED : convertToCommaSeparatedString(reqRep.getQuerystring()));
        keyMap.put("replaceBody", reqRep == null || isCollectionEmpty(reqRep.getBody()) ? NOT_CONFIGURED : convertToCommaSeparatedString(reqRep.getBody()));
        keyMap.put("replaceHeaders", reqRep == null || isCollectionEmpty(reqRep.getHeaders()) ? NOT_CONFIGURED : convertToCommaSeparatedString(reqRep.getHeaders()));
        keyMap.put("appendQuerystrings", reqApp == null || isCollectionEmpty(reqApp.getQuerystring()) ? NOT_CONFIGURED : convertToCommaSeparatedString(reqApp.getQuerystring()));
        keyMap.put("appendBody", reqApp == null || isCollectionEmpty(reqApp.getBody()) ? NOT_CONFIGURED : convertToCommaSeparatedString(reqApp.getBody()));
        keyMap.put("appendHeaders", reqApp == null || isCollectionEmpty(reqApp.getHeaders()) ? NOT_CONFIGURED : convertToCommaSeparatedString(reqApp.getHeaders()));
    }

    private static void modifyKeyMapForResponseTransformerPopover(Map<String, String> keyMap, KongConfigValue config) {
        KongPluginResponseTransformerModification resRem = ((KongPluginResponseTransformer) config).getRemove();
        KongPluginResponseTransformerModification resAdd = ((KongPluginResponseTransformer) config).getAdd();
        KongPluginResponseTransformerModification resRep = ((KongPluginResponseTransformer) config).getReplace();
        KongPluginResponseTransformerModification resApp = ((KongPluginResponseTransformer) config).getAppend();
        keyMap.put("removeJson", resRem == null || isCollectionEmpty(resRem.getJson()) ? NOT_CONFIGURED : convertToCommaSeparatedString(resRem.getJson()));
        keyMap.put("removeHeaders", resRem == null || isCollectionEmpty(resRem.getHeaders()) ? NOT_CONFIGURED : convertToCommaSeparatedString(resRem.getHeaders()));
        keyMap.put("addJson", resAdd == null || isCollectionEmpty(resAdd.getJson()) ? NOT_CONFIGURED : convertToCommaSeparatedString(resAdd.getJson()));
        keyMap.put("addHeaders", resAdd == null || isCollectionEmpty(resAdd.getHeaders()) ? NOT_CONFIGURED : convertToCommaSeparatedString(resAdd.getHeaders()));
        keyMap.put("replaceJson", resRep == null || isCollectionEmpty(resRep.getJson()) ? NOT_CONFIGURED : convertToCommaSeparatedString(resRep.getJson()));
        keyMap.put("replaceHeaders", resRep == null || isCollectionEmpty(resRep.getHeaders()) ? NOT_CONFIGURED : convertToCommaSeparatedString(resRep.getHeaders()));
        keyMap.put("appendJson", resApp == null || isCollectionEmpty(resApp.getJson()) ? NOT_CONFIGURED : convertToCommaSeparatedString(resApp.getJson()));
        keyMap.put("appendHeaders", resApp == null || isCollectionEmpty(resApp.getHeaders()) ? NOT_CONFIGURED : convertToCommaSeparatedString(resApp.getHeaders()));
    }

    private static void modifyKeyMapForJWTPopover(Map<String, String> keyMap, KongConfigValue config) {
        KongPluginJWT jwt = (KongPluginJWT) config;
        keyMap.put("claimsToVerify", isCollectionEmpty(jwt.getClaimsToVerify()) ? NOT_CONFIGURED : convertToCommaSeparatedString(jwt.getClaimsToVerify()));
    }
}
