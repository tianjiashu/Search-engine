package com.engine.common;

import com.engine.Service.DataService;
import com.engine.domain.data;
import com.engine.utils.RedisCache;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ScheduledTasks {

    @Autowired
    private DataService dataService;

    @Autowired
    private RedisCache redisCache;

    @Scheduled(fixedRate = 1000*60*5)
    public void reportCurrentTime() {
        redisCache.deleteObject("hot_content");
        List<data> dataList = dataService.top(50);
        redisCache.setCacheObject("hot_content",dataList);
    }

}
