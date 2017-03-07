package com.t1t.digipolis.util;

import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by michallispashidis on 17/11/15.
 */
public class KeyUtils {
    public static PublicKey getKey(String key){
        try{
            byte[] byteKey = Base64.decode(key.getBytes());
            //byte[] byteKey = Base64.decode(key.getBytes(), Base64.DEFAULT);
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(X509publicKey);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get private key from DER file.
     *
     * @param file
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey getPrivateKey(File file) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        FileInputStream fis = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int)file.length()];
        dis.readFully(keyBytes);
        dis.close();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    /**
     * Get Public key from DER file.
     *
     * @param file
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey getPublicKey(File file) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException  {
        FileInputStream fis = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int)file.length()];
        dis.readFully(keyBytes);
        dis.close();
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    /**
     * Get private key from PEM string.
     *
     * @param privKey
     * @return
     */
    public static PrivateKey getPrivateKey(String privKey) {
        try {
            PemReader pemReader = new PemReader(new StringReader(privKey));
            byte[] content = pemReader.readPemObject().getContent();
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(content);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        }
        catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            return null;
        }
    }

    /**
     * Get Public key from PEM string.
     *
     * @param pubKey
     * @return
     */
    public static PublicKey getPublicKey(String pubKey) {
        try {
            PemReader pemReader = new PemReader(new StringReader(pubKey));
            byte[] content = pemReader.readPemObject().getContent();
            X509EncodedKeySpec spec = new X509EncodedKeySpec(content);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        }
        catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            return null;
        }
    }
}
