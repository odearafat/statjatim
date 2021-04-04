package com.example.bottommenu.fragmen;

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
import android.widget.TextView;

import com.example.bottommenu.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DavitaFragment#newInstance} factory method to
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

    public DavitaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Davita_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DavitaFragment newInstance(String param1, String param2) {
        DavitaFragment fragment = new DavitaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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