package com.engine.shardingMapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.engine.domain.DataSeg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DataSegMapper extends BaseMapper<DataSeg> {
    void createTable(@Param("TableName") String TableName);
    void DropTable(String TableName);
    void insertBatch(@Param("TableName") String TableName, @Param("relations") List<DataSeg> relations);
}
