package com.example.upload;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class UploadController {

    @GetMapping("/upload")
    public String getUpload() {
        return "upload";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String fileUpload(MultipartFile uploadFile) {

        try{
            String UPLOAD_DIR = "C:\\temp\\upload";

            String uuid = UUID.randomUUID().toString();
            Long sysdate = System.currentTimeMillis();

            String originName = uploadFile.getOriginalFilename();

//          점을 기준으로 확장자 ex) p3.jpg
            String fileExt = uploadFile.getOriginalFilename().substring(originName.lastIndexOf(".") + 1, originName.length());

            String transFileName = uuid + "_" + sysdate + "." + fileExt;

            Path path = Paths.get(UPLOAD_DIR, transFileName);
            Files.write(path, uploadFile.getBytes());

        }catch(Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
