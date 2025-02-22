package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE userId = #{userId}")
    List<Credential> getCredentialsByUser(Integer userId);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    Credential getCredentialById(Integer credentialId);

    @Select("SELECT * FROM CREDENTIALS WHERE userId = #{userId} AND url = #{url} AND url = #{url}")
    Credential getCredentialByUsernameAndUrl(String username, String url, Integer userId);

    @Insert("INSERT INTO CREDENTIALS (url, username, password, userId) VALUES (#{url}, #{username}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insertCredential(Credential credential);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, password = #{password} WHERE credentialId = #{credentialId} AND userId = #{userId}")
    int updateCredential(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    int deleteCredential(Integer credentialId);
}
