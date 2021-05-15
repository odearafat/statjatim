package com.bps_jatim_3500.statistik_jatim.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bps_jatim_3500.statistik_jatim.R;
import com.bps_jatim_3500.statistik_jatim.activity.MainActivity;
import com.bps_jatim_3500.statistik_jatim.fragmen.DataListFragment;
import com.bps_jatim_3500.statistik_jatim.model.SubjectItem;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.BrsViewHolderAdapter> {


    Context context;
    private List<SubjectItem> SubjectItemList;
    MainActivity mainActivity;
    FragmentManager fm;

    public SubjectAdapter(Context ct, List<SubjectItem> SubjectItems, FragmentManager fm, MainActivity mainActivity) {

        this.SubjectItemList=SubjectItems;
        context=ct;
        this.fm=fm;
        this.mainActivity=mainActivity;
    }

    @NonNull
    @Override
    public BrsViewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mainActivity.getInternetConnectionCheck().isConnected();
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.fragment_data_subject_lis,parent,false);
        return new BrsViewHolderAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BrsViewHolderAdapter holder, final int position) {
        holder.nmSubject.setText(SubjectItemList.get(position).getTitle());
        if(SubjectItemList.get(position).getSubcat().equals("Ekonomi dan Perdagangan") ||
                SubjectItemList.get(position).getSubcat().equals("Economic and Trade")  ){
            holder.containerIconSubject.setBackgroundResource(R.drawable.bg_circle_orange_small);
            holder.nmSubject.setTextColor(ContextCompat.getColor(context,
                    R.color.orange));
        }else if(SubjectItemList.get(position).getSubcat().equals("Pertanian dan Pertambangan") ||
                SubjectItemList.get(position).getSubcat().equals("Agriculture and Mining")){
            holder.containerIconSubject.setBackgroundResource(R.drawable.bg_circle_green_small);
            holder.nmSubject.setTextColor(ContextCompat.getColor(context,
                    R.color.green));
        }else{
            holder.containerIconSubject.setBackgroundResource(R.drawable.bg_circle_blue_small);
            holder.nmSubject.setTextColor(ContextCompat.getColor(context,
                    R.color.colorPrimaryDark));
        }

        switch (SubjectItemList.get(position).getSub_id()) {
            case 157:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_transportation_157);
                break;
            case 8:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_exim_8);
                break;
            case 7:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_energy_7);
                break;
            case 102:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_harga_eceran_102);
                break;
            case 20:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_ihpb_20);
                break;
            case 162:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_pdrb_kabkot_162);
                break;
            case 9:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_industry_9);
                break;
            case 13:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_keuangan_13);
                break;
            case 2:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_komunikasi_2);
                break;
            case 3:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_inflasi_3);
                break;
            case 25:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_itk_25);
                break;
            case 4:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_konstruksi_2);
                break;
            case 52:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_pdrb_kabkot_162);
                break;
            case 22:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_ntp_22);
                break;
            case 154:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_ntn_154);
                break;
            case 16:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_pariwisata_16);
                break;
            case 163:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_horti_163);
                break;
            case 60:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_kehutanan_60);
                break;
            case 56:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_perikanan_56);
                break;
            case 54:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_perkebunan_54);
                break;
            case 24:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_peternakan_24);
                break;
            case 53:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_tanpang_53);
                break;
            case 28:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_pendidikan_28);
                break;
            case 29:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_perumahan_29);
                break;
            case 34:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_polkam_34);
                break;
            case 158:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_potensi_desa_158);
                break;
            case 27:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_sosial_budaya_27);
                break;
            case 6:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_tenaga_kerja_6);
                break;
            case 40:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_gender_40);
                break;
            case 153:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_geografi_153);
                break;
            case 151:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_iklim_151);
                break;
            case 26:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_ipm_26);
                break;
            case 23:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_kemiskinan_23);
                break;
            case 12:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_kependudukan_12);
                break;
            case 30:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_kesehatan_30);
                break;
            case 5:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_konsumsi_5);
                break;
            case 152:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_lingkungan_hidup_152);
                break;
            case 101:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_data_pemerintahan_101);
                break;
            default:
                holder.imageButtonSubject.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                break;
        }

        holder.imageButtonSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AppCompatActivity activity=(AppCompatActivity) v.getContext();

                //Intent i= new Intent(v.getContext(),DataListFragment.class);
                //i.putExtra("idSubject", SubjectItemList.get(position).getSub_id());

                //System.out.println("id="+SubjectItemList.get(position).getSub_id());

                //activity.getSupportFragmentManager()
                //        .beginTransaction()
                fm.beginTransaction().addToBackStack(null)
                        .replace(R.id.fragmen_container,
                                new DataListFragment(SubjectItemList.get(position).getSub_id(),
                                        SubjectItemList.get(position).getTitle(), mainActivity)).commit();
                //activity.startActivity(i);
            }
        });

    }

    //public void set Change add BrsItemList
    public void addBrsItemList(List<SubjectItem> BrsItemLi){


    }

    @Override
    public int getItemCount() {
        return SubjectItemList.size();
    }

    public class BrsViewHolderAdapter extends RecyclerView.ViewHolder {
        TextView nmSubject;
        ImageButton imageButtonSubject;
        RelativeLayout containerIconSubject;
        public BrsViewHolderAdapter(@NonNull View itemView) {
            super(itemView);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(v.getContext(), "Test Click", Toast.LENGTH_SHORT).show();
//                }
//            });
            nmSubject=(TextView)itemView.findViewById(R.id.nmSubject);
            imageButtonSubject=(ImageButton) itemView.findViewById(R.id.iconData);
            containerIconSubject=(RelativeLayout) itemView.findViewById(R.id.ContainerIconData);
        }
    }
}
