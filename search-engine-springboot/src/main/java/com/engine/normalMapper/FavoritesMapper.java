package com.engine.normalMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FavoritesMapper {
    void createFavoriteTable(Long userid);
    List<String> getAllFavoritesName(String userid);
    List<String> getAllCaptionByFavoritesName(@Param("user_id") String userid,@Param("FavoritesName")String FavoritesName);

    void AddContentsForFavoritesName(@Param("user_id") String userid,@Param("FavoritesName")String FavoritesName, @Param("data_ids") List<Integer> data_ids);
    void DeleteContentsForFavoritesName(@Param("user_id") String userid,@Param("FavoritesName")String FavoritesName, @Param("data_ids") List<Integer> data_ids);


    void DeleteFavorite(@Param("user_id") String userid,@Param("FavoritesName")String FavoritesName);
    void UpdataFavorite(@Param("user_id") String userid,@Param("FavoritesName")String FavoritesName,@Param("newFavoritesName")String newFavoritesName);

    int countFavoriteName(@Param("user_id") String userid,@Param("FavoritesName")String FavoritesName);
    List<Integer> getDataidsByFavoriteName(@Param("user_id") String userid,@Param("FavoritesName")String FavoritesName);

}
