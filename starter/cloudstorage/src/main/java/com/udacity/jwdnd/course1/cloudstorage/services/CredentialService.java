package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public List<Credential> getCredentialsByUser(Integer userId) {
        return credentialMapper.getCredentialsByUser(userId);
    }

    public Credential getCredentialById(Integer credentialId) {
        return credentialMapper.getCredentialById(credentialId);
    }

    public void saveCredential(Credential credential) {
        if (!isUsernameAvailable(credential.getUsername(),credential.getUrl(),credential.getUserId())) {
            throw new IllegalArgumentException("A username for that url already exists.");
        }
        if (credential.getCredentialId() == null) {
                credentialMapper.insertCredential(credential);
        } else {
                credentialMapper.updateCredential(credential);
        }
    }

    public int deleteCredential(Integer credentialId) {
        return credentialMapper.deleteCredential(credentialId);
    }

    public boolean isUsernameAvailable(String username,String url, Integer userId){
        return credentialMapper.getCredentialByUsernameAndUrl(username, url, userId) == null;
    }
}
