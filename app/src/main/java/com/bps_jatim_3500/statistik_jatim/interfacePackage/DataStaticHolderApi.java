package com.bps_jatim_3500.statistik_jatim.interfacePackage;

import com.bps_jatim_3500.statistik_jatim.model.DataStatic;
import com.bps_jatim_3500.statistik_jatim.model.DataStaticDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DataStaticHolderApi {

    @GET("list")
    //model=publication&domain=3500&key=2ad01e6a21b015ea1ff8805ced02600c&page=1&lang=ind")
    Call<DataStatic> getList(
            @Query("model") String model,
            @Query("domain") String domain,
            @Query("key") String key,
            @Query("page") String page,
            @Query("lang") String lang,
            @Query("keyword") String keyword,
            @Query("subject") Integer idsubject
    ) ;

    @GET("view")
    Call<DataStaticDetail> getView(
            @Query("model") String model,
            @Query("domain") String domain,
            @Query("key") String key,
            @Query("id") Integer page,
            @Query("lang") String lang
    ) ;
}
