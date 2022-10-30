package com.engine.Controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.engine.Service.DataService;
import com.engine.common.CodeCache;
import com.engine.common.R;
import com.engine.domain.data;
import com.engine.utils.RedisCache;
import com.engine.utils.SocketUtil;
import com.engine.utils.Trie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {

    @Value("${web.upload-path}")
    private String uploadPath;

    @Autowired
    private SocketUtil socketUtil;

    @Autowired
    private DataService dataService;

    @Autowired
    private RedisCache redisCache;

    /*
        下拉框联想词推荐   && 相关搜索
     */
    @GetMapping("/prefix_word")
    public R<List<String>> getPrefixWord(@RequestParam("word") String searchInfo){
        Trie trie = CodeCache.trie;
        List<String> relatedWords = trie.getRelatedWords(searchInfo);
        return R.success(relatedWords);
    }


    @GetMapping("/search")
    /*
        http://localhost:9090/search_use_split?word=%E6%AC%A7%E7%BE%8E&pageNum=1
        word=欧美&pageNum=1
        返回结果
     */
    public R<Page<data>> search_use_split(@RequestParam("word") String searchInfo, int pageNum,int pageSize) {
        Page<data> dataPage = dataService.getRcordUseSplit(searchInfo, pageSize, pageNum);
        return R.success(dataPage);
    }

    /**
     * 以图搜图
     *上传图
     * @return list里面装的全是url
     */
    @PostMapping("/imageUpload")
    @ResponseBody
    public R<List<String>> register(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String saveUri = uploadPath + "/" + fileName;
        File saveFile = new File(saveUri);
        if (!saveFile.exists()) saveFile.mkdirs();
        file.transferTo(saveFile);
        List<String> UrlList = socketUtil.img2Img(saveUri);//调用python
        return R.success(UrlList);
    }
    /**
     * 文本转图片
     * @param searchInfo
     * @return list里面装的全是url
     * @throws Exception
     */
    @GetMapping("/searchImg")
    public R<List<String>> searchImgBySentence(@RequestParam("sentence") String searchInfo) throws IOException{
        List<String> UrlList = socketUtil.sentence2Img(searchInfo);
        return R.success(UrlList);
    }



    @PutMapping("/click/{id}")
    public R<String> click(@PathVariable int id){
        dataService.click(id);
        return R.success("success");
    }

    @GetMapping("/hot/{Pagenum}")
    public R<List<data>> hot(@PathVariable int Pagenum){
        List<data> page = new ArrayList<>(10);
        List<data> hotContent = redisCache.getCacheObject("hot_content");
        for (int i = 10*(Pagenum-1); i < 10*(Pagenum-1)+10; i++) {
            page.add(hotContent.get(i));
        }
        return R.success(page);
    }
}
