package com.engine.normalMapper;

import com.engine.domain.data;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface normDataMapper {
    void updateclickNum(int id);
    List<data> top(int num);
}
