package com.engine.common;

import lombok.Data;
import java.io.Serializable;


@Data
public class R<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(int code,String msg) {
        R<T> r = new R<>();
        r.msg = msg;
        r.code = code;
        return r;
    }
}
