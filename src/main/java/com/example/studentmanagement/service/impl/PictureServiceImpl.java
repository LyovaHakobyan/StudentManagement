package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.service.PictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class PictureServiceImpl implements PictureService {

    @Value("${user.pic.file.path}")
    private String picFilePath;

    @Override
    public ResponseEntity<Resource> downloadPicture(String picName) {
        File file = new File(picFilePath + File.separator + picName);
        if (file.exists()) {
            Resource resource = new FileSystemResource(file);
            return ResponseEntity.ok().body(resource);
        } else {
            return ResponseEntity.ok().body(null);
        }
    }

}
