package com.bps_jatim_3500.statistik_jatim.HelperClass;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.bps_jatim_3500.statistik_jatim.R;
import com.bps_jatim_3500.statistik_jatim.activity.MainActivity;
import com.bps_jatim_3500.statistik_jatim.fragmen.NoInternetConnectionFragment;

public class InternetConnectionCheck {

    MainActivity mainActivity;
    public InternetConnectionCheck(MainActivity mainActivity) {
        this.mainActivity=mainActivity;
    }

    public void isConnected(){
        ConnectivityManager connectivityManager=(ConnectivityManager) mainActivity.getWindow().getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        //if(!(wifiConn !=null && wifiConn.isConnected())||(mobileConn !=null && mobileConn.isConnected())){
        if(!(wifiConn !=null && wifiConn.isConnected()||(mobileConn !=null && mobileConn.isConnected()))){
            mainActivity.getFm().beginTransaction().addToBackStack(null)
                    .replace(R.id.fragmen_container,new NoInternetConnectionFragment())
                    .commit();
            mainActivity.getNavigationView().setVisibility(View.GONE);
            //mainActivity.getDialogPilihWilayah().dismiss();
        }else{
            mainActivity.getNavigationView().setVisibility(View.VISIBLE);
        }

    }
}
