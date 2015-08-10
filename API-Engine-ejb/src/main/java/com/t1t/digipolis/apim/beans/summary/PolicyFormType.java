package com.t1t.digipolis.apim.beans.summary;

/**
 * The different types of policy forms supported by apiman.  The UI will handle
 * displaying a configuration form for the policy differently depending on the
 * type of policy form.  For example, if the type is Default, then the UI is 
 * expected to know how to create a form using only the policy definition ID.
 * For the built-in apiman policies, this should work well (e.g. IP Whitelisting,
 * Rate Limiting, etc).  However, if the policy definition came from a plugin,
 * then the plugin may have also provided a JsonSchema that defines the 
 * configuration format.  In this case, the UI should use a JsonSchema form
 * generator to show the form.
 * 
 * If the type of a policy def form is Default but the UI does not know how to
 * build a specific form for the policy, a default form (text area) will be 
 * used.
 *
 */
public enum PolicyFormType {
    
    Default, JsonSchema;

}
