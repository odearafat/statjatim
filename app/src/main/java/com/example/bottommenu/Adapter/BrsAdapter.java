package com.example.bottommenu.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottommenu.R;
import com.example.bottommenu.model.BrsItem;
import com.example.bottommenu.model.PublikasiItem;

import java.util.ArrayList;
import java.util.List;

public class BrsAdapter extends RecyclerView.Adapter<BrsAdapter.BrsViewHolderAdapter> {


    Context context;
    private List<BrsItem> BrsItemList;
    DownloadFile downloadFile;
    public BrsAdapter(Context ct, List<BrsItem> BrsItemLists) {
        this.BrsItemList=BrsItemLists;
        context=ct;
        downloadFile=new DownloadFile(ct);

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
                //perform download
                downloadFile.actionDownload(BrsItemList.get(position).getSubj()+
                        " - "+BrsItemList.get(position).getRl_date(),
                        BrsItemList.get(position).getPdf());
            }
        });

        holder.buttonArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.active){
                    //Perform view Detail Berita
                    holder.ulasanBrs.setVisibility(View.VISIBLE);
                    holder.buttonArrow.setBackgroundResource(R.drawable.ic_row_up);
                    holder.active=true;
//                    getViewData(beritaItems.get(position).getNews_id(), holder.progressBar, holder.ulasanSingkat,
//                            holder.fotoDokumentasi );


                }else{
                    holder.ulasanBrs.setVisibility(View.GONE);
                    holder.buttonArrow.setBackgroundResource(R.drawable.ic_row_down);
                    holder.active=false;
                }
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
        Button downloadBrs, buttonArrow;
        boolean active=false;

        public BrsViewHolderAdapter(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Test Click", Toast.LENGTH_SHORT).show();
                }
            });
            judulBrs=(TextView)itemView.findViewById(R.id.judulBrs);
            tglBrs=(TextView)itemView.findViewById(R.id.tglBrs);
            subjectBrs=(TextView)itemView.findViewById(R.id.subjectBrs);
            sizeBrs=(TextView)itemView.findViewById(R.id.sizeBrs);
            ulasanBrs=(TextView)itemView.findViewById(R.id.ulasanBrs);
            buttonArrow=(Button) itemView.findViewById(R.id.arrowBrs);
            downloadBrs=(Button) itemView.findViewById(R.id.bttUnduhBrs);
        }
    }
}
