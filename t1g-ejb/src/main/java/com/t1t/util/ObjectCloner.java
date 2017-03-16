package com.t1t.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by michallispashidis on 25/06/16.
 */
public class ObjectCloner {
    private static final Logger _LOG = LoggerFactory.getLogger(ObjectCloner.class.getName());
    //prohibit instantiation
    private ObjectCloner() {}

    // returns a deep copy of an object
    static public Object deepCopy(Object oldObj) throws Exception {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(oldObj);
            oos.flush();
            ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
            ois = new ObjectInputStream(bin);
            return ois.readObject();
        } catch (Exception e) {
            _LOG.error("Exception in ObjectCloner: {}",e.getMessage());
            throw (e);
        } finally {
            oos.close();
            ois.close();
        }
    }
}
