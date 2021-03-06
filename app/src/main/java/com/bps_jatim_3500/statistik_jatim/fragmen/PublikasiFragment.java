package com.bps_jatim_3500.statistik_jatim.fragmen;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bps_jatim_3500.statistik_jatim.Adapter.PublikasiAdapter;
import com.bps_jatim_3500.statistik_jatim.HelperClass.SpinnerList;
import com.bps_jatim_3500.statistik_jatim.R;
import com.bps_jatim_3500.statistik_jatim.activity.MainActivity;
import com.bps_jatim_3500.statistik_jatim.interfacePackage.PublikasiHolderApi;
import com.bps_jatim_3500.statistik_jatim.model.Page;
import com.bps_jatim_3500.statistik_jatim.model.Publikasi;
import com.bps_jatim_3500.statistik_jatim.model.PublikasiItem;
import com.google.gson.Gson;


import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PublikasiFragment extends Fragment {
    private static final int SPAN_COUNT =2 ;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    protected RecyclerView.LayoutManager mLayoutManager;
    PublikasiAdapter publikasiAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<PublikasiItem> publikasiItems;
    Button cariButton, bttPilihWilayah;
    EditText cariEditText;
    Retrofit retrofit;
    Dialog dialogDetailPublikasi, dialogSettingWilayah;
    ImageButton buttonBackPublikasi, bttSetPublikasi;
    RelativeLayout containerWilayahPublikasi;
    Spinner spinnerWilayah;
    RadioGroup radioGroupBahasa;
    TextView txt_wilayah_publikasi;
    SpinnerList spinner;


    //variable for pagination
    private boolean isLoading=true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previousTotal=0;
    private int view_threshold=10;
    private int pageNumber=1;
    MainActivity mainActivity;


    public PublikasiFragment(MainActivity mainActivity){
        this.mainActivity=mainActivity;
    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected PublikasiFragment.LayoutManagerType mCurrentLayoutManagerType=PublikasiFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

    //RecycleView List of Data
    PublikasiHolderApi jsonPlaceHolderApi;
    String getResult="Nullllllllllllll";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainActivity.getInternetConnectionCheck().isConnected();

        //Call FragmentPublikasi
        View v=inflater.inflate(R.layout.fragment_publikasi,container,false);
        v.setTag("RecyclerViewFragment");

        recyclerView=(RecyclerView) v.findViewById(R.id.RecycleViewListPublikasi);
        progressBar=(ProgressBar) v.findViewById(R.id.progressBarPub);
        cariButton=(Button) v.findViewById(R.id.bttFindPub);
        cariEditText=(EditText) v.findViewById(R.id.editTextFindPub);
        buttonBackPublikasi= (ImageButton) v.findViewById(R.id.buttonBackPublikasi);
        containerWilayahPublikasi=(RelativeLayout) v.findViewById(R.id.containerWilayahPublikasi);
        bttSetPublikasi=(ImageButton) v.findViewById(R.id.btt_set_publikasi);
        txt_wilayah_publikasi=(TextView)v.findViewById(R.id.txt_wilayah_publikasi);

        dialogDetailPublikasi=new Dialog(this.getContext());
        dialogDetailPublikasi.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogSettingWilayah=mainActivity.getDialogPilihWilayah();

        //method handler Setting Wilayah
        setingWilayah();

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mCurrentLayoutManagerType = PublikasiFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (PublikasiFragment.LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        //Retrofit-Untuk Ambil Data dari API
        retrofit=new Retrofit.Builder().baseUrl("https://webapi.bps.go.id/v1/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        //Landing page publication menu
        getListData();

        //actionListener Search Publication
        cariPublikasi();

        //backButtonHandler
        buttonBackPublikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        //setting Button Publikasi Handler
        bttSetPublikasi.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialogSettingWilayah.show();
            }
        });
        containerWilayahPublikasi.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialogSettingWilayah.show();
            }
        });



        return v;
    }

    public void getListData(){
        //JsonHolderInterfaceClass
        jsonPlaceHolderApi=retrofit.create(PublikasiHolderApi.class);
        Call<Publikasi> call=jsonPlaceHolderApi.getList(
                "publication", mainActivity.getIdWilayah(), "2ad01e6a21b015ea1ff8805ced02600c", ""+pageNumber,
                mainActivity.getBahasa(),cariEditText.getText().toString()
        );

        //progressbar muncul
        progressBar.setVisibility(View.VISIBLE);

        //Callback
        call.enqueue(new Callback<Publikasi>() {
            @RequiresApi(api = Build.VERSION_CODES.N)

            @Override
            public void onResponse(Call<Publikasi> call, Response<Publikasi> response) {

                if(!response.isSuccessful()){
                    getResult="Code : "+response.code();
                    return;
                }
                Publikasi publikasi=response.body();

                //Cast to Page
                Page page=new Gson().fromJson(publikasi.getData().get(0).toString(), Page.class);

                //Cast to List of Publikasi Item
                publikasiItems = stringToArray(new Gson().toJson(publikasi.getData().get(1)), PublikasiItem[].class);

                publikasiAdapter=new PublikasiAdapter(getContext(),publikasiItems, dialogDetailPublikasi, mainActivity);
                recyclerView.setAdapter(publikasiAdapter);

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Publikasi> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
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

    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        Gson gson=new Gson();
        T[] arr = gson.fromJson(s, clazz);
        return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }

    public void performPagination(){
        //Callback
        Call<Publikasi> call=jsonPlaceHolderApi.getList(
                "publication", mainActivity.getIdWilayah(), "2ad01e6a21b015ea1ff8805ced02600c", ""+pageNumber,
                mainActivity.getBahasa(),cariEditText.getText().toString()
        );

        //Progressbar Visible
        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<Publikasi>() {
            @RequiresApi(api = Build.VERSION_CODES.N)

            @Override
            public void onResponse(Call<Publikasi> call, Response<Publikasi> response) {

                if(!response.isSuccessful()){
                    getResult="Code : "+response.code();
                    return;
                }
                Publikasi publikasi=response.body();

               //Cast to List of Publikasi Item
                List<PublikasiItem>publikasiItem2 = stringToArray(new Gson().toJson(publikasi.getData().get(1)), PublikasiItem[].class);

                //publikasiItems.addAll(publikasiItem2);
                publikasiAdapter.addPublikasiItemList(publikasiItem2);

                //Progressbar Gone
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Publikasi> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                getResult="Gagal";
                Log.d("I","Message"+t.getMessage());
            }
        });
    }

    private void setRecyclerViewLayoutManager(PublikasiFragment.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(this.getContext(), SPAN_COUNT);
                mCurrentLayoutManagerType = PublikasiFragment.LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(this.getContext());
                mCurrentLayoutManagerType = PublikasiFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(this.getContext());
                mCurrentLayoutManagerType = PublikasiFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.scrollToPosition(scrollPosition);
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

    public void setingWilayah(){
        spinnerWilayah=(Spinner)dialogSettingWilayah.findViewById(R.id.spinner_lokasi);
        radioGroupBahasa=(RadioGroup)dialogSettingWilayah.findViewById(R.id.radio_gruop_bahasa);
        bttPilihWilayah=(Button)dialogSettingWilayah.findViewById(R.id.bttPilihWilayah);

        // you need to have a list of data that you want the spinner to display
        spinner=new SpinnerList();
        List<String> spinnerArray=spinner.spinnerArray();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWilayah.setAdapter(adapter);

        //Set Selection Spinner
        spinnerWilayah.setSelection(spinner.getIdPosition(mainActivity.getWilayah()));
        txt_wilayah_publikasi.setText(mainActivity.getWilayah());

        //Set Selection RadioGroup
        if(mainActivity.getBahasa().equals("ind")){
            ((RadioButton) dialogSettingWilayah.findViewById(R.id.indonesia)).setChecked(true);
        }else{
            ((RadioButton) dialogSettingWilayah.findViewById(R.id.inggris)).setChecked(true);
        }

        //Pilih Button Handler
        bttPilihWilayah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            txt_wilayah_publikasi.setText(spinnerWilayah.getSelectedItem().toString());
            dialogSettingWilayah.dismiss();

            //set id wilayah dan Wilayah
            mainActivity.setWilayah(spinnerWilayah.getSelectedItem().toString());
            mainActivity.setIdWilayah(spinner.getkdKab(spinnerWilayah.getSelectedItem().toString()));

            if(((RadioButton) dialogSettingWilayah.findViewById(R.id.indonesia)).isChecked()){
                System.out.println("masukk ");
                mainActivity.setBahasa("ind");
            }else{
                mainActivity.setBahasa("eng");
            }

                System.out.println("Bahasa :"+mainActivity.getBahasa());
            //load Data
            pageNumber=1;
            getListData();
            }
        });
    }
}


