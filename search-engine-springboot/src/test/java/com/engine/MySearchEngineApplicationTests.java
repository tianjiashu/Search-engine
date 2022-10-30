package com.engine;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.engine.domain.DataSeg;
import com.engine.shardingMapper.DataMapper;
import com.engine.shardingMapper.DataSegMapper;
import com.engine.utils.RedisCache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class MySearchEngineApplicationTests {

    @Value("${web.upload-path}")
    private String uploadPath;

    @Value("${soc_host}")
    private String HOST;
    // 服务端端口
    @Value("${soc_port}")
    private int PORT;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private DataMapper dataMapper;

    @Autowired
    private DataSegMapper dataSegMapper;

    @Test
    void contextLoads() {
        for (int i = 0; i < 100; i++) {
            String tableName = "data_seg_relation_" + i;
            dataSegMapper.DropTable(tableName);
        }
    }

    @Test
    /*
     Cause: javax.xml.bind.JAXBException: Implementation of JAXB-API has not been found on module path or classpath.
     */
    void test_shardingsphere(){
        LambdaQueryWrapper<DataSeg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataSeg::getSegId,1164400);
        List<DataSeg> dataSegs = dataSegMapper.selectList(queryWrapper);
        System.out.println(dataSegs);
    }

    @Test
    void test_dataMapper(){
        Integer count = dataMapper.selectCount(null);
        System.out.println(count);
    }

    @Test
    void test(){
        List<Integer> ids = new ArrayList<>();
        ids.add(1164307);
        ids.add(1163604);
        ids.add(1164723);
        LambdaQueryWrapper<DataSeg> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(DataSeg::getSegId,ids);
        lambdaQueryWrapper.groupBy(DataSeg::getDataId);
        List<DataSeg> dataSegs = dataSegMapper.selectList(lambdaQueryWrapper);
        System.out.println(dataSegs);
        for (DataSeg dataSeg : dataSegs) {
            System.out.println(dataSeg);
            System.out.println(1111);
        }
    }

    @Test
    void test2(){
        List<Long> list = new ArrayList<>();
        list.add(1163710L);
        list.add(1164115L);
        List<Integer> integers = dataMapper.QueryData(list);
        System.out.println(integers);
    }

    @Test
    void testredis(){
        redisCache.setCacheObject("11","asada");
        String cacheObject = redisCache.getCacheObject("11");
        System.out.println(cacheObject);
    }

    @Test
    void testValue(){
        System.out.println(uploadPath);
        System.out.println(HOST);
        System.out.println(PORT);
    }

    @Test
    void testUUID(){
        UUID uuid = UUID.randomUUID();
        System.out.println();
    }

}
