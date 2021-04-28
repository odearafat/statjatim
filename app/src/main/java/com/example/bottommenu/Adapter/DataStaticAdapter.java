package com.example.bottommenu.Adapter;


import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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
import com.example.bottommenu.interfacePackage.DataStaticHolderApi;
import com.example.bottommenu.model.BrsDetail;
import com.example.bottommenu.model.BrsItem;
import com.example.bottommenu.model.DataStatic;
import com.example.bottommenu.model.DataStaticDetail;
import com.example.bottommenu.model.DataStaticItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataStaticAdapter extends RecyclerView.Adapter<DataStaticAdapter.DataStaticViewHolderAdapter> {


    Context context;
    private List<DataStaticItem> DataStaticItemList;
    DownloadFile downloadFile;

    TextView detailJudulStaticData, detailStaticDataRlDate,detailStaticDataSize;
    //        detailStaticDataHtml ;
    Button detailStaticTableDownloadButton;
    Integer positionView=0;
    WebView detailStaticDataHtml;
    MainActivity mainActivity;
    ProgressBar progressBar;
    RelativeLayout detailTitik;

    Dialog dialogDetailDataStatic;
    public DataStaticAdapter(Context ct, List<DataStaticItem> DataStaticLists, DataStaticHolderApi dataStaticHolderApi,
                             Dialog dialogDetailDataStatic, MainActivity mainActivity) {
        mainActivity.getInternetConnectionCheck().isConnected();

        this.DataStaticItemList=DataStaticLists;
        context=ct;
        downloadFile=new DownloadFile(ct);
        this.mainActivity=mainActivity;

        this.dialogDetailDataStatic=dialogDetailDataStatic;
        this.dialogDetailDataStatic.setContentView(R.layout.detail_static_data);

        this.detailJudulStaticData=dialogDetailDataStatic.findViewById(R.id.detailJudulData);
        this.detailStaticDataRlDate=dialogDetailDataStatic.findViewById(R.id.detailTanggalRilisData);
        this.detailStaticDataSize=dialogDetailDataStatic.findViewById(R.id.detailDataSize);
        this.detailTitik=dialogDetailDataStatic.findViewById(R.id.detailtitik);
        this.detailStaticDataHtml=dialogDetailDataStatic.findViewById(R.id.detailStaticDataHtml);
        this.detailStaticTableDownloadButton=dialogDetailDataStatic.findViewById(R.id.bttDownloadInDialog);
        this.progressBar=dialogDetailDataStatic.findViewById(R.id.progressBar);
    }

    @NonNull
    @Override
    public DataStaticViewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.fragment_data_static_list_item,parent,false);
        return new DataStaticViewHolderAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataStaticViewHolderAdapter holder, final int position) {
        holder.judulDataStatic.setText(DataStaticItemList.get(position).getTitle());
        //holder.subjectBrs.setText(DataStaticItemList.get(position).getSubj());
        if(DataStaticItemList.get(position).getUpdt_date()!=null){
            if(DataStaticItemList.get(position).getUpdt_date().isEmpty() ||
                    DataStaticItemList.get(position).getUpdt_date()==""||
                    DataStaticItemList.get(position).getUpdt_date().equals("")||
                    DataStaticItemList.get(position).getUpdt_date()==null){
                holder.tglDataStatic.setText(DataStaticItemList.get(position).getCr_date());
            }else{
                holder.tglDataStatic.setText(DataStaticItemList.get(position).getUpdt_date());
            }
        }

        holder.sizeDataStatic.setText(DataStaticItemList.get(position).getSize());
        holder.downloadDataStatic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //perform download
                downloadFile.actionDownloadExcel(DataStaticItemList.get(position).getTitle()+
                        " - "+DataStaticItemList.get(position).getUpdt_date(),
                        DataStaticItemList.get(position).getExcel());
            }
        });

        holder.viewDataStatic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                viewOnclickListenerDataStatic(position);
            }
        });
    }

    //public void set Change add BrsItemList
    public void addDataStaticItemList(List<DataStaticItem> DataStaticItemLi){

       DataStaticItemList=new ArrayList<>(DataStaticItemList);
        for(DataStaticItem puIt:DataStaticItemLi){
            DataStaticItemList.add(puIt);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        return DataStaticItemList.size();
    }

    public class DataStaticViewHolderAdapter extends RecyclerView.ViewHolder {
        TextView judulDataStatic, tglDataStatic, sizeDataStatic;
        Button downloadDataStatic, viewDataStatic;
        boolean active=false;

        public DataStaticViewHolderAdapter(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    viewOnclickListenerDataStatic(position);
                }
            });
            judulDataStatic=(TextView)itemView.findViewById(R.id.judulDataStatic);
            tglDataStatic=(TextView)itemView.findViewById(R.id.tglDataStatic);
            sizeDataStatic=(TextView)itemView.findViewById(R.id.sizeDataStatic);
            downloadDataStatic=(Button) itemView.findViewById(R.id.bttUnduhDataStatic);
            viewDataStatic=(Button) itemView.findViewById(R.id.bttViewDataStatic);
        }
    }

    public void viewOnclickListenerDataStatic(int position){
        supportViewDialogGone();
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://webapi.bps.go.id/v1/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        DataStaticHolderApi jsonPlaceHolderApi=retrofit.create(DataStaticHolderApi.class);

        int idTable=DataStaticItemList.get(position).getTable_id_id();
        positionView=position;

        Call<DataStaticDetail> call=jsonPlaceHolderApi.getView(
                "statictable", mainActivity.getIdWilayah(), "2ad01e6a21b015ea1ff8805ced02600c"
                ,idTable, mainActivity.getBahasa()
        );

        //Callback
        call.enqueue(new Callback<DataStaticDetail>() {
            @RequiresApi(api = Build.VERSION_CODES.N)

            @Override
            public void onResponse(Call<DataStaticDetail> call, Response<DataStaticDetail> response) {

                if(!response.isSuccessful()){
                    String getResult="Code : "+response.code();
                    return;
                }
                DataStaticDetail dataStaticDetail=response.body();

                detailJudulStaticData.setText(dataStaticDetail.getData().getTitle());
                detailStaticDataRlDate.setText(dataStaticDetail.getData().getCr_date());
                detailStaticDataSize.setText(dataStaticDetail.getData().getSize());

                String a= Html.fromHtml(dataStaticDetail.getData().getTable(),Html.FROM_HTML_MODE_COMPACT).toString();
                System.out.println("html :"+a);
                detailStaticDataHtml.loadDataWithBaseURL(null, a, "text/html", "utf-8", null);
                //detailStaticDataHtml.setVisibility(View.VISIBLE);

                detailStaticTableDownloadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //perform download
                        downloadFile.actionDownloadExcel(DataStaticItemList.get(positionView).getTitle()+
                                        " - "+DataStaticItemList.get(positionView).getUpdt_date(),
                                DataStaticItemList.get(positionView).getExcel());
                    }
                });
                supportViewDialogView();
            }

            @Override
            public void onFailure(Call<DataStaticDetail> call, Throwable t) {
                //getResult="Gagal";
                System.out.println("gagal");
                Log.d("I","Message"+t.getMessage());
            }
        });

        dialogDetailDataStatic.show();
    }

    public void supportViewDialogGone(){
        progressBar.setVisibility(View.VISIBLE);
        detailJudulStaticData.setText("");
        detailStaticDataRlDate.setText("");
        detailStaticDataSize.setText("");
        detailStaticDataHtml.setVisibility(View.GONE);
        detailStaticTableDownloadButton.setVisibility(View.GONE);
        detailTitik.setVisibility(View.GONE);
    }

    public void supportViewDialogView(){
        progressBar.setVisibility(View.GONE);
        detailStaticDataHtml.setVisibility(View.VISIBLE);
        detailStaticTableDownloadButton.setVisibility(View.VISIBLE);
        detailTitik.setVisibility(View.VISIBLE);
    }
}
