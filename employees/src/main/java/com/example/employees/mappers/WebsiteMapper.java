package com.example.employees.mappers;

import com.example.employees.dto.WebsiteDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public interface WebsiteMapper {
    @Select("SELECT * FROM kor_web")
    WebsiteDto getWebsite();

    @Update("UPDATE kor_web SET kor_web_logo = #{korWebLogo}, kor_web_title = #{korWebTitle}, kor_web_menus = #{korWebMenus}, kor_web_url = #{korWebUrl}, kor_web_copyright = #{korWebCopyright}, kor_web_term = #{korWebTerm}")
    void editWebsite(WebsiteDto websiteDto);
}
