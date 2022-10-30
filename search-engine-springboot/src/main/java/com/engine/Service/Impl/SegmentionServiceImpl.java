package com.engine.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.engine.domain.segmentation;
import com.engine.shardingMapper.SegmentionMapper;
import com.engine.Service.SegmentionService;
import org.springframework.stereotype.Service;

@Service
public class SegmentionServiceImpl extends ServiceImpl<SegmentionMapper, segmentation> implements SegmentionService {
}
