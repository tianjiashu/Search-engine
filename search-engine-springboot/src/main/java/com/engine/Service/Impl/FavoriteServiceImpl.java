package com.engine.Service.Impl;

import com.engine.Service.FavoriteService;
import com.engine.common.CustomException;
import com.engine.normalMapper.FavoritesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FavoriteServiceImpl implements FavoriteService{

    @Autowired
    private FavoritesMapper favoritesMapper;

    @Override
    public void newFavorite(String userid, String FavoritesName) {
        int count = favoritesMapper.countFavoriteName(userid, FavoritesName);
        if(count>0)throw new CustomException("收藏夹已存在");
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        favoritesMapper.AddContentsForFavoritesName(userid,FavoritesName,ids);
    }

    @Override
    public void deleteFavorite(String userid, String FavoritesName) {
        favoritesMapper.DeleteFavorite(userid,FavoritesName);
    }

    @Override
    public void updateFavorite(String userid, String FavoritesName, String newFavoritesName) {
        int count = favoritesMapper.countFavoriteName(userid,FavoritesName);
        if(count>0)throw new CustomException("收藏夹已存在");
        favoritesMapper.UpdataFavorite(userid,FavoritesName,newFavoritesName);
    }

    @Override
    public List<String> getAllFavoritesName(String userid) {
        return favoritesMapper.getAllFavoritesName(userid);
    }

    @Override
    public List<String> getAllFavoriteContent(String userid, String FavoritesName) {
        return favoritesMapper.getAllCaptionByFavoritesName(userid,FavoritesName);
    }

    @Override
    public void BatchInsertFavoriteContent(String userid, String FavoritesName, List<Integer> data_ids) {
        int count = favoritesMapper.countFavoriteName(userid,FavoritesName);
        if(count==0)throw new CustomException("收藏夹不存在");
        List<Integer> dataids = favoritesMapper.getDataidsByFavoriteName(userid, FavoritesName);
        Set<Integer> dataidSet = new HashSet<>(dataids);
        for (Integer dataid : dataids) {
            if(dataidSet.contains(dataid)) throw new CustomException("不可以在同一收藏夹添加重复内容");
        }

        favoritesMapper.AddContentsForFavoritesName(userid,FavoritesName,data_ids);
    }

    @Override
    public void BatchDeleteFavoriteContent(String userid, String FavoritesName, List<Integer> data_ids) {
        favoritesMapper.DeleteContentsForFavoritesName(userid,FavoritesName,data_ids);
    }
}
