package com.example.employees.service;

import com.example.employees.dto.BoardDto;
import com.example.employees.dto.BoardPagingDto;
import com.example.employees.dto.EmployeeDto;
import com.example.employees.dto.PagingDto;
import com.example.employees.mappers.BoardMapper;
import com.example.employees.mappers.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardPagingSrv {

    @Autowired
    private BoardMapper boardMapper;
    
    // 1번 객체 : 알고리즘 구현
    public BoardPagingDto BoardPageCalc(int page) {
        int totalCount = boardMapper.getTotalCount();
        BoardPagingDto bpd = new BoardPagingDto();

        int totalPage = (int) Math.ceil(((double) totalCount / bpd.getPageCount())); // 5 / 2 = 2.5
        int startPage = (((int) Math.ceil((double) page / bpd.getBlockCount())) - 1) * bpd.getBlockCount() + 1;
        int endPage = startPage + bpd.getBlockCount() - 1;

        if(endPage > totalPage) {
            endPage = totalPage;
        }

        bpd.setPage(page);
        bpd.setTotalPage(totalPage);
        bpd.setStartPage(startPage);
        bpd.setEndPage(endPage);

        return bpd;
    }
    
    // 2번 객체 : 구현한 알고리즘을 mapper start, limit 변수로 보내
    // 페이지 잘라서 화면에 표시하기
    
    // Controller에서 넘겨준 page 값을 가져 오기
    public List<BoardDto> getPagingBoard(int page) {
        Map<String, Object> map = new HashMap<>();
        BoardPagingDto bpd = new BoardPagingDto();
        int pageStartNum = (page - 1) * bpd.getPageCount();
        map.put("start", pageStartNum);
        map.put("limit", bpd.getPageCount());

        return boardMapper.getBoard(map);
    }
}
