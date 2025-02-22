package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUser(String username);

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES(#{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
    @SelectKey(statement="SELECT users_userid_seq.last_value from users_userid_seq", keyProperty = "userid", before = false, resultType = Integer.class)
    @Options(useGeneratedKeys = true, keyProperty = "userid")
    int insert(User user);
}