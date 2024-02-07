package com.example.studentmanagement.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface PictureService {

    ResponseEntity<Resource> downloadPicture(String picName);

}
