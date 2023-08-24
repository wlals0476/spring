package com.example.upload2;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;

@Controller
public class UploadController {

    @Autowired
    private UploadMapper uploadMapper;

    @GetMapping("/main")
    public String getMain() {
        return "main";
    }

    @GetMapping("/upload")
    public String getUpload() {
        return "upload";
    }

    @PostMapping("/upload")
    @ResponseBody
    public Map<String, Object> fileUpload(MultipartFile uploadFile) {
        Map<String, Object> map = new HashMap<>();

          try {
              String UPLOAD_DIR = "C:\\temp\\upload";
              String oName = uploadFile.getOriginalFilename();
              Long fileSize = uploadFile.getSize();

              // 오늘 날짜에 맞는 폴더이름 2023-06-27
              // File folder = new File(경로 + 생성할폴더명);
              // new SimpleTimeZone() : 2023-06-27
              String folderName = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
              File f = new File(UPLOAD_DIR + "\\" +folderName);

              if( !f.exists() ) {
                  f.mkdir();
                  System.out.println("폴더가 생성 되었습니다.");
              }

//            System.out.println(oName);
//            System.out.println((fileSize / 1000) + "kb");

//            p1.jpg => .을 기중으로 뒤에만 추출(lastIndex) => 시간.jpg
              String ext = oName.substring(oName.lastIndexOf("."));
//            System.out.println(ext);

              String changeName = System.currentTimeMillis() + ext;

//        Path path = Paths.get(저장경로, 파일이름);
              Path path = Paths.get(f.toString(), changeName);
              Files.write(path, uploadFile.getBytes());

              map.put("msg", "success");
              map.put("oName", oName);
              map.put("size", ((double)fileSize / (double) 1000));

          }catch (Exception e) {
              e.printStackTrace();
          }

        return map;
    }

    @GetMapping("/list")
    public String getList() {
        return "list";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> checkLogin(@ModelAttribute UploadDto uploadDto, HttpServletRequest req) {
        UploadDto emp = uploadMapper.getEmp(uploadDto);

        Map<String, Object> map = new HashMap<>();
        
        if(emp != null) { //가입된 회원이 있을 때
            System.out.println(emp);
            HttpSession hs = req.getSession(); // 세션 객체 만든 후
            hs.setAttribute("sessInfo", emp); // 세션 객체에 개인 정보 삽입
            hs.setMaxInactiveInterval(60 * 30); // 세션 유효 시간
            map.put("msg", "success");
                
        }else {
            map.put("msg", "failure");
        }
        return map;
    }
    
    @GetMapping("/logout")
    public String getLogout(HttpSession hs) { //생성된 세션을 삭제
//        만들어진 세션을 없앨때
        hs.invalidate();
        return "redirect:/login";
    }
}
