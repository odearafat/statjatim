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
import com.example.bottommenu.model.DataStatic;
import com.example.bottommenu.model.DataStaticItem;

import java.util.ArrayList;
import java.util.List;

public class DataStaticAdapter extends RecyclerView.Adapter<DataStaticAdapter.DataStaticViewHolderAdapter> {


    Context context;
    private List<DataStaticItem> DataStaticItemList;
    DownloadFile downloadFile;
    public DataStaticAdapter(Context ct, List<DataStaticItem> DataStaticLists) {
        this.DataStaticItemList=DataStaticLists;
        context=ct;
        downloadFile=new DownloadFile(ct);

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
        Button downloadDataStatic;
        boolean active=false;

        public DataStaticViewHolderAdapter(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Test Click", Toast.LENGTH_SHORT).show();
                }
            });
            judulDataStatic=(TextView)itemView.findViewById(R.id.judulDataStatic);
            tglDataStatic=(TextView)itemView.findViewById(R.id.tglDataStatic);
            sizeDataStatic=(TextView)itemView.findViewById(R.id.sizeDataStatic);
            downloadDataStatic=(Button) itemView.findViewById(R.id.bttUnduhDataStatic);
        }
    }
}
