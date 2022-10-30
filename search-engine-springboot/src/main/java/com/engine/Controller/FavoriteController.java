package com.engine.Controller;

import com.engine.Service.FavoriteService;
import com.engine.common.ParseToken;
import com.engine.common.R;
import com.engine.domain.Favorites;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {


    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/newFavorite/{Favoritename}")
    /*
        新建收藏夹，已存在的就不能新建了
     */
    @ParseToken
    public R<String> newFavorite(@PathVariable("Favoritename") String Favoritename, String userId){
        favoriteService.newFavorite(userId,Favoritename);
        return R.success("success!");
    }

    @PutMapping("/updateFavorite/{Favoritename}/{newFavoritename}")
    /*
        修改收藏夹,不能修改默认收藏夹
     */
    @ParseToken
    public R<String> updateFavorite(@PathVariable("Favoritename") String Favoritename,@PathVariable("newFavoritename") String newFavoritename,String userId){
        if(Favoritename.equals("fault"))return R.error(500,"不能修改默认收藏夹");
        favoriteService.updateFavorite(userId,Favoritename,newFavoritename);
        return R.success("success!");
    }

    @DeleteMapping("/deleteFavorite/{Favoritename}")
    //删除收藏夹,不能删除默认收藏夹
    @ParseToken
    public R<String> deleteFavorite(@PathVariable("Favoritename") String Favoritename, String userId){
        if(Favoritename.equals("fault"))return R.error(500,"不能修改默认收藏夹");
        favoriteService.deleteFavorite(userId,Favoritename);
        return R.success("success!");
    }


    @GetMapping("/Favoritenames")
    /*
        获取该用户所有的收藏夹的名字
     */
    @ParseToken
    public R<List<String>> getAllFavoritenames(String userId){
        List<String> favoritesName = favoriteService.getAllFavoritesName(userId);
        return R.success(favoritesName);
    }

    @GetMapping("/getcontent/{Favoritename}")
    /*
        获取指定收藏夹的所有内容
     */
    @ParseToken
    public R<List<String>> getcontent(@PathVariable("Favoritename") String Favoritename, String userId){
        List<String> allFavoriteContent = favoriteService.getAllFavoriteContent(userId, Favoritename);
        return R.success(allFavoriteContent);
    }

    @PostMapping("/addcontent")
    /*
        批量/单独  添加收藏内容到指定收藏夹 不可以在同一文件夹添加重复内容
     */
    @ParseToken
    public R<String> addcontent(@RequestBody Favorites favorites,String userId){
        favoriteService.BatchInsertFavoriteContent(userId,favorites.getFavorites_name(),favorites.getData_ids());
        return R.success("success!");
    }


    @DeleteMapping("/deletecontent")
    /*
        批量/单独  删除指定收藏夹的收藏内容
    */
    @ParseToken
    public R<String> deletecontent(@RequestBody Favorites favorites,String userId){
        favoriteService.BatchDeleteFavoriteContent(userId,favorites.getFavorites_name(),favorites.getData_ids());
        return R.success("success!");
    }
}
