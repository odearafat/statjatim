package com.bps_jatim_3500.statistik_jatim.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bps_jatim_3500.statistik_jatim.HelperClass.InternetConnectionCheck;
import com.bps_jatim_3500.statistik_jatim.R;
import com.bps_jatim_3500.statistik_jatim.fragmen.BeritaFragment;
import com.bps_jatim_3500.statistik_jatim.fragmen.BrsFragment;
import com.bps_jatim_3500.statistik_jatim.fragmen.DataFragment;
import com.bps_jatim_3500.statistik_jatim.fragmen.HomeFragment;
import com.bps_jatim_3500.statistik_jatim.fragmen.PublikasiFragment;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

public class MainActivity extends AppCompatActivity {

    SpaceNavigationView navigationView;
    FragmentManager fm;
    Fragment selectedFragment;
    MainActivity mainActivity;
    boolean homePressed = false, doubleBackToExitPressedOnce = true;
    Dialog dialogPilihWilayah, downloadStatus;
    String bahasa="ind";
    String wilayah="Provinsi Jawa timur";
    String idWilayah="3500";
    InternetConnectionCheck internetConnectionCheck;
    ImageView iconDownloadStatus;
    TextView tvDownloadStatusHeading, tvDownloadStatusDetail;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Declare Mainactivity
        mainActivity=this;

        //declare Frame Manager
        fm= getSupportFragmentManager();

        //declare Navigation View
        navigationView=findViewById(R.id.space);

        //Check Internet Connection
        internetConnectionCheck=new InternetConnectionCheck(mainActivity);
        internetConnectionCheck.isConnected();


        fm.beginTransaction()
                .replace(R.id.fragmen_container,new HomeFragment(this))
                .commit();

        //Dialog pilih Wilayah handler
        dialogPilihWilayah=new Dialog(MainActivity.this);
        dialogPilihWilayah.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogPilihWilayah.setContentView(R.layout.activity_pilih_wilayah_bahasa);

        //Dialog pilih Wilayah handler
        downloadStatus=new Dialog(MainActivity.this);
        downloadStatus.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        downloadStatus.setContentView(R.layout.activity_download_status);
        iconDownloadStatus = downloadStatus.findViewById(R.id.icon_download_status);
        tvDownloadStatusHeading = downloadStatus.findViewById(R.id.tv_download_status_heading);
        tvDownloadStatusDetail = downloadStatus.findViewById(R.id.tv_download_status_detail);



