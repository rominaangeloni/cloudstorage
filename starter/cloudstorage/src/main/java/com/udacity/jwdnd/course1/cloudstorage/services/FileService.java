package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public String storeFile(MultipartFile file, Integer userId) throws IOException {
        if (!isFileNameAvailable(file.getOriginalFilename(), userId)) {
            throw new IllegalArgumentException("A file with this name already exists.");
        }
        File newFile = new File();
        newFile.setFilename(file.getOriginalFilename());
        newFile.setContenttype(file.getContentType());
        newFile.setFilesize(String.valueOf(file.getSize()));
        newFile.setUserid(userId);
        newFile.setFiledata(file.getBytes());

        fileMapper.insertFile(newFile);
        return "File uploaded successfully!";
    }

    public List<File> getFilesByUser(Integer userId) {
        return fileMapper.getFilesByUser(userId);
    }

    public File getFileById(Integer fileId, Integer userId) {
        return fileMapper.getFileById(fileId, userId);
    }

    public int deleteFile(Integer fileId, Integer userId) {
        return fileMapper.deleteFile(fileId, userId);
    }

    public boolean isFileNameAvailable(String fileName, Integer userId) {
        return fileMapper.getFileByNameAndUser(fileName, userId) == null;
    }


}
