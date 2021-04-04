package com.example.bottommenu.Adapter;


import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottommenu.R;
import com.example.bottommenu.interfacePackage.BeritaHolderApi;
import com.example.bottommenu.model.Berita;
import com.example.bottommenu.model.BeritaDetail;
import com.example.bottommenu.model.BeritaItem;
import com.example.bottommenu.model.BeritaItemDetail;
import com.example.bottommenu.model.Page;
import com.example.bottommenu.model.PublikasiItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.BeritaViewHolderAdapter> {

    List<BeritaItem> beritaItems;
    Context context;
    BeritaHolderApi beritaHolderApi;

    public BeritaAdapter(Context ct, List<BeritaItem> beritaItem, BeritaHolderApi beritaHolderApis) {
        this.beritaItems=beritaItem;
        context=ct;
        this.beritaHolderApi=beritaHolderApis;
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
        holder.fotoDokumentasi.setVisibility(View.GONE);
        holder.arrow.setBackgroundResource(R.drawable.ic_row_down);

        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.active){
                    //Perform view Detail Berita
                    holder.fotoDokumentasi.setVisibility(View.VISIBLE);

                    getViewData(beritaItems.get(position).getNews_id(), holder.progressBar, holder.ulasanSingkat,
                            holder.fotoDokumentasi );


                    holder.arrow.setBackgroundResource(R.drawable.ic_row_up);
                    holder.active=true;
                }else{
                    holder.fotoDokumentasi.setVisibility(View.GONE);
                    holder.ulasanSingkat.setText(beritaItems.get(position).getNews());
                    //holder.ulasanSingkat.loadDataWithBaseURL(null, beritaItems.get(position).getNews(), "text/html", "utf-8", null);
                    holder.arrow.setBackgroundResource(R.drawable.ic_row_down);
                    holder.active=false;
                }

            }
        });

    }

    @Override
    public int getItemCount() {

        return beritaItems.size();
    }

    public class BeritaViewHolderAdapter extends RecyclerView.ViewHolder {
        TextView judulBerita, tglKegiatan, jenisKegiatan, ulasanSingkat;
        ImageView fotoDokumentasi;
        Button arrow;
        ProgressBar progressBar;
        boolean active=false;

        public BeritaViewHolderAdapter(@NonNull View itemView) {
            super(itemView);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(v.getContext(), "Test Click", Toast.LENGTH_SHORT).show();
//                }
//            });
            judulBerita=(TextView)itemView.findViewById(R.id.judulBerita);
            tglKegiatan=(TextView)itemView.findViewById(R.id.tglKegiatan);
            jenisKegiatan=(TextView)itemView.findViewById(R.id.jenisKegiatan);
            //ulasanSingkat=(WebView)itemView.findViewById(R.id.ulasanSingkatBerita);
            ulasanSingkat=(TextView)itemView.findViewById(R.id.ulasanSingkatBerita);
            fotoDokumentasi=(ImageView) itemView.findViewById(R.id.dokumentasiFoto);
            arrow=(Button) itemView.findViewById(R.id.arrowBerita);
            progressBar=(ProgressBar)itemView.findViewById(R.id.progressBarItemBerita);

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
                "news", "3500", "2ad01e6a21b015ea1ff8805ced02600c", idBerita,
                "ind"
        );

        //progressbar muncul
        progressBar.setVisibility(View.VISIBLE);

        //Callback
        call.enqueue(new Callback<BeritaDetail>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            String getResult = "Null";

            @Override
            public void onResponse(Call<BeritaDetail> call, Response<BeritaDetail> response) {

                System.out.println(response.toString());
                if(!response.isSuccessful()){
                    getResult ="Code : "+response.code();
                    return;
                }
                BeritaDetail berita=response.body();

                //Get Berita Item
                BeritaItemDetail beritaItemDetail=berita.getData();
                String htmlAsString =   " <html><![CDATA[\n" +
                        "        <h1>Main Title</h1>\n" +
                        "        <h2>A sub-title</h2>\n" +
                        "        <p>This is some html. Look, here\\'s an <u>underline</u>.</p>\n" +
                        "        <p>Look, this is <em>emphasized.</em> And here\\'s some <b>bold</b>.</p>\n" +
                        "        <p>This is a UL list:\n" +
                        "        <ul>\n" +
                        "        <li>One</li>\n" +
                        "        <li>Two</li>\n" +
                        "        <li>Three</li>\n" +
                        "        </ul>\n" +
                        "        <p>This is an OL list:\n" +
                        "        <ol>\n" +
                        "        <li>One</li>\n" +
                        "        <li>Two</li>\n" +
                        "        <li>Three</li>\n" +
                        "        </ol>\n" +
                        "        ]]></html>";    // used by WebView
                Spanned htmlAsSpanned = Html.fromHtml(htmlAsString); // used

                //ulasan.loadDataWithBaseURL(null, htmlAsString, "text/html", "utf-8", null);
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

}
