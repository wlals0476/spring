package com.example.kor.dto;

import lombok.Data;

@Data
public class PageDto {

    private int pageCount = 3; /* 한 페이지에 표시될 게시물 개수 */
    private int blockCount = 5; /* 페이지 번호 개수 */
    private int page; /* 현재 페이지 번호를 담는 필드 */
    private int totalPage;
    private int startPage;
    private int endPage;
}
