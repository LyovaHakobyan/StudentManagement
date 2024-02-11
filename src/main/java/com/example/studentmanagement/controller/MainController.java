package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.security.SpringUser;
import com.example.studentmanagement.service.PictureService;
import com.example.studentmanagement.service.UserService;
import com.example.studentmanagement.util.RegisterReqChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;

    private final PictureService pictureService;

    @Value("${user.pic.file.path}")
    private String picFilePath;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getMainPage() {
        return "main";
    }

    @RequestMapping(value = "/downloadPic/{picName}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> downloadPic(@PathVariable("picName") String picName) {
        return pictureService.downloadPicture(picName);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage(@RequestParam(value = "error", required = false) String error, ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        if (error != null && error.equals("true")) {
            modelMap.put("err", "Wrong Email, Or Password");
        }
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String getRegisterPage(@RequestParam(value = "error", required = false) String error, ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        if (error != null) {
            modelMap.put("err", error);
        }
        return "register";
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String register(@ModelAttribute("newUser") User user, @RequestParam("picture") MultipartFile multipartFile) {
        if (RegisterReqChecker.emptyReq(user) || RegisterReqChecker.wrongUserType(user.getUserType().name())) {
            String err = "Empty Request, Try Again";
            return "redirect:/register?error=" + err;
        }
        if (userService.findByEmail(user.getEmail()) != null) {
            String err = "User By This Email, Already Exists";
            return "redirect:/register?error=" + err;
        }
        if (!multipartFile.isEmpty() && multipartFile.getSize() > 0) {
            String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File file = new File(picFilePath + File.separator + picName);
            try {
                multipartFile.transferTo(file);
            } catch (IOException e) {
                return "redirect:/register";
            }
            user.setPicName(picName);
        }
        userService.save(user);
        return "redirect:/login";
    }

}
