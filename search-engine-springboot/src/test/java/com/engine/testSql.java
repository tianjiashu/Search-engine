package com.engine;

import com.engine.domain.data;
import com.engine.normalMapper.normDataMapper;
import com.engine.normalMapper.FavoritesMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class testSql {

    @Autowired
    private FavoritesMapper favoritesMapper;

    @Autowired
    private normDataMapper dataMapper;

    @Test
    public void test1(){
        //测试建表
        favoritesMapper.createFavoriteTable(213123L);
        List<Integer> list = new ArrayList<>();
        list.add(1);
        favoritesMapper.AddContentsForFavoritesName("213123","default",list);
    }

    @Test
    public void test2(){
        //测试添加收藏夹内容
        List<Integer> list = new ArrayList<>();
        list.add(11);list.add(23);list.add(24);
        favoritesMapper.AddContentsForFavoritesName("213123","default",list);
    }

    @Test
    public void test3(){
        //测试删除收藏夹内容
        List<Integer> list = new ArrayList<>();
        list.add(11);list.add(23);
        favoritesMapper.DeleteContentsForFavoritesName("213123","default",list);
    }

    @Test
    public void test4(){
        //获取所有文件夹名称
        List<String> allFavoritesName = favoritesMapper.getAllFavoritesName("213123");
        System.out.println(allFavoritesName);
    }

    @Test
    public void test5(){
        //获取指定收藏夹内容
        List<String> Captions = favoritesMapper.getAllCaptionByFavoritesName("213123", "default");
        System.out.println(Captions);
    }

    @Test
    public void test6(){
        //修改收藏夹名称
        favoritesMapper.UpdataFavorite("213123","default","I like");
    }

    @Test
    public void test7(){
        //删除收藏夹
        favoritesMapper.DeleteFavorite("213123","I like");
    }

    @Test
    public void test8(){
        List<data> top = dataMapper.top(10);
        System.out.println(top);
    }
}
