package com.bps_jatim_3500.statistik_jatim.fragmen;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bps_jatim_3500.statistik_jatim.R;
import com.bps_jatim_3500.statistik_jatim.activity.MainActivity;

/**
 * create an instance of this fragment.
 */
public class DavitaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MainActivity mainActivity;

    public DavitaFragment(MainActivity mainActivity) {
        // Required empty public constructor
        this.mainActivity=mainActivity;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainActivity.getInternetConnectionCheck().isConnected();

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_davita,container,false);
        WebView wv_davita=view.findViewById(R.id.wvDavita);
        wv_davita.setWebViewClient(new WebViewClient());
        wv_davita.getSettings().setJavaScriptEnabled(true);
        wv_davita.loadUrl("https://api.whatsapp.com/send/?phone=%2B6285235727140&text=halo&app_absent=0");
        wv_davita.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if( URLUtil.isNetworkUrl(url) ) {
                    return false;
                }
                if (appInstalledOrNot(url, view)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity( intent );
                } else {
                    // do something if app is not installed
                }
                return true;
            }

        });

        //wv_davita.loadUrl("https://api.whatsapp.com/send/?phone=%2B6285235727140&text=halo&app_absent=0");
        return view;
    }

    private boolean appInstalledOrNot(String uri, View view) {
        PackageManager pm = view.getContext().getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }
}