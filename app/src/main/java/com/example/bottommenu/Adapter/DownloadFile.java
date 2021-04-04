package com.example.bottommenu.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class DownloadFile {

    Context context;
    public static final int PERMISSION_STORAGE_CODE = 1000;
    public DownloadFile(Context contexts){
        this.context=contexts;

    }

    //actionListener Download Publikasi
    public void actionDownload(String fileNames, String urls){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                    PackageManager.PERMISSION_DENIED){

                //Permission Denied, Request it
                String [] permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE};
                //show popup dor running pemisssion
                requestPermissions((Activity) context,permissions,PERMISSION_STORAGE_CODE);
            }else{
                //Permission Granted, Perform Download
                String filename=fileNames+".pdf";
                downloadfile(filename,urls);
            }
        }else{
            //System OS is less than marsmallow, perform download
            String filename=fileNames+".pdf";
            downloadfile(filename,urls);
        }
    }

    //actionListener Download Publikasi
    public void actionDownloadExcel(String fileNames, String urls){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                    PackageManager.PERMISSION_DENIED){

                //Permission Denied, Request it
                String [] permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE};
                //show popup dor running pemisssion
                requestPermissions((Activity) context,permissions,PERMISSION_STORAGE_CODE);
            }else{
                //Permission Granted, Perform Download
                String filename=fileNames+".xlsx";
                downloadfile(filename,urls);
            }
        }else{
            //System OS is less than marsmallow, perform download
            String filename=fileNames+".xlsx";
            downloadfile(filename,urls);
        }
    }


    public void downloadfile(String filename, String pdf) {
        //Create Download Request
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(pdf));

        //Create Download Request
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE);

        request.setTitle(filename);//Set Title in Download Notification
        request.setDescription("Download Publikasi . .");//Set Description in Download Notification

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename);

        //get download service and enque file
        DownloadManager manager =(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }


    //handle permission Result
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_STORAGE_CODE: {
                if(grantResults.length>0 && grantResults[0]==
                        PackageManager.PERMISSION_GRANTED){
                    //startDownloading();
                }else {
                    Toast.makeText(context,"Permission Denied !!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
