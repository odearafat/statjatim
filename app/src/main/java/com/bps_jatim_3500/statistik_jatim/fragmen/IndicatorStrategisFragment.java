package com.bps_jatim_3500.statistik_jatim.fragmen;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bps_jatim_3500.statistik_jatim.Adapter.BrsAdapter;
import com.bps_jatim_3500.statistik_jatim.Adapter.HomeAdapter;
import com.bps_jatim_3500.statistik_jatim.R;
import com.bps_jatim_3500.statistik_jatim.activity.MainActivity;
import com.bps_jatim_3500.statistik_jatim.interfacePackage.BRSHolderApi;
import com.bps_jatim_3500.statistik_jatim.interfacePackage.IndikatorStrategisHolderApi;
import com.bps_jatim_3500.statistik_jatim.model.IndikatorStrategis;
import com.bps_jatim_3500.statistik_jatim.model.IndikatorStrategisItem;
import com.bps_jatim_3500.statistik_jatim.model.Page;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IndicatorStrategisFragment extends Fragment {

    private static final int SPAN_COUNT =2 ;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    protected RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recyclerView;
    BrsAdapter brsAdapter;
    ProgressBar progressBar;
    HomeAdapter homeAdapter;
    ImageButton buttonBackBrs, bttSetBrs;
    MainActivity mainActivity;


    List<IndikatorStrategisItem>indikatorStrategisItems;
    IndikatorStrategisHolderApi indikatorStrategisHolderApi;



    Button cariButton;
    EditText cariEditText;
    Retrofit retrofit;

    //variable for pagination
    private boolean isLoading=true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previousTotal=0;
    int allPageNumber;
    private int view_threshold=10;
    private int pageNumber=1;

    enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    public IndicatorStrategisFragment(MainActivity mainActivity){

        this.mainActivity=mainActivity;

    }

    protected IndicatorStrategisFragment.LayoutManagerType mCurrentLayoutManagerType;
    BRSHolderApi brsHolderApi;
    String getResult="Nullllllllllllll";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainActivity.getInternetConnectionCheck().isConnected();

        isLoading=true;
        pastVisibleItems =0;
        visibleItemCount=0;
        totalItemCount=0;
        previousTotal =0;
        view_threshold=10;
        pageNumber=1;

        //Call FragmentBrs
        View v=inflater.inflate(R.layout.fragment_indicator_strategis,container,false);
        v.setTag("RecyclerViewFragment");

        //RecycleView List of Data
        recyclerView=(RecyclerView) v.findViewById(R.id.RecycleViewListBrs);
        progressBar=(ProgressBar)v.findViewById(R.id.progressBarBrs);
        buttonBackBrs=(ImageButton) v.findViewById(R.id.buttonBackBrs);


        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mCurrentLayoutManagerType = IndicatorStrategisFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (IndicatorStrategisFragment.LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);


        //Retrofit-Untuk Ambil Data dari API
        retrofit=new Retrofit.Builder()
                .baseUrl("https://webapi.bps.go.id/v1/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Landing page publication menu
        getListData();

        //backButtonHandler
        buttonBackBrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        return v;
    }

    public void getListData(){

        //JsonHolderInterfaceClass
        indikatorStrategisHolderApi=retrofit.create(IndikatorStrategisHolderApi.class);
        Call<IndikatorStrategis> call=indikatorStrategisHolderApi.getList(
                "indicators", "3500", "2ad01e6a21b015ea1ff8805ced02600c", ""+pageNumber,
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
                allPageNumber=page.getPages();

                //Cast to List of IndikatorStrategis Item
                indikatorStrategisItems = stringToArray(new Gson().toJson(indikatorStrategis.getData().get(1)), IndikatorStrategisItem[].class);

                homeAdapter=new HomeAdapter(getContext(),indikatorStrategisItems, mainActivity);
                recyclerView.setAdapter(homeAdapter);

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<IndikatorStrategis> call, Throwable t) {
                getResult="Gagal";
                Log.d("I","Message"+t.getMessage());
            }
        });

        //recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount=mLayoutManager.getChildCount();
                totalItemCount=mLayoutManager.getItemCount();
                pastVisibleItems=((LinearLayoutManager)mLayoutManager).findFirstVisibleItemPosition();

                if(dy>0){
                    if(isLoading){
                        if(totalItemCount>previousTotal){
                            isLoading=false;
                            previousTotal=totalItemCount;
                        }
                    }

                    if(!isLoading&&(totalItemCount-visibleItemCount)<=(pastVisibleItems+view_threshold)
                            && pageNumber<allPageNumber){
                        pageNumber++;
                        performPagination();
                        isLoading=true;
                    }
                }
            }
        });
    }

    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        Gson gson=new Gson();
        T[] arr = gson.fromJson(s, clazz);
        return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }

    public void performPagination(){
        //Callback
        Call<IndikatorStrategis> call=indikatorStrategisHolderApi.getList(
                "indicators", "3500", "2ad01e6a21b015ea1ff8805ced02600c", ""+pageNumber,
                "ind"
        );

        //Progressbar Visible
        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<IndikatorStrategis>() {
            @RequiresApi(api = Build.VERSION_CODES.N)

            @Override
            public void onResponse(Call<IndikatorStrategis> call, Response<IndikatorStrategis> response) {

                if(!response.isSuccessful()){
                    getResult="Code : "+response.code();
                    return;
                }
                IndikatorStrategis indikatorStrategis=response.body();

                //Cast to List of Publikasi Item
                List<IndikatorStrategisItem>indikatorStrategisItem2 = stringToArray(new Gson().toJson(indikatorStrategis.getData().get(1)), IndikatorStrategisItem[].class);

                //publikasiItems.addAll(publikasiItem2);
                homeAdapter.addPublikasiItemList(indikatorStrategisItem2);

                //Progressbar Gone
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<IndikatorStrategis> call, Throwable t) {
                getResult="Gagal";
                Log.d("I","Message"+t.getMessage());
            }
        });
    }


    private void setRecyclerViewLayoutManager(IndicatorStrategisFragment.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = IndicatorStrategisFragment.LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = IndicatorStrategisFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = IndicatorStrategisFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

}
