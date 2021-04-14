package com.example.bottommenu.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    SpaceNavigationView navigationView;
    FragmentManager fm;
    boolean homePressed = true, doubleBackToExitPressedOnce = false;

    
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //memunculkan HomeFragment diAwal
        fm= getSupportFragmentManager();
                fm.beginTransaction().addToBackStack(null)
                .replace(R.id.fragmen_container,new HomeFragment())
                .commit();

        //Navigation Bottom
        navigationView=findViewById(R.id.space);
        navigationView.initWithSaveInstanceState(savedInstanceState);
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_publikasi_bottom));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_berita_bottom));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_brs_bottom));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_data_bottom));

        navigationView.setActiveSpaceItemColor(getColor(R.color.black));
        navigationView.changeCurrentItem(-1);
        navigationView.setActiveSpaceItemColor(getColor(R.color.colorPrimaryDark));
        navigationView.setInActiveCentreButtonIconColor(getColor(R.color.colorPrimary));
        navigationView.setActiveCentreButtonIconColor(getColor(R.color.colorPrimaryDark));
        navigationView.setSpaceItemIconSize(100);


        navigationView.setSpaceOnClickListener(new SpaceOnClickListener() {

            Fragment selectedFragment=null;
            @Override
            public void onCentreButtonClick() {
                //Toast.makeText(MainActivity.this,"onCentreButtonClick", Toast.LENGTH_SHORT).show();
                selectedFragment=new HomeFragment();

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
                        selectedFragment=new PublikasiFragment();
                        break;
                    case 1:
                        selectedFragment=new BeritaFragment();
                        break;
                    case 2:
                        selectedFragment=new BrsFragment();
                        break;
                    case 3:
                        selectedFragment=new DataFragment();
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
                Toast.makeText(MainActivity.this, pesan, Toast.LENGTH_SHORT).show();
            }
        });

        //navigationView.setCentreButtonSelected();

    }

    @Override
    public void onBackPressed() {

        int backStackEntryCount = fm.getBackStackEntryCount();
        System.out.println("count : "+backStackEntryCount);
        //backStackEntryCount==0 -> no fragments more.. so close the activity with warning
        if (backStackEntryCount == 0){
            if (homePressed) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
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
            super.onBackPressed();
        }

    }


}