package me.deejack.chatserver.utils;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;

/**
 * @author DeeJack
 */
public class Encrypter {
    private String key;
    private String password;

    public Encrypter(String key, String password) {
        this.key = key;
        this.password = password;
    }

    public Password encrypt() throws Exception {
        return new Password(password, key);
    }

    public class Password {
        private String password;
        private String key;

        public Password(String password, String key) {
            this.password = password;
            this.key = key;
        }

        public String getPassword() {
            return password;
        }

        public String getKey() {
            return key;
        }
    }
}
