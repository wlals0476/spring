package com.example.kor.dto;

import lombok.Data;

@Data
public class ArticleDto {
    private int id;
    private String subject;
    private String writer;
    private String content;
    private int grp;
    private int depth;
    private String code;
}
