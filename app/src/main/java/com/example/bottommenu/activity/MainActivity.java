package com.example.bottommenu.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bottommenu.HelperClass.InternetConnectionCheck;
import com.example.bottommenu.R;
import com.example.bottommenu.fragmen.BeritaFragment;
import com.example.bottommenu.fragmen.BrsFragment;
import com.example.bottommenu.fragmen.DataFragment;
import com.example.bottommenu.fragmen.DataListFragment;
import com.example.bottommenu.fragmen.DavitaFragment;
import com.example.bottommenu.fragmen.HomeFragment;
import com.example.bottommenu.fragmen.PublikasiFragment;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

public class MainActivity<selectedFragment> extends AppCompatActivity {

    SpaceNavigationView navigationView;
    FragmentManager fm;
    Fragment selectedFragment;
    MainActivity mainActivity;
    boolean homePressed = true, doubleBackToExitPressedOnce = false;
    Dialog dialogPilihWilayah;
    String bahasa="ind";
    String wilayah="Provinsi Jawa timur";
    String idWilayah="3500";
    InternetConnectionCheck internetConnectionCheck;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity=this;
        //memunculkan HomeFragment diAwal

        //declare Frame Manager
        fm= getSupportFragmentManager();

        //declare Navigation View
        navigationView=findViewById(R.id.space);

        //Check Internet Connection
        internetConnectionCheck=new InternetConnectionCheck(mainActivity);
        internetConnectionCheck.isConnected();


        fm.beginTransaction().addToBackStack(null)
                .replace(R.id.fragmen_container,new HomeFragment(this))
                .commit();

        //Dialog pilih Wilayah handler
        dialogPilihWilayah=new Dialog(MainActivity.this);
        dialogPilihWilayah.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogPilihWilayah.setContentView(R.layout.activity_pilih_wilayah_bahasa);

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

        System.out.println("count : " + backStackEntryCount);
        //System.out.println("count : " + fm.getB);
        //backStackEntryCount==0 -> no fragments more.. so close the activity with warning
        if (backStackEntryCount == 0) {
            if (homePressed) {
                if (doubleBackToExitPressedOnce) {
                   super.onBackPressed();
                    if(fm.getFragments().get(0).getClass()==PublikasiFragment.class){
                        navigationView.changeCurrentItem(0);
                    }else if(fm.getFragments().get(0).getClass()==BeritaFragment.class){
                        navigationView.changeCurrentItem(1);
                    }else if(fm.getFragments().get(0).getClass()==BrsFragment.class){
                        navigationView.changeCurrentItem(2);
                    }else if(fm.getFragments().get(0).getClass()==DataFragment.class){
                        navigationView.changeCurrentItem(3);
                    }else{
                        navigationView.changeCurrentItem(-1);
                    }
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Klik Back Lagi Untuk Menutup Aplikasi", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } else {
                homePressed = true;
            }
        }
        //some fragments are there.. so allow the back press action
        else {
            //System.out.println("aa :"+fm.);
            super.onBackPressed();
            if(fm.getFragments().get(0).getClass()==PublikasiFragment.class){
                navigationView.changeCurrentItem(0);
            }else if(fm.getFragments().get(0).getClass()==BeritaFragment.class){
                navigationView.changeCurrentItem(1);
            }else if(fm.getFragments().get(0).getClass()==BrsFragment.class){
                navigationView.changeCurrentItem(2);
            }else if(fm.getFragments().get(0).getClass()==DataFragment.class){
                navigationView.changeCurrentItem(3);
            }else{
                navigationView.changeCurrentItem(-1);
            }

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
}