package com.example.bottommenu.Adapter;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottommenu.R;
import com.example.bottommenu.activity.MainActivity;
import com.example.bottommenu.interfacePackage.PublikasiHolderApi;
import com.example.bottommenu.model.Page;
import com.example.bottommenu.model.Publikasi;
import com.example.bottommenu.model.PublikasiItem;
import com.example.bottommenu.model.PublikasiView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.getSystemService;

public class PublikasiAdapter extends RecyclerView.Adapter<PublikasiAdapter.PublikasiViewHolderAdapter> {

    //public static final int WRITE_PERMISSION = 1001;
    public static final int PERMISSION_STORAGE_CODE = 1000;
    private List<PublikasiItem> publikasiItemList;
    Dialog dialogDetailPublikasi;
    TextView detailJudulPublikasi, detailRl_date, detailUpdt_date, detailIssn, detailSize, detailAbstract;
    Button detailDownloadbutton;
    ImageView detailCover;



    Context context;

    public PublikasiAdapter(Context ct, List<PublikasiItem> publikasiItems, Dialog dialogDetailPublikasis) {
        this.publikasiItemList=publikasiItems;
        context=ct;
        this.dialogDetailPublikasi=dialogDetailPublikasis;


        dialogDetailPublikasi.setContentView(R.layout.detail_publikasi);
        this.detailJudulPublikasi=dialogDetailPublikasi.findViewById(R.id.detailJudulPublikasi);
        this.detailRl_date=dialogDetailPublikasi.findViewById(R.id.detailTanggalRilis);
        this.detailUpdt_date=dialogDetailPublikasi.findViewById(R.id.detailTanggalUpdate);
        this.detailIssn=dialogDetailPublikasi.findViewById(R.id.detailIssn);
        this.detailSize=dialogDetailPublikasi.findViewById(R.id.detailUkuranFile);
        this.detailAbstract=dialogDetailPublikasi.findViewById(R.id.detailAbstract);

        this.detailCover=dialogDetailPublikasi.findViewById(R.id.detailCover);
        this.detailDownloadbutton=dialogDetailPublikasi.findViewById(R.id.detailBttDownload);

    }

