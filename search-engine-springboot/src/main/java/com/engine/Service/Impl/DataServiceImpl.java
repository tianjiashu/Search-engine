package com.engine.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.engine.Service.DataService;
import com.engine.domain.segmentation;
import com.engine.normalMapper.normDataMapper;
import com.engine.shardingMapper.DataMapper;
import com.engine.shardingMapper.SegmentionMapper;
import com.engine.common.CodeCache;
import com.engine.domain.data;
import com.engine.utils.Trie;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class DataServiceImpl extends ServiceImpl<DataMapper,data> implements DataService {


    @Autowired
    private SegmentionMapper segmentionMapper;

    @Autowired
    private DataMapper dataMapper;

    @Autowired
    private normDataMapper normDateMapper;

    @Override
    public Page<data> getRcordUseSplit(String searchInfo, int pageSize, int pageNum) {

        Trie trie = CodeCache.trie;
        JiebaSegmenter segmenter = new JiebaSegmenter();//准备分词

        // -----处理过滤词-----start
        String[] words = searchInfo.split("\\s+");//words:{"王者荣耀","-张良"}
        List<String> filterWords = new ArrayList<>();
        boolean find = false;
        int filterWordIndex = -1;
        for (int i = 0; i < words.length; i++) {
            String str = words[i];
            if (Pattern.matches("^-.*?$", str)) {// 匹配  -***
                if (!find) {
                    filterWordIndex = searchInfo.indexOf(str);
                    find = true;
                }
                filterWords.add(str.substring(1));
            }
        }
        if (filterWordIndex != -1) {//去掉 searchInf的过滤词
            searchInfo = searchInfo.substring(0, filterWordIndex);
        }
        // -----处理过滤词-----end

        /*
        处理搜索词主体
        将搜索词主体分词，然后对每个分词查询segid
         */
        List<Long> SegIds = new ArrayList<>();
        List<SegToken> segTokens = segmenter.process(searchInfo, JiebaSegmenter.SegMode.SEARCH);//查询主体进行分词
        for (SegToken segToken : segTokens) {
            if(!trie.search(segToken.word))continue;
            if(segToken.word.trim().equals(""))continue;

            LambdaQueryWrapper<segmentation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(segmentation::getWord,segToken.word);
            segmentation seg = segmentionMapper.selectOne(queryWrapper);
            Long segId = seg.getId();

            SegIds.add(segId);
        }
        /*
            不用对过滤词分词，去分词表查过滤词的segid
         */
        List<Long> filterWordSegIds = new ArrayList<>();
        boolean filterWordInSegmentation = false;
        if (filterWords.size() > 0) {
            for (String filterWord : filterWords) {
                if(!trie.search(filterWord))continue;//看看分词库里是否有
                filterWordInSegmentation = true;
                LambdaQueryWrapper<segmentation> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(segmentation::getWord,filterWord);
                segmentation seg = segmentionMapper.selectOne(queryWrapper);
                Long segId = seg.getId();
                filterWordSegIds.add(segId);
            }
        }
        Page<data> dataPage = new Page<>(pageNum,pageSize);
        List<Integer> DataIds = dataMapper.QueryData(SegIds);
        if (filterWords.size() > 0 && filterWordInSegmentation) {
            Set<Integer> exculde_dataid = new HashSet<>(dataMapper.QueryData(filterWordSegIds));
            DataIds.removeAll(exculde_dataid);
        }
        LambdaQueryWrapper<data> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(data::getId,DataIds);
        this.page(dataPage,wrapper);
            /*
            select * FROM `data` where id in (
		            select data_id from data_seg_relation_23 where seg_id in (1164323) GROUP BY data_id order by sum(tidif_value) DESC
              );
             */

        return dataPage;
    }

    @Override
    public void click(int id) {
        normDateMapper.updateclickNum(id);
    }

    @Override
    public List<data> top(int num) {
        return normDateMapper.top(num);
    }
}
