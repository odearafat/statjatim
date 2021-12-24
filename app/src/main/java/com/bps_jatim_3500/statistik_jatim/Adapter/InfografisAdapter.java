package com.bps_jatim_3500.statistik_jatim.Adapter;


import static androidx.core.app.ActivityCompat.requestPermissions;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bps_jatim_3500.statistik_jatim.R;
import com.bps_jatim_3500.statistik_jatim.activity.MainActivity;
import com.bps_jatim_3500.statistik_jatim.interfacePackage.PublikasiHolderApi;
import com.bps_jatim_3500.statistik_jatim.model.InfografisItem;
import com.bps_jatim_3500.statistik_jatim.model.PublikasiItem;
import com.bps_jatim_3500.statistik_jatim.model.PublikasiView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InfografisAdapter extends RecyclerView.Adapter<InfografisAdapter.InfografisViewHolderAdapter> {

    //public static final int WRITE_PERMISSION = 1001;
    public static final int PERMISSION_STORAGE_CODE = 1000;
    private List<InfografisItem> infografisItemList;
    Dialog dialogDetailPublikasi;
    TextView detailJudulPublikasi, detailRl_date, detailUpdt_date, detailIssn, detailSize, detailAbstract;
    ProgressBar progressBar;
    Button detailDownloadbutton;
    ImageView detailCover;
    Integer positionInView;
    MainActivity mainActivity;



    Context context;

    public InfografisAdapter(Context ct, List<InfografisItem> infografisItems, Dialog dialogDetailPublikasis,
                             MainActivity mainActivity) {

        this.infografisItemList=infografisItems;
        context=ct;
        this.dialogDetailPublikasi=dialogDetailPublikasis;
        this.mainActivity=mainActivity;


        dialogDetailPublikasi.setContentView(R.layout.detail_publikasi);
        this.detailJudulPublikasi=dialogDetailPublikasi.findViewById(R.id.detailJudulPublikasi);
        this.detailRl_date=dialogDetailPublikasi.findViewById(R.id.detailTanggalRilis);
        this.detailUpdt_date=dialogDetailPublikasi.findViewById(R.id.detailTanggalUpdate);
        this.detailIssn=dialogDetailPublikasi.findViewById(R.id.detailIssn);
        this.detailSize=dialogDetailPublikasi.findViewById(R.id.detailUkuranFile);
        this.detailAbstract=dialogDetailPublikasi.findViewById(R.id.detailAbstract);
        this.progressBar=dialogDetailPublikasi.findViewById(R.id.progressBar);

        this.detailCover=dialogDetailPublikasi.findViewById(R.id.detailCover);
        this.detailDownloadbutton=dialogDetailPublikasi.findViewById(R.id.detailBttDownload);

    }

    @NonNull
    @Override
    public InfografisViewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.fragment_infografis_list_data,parent,false);
        return new InfografisViewHolderAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InfografisViewHolderAdapter holder, final int position) {
        holder.judulPublikasi.setText(infografisItemList.get(position).getTitle());
        ///holder.tanggalRilis.setText(infografisItemList.get(position).getRl_date());
        //holder.ukuranFile.setText(infografisItemList.get(position).getSize());
        holder.cover.setClipToOutline(true);
        new LoadImage(holder.cover).execute(infografisItemList.get(position).getImg());
        holder.bttUnduhPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionDownload(position);

            }


        });

        holder.bttDetailPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //viewActionClicklistener(position);
            }
        });


    }
/*
    public void viewActionClicklistener(int position){
        //JsonHolderInterfaceClass
        mainActivity.getInternetConnectionCheck().isConnected();


        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://webapi.bps.go.id/v1/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        PublikasiHolderApi jsonPlaceHolderApi=retrofit.create(PublikasiHolderApi.class);

        Integer idPub=infografisItemList.get(position).getInf_id();
        positionInView=position;

        supportViewDialogGone();

        //354475b2d04ee5be705892c01701899d
        Call<PublikasiView> call=jsonPlaceHolderApi.getView(
                "publication", mainActivity.getIdWilayah(), "2ad01e6a21b015ea1ff8805ced02600c",
                mainActivity.getBahasa(),idPub
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

                detailCover.setClipToOutline(true);
                new LoadImage(detailCover).execute(publikasiItemList.get(positionInView).getCover());

                supportViewDialogView();
            }

            @Override
            public void onFailure(Call<PublikasiView> call, Throwable t) {
                //getResult="Gagal";
                System.out.println("gagal");
                Log.d("I","Message"+t.getMessage());
            }
        });


        detailDownloadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionDownload(positionInView);
            }
        });

        dialogDetailPublikasi.show();
    }
*/
    //public void set Change add PublikasiItemList
    public void addInfografisItemList(List<InfografisItem> infografisItemLi){

        infografisItemList=new ArrayList<>(infografisItemList);
        for(InfografisItem puIt:infografisItemLi){
            infografisItemList.add(puIt);
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
                String filename=infografisItemList.get(position).getTitle()+".pdf";
                downloadfile(filename.replaceAll("[\\\\/:*?\"<>|]", " "),infografisItemList.get(position).getDl());
            }
        }else{
            //System OS is less than marsmallow, perform download
            String filename=infografisItemList.get(position).getTitle()+".pdf";
            downloadfile(filename.replaceAll("[\\\\/:*?\"<>|]", " "),infografisItemList.get(position).getDl());
        }
    }


    public void downloadfile(String filename, String pdf) {
        //Create Download Request
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(pdf));

        //Create Download Request
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE);

        request.setTitle(filename.replaceAll("[\\\\/:*?\"<>|]", " "));//Set Title in Download Notification
        request.setDescription("Download Publikasi . .");//Set Description in Download Notification

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename);

        //get download service and enque file
        DownloadManager manager =(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

        mainActivity.getTvDownloadStatusHeading().setText("Publikasi Telah Didownload");
        mainActivity.getDownloadStatus().show();
       
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

        return infografisItemList.size();
    }

    public class InfografisViewHolderAdapter extends RecyclerView.ViewHolder {
        TextView judulPublikasi, tanggalRilis, ukuranFile;
        ImageView cover;
        Button bttUnduhPub, bttDetailPub;

        public InfografisViewHolderAdapter(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    //viewActionClicklistener(position);
                }
            });
            judulPublikasi=(TextView)itemView.findViewById(R.id.judulPublikasi);
            tanggalRilis=(TextView)itemView.findViewById(R.id.tanggalRilis);
            ukuranFile=(TextView)itemView.findViewById(R.id.ukuranFile);
            cover=(ImageView)itemView.findViewById(R.id.cover);
            bttDetailPub=(Button)itemView.findViewById(R.id.bttDetailPub);
            bttUnduhPub=(Button)itemView.findViewById(R.id.bttUnduhPub);
        }
    }

    public void supportViewDialogGone(){
        progressBar.setVisibility(View.VISIBLE);
        detailAbstract.setText("");
        detailIssn.setText("");
        detailJudulPublikasi.setText("");
        detailRl_date.setText("");
        detailSize.setText("");
        detailUpdt_date.setText("");
        detailDownloadbutton.setVisibility(View.GONE);
        detailCover.setVisibility(View.GONE);
        detailCover.setImageResource(0);
    }

    public void supportViewDialogView(){
        progressBar.setVisibility(View.GONE);
        detailDownloadbutton.setVisibility(View.VISIBLE);
        detailCover.setVisibility(View.VISIBLE);
    }
}
