package com.example.employees.controller;

import com.example.employees.dto.EmployeeDto;
import com.example.employees.mappers.EmployeeMapper;
import com.example.employees.mappers.LevelMapper;
import com.example.employees.mappers.RegisterMapper;
import com.example.employees.service.PagingSrv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Controller
public class EmployeesController {


    private String UPLOAD_LOCATION = "D:\\koreait\\employees\\src\\main\\resources\\static\\upload";

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private PagingSrv pagingSrv;

    @Autowired
    private LevelMapper levelMapper;

    @Autowired
    private RegisterMapper registerMapper;

    @Autowired
    public EmployeesController(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    @GetMapping("/admin/employees")
    public String getEmpList(Model model, @RequestParam(defaultValue = "1", value="page") int page) {
//        "emp" : 메모리 주소
        model.addAttribute("dept", registerMapper.getDept());
//        model.addAttribute("emp", employeeMapper.getEmpList());
        model.addAttribute("level", employeeMapper.getLevel());

        /* paging 처리한 목록 출력하기 */
        model.addAttribute("emp", pagingSrv.getPagingEmp(page));
        
        // page 번호 출력
        model.addAttribute("pagination", pagingSrv.pageCalc(page));

        return "admin/employees";
    }

    @GetMapping("/admin/employees/delete")
    @ResponseBody
    public Map<String, Object> deleteEmp(@RequestParam int korEmpId) {
        Map<String, Object> map = new HashMap<>();
        if( korEmpId > 0 ) {
            EmployeeDto edto = employeeMapper.getImageName(korEmpId); // image 파일

            System.out.println(edto.getKorEmpImageName());
            File file = new File(UPLOAD_LOCATION + "\\" + edto.getKorEmpImageName());
            boolean b = file.delete();

            employeeMapper.deleteEmp(korEmpId); // db
            map.put("msg", "success");
        }
        return map;
    }

    @GetMapping("/admin/employees/view")
    public String getEmpView(@RequestParam int korEmpId, Model model) {
        if(korEmpId > 0) {
            model.addAttribute("emp", employeeMapper.getEmpView(korEmpId));
            model.addAttribute("level", levelMapper.getLevel());
        }
        return "admin/empView";
    }

    @GetMapping("/admin/employees/popup")
    public String getPopup() {
        return "admin/popup";
    }

    @PostMapping("/admin/employees/upload")
    @ResponseBody
    public Map<String, Object> fileUpload(MultipartFile uploadFile, int korEmpId) {
//        이름 주소 파일이란 객체가 담겨 있음
        Map<String, Object> map = new HashMap<>();

        try {

            if(uploadFile != null) {
                EmployeeDto employeeDto = new EmployeeDto();
                employeeDto.setKorEmpImageName(uploadFile.getOriginalFilename());
                employeeDto.setKorEmpImageSize(uploadFile.getSize());
                employeeDto.setKorEmpId(korEmpId);

                employeeMapper.fileUpload(employeeDto);

//                Path path = Paths.get(경로, 파일명);
                Path path = Paths.get(UPLOAD_LOCATION, uploadFile.getOriginalFilename());
                Files.write(path, uploadFile.getBytes());

                map.put("msg", "success");
            }

        }catch(Exception ex) {
            ex.printStackTrace();
        }

        return map;
    }

    @GetMapping("/admin/employees/updateLevel")
    @ResponseBody
    public Map<String, Object> updateLevel(@ModelAttribute EmployeeDto employeeDto) {
        Map<String, Object> map = new HashMap<>();
        if(employeeDto.getKorEmpId() > 0 && employeeDto.getKorEmpLevel() > 0) {
            // update query
            employeeMapper.updateLevel(employeeDto);

            map.put("msg", "success");
        }

        return map;
    }

    @GetMapping("/admin/employees/update")
    public String getEmpUpdate(@RequestParam int korEmpId, Model model) {
        model.addAttribute("emp", employeeMapper.getEmpView(korEmpId));
        model.addAttribute("dept", registerMapper.getDept());
        model.addAttribute("level", employeeMapper.getLevel());
        return "admin/empUpdate";
    }

    @PostMapping("/admin/employees/update")
    @ResponseBody
    public Map<String, Object> setEmpUpdate(@ModelAttribute EmployeeDto employeeDto) {
        Map<String, Object> map = new HashMap<>();
        if(employeeDto.getKorEmpId() > 0) {
            employeeMapper.setEmpUpdate(employeeDto);
            map.put("msg", "success");
        }
       return map;
    }
}
