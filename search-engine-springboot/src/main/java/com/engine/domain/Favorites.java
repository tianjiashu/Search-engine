package com.engine.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorites implements Serializable {

    private static final long serialVersionUID = -203562312346862323L;

    private String favorites_name;
    private List<Integer> data_ids;
}
