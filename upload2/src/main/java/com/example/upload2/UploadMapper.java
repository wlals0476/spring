package com.example.upload2;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UploadMapper {

    @Select("SELECT * FROM emp_tb WHERE email = #{email} AND passwd = #{passwd}")
    UploadDto getEmp(UploadDto uploadDto);
}
