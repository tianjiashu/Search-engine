package com.engine.init;

import com.engine.domain.DataSeg;
import com.engine.domain.segmentation;
import com.engine.shardingMapper.DataMapper;
import com.engine.shardingMapper.DataSegMapper;
import com.engine.Service.SegmentionService;
import com.engine.domain.data;
import com.engine.utils.jieba.Keyword;
import com.engine.utils.jieba.TFIDFAnalyzer;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

@SpringBootTest
public class init {
/*
    这是初始化数据库的代码，但由于后期配置了ShardingJDBC，因此这里的代码就不能运行了。
    因为ShardingJDBC不支持复杂的SQL和动态建表。
    但是可以直接运行项目中的Sql文件。
 */

    JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();
    TFIDFAnalyzer tfidfAnalyzer=new TFIDFAnalyzer();
    @Autowired
    private DataMapper dataMapper;

    @Autowired
    private SegmentionService segmentionService;

    @Autowired
    private DataSegMapper dataSegMapper;

    private Set<String> stopWordsSet;

    @Test
    /*
        先将data表的数据分词，然后插入到segmention
     */
    public void addSegs(){
        //创建布隆过滤器

        BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.forName("UTF-8")),10000000,0.01);//最多放10000000个数据，可以容忍误判的概率为百分之1
        //加载停词表
        if(stopWordsSet==null){
            stopWordsSet = new HashSet<>();
            loadStopWords(stopWordsSet, this.getClass().getResourceAsStream("/jieba/stop_words.txt"));//读文件
        }
        //10000个10000个的查data
        int count = dataMapper.selectCount(null);//data数据个数
        for (int loop=0;loop<=count/10000;loop++){
            List<segmentation> segs = new ArrayList<>();
            List<data> partData = dataMapper.GetPartData(loop * 10000, loop*10000+10000);
            for (data data : partData) {
                String caption = data.getCaption();
                List<SegToken> segTokens = jiebaSegmenter.process(caption, JiebaSegmenter.SegMode.INDEX);//Index模式，用于对索引文档分词
                List<Keyword> keywords = tfidfAnalyzer.analyze(caption,5);
//                segTokens是分词
                for (SegToken segToken : segTokens) {
                    String word = segToken.word;
                    if(stopWordsSet.contains(word))continue;//如果是停止词就跳过，不加入分词表
                    if(!bloomFilter.mightContain(word)){//布隆过滤器去重
                        bloomFilter.put(word);
                        segs.add(new segmentation(null,word));
                    }
                }
            }
            segmentionService.saveBatch(segs);
        }
    }

    @Test
    /*
        创建数据 和 分词的关系表，倒排索引
     */
    public void addAllSegUseSplit(){
        List<segmentation> segmentations = segmentionService.list();
        Map<String,Long> WordToId = new HashMap<>();
        for (segmentation seg : segmentations) {
            WordToId.put(seg.getWord(),seg.getId());
        }

        Map<Long,List<DataSeg>> map = new HashMap<>(1000000); //用来建表用，键是表号，值是表数据。

        //加载分词表
        if (stopWordsSet == null) {
            stopWordsSet = new HashSet<>();
            loadStopWords(stopWordsSet, this.getClass().getResourceAsStream("/jieba/stop_words.txt"));
        }
        int cnt = 0;

        //分批查数据
        int table_count = dataMapper.selectCount(null);//data数据个数
        System.out.println("data表数据数量: "+table_count);
        for (int loop=0;loop<=table_count/10000;loop++){
            System.out.println("loop: "+loop);
            List<data> partData = dataMapper.GetPartData(loop * 10000, loop * 10000 + 10000);
            for (data data : partData) {
                Map<String, DataSeg> countMap = new HashMap<>();//统计词频用
                //数据Id
                int dataId = data.getId();
                List<Keyword> keywords = tfidfAnalyzer.analyze(data.getCaption(),5);
                List<SegToken> segTokens = jiebaSegmenter.process(data.getCaption(), JiebaSegmenter.SegMode.INDEX);
                for (SegToken segToken : segTokens) {
                    String word = segToken.word;
                    if(word.equals(" "))continue;
                    if(stopWordsSet.contains(word))continue;
                    //获取分词的Id
                    Long wordId = WordToId.get(word);
                    //获取关联度
                    double tfidfvalue = 0;
                    for (Keyword keyword : keywords) {
                        if(word.equals(keyword.getName())){
                            tfidfvalue = keyword.getTfidfvalue();
                            break;
                        }
                    }
                    //统计次数
                    if(!countMap.containsKey(word)){
                        countMap.put(word,new DataSeg(null,dataId,wordId,tfidfvalue,1));
                    }else {
                        DataSeg dataSeg = countMap.get(word);
                        int count = dataSeg.getCount();
                        dataSeg.setCount(++count);
                        countMap.put(word,dataSeg);
                    }
                }
                //数据获取完毕了，该建表了
                for (String key : countMap.keySet()) {
                    DataSeg value = countMap.get(key);
                    long segId = value.getSegId();
                    long ind = segId%100;
                    List<DataSeg> segList = map.getOrDefault(ind, new ArrayList<>(10000));
                    segList.add(value);
                    map.put(ind,segList);
                    cnt++;
                }
                if (cnt > 100000) {  // 之所以这么搞，是因为在最后直接insert的话，会爆堆空间，虽然我已经开了4个G但好像还是不行。
                    cnt = 0;
                    for (Long idx : map.keySet()) {
                        String tableName = "data_seg_relation_" + idx;
                        dataSegMapper.createTable(tableName);
                        dataSegMapper.insertBatch(tableName,map.get(idx));
                    }
                    map = new HashMap<>(100000);
                }
            }
        }
        System.out.println("END");
        if (cnt > 0) {
            for (Long idx : map.keySet()) {
                String tableName = "data_seg_relation_" + idx;
                dataSegMapper.createTable(tableName);
                dataSegMapper.insertBatch(tableName,map.get(idx));
            }
        }
    }


    private void loadStopWords(Set<String> set, InputStream in){
        BufferedReader bufr;
        try
        {
            bufr = new BufferedReader(new InputStreamReader(in));
            String line=null;
            while((line=bufr.readLine())!=null) {
                set.add(line.trim());
            }
            try
            {
                bufr.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
