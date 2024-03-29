package com.example.employees.controller;

import com.example.employees.dto.RegisterDto;
import com.example.employees.mappers.LoginMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main")
public class LoginController {

    @Autowired
    private LoginMapper loginMapper;

    @GetMapping("/login")
    public String getLogin() {
        return "/main/login";
    }

//    login : HttpServletRequest
//    logout : HttpSession

    @PostMapping("/login")
    public String  checkLogin(@ModelAttribute RegisterDto registerDto, HttpServletRequest req) {
        System.out.println(loginMapper.getEmpInfo(registerDto));
        RegisterDto r = loginMapper.getEmpInfo(registerDto);
        
        if( r != null) {
            if( r.getKorEmpAuth().equals("Y")) {
                System.out.println("관리자 페이지로 이동");
                //세션 정보 생성

                HttpSession hs = req.getSession();
                //만들 세션 정보를 입력, 시간
                //hs.setAttribute("별명", 값);
                hs.setAttribute("emp", r);
                hs.setMaxInactiveInterval(60 * 10);

                return "redirect:/admin";

            }else {
                System.out.println("사용자 페이지로 이동");
                return "redirect:/home";
            }
            
        }else {
            System.out.println("이메일 또는 비밀번호를 확인하세요.");
            return "redirect:/main/login";
        }
    }

    @GetMapping("/logout")
    public String getLogout(HttpSession hs) {
        hs.invalidate();
        return "/main/login";
    }

}
