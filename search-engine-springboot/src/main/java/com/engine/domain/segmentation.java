package com.engine.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class segmentation implements Serializable {

    private static final long serialVersionUID = -40356785423868323L;

    private Long id;
    private String word;
}
