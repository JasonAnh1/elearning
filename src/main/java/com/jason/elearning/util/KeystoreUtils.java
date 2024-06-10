package com.jason.elearning.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;

public class KeystoreUtils {
    public static KeyStore loadKeyStore(String keystorePath, String keystorePassword) throws Exception {
        KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(new FileInputStream(keystorePath), keystorePassword.toCharArray());
        return keystore;
    }

    public static PrivateKey getPrivateKey(KeyStore keystore, String alias, String keyPassword) throws Exception {
        if (!keystore.containsAlias(alias)) {
            throw new Exception("Alias '" + alias + "' không tồn tại trong keystore");
        }
        Key key = keystore.getKey(alias, keyPassword.toCharArray());
        if (key instanceof PrivateKey) {
            return (PrivateKey) key;
        } else {
            throw new Exception("Alias '" + alias + "' không phải là private key");
        }
    }

    public static byte[] signData(PrivateKey privateKey, byte[] data) throws Exception {
        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(privateKey);
        signer.update(data); // Ký dữ liệu của tập tin PDF

        byte[] signature = signer.sign(); // Lấy chữ ký đã tạo

        // Kết hợp dữ liệu của tập tin PDF và chữ ký thành một cấu trúc mới
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(data); // Ghi dữ liệu của tập tin PDF
        outputStream.write(signature); // Ghi chữ ký vào sau dữ liệu của tập tin PDF

        return outputStream.toByteArray(); // Trả về dữ liệu của tập tin PDF đã ký
    }
}
