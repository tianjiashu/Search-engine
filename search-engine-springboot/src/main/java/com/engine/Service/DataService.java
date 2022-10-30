package com.engine.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.engine.domain.data;

import java.util.List;

public interface DataService {
    Page<data> getRcordUseSplit(String searchInfo, int pageSize, int pageNum);
    void click(int id);
    List<data> top(int num);
}
