package com.example.bottommenu.fragmen;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottommenu.Adapter.BrsAdapter;
import com.example.bottommenu.Adapter.SubjectAdapter;
import com.example.bottommenu.R;
import com.example.bottommenu.interfacePackage.BRSHolderApi;
import com.example.bottommenu.interfacePackage.SubjectHolderApi;
import com.example.bottommenu.model.Brs;
import com.example.bottommenu.model.BrsItem;
import com.example.bottommenu.model.Page;
import com.example.bottommenu.model.Subject;
import com.example.bottommenu.model.SubjectItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataFragment extends Fragment {

    private static final int SPAN_COUNT =2 ;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    protected RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recyclerView;
    SubjectAdapter subjectAdapter;
    //ProgressBar progressBar;
    List<SubjectItem> subjectItems;

    Button cariButton;
    EditText cariEditText;
    Retrofit retrofit;
    Call<Subject> call;


    //variable for pagination
    private boolean isLoading=true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previousTotal=0;
    private int view_threshold=10;
    private int pageNumber=1;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected DataFragment.LayoutManagerType mCurrentLayoutManagerType;
    SubjectHolderApi subjectHolderApi;
    String getResult="Nullllllllllllll";
    int pages = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Call FragmentBrs
        View v=inflater.inflate(R.layout.fragment_data,container,false);
        v.setTag("RecyclerViewFragment");

        //RecycleView List of Data
        recyclerView=(RecyclerView) v.findViewById(R.id.RecycleViewListSosduk);
        //progressBar=(ProgressBar)v.findViewById(R.id.progressBarBrs);
        cariButton=(Button) v.findViewById(R.id.bttFindData);
        cariEditText=(EditText) v.findViewById(R.id.editTextFindData);


        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.

//        mLayoutManager = new GridLayoutManager(this.getContext(),3,GridLayoutManager.HORIZONTAL,true);

//
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;


        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (DataFragment.LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);


        //Retrofit-Untuk Ambil Data dari API
        retrofit=new Retrofit.Builder()
                .baseUrl("https://webapi.bps.go.id/v1/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        subjectHolderApi=retrofit.create(SubjectHolderApi.class);

        //Landing page Data menu
        getListData();


        //actionListener Search Publication
        //cariPublikasi();
        return v;
    }

    public void getListData(){
        subjectItems=new ArrayList<>();
        ArrayList<Object> subjectItemsfinal = new ArrayList<>();
        //JsonHolderInterfaceClass
        call=subjectHolderApi.getList(
                "subject", "3500", "2ad01e6a21b015ea1ff8805ced02600c", ""+pageNumber,
                "ind", cariEditText.getText().toString()
        );
        call.enqueue(new Callback<Subject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<Subject> call, Response<Subject> response) {
                System.out.println("Response");
                if(!response.isSuccessful()){
                    getResult="Code : "+response.code();
                    return;
                }
                Subject subject=response.body();

                //Cast to Page
                Page page=new Gson().fromJson(subject.getData().get(0).toString(), Page.class);

                for (pageNumber=1;pageNumber<=page.getPages(); pageNumber++){
                    if(pageNumber==page.getPages()){
                        call=subjectHolderApi.getList(
                                "subject", "3500", "2ad01e6a21b015ea1ff8805ced02600c", ""+pageNumber,
                                "ind", cariEditText.getText().toString()
                        );
                        call.enqueue(new Callback<Subject>() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onResponse(Call<Subject> call, Response<Subject> response) {
                                System.out.println("Response");
                                if(!response.isSuccessful()){
                                    getResult="Code : "+response.code();
                                    return;
                                }
                                Subject subject=response.body();

                                //subjectItems = stringToArray(new Gson().toJson(subject.getData().get(1)), SubjectItem[].class);
                                subjectItems=new ArrayList<>(subjectItems);
                                subjectItems.addAll(stringToArray(new Gson().toJson(subject.getData().get(1)), SubjectItem[].class));

                                System.out.println("Jumlah :"+subjectItems.size());


                            }


                            @Override
                            public void onFailure(Call<Subject> call, Throwable t) {
                                getResult="Gagal";
                                System.out.println("gagal");
                                Log.d("I","Message"+t.getMessage());
                            }
                        });
                    }else{
                        call=subjectHolderApi.getList(
                                "subject", "3500", "2ad01e6a21b015ea1ff8805ced02600c", ""+pageNumber,
                                "ind", cariEditText.getText().toString()
                        );
                        call.enqueue(new Callback<Subject>() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onResponse(Call<Subject> call, Response<Subject> response) {
                                System.out.println("Response");
                                if(!response.isSuccessful()){
                                    getResult="Code : "+response.code();
                                    return;
                                }
                                Subject subject=response.body();

                                //subjectItems = stringToArray(new Gson().toJson(subject.getData().get(1)), SubjectItem[].class);
                                subjectItems=new ArrayList<>(subjectItems);
                                subjectItems.addAll(stringToArray(new Gson().toJson(subject.getData().get(1)), SubjectItem[].class));

                                System.out.println("Jumlah :"+subjectItems.size());

                                System.out.println("Test :"+subjectItems.size());
                                Collections.sort(subjectItems, new Comparator<SubjectItem>() {
                                    @Override
                                    public int compare(SubjectItem o1, SubjectItem o2) {
                                        return o1.getSubcat().compareTo(o2.getSubcat());
                                        //return o1.getTitle().compareTo(o2.getTitle());
                                    }
                                });
                                subjectAdapter=new SubjectAdapter(getContext(),subjectItems );
                                recyclerView.setAdapter(subjectAdapter);
                            }


                            @Override
                            public void onFailure(Call<Subject> call, Throwable t) {
                                getResult="Gagal";
                                System.out.println("gagal");
                                Log.d("I","Message"+t.getMessage());
                            }
                        });
                    }
                    }
            }

            @Override
            public void onFailure(Call<Subject> call, Throwable t) {
                getResult="Gagal";
                Log.d("I","Message"+t.getMessage());
            }
        });
        System.out.println("Jumlah3 :"+subjectItems.size());
        //return subjectItems;
        //recyclerView.setNestedScrollingEnabled(false);
    }

    public void getSubject(){
        call=subjectHolderApi.getList(
                "subject", "3500", "2ad01e6a21b015ea1ff8805ced02600c", ""+pageNumber,
                "ind", cariEditText.getText().toString()
        );
        call.enqueue(new Callback<Subject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<Subject> call, Response<Subject> response) {
                System.out.println("Response");
                if(!response.isSuccessful()){
                    getResult="Code : "+response.code();
                    return;
                }
                Subject subject=response.body();

                //Cast to Page
                Page page=new Gson().fromJson(subject.getData().get(0).toString(), Page.class);
                //Cast to List of Publikasi Item

            }

            @Override
            public void onFailure(Call<Subject> call, Throwable t) {
                getResult="Gagal";
                System.out.println("gagal");
                Log.d("I","Message"+t.getMessage());
            }
        });
    }

    public void getPageCount(){


    }

    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        Gson gson=new Gson();
        T[] arr = gson.fromJson(s, clazz);
        return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }

    public void performPagination(){
        //Progressbar Visible
        //progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<Subject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)

            @Override
            public void onResponse(Call<Subject> call, Response<Subject> response) {

                if(!response.isSuccessful()){
                    getResult="Code : "+response.code();
                    return;
                }
                Subject subject=response.body();

                //Cast to List of Publikasi Item
                //List<SubjectItem>subjectItems2 = stringToArray(new Gson().toJson(subject.getData().get(1)), BrsItem[].class);

                //publikasiItems.addAll(publikasiItem2);
                //subjectAdapter.addBrsItemList(BrsItem2);

                //Progressbar Gone
                //progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Subject> call, Throwable t) {
                getResult="Gagal";
                Log.d("I","Message"+t.getMessage());
            }
        });
    }


    private void setRecyclerViewLayoutManager(DataFragment.LayoutManagerType layoutManagerType) {
        //int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        //if (recyclerView.getLayoutManager() != null) {
        //    scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
        //            .findFirstCompletelyVisibleItemPosition();
       // }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), 4);
                mCurrentLayoutManagerType = DataFragment.LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = DataFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = DataFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.scrollToPosition(scrollPosition);
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
