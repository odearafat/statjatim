package com.bps_jatim_3500.statistik_jatim.Adapter;


import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bps_jatim_3500.statistik_jatim.R;
import com.bps_jatim_3500.statistik_jatim.activity.MainActivity;
import com.bps_jatim_3500.statistik_jatim.interfacePackage.BeritaHolderApi;
import com.bps_jatim_3500.statistik_jatim.model.BeritaDetail;
import com.bps_jatim_3500.statistik_jatim.model.BeritaItem;
import com.bps_jatim_3500.statistik_jatim.model.BeritaItemDetail;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.BeritaViewHolderAdapter> {

    List<BeritaItem> beritaItems;
    Context context;
    BeritaHolderApi beritaHolderApi;
    Dialog dialogDetailBerita;
    TextView detailJudulBerita, detailBeritaDate, detailBeritaKatKegiatan, detailBeritaUlasan;
    ImageView fotoKegiatan;
    MainActivity mainActivity;
    RelativeLayout detailTitik;
    ScrollView scrollViewDetail;
    ProgressBar progressBar;

    public BeritaAdapter(Context ct, List<BeritaItem> beritaItem, BeritaHolderApi beritaHolderApis,
                         Dialog dialogDetailBerita, MainActivity mainActivity){
        mainActivity.getInternetConnectionCheck().isConnected();

        this.beritaItems=beritaItem;
        context=ct;
        this.beritaHolderApi=beritaHolderApis;
        this.mainActivity=mainActivity;

        this.dialogDetailBerita=dialogDetailBerita;
        this.dialogDetailBerita.setContentView(R.layout.detail_berita);

        this.detailJudulBerita=dialogDetailBerita.findViewById(R.id.detailJudulBerita);
        this.detailBeritaDate=dialogDetailBerita.findViewById(R.id.detailTanggalRilisBerita);
        this.detailBeritaKatKegiatan=dialogDetailBerita.findViewById(R.id.detailBeritaJenisKegiatan);
        this.detailBeritaUlasan=dialogDetailBerita.findViewById(R.id.detailBeritaUlasan);
        this.detailTitik=dialogDetailBerita.findViewById(R.id.detailtitik);
        this.scrollViewDetail=dialogDetailBerita.findViewById(R.id.scrollViewDetail);
        this.progressBar=dialogDetailBerita.findViewById(R.id.progressBar);
        this.fotoKegiatan=dialogDetailBerita.findViewById((R.id.fotoKegiatan));
    }

    @NonNull
    @Override
    public BeritaViewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.fragment_berita_list_data,parent,false);
        return new BeritaViewHolderAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BeritaViewHolderAdapter holder, final int position) {
        holder.judulBerita.setText(beritaItems.get(position).getTitle());
        holder.tglKegiatan.setText(beritaItems.get(position).getRl_date());
        holder.jenisKegiatan.setText(beritaItems.get(position).getNewscat_name());
        //holder.ulasanSingkat.loadDataWithBaseURL(null, beritaItems.get(position).getNews(), "text/html", "utf-8", null);
        holder.ulasanSingkat.setText(beritaItems.get(position).getNews());
        //holder.fotoDokumentasi.setVisibility(View.GONE);
        //holder.arrow.setBackgroundResource(R.drawable.ic_row_down);
    }

    @Override
    public int getItemCount() {

        return beritaItems.size();
    }

    public class BeritaViewHolderAdapter extends RecyclerView.ViewHolder {
        TextView judulBerita, tglKegiatan, jenisKegiatan, ulasanSingkat;
        ImageView fotoDokumentasi;
        RelativeLayout containerListBerita;
        //Button arrow;
        ProgressBar progressBar;
        //boolean active=false;

        public BeritaViewHolderAdapter(@NonNull final View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    //Toast.makeText(v.getContext(), position, Toast.LENGTH_SHORT).show();
                    supportViewDialogGone();

                    //JsonHolderInterfaceClass
                    Retrofit retrofit=new Retrofit.Builder().baseUrl("https://webapi.bps.go.id/v1/api/")
                            .addConverterFactory(GsonConverterFactory.create()).build();
                    BeritaHolderApi jsonPlaceHolderApi=retrofit.create(BeritaHolderApi.class);

                    int idPub=beritaItems.get(position).getNews_id();
                    //354475b2d04ee5be705892c01701899d
                    Call<BeritaDetail> call=jsonPlaceHolderApi.getView(
                            "news", mainActivity.getIdWilayah(), "2ad01e6a21b015ea1ff8805ced02600c"
                            ,idPub, mainActivity.getBahasa()
                    );

                    //Callback
                    call.enqueue(new Callback<BeritaDetail>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)

                        @Override
                        public void onResponse(Call<BeritaDetail> call, Response<BeritaDetail> response) {

                            if(!response.isSuccessful()){
                                String getResult="Code : "+response.code();
                                return;
                            }
                            BeritaDetail beritaDetail=response.body();

                            detailJudulBerita.setText(beritaDetail.getData().getTitle());
                            detailBeritaDate.setText(beritaDetail.getData().getRl_date());
                            detailBeritaKatKegiatan.setText(beritaDetail.getData().getNews_type());
                            String a=Html.fromHtml(beritaDetail.getData().getNews(),Html.FROM_HTML_MODE_COMPACT).toString();
                            detailBeritaUlasan.setText(Html.fromHtml(a));
                            detailBeritaUlasan.setVisibility(View.VISIBLE);
                            fotoKegiatan.setClipToOutline(true);
                            new LoadImage(fotoKegiatan).execute(beritaDetail.getData().getPicture());

                            supportViewDialogView();
                        }

                        @Override
                        public void onFailure(Call<BeritaDetail> call, Throwable t) {
                            //getResult="Gagal";
                            System.out.println("gagal");
                            Log.d("I","Message"+t.getMessage());
                        }
                    });

                    dialogDetailBerita.show();

                }
            });
            judulBerita=(TextView)itemView.findViewById(R.id.judulBerita);
            tglKegiatan=(TextView)itemView.findViewById(R.id.tglKegiatan);
            jenisKegiatan=(TextView)itemView.findViewById(R.id.jenisKegiatan);
            ulasanSingkat=(TextView)itemView.findViewById(R.id.ulasanSingkatBerita);
        }
    }

    //public void set Change add PublikasiItemList
    public void addBeritaItemList(List<BeritaItem> beritaItemLi){
        beritaItems=new ArrayList<>(beritaItems);
        for(BeritaItem berIt:beritaItemLi){
            beritaItems.add(berIt);
        }
        notifyDataSetChanged();
    }


    //Method for retrieve News and Picture
    public void getViewData(Integer idBerita, final ProgressBar progressBar, final TextView ulasan,
                            final ImageView foto){

        //JsonHolderInterfaceClass
        Call<BeritaDetail> call=beritaHolderApi.getView(
                "news", mainActivity.getIdWilayah(), "2ad01e6a21b015ea1ff8805ced02600c", idBerita,
                mainActivity.getBahasa()
        );

        //progressbar muncul
        progressBar.setVisibility(View.VISIBLE);

        //Callback
        call.enqueue(new Callback<BeritaDetail>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            String getResult = "Null";

            @Override
            public void onResponse(Call<BeritaDetail> call, Response<BeritaDetail> response) {

                //System.out.println(response.toString());
                if(!response.isSuccessful()){
                    getResult ="Code : "+response.code();
                    return;
                }
                BeritaDetail berita=response.body();

                //Get Berita Item
                BeritaItemDetail beritaItemDetail=berita.getData();

                ulasan.setText(beritaItemDetail.getNews());

                foto.setClipToOutline(true);
                new LoadImage(foto).execute(beritaItemDetail.getPicture());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<BeritaDetail> call, Throwable t) {
                getResult ="Gagal";
                System.out.println("masuk Gagal");
                Log.d("I","Message"+t.getMessage());
            }
        });
    }

    public void supportViewDialogGone(){
        progressBar.setVisibility(View.VISIBLE);
        detailJudulBerita.setText("");
        detailBeritaDate.setText("");
        detailBeritaKatKegiatan.setText("");
        detailBeritaUlasan.setText("");
        detailTitik.setVisibility(View.GONE);
        fotoKegiatan.setVisibility(View.GONE);
        fotoKegiatan.setImageResource(0);
        scrollViewDetail.setVisibility(View.GONE);
    }

    public void supportViewDialogView(){
        progressBar.setVisibility(View.GONE);
        detailTitik.setVisibility(View.VISIBLE);
        fotoKegiatan.setImageResource(0);
        fotoKegiatan.setVisibility(View.VISIBLE);
        scrollViewDetail.setVisibility(View.VISIBLE);
    }

}
