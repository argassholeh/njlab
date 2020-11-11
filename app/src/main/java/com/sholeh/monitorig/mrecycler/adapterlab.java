package com.sholeh.monitorig.mrecycler;

import android.content.Context;
import android.content.Intent;
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
import com.sholeh.monitorig.DetailLab;
import com.sholeh.monitorig.R;
import com.sholeh.monitorig.config.koneksi;

import java.util.ArrayList;

public class adapterlab extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ModelLab> dataList;
    public adapterlab(ArrayList<ModelLab> dataList){
        this.dataList = dataList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflat = LayoutInflater.from(parent.getContext());
        v = inflat.inflate(R.layout.custom_lab,parent,false);
        return new MyHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final MyHolder myHolder = (MyHolder) holder;
        Glide.with(myHolder.context).load(koneksi.tampilFotoLab+dataList.get(position).getImage()).apply(new RequestOptions().placeholder(R.drawable.no_image).centerCrop()).into(myHolder.imgLab);
        myHolder.tvNamaLab.setText(dataList.get(position).getNamalab());

        myHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(, "", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(myHolder.context, DetailLab.class);
                i.putExtra("id lab", dataList.get(position).getIdlab());
                i.putExtra("foto_lab", koneksi.tampilFotoLab+dataList.get(position).getImage());

                myHolder.context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView tvNamaLab;
        ImageView imgLab;
        CardView card;
        Context context;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            card = itemView.findViewById(R.id.cardlab);
            tvNamaLab = itemView.findViewById(R.id.tvRNamaLab);
            imgLab = itemView.findViewById(R.id.imgRLab);
        }
    }
}
