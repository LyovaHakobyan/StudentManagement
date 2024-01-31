package com.example.studentmanagement.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;

@Controller
public class MainController {

    @Value("${user.pic.file.path}")
    private String picFilePath;

    @GetMapping("/")
    public String getMainPage() {
        return "main";
    }

    @GetMapping(value = "/downloadPic/{picName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> downloadPic(@PathVariable("picName") String picName) {
        File file = new File(picFilePath + File.separator + picName);
        if (file.exists()) {
            Resource resource = new FileSystemResource(file);
            return ResponseEntity.ok().body(resource);
        } else {
            return ResponseEntity.ok().body(null);
        }
    }

}
