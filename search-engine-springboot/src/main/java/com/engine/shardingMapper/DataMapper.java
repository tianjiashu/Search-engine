package com.engine.shardingMapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.engine.domain.data;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DataMapper extends BaseMapper<data> {
    List<data> GetPartData(@Param("start") int start, @Param("length") int length);
    List<Integer> QueryData(@Param("ids") List<Long> ids);
}
