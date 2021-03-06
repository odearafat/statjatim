package com.bps_jatim_3500.statistik_jatim.interfacePackage;

import com.bps_jatim_3500.statistik_jatim.model.Berita;
import com.bps_jatim_3500.statistik_jatim.model.BeritaDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BeritaHolderApi {

    @GET("list")
    //model=news&domain=3500&key=2ad01e6a21b015ea1ff8805ced02600c&page=1&lang=ind")
    Call<Berita> getList(
            @Query("model") String model,
            @Query("domain") String domain,
            @Query("key") String key,
            @Query("page") String page,
            @Query("lang") String lang,
            @Query("keyword") String keyword
    ) ;

    @GET("view")
    Call<BeritaDetail> getView(
            @Query("model") String model,
            @Query("domain") String domain,
            @Query("key") String key,
            @Query("id") Integer page,
            @Query("lang") String lang
    ) ;

}
