package com.engine.utils;

import com.alibaba.fastjson.JSON;
import com.engine.common.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Java socket客户端
 * 与python同行调用深度学习模型
 *
 *
 */
@Component
@Slf4j
public class SocketUtil {
    // 服务端IP地址
    @Value("${soc_host}")
    private String HOST;
    // 服务端端口
    @Value("${soc_port}")
    private int PORT;

    /**
     * 以文本搜图
     * @return
     */
    public List<String> sentence2Img(String sentence) throws IOException{
        List<String> urlList = new ArrayList<>();

        // 发送给服务端的参数 <状态码，数据>
        Map<String, String> paramMap = new HashMap<>();
        //图片转换服务
        paramMap.put("code", "100");
        paramMap.put("sentence", sentence);
        // map转成json字符串，需要引入fastjson依赖
        String param = JSON.toJSONString(paramMap);

        // 接收服务端返回的信息
        String socketServerMsg = getSocketServerMsg(HOST, PORT, param);
        log.info("服务端信息：" + socketServerMsg);
        /*
        {
            "code": "200",
            "status": "10",
            "url0": "https://pic.rmb.bdstatic.com/19539b3b1a7e1daee93b0f3d99b8e795.png",
            "url1": "https://pic.rmb.bdstatic.com/19539b3b1a7e1daee93b0f3d99b8e795.png",
            "url2": "https://pic.rmb.bdstatic.com/19539b3b1a7e1daee93b0f3d99b8e795.png"
          }
          处理服务端信息
         */
        // json字符串转成map，需要引入fastjson依赖
        Map<String,String> resultMap = (Map<String,String>) JSON.parse(socketServerMsg);

        String code=String.valueOf(resultMap.get("code"));
        System.out.println("服务码  : "+code);
        String status=resultMap.get("status");
        System.out.println("状态码  : "+status);

        //文字搜图服务
        if(!(code.equals("100")&&status.equals("10")))throw new CustomException("文字搜图服务失败！");//检查状态码和服务码

        for (String url : resultMap.values()) {
            if (!(url.equals("10") || url.equals("100")))urlList.add(url);
        }

        return urlList;
    }

    /**
     * 以图搜图
     * @return
     */
    public List<String> img2Img(String filepath) throws IOException {
        List<String> urlList = new ArrayList<>();

        // 发送给服务端的参数 <状态码，数据>
        Map<String, String> paramMap = new HashMap<>();
        //图片转换服务
        paramMap.put("code", "200");
        paramMap.put("filepath", filepath);
        // map转成json字符串，需要引入fastjson依赖
        String param = JSON.toJSONString(paramMap);

        // 接收服务端返回的信息
        String socketServerMsg = getSocketServerMsg(HOST, PORT, param);
        log.info("服务端信息：" + socketServerMsg);
        // json字符串转成map，需要引入fastjson依赖
        Map<String,String> resultMap = (Map) JSON.parse(socketServerMsg);

        String code=resultMap.get("code");
        String status=resultMap.get("status");

        //以图搜图服务
        if(!("200".equals(code)&&status.equals("10")))throw new CustomException("以图搜图服务失败");

        for (String url : resultMap.values()) {
            if (!(url.equals("10") || url.equals("200")))urlList.add(url);
        }
        return urlList;
    }


    public static String getSocketServerMsg(String host, int port, String message) throws IOException {
        // 与服务端建立连接
        Socket socket = new Socket(host,port);
        // 获得输出流
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter pWriter = new PrintWriter(outputStream);
        pWriter.write(message);
        pWriter.flush();
        // 通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
        socket.shutdownOutput();
        // 获得输入流
        InputStream inputStream = socket.getInputStream();

        byte[] bytes = new byte[1024];
        int len;
        StringBuilder sb = new StringBuilder();
        while ((len = inputStream.read(bytes)) != -1) {
            // 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
            sb.append(new String(bytes, 0, len, "UTF-8"));
        }

        inputStream.close();
        outputStream.close();
        socket.close();
        return sb.toString();
    }

}
