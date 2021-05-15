package com.bps_jatim_3500.statistik_jatim.HelperClass;

import android.app.ProgressDialog;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {
    ProgressDialog pd;
    public MyWebViewClient(ProgressDialog pd) {
        this.pd=pd;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);

        if (!pd.isShowing()) {
            pd.show();
        }

        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        System.out.println("on finish");
        if (pd.isShowing()) {
            pd.dismiss();
        }

    }
}