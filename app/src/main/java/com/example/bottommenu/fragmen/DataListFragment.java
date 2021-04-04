package com.example.bottommenu.fragmen;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.bottommenu.Adapter.DataStaticAdapter;
import com.example.bottommenu.R;
import com.example.bottommenu.interfacePackage.DataStaticHolderApi;
import com.example.bottommenu.model.DataStatic;
import com.example.bottommenu.model.DataStaticItem;
import com.example.bottommenu.model.Page;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataListFragment extends Fragment {

    Integer idSubject;
    String nmSubject;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    RecyclerView recyclerView;
    DataStaticAdapter dataStaticAdapter;
    ProgressBar progressBar;
   List<DataStaticItem> DataStaticItems;
    private List<DataStaticItem> DataStaticItemList;

    String getResult="Nullllllllllllll";

    //variable for pagination
    private boolean isLoading=true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previousTotal=0;
    private int view_threshold=10;
    private int pageNumber=1;


    Button cariButton;
    EditText cariEditText;
    Retrofit retrofit;
    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    protected RecyclerView.LayoutManager mLayoutManager;
    protected DataListFragment.LayoutManagerType mCurrentLayoutManagerType;

    DataStaticHolderApi dataStaticHolderApi;


    public DataListFragment(Integer idSubject, String namaSubjct) {
        this.idSubject=idSubject;
        this.nmSubject=namaSubjct;
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_data_list,container,false);

        TextView tv=view.findViewById(R.id.title_data_list);
        tv.setText(nmSubject.toString());


        //RecycleView List of Data
        recyclerView=(RecyclerView) view.findViewById(R.id.RecycleViewListData);
        progressBar=(ProgressBar)view.findViewById(R.id.progressBarListData);
        cariButton=(Button) view.findViewById(R.id.bttFindDataStatic);
        cariEditText=(EditText) view.findViewById(R.id.editTextFindDataStatic);


        mLayoutManager = new LinearLayoutManager(this.getContext());
        mCurrentLayoutManagerType = DataListFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (DataListFragment.LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);


        //Retrofit-Untuk Ambil Data dari API
        retrofit=new Retrofit.Builder()
                .baseUrl("https://webapi.bps.go.id/v1/api/list/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Landing page publication menu
        getListData();

        //actionListener Search Publication
        cariPublikasi();


        return view;
    }

    public void getListData(){
        //JsonHolderInterfaceClass
        dataStaticHolderApi=retrofit.create(DataStaticHolderApi.class);
        Call<DataStatic> call=dataStaticHolderApi.getList(
                "statictable", "3500", "2ad01e6a21b015ea1ff8805ced02600c", ""+pageNumber,
                "ind", cariEditText.getText().toString(), idSubject
        );

        //progressbar muncul
        progressBar.setVisibility(View.VISIBLE);

        //Callback
        call.enqueue(new Callback<DataStatic>() {
            @RequiresApi(api = Build.VERSION_CODES.N)

            @Override
            public void onResponse(Call<DataStatic> call, Response<DataStatic> response) {
                if(!response.isSuccessful()){
                    getResult="Code : "+response.code();
                    return;
                }
                DataStatic dataStatic=response.body();

                //Cast to Page
                Page page=new Gson().fromJson(dataStatic.getData().get(0).toString(), Page.class);

                //Cast to List of Publikasi Item
                DataStaticItems = stringToArray(new Gson().toJson(dataStatic.getData().get(1)), DataStaticItem[].class);


                dataStaticAdapter=new DataStaticAdapter(getContext(),DataStaticItems);
                recyclerView.setAdapter(dataStaticAdapter);

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DataStatic> call, Throwable t) {
                getResult="Gagal";
                Log.d("I","Message"+t.getMessage());
            }
        });

        recyclerView.setNestedScrollingEnabled(false);
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

                    if(!isLoading&&(totalItemCount-visibleItemCount)<=(pastVisibleItems+view_threshold)){
                        pageNumber++;
                        performPagination();
                        isLoading=true;
                    }
                }
            }
        });
    }

    private void setRecyclerViewLayoutManager(DataListFragment.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), 3);
                mCurrentLayoutManagerType = DataListFragment.LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = DataListFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = DataListFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        Gson gson=new Gson();
        T[] arr = gson.fromJson(s, clazz);
        return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }

    public void performPagination(){
        //Callback
        Call<DataStatic> call=dataStaticHolderApi.getList(
                "pressrelease", "3500", "2ad01e6a21b015ea1ff8805ced02600c", ""+pageNumber,
                "ind", cariEditText.getText().toString(), idSubject
        );

        //Progressbar Visible
        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<DataStatic>() {
            @RequiresApi(api = Build.VERSION_CODES.N)

            @Override
            public void onResponse(Call<DataStatic> call, Response<DataStatic> response) {

                if(!response.isSuccessful()){
                    getResult="Code : "+response.code();
                    return;
                }
                DataStatic dataStatic=response.body();

                //Cast to List of Publikasi Item
                List<DataStaticItem>DataStaticItem2 = stringToArray(new Gson().toJson(dataStatic.getData().get(1)), DataStaticItem[].class);

                //publikasiItems.addAll(publikasiItem2);
                dataStaticAdapter.addDataStaticItemList(DataStaticItem2);

                //Progressbar Gone
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DataStatic> call, Throwable t) {
                getResult="Gagal";
                Log.d("I","Message"+t.getMessage());
            }
        });
    }

    public void cariPublikasi(){
        cariButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNumber=1;
                getListData();
            }
        });

        cariEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction()== KeyEvent.ACTION_DOWN) && (keyCode==KeyEvent.KEYCODE_ENTER)){
                    pageNumber=1;
                    getListData();
                    return true;
                }
                return false;
            }
        });
    }

}