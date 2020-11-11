package com.sholeh.monitorig.mrecycler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sholeh.monitorig.DetailKeluhan;
import com.sholeh.monitorig.Preferences;
import com.sholeh.monitorig.R;
import com.sholeh.monitorig.config.koneksi;

import java.util.ArrayList;

public class adapterk extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Preferences preferences;
    Context context;

    private ArrayList<ModelK> dataList;
    public adapterk(Context context,ArrayList<ModelK> dataList){
        this.dataList = dataList;
        this.context = context;
    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflat = LayoutInflater.from(parent.getContext());
        v = inflat.inflate(R.layout.custom_keluhan,parent,false);
        return new adapterk.MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,final int position) {
        final adapterk.MyHolder myHolder = (adapterk.MyHolder) holder;

        preferences = new Preferences(context);
        preferences.getSPStatus();
        Glide.with(myHolder.context).load(koneksi.tampilFotoP+dataList.get(position).getFoto()).apply(new RequestOptions().placeholder(R.drawable.profile).centerCrop()).into(myHolder.foto);
        myHolder.tvIduser.setText(dataList.get(position).getIduser());
        myHolder.tvNamauser.setText(dataList.get(position).getNamauser());
        myHolder.tvNamaLab.setText(dataList.get(position).getNamalab());
        myHolder.tvNamabarang.setText(dataList.get(position).getNamabarang());
        myHolder.tvTanggal.setText(dataList.get(position).getTanggal());
        myHolder.tvStatus.setText(dataList.get(position).getStatus_barang());

        if(preferences.getSPStatus().equalsIgnoreCase("asisten lab")){
            myHolder.tvNamauser.setVisibility(View.GONE);
            myHolder.tvNamaLab.setTypeface(Typeface.DEFAULT_BOLD);
            Glide.with(myHolder.context).load(koneksi.tampilFotoTrouble+dataList.get(position).getFoto_kerusakan()).apply(new RequestOptions().placeholder(R.drawable.no_image).centerCrop()).into(myHolder.fotokerusakan);
            myHolder.foto.setVisibility(View.GONE);
            myHolder.fotokerusakan.setVisibility(View.VISIBLE);

        }

        myHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(myHolder.context, DetailKeluhan.class);
                i.putExtra("id keluhan", dataList.get(position).getIdkeluhan());
                i.putExtra("id maintenance", dataList.get(position).getIdmaintenance());
                myHolder.context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private class MyHolder extends RecyclerView.ViewHolder {

        TextView tvIduser, tvNamauser, tvNamaLab, tvNamabarang, tvTanggal, tvStatus;
        ImageView foto, fotokerusakan;
        CardView card;
        Context context;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            card = itemView.findViewById(R.id.cardp);
            tvIduser = itemView.findViewById(R.id.tv_iduserp);
            tvNamauser = itemView.findViewById(R.id.tv_namap);
            tvNamaLab = itemView.findViewById(R.id.tv_labp);
            tvNamabarang = itemView.findViewById(R.id.tv_barangp);
            tvTanggal = itemView.findViewById(R.id.tv_tanggalp);
            tvStatus = itemView.findViewById(R.id.tv_statusbarang);
            foto = itemView.findViewById(R.id.imgp);
            fotokerusakan = itemView.findViewById(R.id.imgkerusakan);


        }
    }
}