        //Navigation Bottom
        navigationView.initWithSaveInstanceState(savedInstanceState);
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_publikasi_bottom));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_berita_bottom));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_brs_bottom));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_data_bottom));


        navigationView.setActiveSpaceItemColor(getColor(R.color.black));
        //navigationView.changeCurrentItem(-1);
        navigationView.changeCurrentItem(-1);
        navigationView.setActiveSpaceItemColor(getColor(R.color.colorPrimaryDark));
        navigationView.setInActiveCentreButtonIconColor(getColor(R.color.colorPrimary));
        navigationView.setActiveCentreButtonIconColor(getColor(R.color.colorPrimaryDark));
        navigationView.setSpaceItemIconSize(100);
        selectedFragment=null;

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                navigationView.showBadgeAtIndex(2, 2, getColor(R.color.orange));
//                navigationView.showBadgeAtIndex(1, 2, getColor(R.color.orange));
//                navigationView.showBadgeAtIndex(0, 2, getColor(R.color.orange));
//                navigationView.showBadgeAtIndex(3, 2, getColor(R.color.orange));
//            }
//        }, 2000);


        navigationView.setSpaceOnClickListener(new SpaceOnClickListener() {

            @Override
            public void onCentreButtonClick() {
                //Toast.makeText(MainActivity.this,"onCentreButtonClick", Toast.LENGTH_SHORT).show();
                selectedFragment=new HomeFragment(mainActivity);

                //fm= getSupportFragmentManager();
                        fm.beginTransaction().addToBackStack(null)
                                .replace(R.id.fragmen_container,selectedFragment)
                        .commit();
                navigationView.setCentreButtonSelectable(true);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                //Toast.makeText(MainActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
                switch (itemIndex){
                    case 0:
                        selectedFragment=new PublikasiFragment(mainActivity);
                        break;
                    case 1:
                        selectedFragment=new BeritaFragment(mainActivity);
                        break;
                    case 2:
                        selectedFragment=new BrsFragment(mainActivity);
                        break;
                    case 3:
                        selectedFragment=new DataFragment(mainActivity);
                        homePressed=false;
                        break;
                    case -1:
                        selectedFragment=new HomeFragment(mainActivity);
                        break;
                }

                        fm.beginTransaction().addToBackStack(null)
                        .replace(R.id.fragmen_container,selectedFragment)
                        .commit();
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                String pesan="";
                switch (itemIndex){
                    case 0:
                        pesan="Anda Telah di Menu Publikasi";
                        break;
                    case 1:
                        pesan="Anda Telah di Menu Berita";
                        break;
                    case 2:
                        pesan="Anda Telah di Menu Berita Resmi Statistik";
                        break;
                    case 3:
                        pesan="Anda Telah di Menu Data";
                        break;
                }
                //Toast.makeText(MainActivity.this, pesan, Toast.LENGTH_SHORT).show();
            }
        });

        //navigationView.setCentreButtonSelected();

    }

    @Override
    public void onBackPressed() {
        int backStackEntryCount = fm.getBackStackEntryCount();
        System.out.println("homepress "+homePressed);
        System.out.println("count : " + backStackEntryCount);
            if(fm.getFragments().size()>0){
                System.out.println(fm.getFragments().size());
                super.onBackPressed();
                if(fm.getFragments().get(0).getClass()==PublikasiFragment.class){
                    navigationView.changeCurrentItem(0);

                    //super.onBackPressed();
                }else if(fm.getFragments().get(0).getClass()==BeritaFragment.class){
                    navigationView.changeCurrentItem(1);

                    //super.onBackPressed();
                }else if(fm.getFragments().get(0).getClass()==BrsFragment.class){
                    navigationView.changeCurrentItem(2);

                }else if(fm.getFragments().get(0).getClass()==DataFragment.class){
                    navigationView.changeCurrentItem(3);

                    //super.onBackPressed();
                }else {
                    navigationView.changeCurrentItem(-1);


                    //super.onBackPressed();
                }
            }else{
//                super.onBackPressed();
//                navigationView.changeCurrentItem(-1);
//                //Toast.makeText(this, "Klik Back Lagi Untuk Menutup Aplikasi", Toast.LENGTH_SHORT).show();
//                if(doubleBackToExitPressedOnce) {//doubleBackToExitPressedOnce = false;
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            doubleBackToExitPressedOnce = true;
//                        }
//                    }, 2000);
//                    doubleBackToExitPressedOnce=false;
//                }else{
                    System.exit(0);
              //  }
            }
    }

    public FragmentManager getFm() {
        return fm;
    }

    public String getBahasa() {
        return bahasa;
    }

    public String getWilayah() {
        return wilayah;
    }

    public String getIdWilayah() {
        return idWilayah;
    }

    public void setBahasa(String bahasa) {
        this.bahasa = bahasa;
    }

    public void setWilayah(String wilayah) {
        this.wilayah = wilayah;
    }

    public void setIdWilayah(String idWilayah) {
        this.idWilayah = idWilayah;
    }

    public Dialog getDialogPilihWilayah() {
        return dialogPilihWilayah;
    }

    public Fragment getSelectedFragment() {
        return selectedFragment;
    }

    public void setSelectedFragment(Fragment selectedFragment) {
        this.selectedFragment = selectedFragment;
    }

    public SpaceNavigationView getNavigationView() {
        return navigationView;
    }

    public InternetConnectionCheck getInternetConnectionCheck() {
        return internetConnectionCheck;
    }

    public Dialog getDownloadStatus() {
        return downloadStatus;
    }

    public ImageView getIconDownloadStatus() {
        return iconDownloadStatus;
    }

    public TextView getTvDownloadStatusHeading() {
        return tvDownloadStatusHeading;
    }

    public TextView getTvDownloadStatusDetail() {
        return tvDownloadStatusDetail;
    }
}