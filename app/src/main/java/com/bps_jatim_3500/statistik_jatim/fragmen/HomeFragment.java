package com.bps_jatim_3500.statistik_jatim.fragmen;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.bps_jatim_3500.statistik_jatim.Adapter.LoadImage;
import com.bps_jatim_3500.statistik_jatim.HelperClass.LoginAndAuthHandler;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.bps_jatim_3500.statistik_jatim.Adapter.HomeAdapter;
import com.bps_jatim_3500.statistik_jatim.HelperClass.HelpSliderAdapter;
import com.bps_jatim_3500.statistik_jatim.R;
import com.bps_jatim_3500.statistik_jatim.activity.MainActivity;
import com.bps_jatim_3500.statistik_jatim.interfacePackage.IndikatorStrategisHolderApi;
import com.bps_jatim_3500.statistik_jatim.model.IndikatorStrategis;
import com.bps_jatim_3500.statistik_jatim.model.IndikatorStrategisItem;
import com.bps_jatim_3500.statistik_jatim.model.Page;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {

    private static final int SPAN_COUNT =2 ;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    protected RecyclerView.LayoutManager mLayoutManager;
    HomeAdapter homeAdapter;
    FragmentManager fm;
    MainActivity mainActivity;

    public HomeFragment( MainActivity mainActivity) {
        this.mainActivity=mainActivity;
        this.fm=mainActivity.getFm();
    }

    enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    //RecycleView List of Data
    RecyclerView recyclerView;
    ProgressBar progressBar;
    Retrofit retrofit;
    List<IndikatorStrategisItem>indikatorStrategisItems;
    IndikatorStrategisHolderApi indikatorStrategisHolderApi;
    String getResult="Null";
    ImageButton  helpButton;
    RelativeLayout settingButton;
    ImageButton imgBttDavita;
    ImageButton imgBttChatUs;
    ImageButton imgBttDataCorner;
    RelativeLayout allIndicatorStrat;
    TextView nmPengguna, nmSelamatDatang;
    LoginAndAuthHandler loginAndAuthHandler;

    //help Asstes
    Dialog dialogHelpSlider, dialogLogin;
    ViewPager viewPager;
    LinearLayout dotsLayout;
    TextView [] dots;


    //variable for pagination
    private boolean isLoading;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previousTotal;
    private int view_threshold;
    private int pageNumber;

    int allPageNumber;

    //Google SignIn
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN=0;
    Button logoutButton;
    ImageView no_profile_pict,with_profile_pict_iv, imgFotoProfile;
    TextView with_profile_pict_tv, loginHeadingTv, emailLoginTv;

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
        //Call FragmentHome
        View v=inflater.inflate(R.layout.fragment_home,container,false);
        v.setTag("RecyclerViewFragment");

        //RecycleView List of Data
        recyclerView= v.findViewById(R.id.RecycleViewListDataHome);
        progressBar= v.findViewById(R.id.progressBarHome);
        helpButton= v.findViewById(R.id.helpButton);
        settingButton= v.findViewById(R.id.settingButton);


        //Button untuk AppBPS
        imgBttDavita= v.findViewById(R.id.bttDavita);
        imgBttChatUs= v.findViewById(R.id.bttChatUs);
        imgBttDataCorner= v.findViewById(R.id.bttDataCorner);

        //Dialog pilih Wilayah handler
        dialogHelpSlider=new Dialog(mainActivity.getWindow().getContext());
        dialogHelpSlider.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogHelpSlider.setContentView(R.layout.activity_help_onboarding);

        viewPager=dialogHelpSlider.findViewById(R.id.view_pager_help);
        dotsLayout=dialogHelpSlider.findViewById(R.id.dots_layout);
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpSliderAdapter helpSliderAdapter=new HelpSliderAdapter(dialogHelpSlider.getContext());
                System.out.println("Count : "+helpSliderAdapter.getCount());
                viewPager.setAdapter(helpSliderAdapter);
                dialogHelpSlider.show();
            }
        });

        //indicator Strategis Button All
        allIndicatorStrat=v.findViewById(R.id.all_Indicator_Strat);
        allIndicatorStrat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().addToBackStack(null)
                                .replace(R.id.fragmen_container,
                                        new IndicatorStrategisFragment(mainActivity)).commit();
            }
        });

        //Dialog Login Handler
        dialogLogin=new Dialog(mainActivity.getWindow().getContext());
        dialogLogin.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogLogin.setContentView(R.layout.activity_login_profil);
        logoutButton=dialogLogin.findViewById(R.id.log_out_button);
        imgFotoProfile=dialogLogin.findViewById(R.id.img_foto_profile);
        loginHeadingTv=dialogLogin.findViewById(R.id.loginHeadingTv);
        emailLoginTv=dialogLogin.findViewById(R.id.emailLoginTv);
        //loginAndAuthHandler=new LoginAndAuthHandler(mainActivity, dialogLogin, this);

        //SSO Google Handler
        nmPengguna=v.findViewById(R.id.nm_pengguna_bawah);
        nmSelamatDatang=v.findViewById(R.id.nm_pengguna_atas);
        no_profile_pict=v.findViewById(R.id.no_profile_pict);
        with_profile_pict_iv=v.findViewById(R.id.with_profile_pict_iv);
        with_profile_pict_tv=v.findViewById(R.id.with_profile_pict_tv);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        //Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(mainActivity,gso);
        //loginAndAuthHandler.loginClick();

        if(mainActivity.getAcct()!=null){
            if(!mainActivity.getAcct().getFamilyName().equals("null")){
                nmPengguna.setText(mainActivity.getAcct().getFamilyName());
                with_profile_pict_tv.setText(mainActivity.getAcct().getEmail().substring(0,1).toUpperCase());
            }else{
                String [] namaString=mainActivity.getAcct().getEmail().split("@");
                nmPengguna.setText(namaString[0]);
                with_profile_pict_tv.setText(namaString[0].substring(0,1).toUpperCase());
            }
            nmSelamatDatang.setText("Selamat Datang");
            no_profile_pict.setVisibility(View.GONE);
            with_profile_pict_iv.setVisibility(View.VISIBLE);
            with_profile_pict_tv.setVisibility(View.VISIBLE);
            setFoto(mainActivity.getAcct());

        }else{
            nmPengguna.setText("");
            nmSelamatDatang.setText("");
            no_profile_pict.setVisibility(View.VISIBLE);
            with_profile_pict_iv.setVisibility(View.GONE);
            with_profile_pict_tv.setVisibility(View.GONE);
        }

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainActivity.getAcct()!=null){
                    dialogLogin.show();
                }else{
                    signIn();
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

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
        retrofit=new Retrofit.Builder()
                .baseUrl("https://webapi.bps.go.id/v1/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Landing page publication menu
        getListData();


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
        });



        imgBttChatUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().addToBackStack(null)
                        .replace(R.id.fragmen_container,
                                new ChatUsFragment(mainActivity)).commit();

            }
        });

        imgBttDataCorner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AppCompatActivity activity=(AppCompatActivity) v.getContext();
                //activity.getSupportFragmentManager()
                //        .beginTransaction()
                fm.beginTransaction().addToBackStack(null)
                        .replace(R.id.fragmen_container,
                                new DataCornerFragment(mainActivity)).commit();

            }
        });

        return v;
    }


    //SSO Google
    private void signIn() {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(mainActivity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        nmPengguna.setText("");
                        nmSelamatDatang.setText("");
                        no_profile_pict.setVisibility(View.VISIBLE);
                        with_profile_pict_iv.setVisibility(View.GONE);
                        with_profile_pict_tv.setVisibility(View.GONE);
                        mainActivity.setAcct(null);
                        dialogLogin.dismiss();
                    }
                });
    }
    public void setFoto(GoogleSignInAccount account){
        if(account.getPhotoUrl()!=null){
            with_profile_pict_iv.setClipToOutline(true);
            new LoadImage(with_profile_pict_iv).execute(mainActivity.getAcct().getPhotoUrl().toString());
            with_profile_pict_tv.setVisibility(View.GONE);

            imgFotoProfile.setClipToOutline(true);
            new LoadImage(imgFotoProfile).execute(mainActivity.getAcct().getPhotoUrl().toString());
            loginHeadingTv.setText(mainActivity.getAcct().getDisplayName());
            emailLoginTv.setText(mainActivity.getAcct().getEmail());
        }else{
            with_profile_pict_iv.setBackgroundResource(R.drawable.bg_bttn_white);
            with_profile_pict_iv.setImageDrawable(null);
            with_profile_pict_tv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            //if(mainActivity.getAcct()!=null){
                dialogLogin.show();
                GoogleSignInAccount account = completedTask.getResult(ApiException.class);
                mainActivity.setAcct(account);
                if(!account.getFamilyName().equals("null")){
                    nmPengguna.setText(account.getFamilyName());
                    with_profile_pict_tv.setText(account.getEmail().substring(0,1).toUpperCase());
                }else{
                    String [] namaString=account.getEmail().split("@");
                    nmPengguna.setText(namaString[0]);
                    with_profile_pict_tv.setText(namaString[0].substring(0,1).toUpperCase());
                }
                nmSelamatDatang.setText("Selamat Datang");
                no_profile_pict.setVisibility(View.GONE);
                with_profile_pict_iv.setVisibility(View.VISIBLE);
                with_profile_pict_tv.setVisibility(View.VISIBLE);
                setFoto(account);
            //}
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
            System.out.println("Gagal");
        }
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

    public void addDots(int position){
        dots=new TextView[4];
        dotsLayout.removeAllViews();
        for(int i=0; i<dots.length; i++){
            dots[i]=new TextView(mainActivity.getWindow().getContext());
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            dotsLayout.addView(dots[i]);

        }
        if(dots.length>0){
            dots[position].setTextColor(getResources().getColor(R.color.orange));
        }
    }


    //ViewPager OnClickListener
    ViewPager.OnPageChangeListener changeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

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


}
