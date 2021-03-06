package com.bps_jatim_3500.statistik_jatim.fragmen;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bps_jatim_3500.statistik_jatim.HelperClass.MyWebViewClient;
import com.bps_jatim_3500.statistik_jatim.R;
import com.bps_jatim_3500.statistik_jatim.activity.MainActivity;
import com.bps_jatim_3500.statistik_jatim.model.IndikatorStrategisItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DataStrategisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DataStrategisFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageButton buttonBackChatUs;
    IndikatorStrategisItem indikatorStrategisItem;
    MainActivity mainActivity;
    ProgressDialog pd;

    public DataStrategisFragment(IndikatorStrategisItem indikatorStrategisItem, MainActivity mainActivity) {
        // Required empty public constructor
        this.indikatorStrategisItem=indikatorStrategisItem;
        this.mainActivity=mainActivity;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatUsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public DataStrategisFragment newInstance(String param1, String param2) {
        DataStrategisFragment fragment = new DataStrategisFragment(this.indikatorStrategisItem, this.mainActivity);
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
        mainActivity.getInternetConnectionCheck().isConnected();
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_chat_us,container,false);
        WebView wv_davita=view.findViewById(R.id.wvChatUs);
        buttonBackChatUs=(ImageButton)view.findViewById(R.id.buttonBackChatUs);



        TextView tvBarChatUs=view.findViewById(R.id.textBarChatUs);
        tvBarChatUs.setText("Indikator Strategis");



        //backButtonHandler
        buttonBackChatUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
                //Enable Javasript Webview
        wv_davita.getSettings().setJavaScriptEnabled(true);
        wv_davita.getSettings().setLoadsImagesAutomatically(true);
        wv_davita.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


        pd= new ProgressDialog(mainActivity.getWindow().getContext());
        pd.setMessage("Mohon Menunggu. . .");
        pd.show();
        wv_davita.setWebViewClient(new MyWebViewClient(pd));
        //web_view.loadUrl("ur site name");

        //wv_davita.setWebViewClient(new WebViewClient());
        wv_davita.loadUrl("https://webapps.bps.go.id/jatim/statjatim/index.php?id="+indikatorStrategisItem.getIndicator_id());
        //wv_davita.loadUrl("https://jatim.bps.go.id");


//        wv_davita.setDownloadListener(new DownloadListener() {
//            @Override
//            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
//                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//
//                request.setMimeType(mimeType);
//                //------------------------COOKIE!!------------------------
//                String cookies = CookieManager.getInstance().getCookie(url);
//                request.addRequestHeader("cookie", cookies);
//                //------------------------COOKIE!!------------------------
//                request.addRequestHeader("User-Agent", userAgent);
//                request.setDescription("Downloading file...");
//                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
//                request.allowScanningByMediaScanner();
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
//                DownloadManager dm = (DownloadManager) getContext().getSystemService(DOWNLOAD_SERVICE);
//                dm.enqueue(request);
//                Toast.makeText(getContext().getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
//            }
//        });

        wv_davita.setDownloadListener(new DownloadListener() {
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




        //handle downloading
//        wv_davita.setDownloadListener(new DownloadListener() {
//            @Override
//            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
//                System.out.println("url");
//
//                Log.println(Log.INFO,"dada","dsadsa");
//            }
//        });


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
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection urlConnection = null;
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoOutput(true);
                    urlConnection.connect();
                    int lengthOfFile = urlConnection.getContentLength();
                    //SDCard = Environment.getExternalStorageDirectory() + File.separator + "downloads";
                    SDCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"";
                    int k = 0;
                    boolean file_exists;
                    String finalValue = params[1];
                    do {
                        if (k > 0) {
                            if (params[1].length() > 0) {
                                String s = params[1].substring(0, params[1].lastIndexOf("."));
                                String extension = params[1].replace(s, "");

                                finalValue = s + "(" + k + ")" + extension;
                            } else {
                                String fileName = params[0].substring(params[0].lastIndexOf('/') + 1);
                                String s = fileName.substring(0, fileName.lastIndexOf("."));
                                String extension = fileName.replace(s, "");
                                finalValue = s + "(" + k + ")" + extension;
                            }
                        }
                        File fileIn = new File(SDCard, finalValue);
                        file_exists = fileIn.exists();
                        k++;
                    } while (file_exists);

                    File file = new File(SDCard, finalValue);
                    FileOutputStream fileOutput = null;
                    fileOutput = new FileOutputStream(file, true);
                    InputStream inputStream = null;
                    inputStream = urlConnection.getInputStream();
                    byte[] buffer = new byte[1024];
                    int count;
                    long total = 0;
                    while ((count = inputStream.read(buffer)) != -1) {
                        total += count;
                        //publishProgress(""+(int)((total*100)/lengthOfFile));
                        fileOutput.write(buffer, 0, count);
                    }
                    fileOutput.flush();
                    fileOutput.close();
                    inputStream.close();
                } catch (MalformedURLException e){
                } catch (ProtocolException e){
                } catch (FileNotFoundException e){
                } catch (IOException e){
                } catch (Exception e){
                }
                return params[1];
            }

            @Override
            protected void onPostExecute(final String result) {

            }

        }.execute(url, filename);
    }
}

