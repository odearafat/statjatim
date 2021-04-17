package com.example.bottommenu.interfacePackage;

import com.example.bottommenu.model.Berita;
import com.example.bottommenu.model.Brs;
import com.example.bottommenu.model.BrsDetail;
import com.example.bottommenu.model.Publikasi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BRSHolderApi {

    @GET("list")
    //model=publication&domain=3500&key=2ad01e6a21b015ea1ff8805ced02600c&page=1&lang=ind")
    Call<Brs> getList(
            @Query("model") String model,
            @Query("domain") String domain,
            @Query("key") String key,
            @Query("page") String page,
            @Query("lang") String lang,
            @Query("keyword") String keyword
    ) ;

    @GET("view")
    Call<BrsDetail> getView(
            @Query("model") String model,
            @Query("domain") String domain,
            @Query("key") String key,
            @Query("id") Integer page,
            @Query("lang") String lang
    );
}
