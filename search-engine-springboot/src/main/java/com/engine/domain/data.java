package com.engine.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class data implements Serializable {

    private static final long serialVersionUID = -98567854822217893L;

    private int id;
    private String url;
    private String caption;
}
