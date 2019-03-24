package cn.com.play.controllers.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Slf4j
public class AESUtil {

    public static final String UTF_8 = Charset.defaultCharset().name();

    private static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
    private static final String ALGORITHM_MODE = "AES/ECB/PKCS5Padding";
    private static final String KEY_ALGORITHM = "AES";

    private static Key getSecretKey(String seed) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM);
        secureRandom.setSeed(seed.getBytes());
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(128, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        return new SecretKeySpec(enCodeFormat, KEY_ALGORITHM);
    }

    public static String encrypt(String key, String value) {
        try {
            Key secretKey = getSecretKey(key);
            Cipher cipher = Cipher.getInstance(ALGORITHM_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] bytes = Base64.encodeBase64(cipher.doFinal(value.getBytes(UTF_8)));
            return new String(bytes, UTF_8);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
                | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            log.error(String.format("Encrypt value by AES failed, value = %s.", value), e);
            return null;
        }
    }

    public static String decryptByPKCS5Padding(String key, String value) {
        byte[] reqInfoBytes ;
        try {
            Key secretKey = getSecretKey(key);
            Cipher cipher = Cipher.getInstance(ALGORITHM_MODE);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            reqInfoBytes = Base64.decodeBase64(value.getBytes(UTF_8));
            return new String(cipher.doFinal(reqInfoBytes), UTF_8);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
            log.error("failed to decrypt [" + value + "], return empty. ", e);
            return null;
        }
    }

}
