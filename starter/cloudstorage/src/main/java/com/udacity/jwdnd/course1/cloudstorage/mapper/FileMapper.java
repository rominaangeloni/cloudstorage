package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            "VALUES (#{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(File file);

    @Select("SELECT * FROM FILES WHERE userid = #{userid}")
    List<File> getFilesByUser(Integer userid);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId} AND userId = #{userId}")
    File getFileById(@Param("fileId") Integer fileId, @Param("userId") Integer userId);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId} AND userId = #{userId}")
    int deleteFile(@Param("fileId") Integer fileId, @Param("userId") Integer userId);

    @Select("SELECT * FROM FILES WHERE filename = #{filename} AND userId = #{userId}")
    File getFileByNameAndUser(@Param("filename") String filename, @Param("userId") Integer userId);

}

