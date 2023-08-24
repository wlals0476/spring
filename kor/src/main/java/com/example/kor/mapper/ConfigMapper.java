package com.example.kor.mapper;

import com.example.kor.dto.ArticleDto;
import com.example.kor.dto.ConfigDto;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mapper
public interface ConfigMapper {

    @Insert("INSERT INTO config VALUES(#{code}, #{title}, #{color})")
    void setConfig(ConfigDto configDto);

    @Select("create table kortb_${code}(\n" +
            "id int not null auto_increment,\n" +
            "subject varchar(100),\n" +
            "writer varchar(20),\n" +
            "content text,\n" +
            "grp int,\n" +
            "depth int,\n" +
            "primary key(id)\n" +
            ");")
    void createTable(String code);

    @Select("SELECT * FROM config ORDER BY code ASC")
    List<ConfigDto> getConfig();

    @Delete("DELETE FROM config WHERE code = #{code}")
    void deleteConfig(String code);

    @Select("DROP TABLE kortb_${code}")
    void dropTable(String code);

    @Select("SELECT * FROM config WHERE code = #{code}")
    ConfigDto getConfigOne(String code);

    @Select("SELECT * FROM kortb_${code} ${searchQuery} ORDER BY grp DESC, depth ASC LIMIT #{startNum}, #{count}")
    List<ArticleDto> getList(Map<String, Object> map);

    @Insert("INSERT INTO kortb_${code} VALUES(NULL, #{subject}, #{writer}, #{content}, #{grp}, 1)")
    void setWrite(ArticleDto articleDto);

    @Select("SELECT ifnull( max(grp) + 1, 1) as maxCnt FROM kortb_${code}")
    int getMaxCnt(String code);

    @Delete("DELETE FROM kortb_${code} WHERE id = #{id}")
    void delArticle(ArticleDto articleDto);

    @Select("SELECT * FROM kortb_${code} WHERE id = #{id}")
    ArticleDto getEdit(ArticleDto articleDto);

    @Update("UPDATE kortb_${code} SET subject = #{subject}, writer = #{writer}, content = #{content} WHERE id = #{id}")
    void setEdit(ArticleDto articleDto);

    @Insert("INSERT INTO kortb_${code} VALUES(NULL, #{subject}, #{writer}, #{content}, #{grp}, #{depth})")
    void setReply(ArticleDto articleDto);

    // 페이지 계산을 위한 전체 게시물 수
    @Select("SELECT COUNT(*) FROM kortb_${code}")
    int getArticleCount(String code);
}
