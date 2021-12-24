package com.bps_jatim_3500.statistik_jatim.interfacePackage;

import com.bps_jatim_3500.statistik_jatim.model.Infografis;
import com.bps_jatim_3500.statistik_jatim.model.PublikasiView;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InfografisHolderApi {

    @GET("list")
    //model=publication&domain=3500&key=2ad01e6a21b015ea1ff8805ced02600c&page=1&lang=ind")
    Call<Infografis> getList(
            @Query("model") String model,
            @Query("domain") String domain,
            @Query("key") String key,
            @Query("page") String page,
            @Query("lang") String lang,
            @Query("keyword") String keyword
    ) ;

//    @GET("view")
//        //model=publication&domain=3500&key=2ad01e6a21b015ea1ff8805ced02600c&page=1&lang=ind")
//    Call<PublikasiView> getView(
//            @Query("model") String model,
//            @Query("domain") String domain,
//            @Query("key") String key,
//            @Query("lang") String lang,
//            @Query("id") String id
//    ) ;

}
