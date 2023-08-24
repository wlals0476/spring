package com.example.employees.controller;

import com.example.employees.dto.BoardDto;
import com.example.employees.mappers.BoardMapper;
import com.example.employees.service.BoardPagingSrv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/board")
public class BoardController {

    @Value("${spring.servlet.multipart.location}")
    String UPLOAD_LOCATION;

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private BoardPagingSrv boardPagingSrv;

    @GetMapping("")
    public String getBoardList(Model model, @RequestParam(defaultValue = "1", value="page") int page) {

        model.addAttribute("board", boardPagingSrv.getPagingBoard(page));
        model.addAttribute("pagination", boardPagingSrv.BoardPageCalc(page));

        return "board/list";
    }

    @GetMapping("/write")
    public String getBoardWrite() {
        return "board/write";
    }

    @PostMapping("/write")
    @ResponseBody
    public Map<String, Object> setBoardWrite(@ModelAttribute BoardDto boardDto, MultipartFile uploadFile) {
            Map<String, Object> map = new HashMap<>();


//        // 원본파일 이름, 원본파일 용량, 원본파일 이름 바꾸기(날짜로)
//        System.out.println("원본파일명 : " + uploadFile.getOriginalFilename());
//        System.out.println("원본파일용량 : " + uploadFile.getSize() + "bytes");
//
//        // 현재파일명.hwp
//        String oName = uploadFile.getOriginalFilename();
//        String ext = oName.substring(oName.lastIndexOf("."));
//        System.out.println("원본파일확장자 : " + ext);
//                        // 현재시간을 1/1000초.확장자
//        String tName = System.currentTimeMillis() + ext;
//        System.out.println("변환된파일명 : " + tName);

        try {
            
            if(uploadFile != null) {
                // 날짜별 폴더 이름 만들기
                String saveDir = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());

                // 폴더생성 : File 객체 사용
                // File f = new File(경로 + 폴더명);
                File f = new File(UPLOAD_LOCATION + "\\" + saveDir);

                // 폴더가 없을 때만 만들기
                if(!f.exists()) {
                    f.mkdir();
                }

                String oName = uploadFile.getOriginalFilename();
                String ext = oName.substring(oName.lastIndexOf("."));
                String tName = System.currentTimeMillis() + ext;

                // 파일 저장 : Path(경로, 변환된 파일명) + Files.Write(경로, 파일명.getBytes)
                // Path p = Paths.get(파일경로, 이름);
                Path p = Paths.get(String.valueOf(f), tName);
                // Files.write(p, 원본이름);
                Files.write(p, uploadFile.getBytes());

                // 첨부파일
                boardDto.setKorBoardUploadName(uploadFile.getOriginalFilename());
                boardDto.setKorBoardUploadUrl(saveDir);
                boardDto.setKorBoardUploadSize(uploadFile.getSize());
                boardDto.setKorBoardUploadTrans(tName);
            }

            // 1 -> 2 -> 3
            boardDto.setKorBoardReplyGrp(boardMapper.getMaxCnt());
            // 첨부파일 O, X 실행되어야 하는 구문
            boardMapper.setBoardWrite(boardDto);

            map.put("msg", "success");

        }catch(Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    @GetMapping("/delete")
    public String deleteBoard(@RequestParam int korBoardId) {
        if(korBoardId > 0) {
            boardMapper.deleteBoard(korBoardId);
        }
       return "redirect:/admin/board?page=1";
    }

    @GetMapping("/view")
    public String viewBoard(@RequestParam int korBoardId, Model model) {
        if(korBoardId > 0) {
            boardMapper.updateVisit(korBoardId);
            model.addAttribute("board", boardMapper.viewBoard(korBoardId));
        }
        return "board/view";
    }

    @GetMapping("/modify")
    public String modifyBoard(@RequestParam int korBoardId, Model model) {
        if(korBoardId > 0) {
            model.addAttribute("board", boardMapper.viewBoard(korBoardId));
        }
        return "board/modify";
    }

    @PostMapping("/modify")
    @ResponseBody
    public Map<String, Object> setBoardUpdate(@ModelAttribute BoardDto boardDto, MultipartFile uploadFile) {
        Map<String, Object> map = new HashMap<>();

        /* 첨부파일 업로드 여부에 따라... */

        try {

            if(uploadFile != null) {
                // 날짜별 폴더 이름 만들기
                String saveDir = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());

                // 폴더생성 : File 객체 사용
                // File f = new File(경로 + 폴더명);
                File f = new File(UPLOAD_LOCATION + "\\" + saveDir);

                // 폴더가 없을 때만 만들기
                if(!f.exists()) {
                    f.mkdir();
                }

                String oName = uploadFile.getOriginalFilename();
                String ext = oName.substring(oName.lastIndexOf("."));
                String tName = System.currentTimeMillis() + ext;

                // 파일 저장 : Path(경로, 변환된 파일명) + Files.Write(경로, 파일명.getBytes)
                // Path p = Paths.get(파일경로, 이름);
                Path p = Paths.get(String.valueOf(f), tName);
                // Files.write(p, 원본이름);
                Files.write(p, uploadFile.getBytes());

                // 첨부파일
                boardDto.setKorBoardUploadName(uploadFile.getOriginalFilename());
                boardDto.setKorBoardUploadUrl(saveDir);
                boardDto.setKorBoardUploadSize(uploadFile.getSize());
                boardDto.setKorBoardUploadTrans(tName);

            }else {
                //원래 자기가 가지고 있던 첨부파일 원본 이름, 용량, 변환된 이름을 그대로 다시 DB에 업데이트
                boardDto.setKorBoardUploadName(boardDto.getKorBoardUploadName());
                boardDto.setKorBoardUploadSize(boardDto.getKorBoardUploadSize());
                boardDto.setKorBoardUploadTrans(boardDto.getKorBoardUploadTrans());
            }

            boardMapper.setBoardUpdate(boardDto);
            map.put("msg", "success");

        }catch(Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    @GetMapping("/reply")
    public String setReply(@ModelAttribute BoardDto boardDto, Model model) {
        model.addAttribute("board", boardMapper.viewBoard(boardDto.getKorBoardId()));
        return "board/reply";
    }

    @PostMapping("/reply")
    @ResponseBody
    public Map<String, Object> setReply(@ModelAttribute BoardDto boardDto, MultipartFile uploadFile) {
        Map<String, Object> map = new HashMap<>();

        try {

            if(uploadFile != null) {
                // 날짜별 폴더 이름 만들기
                String saveDir = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());

                // 폴더생성 : File 객체 사용
                // File f = new File(경로 + 폴더명);
                File f = new File(UPLOAD_LOCATION + "\\" + saveDir);

                // 폴더가 없을 때만 만들기
                if(!f.exists()) {
                    f.mkdir();
                }

                String oName = uploadFile.getOriginalFilename();
                String ext = oName.substring(oName.lastIndexOf("."));
                String tName = System.currentTimeMillis() + ext;

                // 파일 저장 : Path(경로, 변환된 파일명) + Files.Write(경로, 파일명.getBytes)
                // Path p = Paths.get(파일경로, 이름);
                Path p = Paths.get(String.valueOf(f), tName);
                // Files.write(p, 원본이름);
                Files.write(p, uploadFile.getBytes());

                // 첨부파일
                boardDto.setKorBoardUploadName(uploadFile.getOriginalFilename());
                boardDto.setKorBoardUploadUrl(saveDir);
                boardDto.setKorBoardUploadSize(uploadFile.getSize());
                boardDto.setKorBoardUploadTrans(tName);
            }

            /* reply에 필요한 값 설정 */
            boardDto.setKorBoardReplyGrp(boardDto.getKorBoardReplyGrp());
            boardDto.setKorBoardReplyGrpSeq(boardDto.getKorBoardReplyGrpSeq() + 1);
            boardDto.setKorBoardReplyGrpSeqIndent(boardDto.getKorBoardReplyGrpSeqIndent() + 1);

            boardMapper.replyBoard(boardDto);

            map.put("msg", "success");

        }catch(Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> getDownload(@RequestParam String dir, @RequestParam String filename) throws IOException {
        
        //다운로드 할 저장 위치
        Path p = Paths.get(UPLOAD_LOCATION + "\\" + dir + "\\" + filename);

        /* header 정보 */
        String contentType = Files.probeContentType(p);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(filename, StandardCharsets.UTF_8).build());

        headers.add(HttpHeaders.CONTENT_TYPE, contentType);

        Resource resource = new InputStreamResource(Files.newInputStream(p));

        return new ResponseEntity<>(resource, headers, HttpStatus.OK); // 정상적으로 전송됐으면...
    }
}
