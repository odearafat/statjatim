package com.example.bottommenu.fragmen;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.bottommenu.R;
import com.example.bottommenu.activity.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * create an instance of this fragment.
 */
public class DataCornerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageButton buttonBackDataCorner;
    MainActivity mainActivity;

    public DataCornerFragment(MainActivity mainActivity) {
        // Required empty public constructor
        this.mainActivity=mainActivity;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ChatUsFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static DataCornerFragment newInstance(String param1, String param2) {
//        DataCornerFragment fragment = new DataCornerFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainActivity.getInternetConnectionCheck().isConnected();
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_data_corner,container,false);
        WebView wv_data_corner=view.findViewById(R.id.dataCorner);
        buttonBackDataCorner=(ImageButton) view.findViewById(R.id.buttonBackDataCorner);

        //backButtonHandler
        buttonBackDataCorner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        //Enable Javasript Webview
        wv_data_corner.getSettings().setJavaScriptEnabled(true);
        wv_data_corner.getSettings().setSupportMultipleWindows(true);
        wv_data_corner.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv_data_corner.getSettings().setAllowFileAccess(true);
        wv_data_corner.getSettings().setJavaScriptEnabled(true);
        wv_data_corner.getSettings().setBuiltInZoomControls(true);
        wv_data_corner.getSettings().setDisplayZoomControls(false);
        wv_data_corner.getSettings().setLoadWithOverviewMode(true);
        wv_data_corner.getSettings().setUseWideViewPort(true);


        wv_data_corner.setWebViewClient(new WebViewClient());
        wv_data_corner.loadUrl("http://124.158.151.235:7777/elastic/");

        wv_data_corner.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                String fileName;
                try {
                    fileName = URLUtil.guessFileName(url, contentDisposition, mimeType);
                    downloadFileAsync(url, fileName);
                }catch (Exception e){

                }
            }
        });
        return view;
    }

    private void downloadFileAsync(String url, String filename){

        new AsyncTask<String, Void, String>() {
            String SDCard;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(String... params) {
                String filename = "inputAFileName";

                HttpURLConnection c;
                try {
                    URL url = new URL("http://someurl/" + filename);
                    c = (HttpURLConnection) url.openConnection();
                    c.setRequestMethod("GET");
                    c.setDoOutput(true);
                    c.connect();
                } catch (IOException e1) {
                    return e1.getMessage();
                }

                File myFilesDir = new File(Environment
                        .getExternalStorageDirectory().getAbsolutePath()
                        + "/Download");

                File file = new File(myFilesDir, filename);

                if (file.exists()) {
                    file.delete();
                }

                if ((myFilesDir.mkdirs() || myFilesDir.isDirectory())) {
                    try {
                        InputStream is = c.getInputStream();
                        FileOutputStream fos = new FileOutputStream(myFilesDir
                                + "/" + filename);

                        byte[] buffer = new byte[1024];
                        int len1 = 0;
                        while ((len1 = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len1);
                        }
                        fos.close();
                        is.close();

                    } catch (Exception e) {
                        return e.getMessage();
                    }

                    if (file.exists()) {
                        return "File downloaded!";
                    } else {
                        Log.e(TAG, "file not found");
                    }
                } else {
                    Log.e(TAG, "unable to create folder");
                }
                return params[1];
            }

//            @Override
//            protected String doInBackground(String... params) {
//                try {
//                    URL url = new URL(params[0]);
//                    HttpURLConnection urlConnection = null;
//                    urlConnection = (HttpURLConnection) url.openConnection();
//                    urlConnection.setRequestMethod("GET");
//                    urlConnection.setDoOutput(true);
//                    urlConnection.connect();
//                    int lengthOfFile = urlConnection.getContentLength();
//                    //SDCard = Environment.getExternalStorageDirectory() + File.separator + "downloads";
//                    SDCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"";
//                    int k = 0;
//                    boolean file_exists;
//                    String finalValue = params[1];
//                    do {
//                        if (k > 0) {
//                            if (params[1].length() > 0) {
//                                String s = params[1].substring(0, params[1].lastIndexOf("."));
//                                String extension = params[1].replace(s, "");
//
//                                finalValue = s + "(" + k + ")" + extension;
//                            } else {
//                                String fileName = params[0].substring(params[0].lastIndexOf('/') + 1);
//                                String s = fileName.substring(0, fileName.lastIndexOf("."));
//                                String extension = fileName.replace(s, "");
//                                finalValue = s + "(" + k + ")" + extension;
//                            }
//                        }
//                        File fileIn = new File(SDCard, finalValue);
//                        file_exists = fileIn.exists();
//                        k++;
//                    } while (file_exists);
//
//                    File file = new File(SDCard, finalValue);
//                    FileOutputStream fileOutput = null;
//                    fileOutput = new FileOutputStream(file, true);
//                    InputStream inputStream = null;
//                    inputStream = urlConnection.getInputStream();
//                    byte[] buffer = new byte[1024];
//                    int count;
//                    long total = 0;
//                    while ((count = inputStream.read(buffer)) != -1) {
//                        total += count;
//                        //publishProgress(""+(int)((total*100)/lengthOfFile));
//                        fileOutput.write(buffer, 0, count);
//                    }
//                    fileOutput.flush();
//                    fileOutput.close();
//                    inputStream.close();
//                } catch (MalformedURLException e){
//                } catch (ProtocolException e){
//                } catch (FileNotFoundException e){
//                } catch (IOException e){
//                } catch (Exception e){
//                }
//                return params[1];
//            }

            @Override
            protected void onPostExecute(final String result) {

            }

        }.execute(url, filename);
    }
}