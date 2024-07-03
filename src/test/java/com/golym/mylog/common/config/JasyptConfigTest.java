package com.golym.mylog.common.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JasyptConfigTest extends JasyptConfig {

    @Test
    public void jasypt_encrypt_decrypt_test() {

        String encrptKey = System.getProperty("jasypt.encryptor.password");
        //String plainText = "jdbc:mysql://localhost:3306/mylog_db?useSSL=false&allowPublicKeyRetrieval=true";
        String plainText = "example";


        StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
        jasypt.setPassword(encrptKey);

        String encryptedText = jasypt.encrypt(plainText);
        String decryptedText = jasypt.decrypt(encryptedText);

        System.out.println("encryptedText = " + encryptedText);
        System.out.println("decryptedText = " + decryptedText);

        assertThat(plainText).isEqualTo(decryptedText);
    }
}
