package com.StudentMS.service;

import com.StudentMS.entity.Course;
import com.StudentMS.entity.File;
import com.StudentMS.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface FileService {
    void storeFile(@NotNull MultipartFile file, User uploader, boolean visibility, Course courseId) throws IOException;

    Resource loadFileAsResource(Long id) throws MalformedURLException, FileNotFoundException;

    void deleteFile(Long fileId);

    List<File> findAll();
}