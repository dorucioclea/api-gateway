package com.t1t.apim.beans.managedapps;

/**
 * @author Guillaume Vandecasteele
 * @author Michallis
 * @since 2016
 *
 * Type identifier for managed applications.
 * A managed applications is a direct consumer of the API Engine, and can be:
 * <ul>
 *     <li>Marketplace: acts as a marketplace for internal and external markatplaces (consolidated APIs)</li>
 *     <li>InternalMarketplace: acts as a marketplace for externally published APIs</li>
 *     <li>ExternalMarketplace: acts as a marketplace for internally published APIs </li>
 *     <li>Publisher: acts as a publisher (can only be one publisher active) for publishing APIs to marketplaces</li>
 *     <li>Consent: acts as a special application accessing all APIs but in the context of authentication/delegation provisioning</li>
 * </ul>
 */
public enum ManagedApplicationTypes {
    InternalMarketplace, ExternalMarketplace, Publisher, Consent, Admin
}
