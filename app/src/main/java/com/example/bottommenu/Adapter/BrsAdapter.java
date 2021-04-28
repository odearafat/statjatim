package com.example.bottommenu.Adapter;


import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottommenu.R;
import com.example.bottommenu.activity.MainActivity;
import com.example.bottommenu.interfacePackage.BRSHolderApi;
import com.example.bottommenu.interfacePackage.BeritaHolderApi;
import com.example.bottommenu.model.BeritaDetail;
import com.example.bottommenu.model.BrsDetail;
import com.example.bottommenu.model.BrsItem;
import com.example.bottommenu.model.PublikasiItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BrsAdapter extends RecyclerView.Adapter<BrsAdapter.BrsViewHolderAdapter> {


    Context context;
    private List<BrsItem> BrsItemList;
    DownloadFile downloadFile;

    Dialog dialogDetailBrs;
    BRSHolderApi brsHolderApi;
    MainActivity mainActivity;
    ProgressBar progressBar;
    RelativeLayout detailtitik;

    TextView detailJudulBrs, detailBrsDate, detailBrsSize, detailBrsUlasan;
    Button bttDownloadInDialog;

    public int positionView=0;

    public BrsAdapter(Context ct, List<BrsItem> BrsItemLists, BRSHolderApi brsHolderApi,
                      Dialog dialogDetailBrs, MainActivity mainActivity) {
        mainActivity.getInternetConnectionCheck().isConnected();

        this.BrsItemList=BrsItemLists;
        context=ct;
        downloadFile=new DownloadFile(ct,mainActivity, "BRS");
        this.mainActivity=mainActivity;

        this.dialogDetailBrs=dialogDetailBrs;
        this.dialogDetailBrs.setContentView(R.layout.detail_brs);

        this.brsHolderApi=brsHolderApi;

        this.detailJudulBrs=dialogDetailBrs.findViewById(R.id.detailJudulBrs);
        this.detailBrsDate=dialogDetailBrs.findViewById(R.id.detailTanggalRilisBrs);
        this.detailBrsSize=dialogDetailBrs.findViewById(R.id.detailBrsSize);
        this.detailBrsUlasan=dialogDetailBrs.findViewById(R.id.detailBrsUlasan);
        this.bttDownloadInDialog=dialogDetailBrs.findViewById(R.id.bttDownloadInDialog);
        this.progressBar=dialogDetailBrs.findViewById(R.id.progressBar);
        this.detailtitik=dialogDetailBrs.findViewById(R.id.detailtitik);
        //this.containerContent=dialogDetailBrs.findViewById(R.id.container_content);
    }

    @NonNull
    @Override
    public BrsViewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.fragment_brs_list_data,parent,false);
        return new BrsViewHolderAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BrsViewHolderAdapter holder, final int position) {
        holder.judulBrs.setText(BrsItemList.get(position).getTitle());
        holder.subjectBrs.setText(BrsItemList.get(position).getSubj());
        holder.tglBrs.setText(BrsItemList.get(position).getRl_date());
        holder.sizeBrs.setText(BrsItemList.get(position).getSize());
        holder.downloadBrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile.actionDownload(BrsItemList.get(position).getSubj()+
                        " - "+BrsItemList.get(position).getRl_date(),
                        BrsItemList.get(position).getPdf());
            }
        });

        holder.bttDetailBrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOnclickListenerBrs(position);
            }
        });
    }

    //public void set Change add BrsItemList
    public void addBrsItemList(List<BrsItem> BrsItemLi){

        BrsItemList=new ArrayList<>(BrsItemList);
        for(BrsItem puIt:BrsItemLi){
            BrsItemList.add(puIt);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return BrsItemList.size();
    }

    public class BrsViewHolderAdapter extends RecyclerView.ViewHolder {
        TextView judulBrs, tglBrs, subjectBrs, sizeBrs, ulasanBrs;
        Button downloadBrs, bttDetailBrs;
        boolean active=false;

        public BrsViewHolderAdapter(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    viewOnclickListenerBrs(position);
                }
            });

            judulBrs=(TextView)itemView.findViewById(R.id.judulBrs);
            tglBrs=(TextView)itemView.findViewById(R.id.tglBrs);
            subjectBrs=(TextView)itemView.findViewById(R.id.subjectBrs);
            sizeBrs=(TextView)itemView.findViewById(R.id.sizeBrs);
            ulasanBrs=(TextView)itemView.findViewById(R.id.ulasanBrs);
            bttDetailBrs=(Button) itemView.findViewById(R.id.bttDetailBrs);
            downloadBrs=(Button) itemView.findViewById(R.id.bttUnduhBrs);

        }
    }

    public void viewOnclickListenerBrs(int position){

        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://webapi.bps.go.id/v1/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        BRSHolderApi jsonPlaceHolderApi=retrofit.create(BRSHolderApi.class);

        int idBrs=BrsItemList.get(position).getBrs_id();
        positionView=position;


        supportViewDialogGone();

        Call<BrsDetail> call=jsonPlaceHolderApi.getView(
                "pressrelease", mainActivity.getIdWilayah(), "2ad01e6a21b015ea1ff8805ced02600c"
                ,idBrs, mainActivity.getBahasa()
        );

        //Callback
        call.enqueue(new Callback<BrsDetail>() {
            @RequiresApi(api = Build.VERSION_CODES.N)

            @Override
            public void onResponse(Call<BrsDetail> call, Response<BrsDetail> response) {

                if(!response.isSuccessful()){
                    String getResult="Code : "+response.code();
                    return;
                }
                BrsDetail brsDetail=response.body();

                detailJudulBrs.setText(brsDetail.getData().getTitleBrs());
                detailBrsDate.setText(brsDetail.getData().getRl_date());
                detailBrsSize.setText(brsDetail.getData().getSizeBrs());

                String a= Html.fromHtml(brsDetail.getData().getAbstract_brs(),Html.FROM_HTML_MODE_COMPACT).toString();
                detailBrsUlasan.setText(Html.fromHtml(a));
                detailBrsUlasan.setVisibility(View.VISIBLE);

                bttDownloadInDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadFile.actionDownload(BrsItemList.get(positionView).getSubj()+
                                        " - "+BrsItemList.get(positionView).getRl_date(),
                                BrsItemList.get(positionView).getPdf());

                    }
                });

                supportViewDialogView();
                //containerContent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<BrsDetail> call, Throwable t) {
                //getResult="Gagal";
                System.out.println("gagal");
                Log.d("I","Message"+t.getMessage());
            }
        });

        dialogDetailBrs.show();
    }

    public void supportViewDialogGone(){
        progressBar.setVisibility(View.VISIBLE);
        detailJudulBrs.setText("");
        detailBrsUlasan.setText("");
        detailBrsSize.setText("");
        detailBrsDate.setText("");
        bttDownloadInDialog.setVisibility(View.GONE);
        detailtitik.setVisibility(View.GONE);
    }

    public void supportViewDialogView(){
        progressBar.setVisibility(View.GONE);
        detailtitik.setVisibility(View.VISIBLE);
        bttDownloadInDialog.setVisibility(View.VISIBLE);
    }
}
