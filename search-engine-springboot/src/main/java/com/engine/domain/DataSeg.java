package com.engine.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("data_seg_relation")
public class DataSeg implements Serializable {

    private static final long serialVersionUID = -21312367868542323L;

    private Integer id;
    private Integer dataId;
    private Long segId;
    private Double tidifValue;
    private Integer count;
}