    @NonNull
    @Override
    public PublikasiViewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.fragment_publikasi_list_data,parent,false);
        return new PublikasiViewHolderAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PublikasiViewHolderAdapter holder, final int position) {
        holder.judulPublikasi.setText(publikasiItemList.get(position).getTitle());
        holder.tanggalRilis.setText(publikasiItemList.get(position).getRl_date());
        holder.ukuranFile.setText(publikasiItemList.get(position).getSize());
        holder.cover.setClipToOutline(true);
        new LoadImage(holder.cover).execute(publikasiItemList.get(position).getCover());
        holder.bttUnduhPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionDownload(position);

            }


        });

        holder.bttDetailPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //JsonHolderInterfaceClass
                Retrofit retrofit=new Retrofit.Builder().baseUrl("https://webapi.bps.go.id/v1/api/")
                        .addConverterFactory(GsonConverterFactory.create()).build();
                PublikasiHolderApi jsonPlaceHolderApi=retrofit.create(PublikasiHolderApi.class);

                String idPub=publikasiItemList.get(position).getPub_id();
                //354475b2d04ee5be705892c01701899d
                Call<PublikasiView> call=jsonPlaceHolderApi.getView(
                        "publication", "3500", "2ad01e6a21b015ea1ff8805ced02600c",
                        "ind",idPub
                );

                //Callback
                call.enqueue(new Callback<PublikasiView>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)

                    @Override
                    public void onResponse(Call<PublikasiView> call, Response<PublikasiView> response) {

                        if(!response.isSuccessful()){
                            String getResult="Code : "+response.code();
                            return;
                        }
                        PublikasiView publikasiview=response.body();
                        System.out.println("Berhasil :"+publikasiview.getData().getSize());

                        detailJudulPublikasi.setText(publikasiview.getData().getTitle());
                        detailIssn.setText("ISSN/ISBN : "+publikasiview.getData().getIssn());
                        detailRl_date.setText("Tanggal Rilis : "+publikasiview.getData().getRl_date());
                        detailUpdt_date.setText("Tanggal Update : "+publikasiview.getData().getSch_date());
                        detailSize.setText("Ukuran File : "+publikasiview.getData().getSize());
                        detailAbstract.setText(publikasiview.getData().getAbstract_pub().replace("\n", ""));
                        //Cast to Page
                        //Page page=new Gson().fromJson(publikasi.getData().get(0).toString(), Page.class);

                        //Cast to List of Publikasi Item
                        //publikasiItems = stringToArray(new Gson().toJson(publikasi.getData().get(1)), PublikasiItem[].class);

                        //publikasiAdapter=new PublikasiAdapter(getContext(),publikasiItems, dialogDetailPublikasi, retrofit);
                        //recyclerView.setAdapter(publikasiAdapter);

                        //progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<PublikasiView> call, Throwable t) {
                        //getResult="Gagal";
                        System.out.println("gagal");
                        Log.d("I","Message"+t.getMessage());
                    }
                });

                detailJudulPublikasi.setText(publikasiItemList.get(position).getTitle());
                detailIssn.setText("ISSN/ISBN : "+publikasiItemList.get(position).getIssn());
                detailRl_date.setText("Tanggal Rilis : "+publikasiItemList.get(position).getRl_date());
                detailUpdt_date.setText("Tanggal Update : "+publikasiItemList.get(position).getRl_date());
                detailSize.setText("Ukuran File : "+publikasiItemList.get(position).getSize());
                detailAbstract.setText(publikasiItemList.get(position).getSize());

                detailCover.setClipToOutline(true);
                new LoadImage(detailCover).execute(publikasiItemList.get(position).getCover());

                detailDownloadbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actionDownload(position);
                    }
                });

                //dialogDetailPublikasi.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                dialogDetailPublikasi.show();
            }
        });


    }

    //public void set Change add PublikasiItemList
    public void addPublikasiItemList(List<PublikasiItem> publikasiItemLi){

        publikasiItemList=new ArrayList<>(publikasiItemList);
        for(PublikasiItem puIt:publikasiItemLi){
            publikasiItemList.add(puIt);
        }
        notifyDataSetChanged();
    }

    //actionListener Download Publikasi
    public void actionDownload(int position){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                    PackageManager.PERMISSION_DENIED){

                //Permission Denied, Request it
                String [] permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE};
                //show popup dor running pemisssion
                requestPermissions((Activity) context,permissions,PERMISSION_STORAGE_CODE);
            }else{
                //Permission Granted, Perform Download
                String filename=publikasiItemList.get(position).getTitle()+".pdf";
                downloadfile(filename,publikasiItemList.get(position).getPdf());
            }
        }else{
            //System OS is less than marsmallow, perform download
            String filename=publikasiItemList.get(position).getTitle()+".pdf";
            downloadfile(filename,publikasiItemList.get(position).getPdf());
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


    @Override
    public int getItemCount() {

        return publikasiItemList.size();
    }

    public class PublikasiViewHolderAdapter extends RecyclerView.ViewHolder {
        TextView judulPublikasi, tanggalRilis, ukuranFile;
        ImageView cover;
        Button bttUnduhPub, bttDetailPub;

        public PublikasiViewHolderAdapter(@NonNull View itemView) {
            super(itemView);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(v.getContext(), "Test Click", Toast.LENGTH_SHORT).show();
//                }
//            });
            judulPublikasi=(TextView)itemView.findViewById(R.id.judulPublikasi);
            tanggalRilis=(TextView)itemView.findViewById(R.id.tanggalRilis);
            ukuranFile=(TextView)itemView.findViewById(R.id.ukuranFile);
            cover=(ImageView)itemView.findViewById(R.id.cover);
            bttDetailPub=(Button)itemView.findViewById(R.id.bttDetailPub);
            bttUnduhPub=(Button)itemView.findViewById(R.id.bttUnduhPub);
        }
    }
}
