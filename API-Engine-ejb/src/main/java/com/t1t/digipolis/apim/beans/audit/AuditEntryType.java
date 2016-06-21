package com.t1t.digipolis.apim.beans.audit;

/**
 * Indicates type type of auditing event is represented by an audit entry.
 *
 */
public enum AuditEntryType {

    // Entity events
    Create, Update, Delete, Clone, 
    // Action events
    Grant, Revoke, Transfer,
    Publish, Retire,
    Register, Unregister,
    AddPolicy, RemovePolicy, UpdatePolicy, ReorderPolicies,
    CreateContract, BreakContract,
    Lock,
    UpdateDefinition, DeleteDefinition, Deprecate,
    //Other
    OAuth2Reissued, KeyAuthReissued
}
