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
import com.example.bottommenu.fragmen.HomeFragment;
import com.example.bottommenu.model.IndikatorStrategisItem;

import java.util.List;
import java.util.zip.Inflater;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolderAdapter> {

    List<IndikatorStrategisItem> indikatorStrategisItems;
    Context context;

    public HomeAdapter(Context ct, List<IndikatorStrategisItem> indikatorStrategisItemss) {
        this.indikatorStrategisItems=indikatorStrategisItemss;
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
        holder.indikator.setText(indikatorStrategisItems.get(position).getTitle());
        holder.satuan.setText(indikatorStrategisItems.get(position).getUnit());
        holder.nilai.setText(String.format("%.2f",indikatorStrategisItems.get(position).getNilai()));
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

//            itemView.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    Toast.makeText(v.getContext(), "Test Click", Toast.LENGTH_SHORT).show();
////                }
////            });
            indikator=(TextView)itemView.findViewById(R.id.nmData);
            description=(TextView)itemView.findViewById(R.id.description);
            nilai=(TextView)itemView.findViewById(R.id.value);
            satuan=(TextView)itemView.findViewById(R.id.satuan);
            line=(RelativeLayout)itemView.findViewById(R.id.line);
            arrow=(Button)itemView.findViewById(R.id.arrow);
        }
    }
}
