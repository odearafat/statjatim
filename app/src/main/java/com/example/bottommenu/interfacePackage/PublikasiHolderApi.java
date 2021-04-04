package com.example.bottommenu.interfacePackage;

import com.example.bottommenu.model.Berita;
import com.example.bottommenu.model.Publikasi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PublikasiHolderApi {

    @GET("list")
    //model=publication&domain=3500&key=2ad01e6a21b015ea1ff8805ced02600c&page=1&lang=ind")
    Call<Publikasi> getList(
            @Query("model") String model,
            @Query("domain") String domain,
            @Query("key") String key,
            @Query("page") String page,
            @Query("lang") String lang,
            @Query("keyword") String keyword
    ) ;

}
