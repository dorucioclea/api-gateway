package com.t1t.apim.core.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Wrapper around a map so that mvel doesn't fail when a property is missing.  Instead it
 * will get a 'null' value.
 */
public class PolicyConfigMap implements Map<String, Object> {

    private Map<String, Object> delegate;

    /**
     * Constructor.
     *
     * @param delegate the delegate map
     */
    public PolicyConfigMap(Map<String, Object> delegate) {
        this.delegate = delegate;
    }

    /**
     * @see Map#size()
     */
    @Override
    public int size() {
        return delegate.size();
    }

    /**
     * @see Map#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * @see Map#containsKey(Object)
     */
    @Override
    public boolean containsKey(Object key) {
        return true;
    }

    /**
     * @see Map#containsValue(Object)
     */
    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    /**
     * @see Map#get(Object)
     */
    @Override
    public Object get(Object key) {
        if (delegate.containsKey(key)) {
            return delegate.get(key);
        } else {
            return null;
        }
    }

    /**
     * @see Map#put(Object, Object)
     */
    @Override
    public Object put(String key, Object value) {
        return delegate.put(key, value);
    }

    /**
     * @see Map#remove(Object)
     */
    @Override
    public Object remove(Object key) {
        return delegate.remove(key);
    }

    /**
     * @see Map#putAll(Map)
     */
    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        delegate.putAll(m);
    }

    /**
     * @see Map#clear()
     */
    @Override
    public void clear() {
        delegate.clear();
    }

    /**
     * @see Map#keySet()
     */
    @Override
    public Set<String> keySet() {
        return delegate.keySet();
    }

    /**
     * @see Map#values()
     */
    @Override
    public Collection<Object> values() {
        return delegate.values();
    }

    /**
     * @see Map#entrySet()
     */
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return delegate.entrySet();
    }

}
