package com.example.bottommenu.fragmen;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.bottommenu.Adapter.HomeAdapter;
import com.example.bottommenu.Adapter.PublikasiAdapter;
import com.example.bottommenu.R;
import com.example.bottommenu.activity.MainActivity;
import com.example.bottommenu.interfacePackage.IndikatorStrategisHolderApi;
import com.example.bottommenu.interfacePackage.PublikasiHolderApi;
import com.example.bottommenu.model.IndikatorStrategis;
import com.example.bottommenu.model.IndikatorStrategisItem;
import com.example.bottommenu.model.Page;
import com.example.bottommenu.model.Publikasi;
import com.example.bottommenu.model.PublikasiItem;
import com.google.gson.Gson;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private static final int SPAN_COUNT =2 ;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    protected RecyclerView.LayoutManager mLayoutManager;
    HomeAdapter homeAdapter;

    enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    //RecycleView List of Data
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<IndikatorStrategisItem>indikatorStrategisItems;
    IndikatorStrategisHolderApi indikatorStrategisHolderApi;
    String getResult="Null";
    ImageButton imgBttDavita;
    ImageButton imgBttChatUs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Call FragmentHome
        View v=inflater.inflate(R.layout.fragment_home,container,false);
        v.setTag("RecyclerViewFragment");

        //RecycleView List of Data
        recyclerView=(RecyclerView) v.findViewById(R.id.RecycleViewListDataHome);
        progressBar=(ProgressBar) v.findViewById(R.id.progressBarHome);


        //Button untuk AppBPS
        imgBttDavita=(ImageButton) v.findViewById(R.id.bttDavita);
        imgBttChatUs=(ImageButton) v.findViewById(R.id.bttChatUs);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);


        //Retrofit-Untuk Ambil Data dari API
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://webapi.bps.go.id/v1/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //JsonHolderInterfaceClass
        indikatorStrategisHolderApi=retrofit.create(IndikatorStrategisHolderApi.class);
        Call<IndikatorStrategis> call=indikatorStrategisHolderApi.getList(
                "indicators", "3500", "2ad01e6a21b015ea1ff8805ced02600c", "1",
                "ind"
        );

        //progressbar muncul
        progressBar.setVisibility(View.VISIBLE);

        //Callback
        call.enqueue(new Callback<IndikatorStrategis>() {
            @RequiresApi(api = Build.VERSION_CODES.N)

            @Override
            public void onResponse(Call<IndikatorStrategis> call, Response<IndikatorStrategis> response) {

                if(!response.isSuccessful()){
                    getResult="Code : "+response.code();
                    return;
                }
                IndikatorStrategis indikatorStrategis=response.body();

                //Cast to Page
                Page page=new Gson().fromJson(indikatorStrategis.getData().get(0).toString(), Page.class);

                //Cast to List of IndikatorStrategis Item
                indikatorStrategisItems = stringToArray(new Gson().toJson(indikatorStrategis.getData().get(1)), IndikatorStrategisItem[].class);

                homeAdapter=new HomeAdapter(getContext(),indikatorStrategisItems);
                recyclerView.setAdapter(homeAdapter);

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<IndikatorStrategis> call, Throwable t) {
                getResult="Gagal";
                Log.d("I","Message"+t.getMessage());
            }
        });

        //Image Slider
        ImageSlider imageSlider= v.findViewById(R.id.slider);
        List<SlideModel> slideModels= new ArrayList<>();

        slideModels.add(new SlideModel("https://s.bps.go.id/screen-1"));
        slideModels.add(new SlideModel("https://s.bps.go.id/screen-2"));
        slideModels.add(new SlideModel("https://s.bps.go.id/screen-3"));
        slideModels.add(new SlideModel("https://s.bps.go.id/screen-4"));
        imageSlider.setImageList(slideModels,true);

        imgBttDavita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //So you can get the edittext value
                String mobileNumber = "82162900900";
                String message = "halo";
                boolean installed = appInstalledOrNot("com.whatsapp", v);
                if (installed){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"+62"+mobileNumber + "&text="+message));
                    startActivity(intent);
                }else {
                    Toast.makeText(v.getContext(), "Whats app not installed on your device", Toast.LENGTH_SHORT).show();
                }

            }

//                AppCompatActivity activity=(AppCompatActivity) v.getContext();
//                activity.getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.fragmen_container,
//                                new DavitaFragment()).commit();

 //           }
        });



        imgBttChatUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity=(AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmen_container,
                                new ChatUsFragment()).commit();

            }
        });


        return v;
    }

    //Create method appInstalledOrNot
    private boolean appInstalledOrNot(String url, View v){
        PackageManager packageManager =v.getContext().getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo(url,PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }catch (PackageManager.NameNotFoundException e){
            app_installed = false;
        }
        return app_installed;
    }

    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        Gson gson=new Gson();
        T[] arr = gson.fromJson(s, clazz);
        return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }

    private void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        //int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        //if (recyclerView.getLayoutManager() != null) {
        //    scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
       //             .findFirstCompletelyVisibleItemPosition();
        //}

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }
        recyclerView.stopScroll();
        recyclerView.setLayoutManager(mLayoutManager);
       // recyclerView.scrollToPosition(scrollPosition);
    }

}
