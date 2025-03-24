package dev.poporo.labgithubactionstrivy.service;

import java.security.MessageDigest;
import java.util.Base64;
import org.springframework.stereotype.Service;

@Service
public class WeakEncryptionService {

    public String encrypt(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");  // 약한 해시 알고리즘
        byte[] hash = md.digest(data.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }
}
