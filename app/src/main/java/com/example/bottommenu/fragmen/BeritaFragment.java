package com.example.bottommenu.fragmen;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottommenu.Adapter.BeritaAdapter;
import com.example.bottommenu.R;
import com.example.bottommenu.interfacePackage.BeritaHolderApi;
import com.example.bottommenu.model.Berita;
import com.example.bottommenu.model.BeritaItem;
import com.example.bottommenu.model.Page;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BeritaFragment extends Fragment {

    private static final int SPAN_COUNT =2 ;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    protected RecyclerView.LayoutManager mLayoutManager;
    BeritaAdapter beritaAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<BeritaItem> beritaItems;
    Retrofit retrofit;
    Button cariButton;
    EditText cariEditText;
    Dialog dialogDetailBerita;

    //variable for pagination
    private boolean isLoading=true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previousTotal=0;
    private int view_threshold=10;
    private int pageNumber=1;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected BeritaFragment.LayoutManagerType mCurrentLayoutManagerType;

    //RecycleView List of Data
    BeritaHolderApi beritaHolderApi;
    String getResult="Nullllllllllllll";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Call FragmentBerita
        View v=inflater.inflate(R.layout.fragment_berita,container,false);
        v.setTag("RecyclerViewFragment");


        recyclerView=(RecyclerView) v.findViewById(R.id.RecycleViewListDataBerita);
        progressBar=(ProgressBar) v.findViewById(R.id.progressBarBerita);
        cariButton=(Button) v.findViewById(R.id.bttFindBerita);
        cariEditText=(EditText) v.findViewById(R.id.editTextFindBerita);

        dialogDetailBerita=new Dialog(this.getContext());
        dialogDetailBerita.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mCurrentLayoutManagerType = BeritaFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (BeritaFragment.LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);


        //Retrofit-Untuk Ambil Data dari API
        retrofit=new Retrofit.Builder()
                .baseUrl("https://webapi.bps.go.id/v1/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Landing Page Berita Menu
        getListData();

        //actionListener Search Berita
        cariBerita();

        return v;
    }

    public void getListData(){
        //JsonHolderInterfaceClass
        beritaHolderApi=retrofit.create(BeritaHolderApi.class);
        Call<Berita> call=beritaHolderApi.getList(
                "news", "3500", "2ad01e6a21b015ea1ff8805ced02600c", ""+pageNumber,
                "ind",cariEditText.getText().toString()
        );

        //progressbar muncul
        progressBar.setVisibility(View.VISIBLE);

        //Callback
        call.enqueue(new Callback<Berita>() {
            @RequiresApi(api = Build.VERSION_CODES.N)

            @Override
            public void onResponse(Call<Berita> call, Response<Berita> response) {

                if(!response.isSuccessful()){
                    getResult="Code : "+response.code();
                    return;
                }
                Berita berita=response.body();

                //Cast to Page
                Page page=new Gson().fromJson(berita.getData().get(0).toString(), Page.class);

                //Cast to List of Berita Item
                beritaItems = stringToArray(new Gson().toJson(berita.getData().get(1)), BeritaItem[].class);

                beritaAdapter=new BeritaAdapter(getContext(),beritaItems, beritaHolderApi, dialogDetailBerita);
                recyclerView.setAdapter(beritaAdapter);

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Berita> call, Throwable t) {
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

                    if(!isLoading&&(totalItemCount-visibleItemCount)<=(pastVisibleItems+view_threshold)){
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
        Call<Berita> call=beritaHolderApi.getList(
                "news", "3500", "2ad01e6a21b015ea1ff8805ced02600c", ""+pageNumber,
                "ind", cariEditText.getText().toString()
        );

        //Progressbar Visible
        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<Berita>() {
            @RequiresApi(api = Build.VERSION_CODES.N)

            @Override
            public void onResponse(Call<Berita> call, Response<Berita> response) {

                if(!response.isSuccessful()){
                    getResult="Code : "+response.code();
                    return;
                }
                Berita berita=response.body();

                //Cast to List of Berita Item
                List<BeritaItem>beritaItem2 = stringToArray(new Gson().toJson(berita.getData().get(1)), BeritaItem[].class);

                beritaAdapter.addBeritaItemList(beritaItem2);

                //Progressbar Gone
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Berita> call, Throwable t) {
                getResult="Gagal";
                Log.d("I","Message"+t.getMessage());
            }
        });
    }


    private void setRecyclerViewLayoutManager(BeritaFragment.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = BeritaFragment.LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = BeritaFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = BeritaFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    public void cariBerita(){
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
