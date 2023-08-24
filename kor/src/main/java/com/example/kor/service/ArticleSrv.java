package com.example.kor.service;

import com.example.kor.dto.ArticleDto;
import com.example.kor.dto.PageDto;
import com.example.kor.mapper.ConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleSrv {

    @Autowired
    private ConfigMapper configMapper;
    
    public PageDto pageCalc(int page, String code) {
        // 페이지 계산 알고리즘 메소드
        PageDto pageDto = new PageDto(); // pageDto.getPageCount();
        int totalCount = configMapper.getArticleCount(code);

        // 전체 페이지 번호 구하기 : 전체 게시물 / 한 페이지에 표시할 게시물 수
        // 7 / 3 =  2.3 -> 3.0 -> 3
        int totalPage = (int) Math.ceil( (double) totalCount / pageDto.getPageCount() );
        
        // 시작 페이지 구하기
        int startPage = ((int) Math.ceil( (double) page / pageDto.getBlockCount() ) - 1) * pageDto.getStartPage() + 1;

        // 끝 페이지 구하기
        int endPage = startPage + pageDto.getBlockCount() - 1;

        if(endPage > totalPage) {
            endPage = totalPage;
        }

        pageDto.setTotalPage(totalPage);
        pageDto.setStartPage(startPage);
        pageDto.setEndPage(endPage);
        pageDto.setPage(page);

        return pageDto;
    }

    public List<ArticleDto> getList(String code, String searchType, String words, int page) {

        // LIMIT 0, 3 : 1page
        // LIMIT 3, 3 : 2page
        // LIMIT 6, 3 : 3page

        PageDto pageDto = new PageDto();

        int startNum = (page - 1) * pageDto.getPageCount();


        // 검색어
        String searchQuery = "";
        if(searchType.equals("subject") || searchType.equals("writer")) {
            // where 컬럼명 = 검색어
            // where subject(writer) = '검색어'
            searchQuery = "WHERE "+searchType+" = '"+words+"'";

        }else if (searchType.equals("content")) {
            // where 컬럼명 like %검색어%
            searchQuery = "WHERE content LIKE '%"+words+"%'";

        }else {
            // 검색어가 없으면 전체 값이 출력
            searchQuery = "";
        }

        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("searchQuery", searchQuery);
        map.put("startNum", startNum);
        map.put("count", pageDto.getPageCount());

        return configMapper.getList(map);
    }
}
