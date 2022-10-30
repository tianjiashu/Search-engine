package com.engine.Service;


import java.util.List;

public interface FavoriteService {

    void newFavorite(String userid,String FavoritesName);

    void deleteFavorite(String userid,String FavoritesName);

    void updateFavorite(String userid,String FavoritesName,String newFavoritesName);

    List<String> getAllFavoritesName(String userid);

    List<String> getAllFavoriteContent(String userid,String FavoritesName);

    void BatchInsertFavoriteContent(String userid,String FavoritesName,List<Integer> data_ids);

    void BatchDeleteFavoriteContent(String userid,String FavoritesName,List<Integer> data_ids);

}
