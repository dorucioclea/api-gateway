package com.t1t.util;

import com.t1t.apim.exceptions.ExceptionFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * A simple AES encryptor.
 */
public final class AesEncrypter {

    private static final String SECRET_KEY = "d9ab32eb-b8c7433";
    private static final String AES = "AES";
    private static final String PREFIX = "$CRYPT::";

    private static SecretKeySpec secretKeySpec;

    static {
        final byte[] ivraw = SECRET_KEY.getBytes();
        secretKeySpec = new SecretKeySpec(ivraw, AES);
    }

    private AesEncrypter() {}

    /**
     * Encrypt.
     *
     * @param plainText the plain text
     * @return the string
     */
    public static String encrypt(final String plainText) {
        if (StringUtils.isBlank(plainText)) {
            return null;
        }

        byte[] encrypted;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        } catch (final NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw ExceptionFactory.encryptionFailed("encryption", plainText, e);
        }
        try {
            encrypted = cipher.doFinal(plainText.getBytes());
        } catch (final IllegalBlockSizeException | BadPaddingException e) {
            throw ExceptionFactory.encryptionFailed("encryption", plainText, e);
        }
        return PREFIX + new String(Base64.encodeBase64(encrypted));
    }

    /**
     * Decrypt.
     *
     * @param encryptedText the encrypted text
     * @return the string
     */
    public static String decrypt(String encryptedText) {
        if (StringUtils.isBlank(encryptedText)) {
            return null;
        }

        if (!encryptedText.startsWith(PREFIX)) {
            return encryptedText;
        }

        byte[] decoded = Base64.decodeBase64(encryptedText.substring(8).getBytes());
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        } catch (final NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw ExceptionFactory.encryptionFailed("decryption", encryptedText, e);
        }
        try {
            return new String(cipher.doFinal(decoded));
        } catch (final IllegalBlockSizeException | BadPaddingException e) {
            throw ExceptionFactory.encryptionFailed("decryption", encryptedText, e);
        }
    }
}
