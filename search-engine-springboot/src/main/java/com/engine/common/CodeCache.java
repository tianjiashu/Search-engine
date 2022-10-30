package com.engine.common;



import com.engine.Service.DataService;
import com.engine.Service.SegmentionService;
import com.engine.domain.data;
import com.engine.domain.segmentation;
import com.engine.utils.RedisCache;
import com.engine.utils.Trie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
/**
 * 将某些数据缓存到全局变量中
 */
public class CodeCache {

    public static Trie trie = new Trie();

    @Autowired
    private SegmentionService segmentionService;

    @Autowired
    private DataService dataService;

    @Autowired
    private RedisCache redisCache;

    @PostConstruct
    public void init() {
        List<segmentation> segmentations = segmentionService.list();
        for (segmentation seg : segmentations) {
            String word = seg.getWord();
            trie.add(word);
        }

        List<data> dataList = dataService.top(50);
        redisCache.setCacheObject("hot_content",dataList);
    }
}
