package com.example.bottommenu.Adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.example.bottommenu.R;
import com.example.bottommenu.activity.MainActivity;
import com.example.bottommenu.fragmen.ChatUsFragment;
import com.example.bottommenu.fragmen.DataStrategisFragment;
import com.example.bottommenu.fragmen.HomeFragment;
import com.example.bottommenu.model.IndikatorStrategis;
import com.example.bottommenu.model.IndikatorStrategisItem;
import com.example.bottommenu.model.PublikasiItem;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolderAdapter> {

    List<IndikatorStrategisItem> indikatorStrategisItems;
    Context context;
    MainActivity mainActivity;

    public HomeAdapter(Context ct, List<IndikatorStrategisItem> indikatorStrategisItemss,
                       MainActivity mainActivity) {
        mainActivity.getInternetConnectionCheck().isConnected();

        this.indikatorStrategisItems=indikatorStrategisItemss;
        this.mainActivity=mainActivity;
        context=ct;
    }

    @NonNull
    @Override
    public HomeViewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.fragment_home_list_data,parent,false);
        return new HomeViewHolderAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeViewHolderAdapter holder, final int position) {
        Double valueData=(Double) indikatorStrategisItems.get(position).getNilai();
        //DecimalFormat valueDataFormat = new DecimalFormat("###0,###");

        NumberFormat numberFormatter = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        numberFormatter.setMaximumFractionDigits(2);
        numberFormatter.setMinimumFractionDigits(2);

        holder.indikator.setText(indikatorStrategisItems.get(position).getTitle());
        holder.satuan.setText(indikatorStrategisItems.get(position).getUnit());
        holder.nilai.setText(numberFormatter.format(valueData).replace("Rp","").replace(",00",""));
        holder.description.setText(indikatorStrategisItems.get(position).getDesc());
        holder.line.setVisibility(View.GONE);
        holder.description.setVisibility(View.GONE);
        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.active){
                    System.out.println("des-"+indikatorStrategisItems.get(position).getDesc()+"-");
                    if(!holder.description.getText().toString().trim().isEmpty()){
                        holder.description.setVisibility(View.VISIBLE);
                        holder.line.setVisibility(View.VISIBLE);
                    }
                    holder.arrow.setBackgroundResource(R.drawable.ic_row_up);
                    holder.active=true;
                }else{
                    holder.description.setVisibility(View.GONE);
                    holder.line.setVisibility(View.GONE);
                    holder.arrow.setBackgroundResource(R.drawable.ic_row_down);
                    holder.active=false;
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return indikatorStrategisItems.size();
    }

    public class HomeViewHolderAdapter extends RecyclerView.ViewHolder {
        TextView indikator, description, nilai, satuan;
        RelativeLayout line;
        Button arrow;
        boolean active=false;
        //ImageView logoIndikator;

        public HomeViewHolderAdapter(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();

                    mainActivity.getFm().beginTransaction().addToBackStack(null)
                            .replace(R.id.fragmen_container,
                                    new DataStrategisFragment(indikatorStrategisItems.get(position),
                                            mainActivity)).commit();
                    //Toast.makeText(v.getContext(), "Test Click", Toast.LENGTH_SHORT).show();
                }
            });

            indikator=itemView.findViewById(R.id.nmData);
            description=itemView.findViewById(R.id.description);
            nilai=itemView.findViewById(R.id.value);
            satuan=itemView.findViewById(R.id.satuan);
            line=itemView.findViewById(R.id.line);
            arrow=itemView.findViewById(R.id.arrow);
        }
    }
    //public void set Change add PublikasiItemList
    public void addPublikasiItemList(List<IndikatorStrategisItem> indikatorStrategisItemLi){

        indikatorStrategisItems=new ArrayList<>(indikatorStrategisItems);
        for(IndikatorStrategisItem puIt:indikatorStrategisItemLi){
            indikatorStrategisItems.add(puIt);
        }
        notifyDataSetChanged();
    }
}
