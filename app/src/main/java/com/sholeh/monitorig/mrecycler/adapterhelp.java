package com.sholeh.monitorig.mrecycler;

import android.app.Dialog;
import android.content.Context;
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
import com.sholeh.monitorig.Preferences;
import com.sholeh.monitorig.R;
import com.sholeh.monitorig.config.koneksi;

import java.util.ArrayList;
import java.util.HashMap;

public class adapterhelp extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Preferences preferences;
    ImageView imgpop;
    Context context;
    Dialog myDialog;
    ArrayList<HashMap<String, String>> tampil = new ArrayList<HashMap<String, String>>();

    private ArrayList<ModelHelp> dataList;

    public adapterhelp(Context context, ArrayList<ModelHelp> dataList) {
        this.dataList = dataList;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflat = LayoutInflater.from(parent.getContext());
        v = inflat.inflate(R.layout.custom_kontak, parent, false);
        return new adapterhelp.MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final adapterhelp.MyHolder myHolder = (adapterhelp.MyHolder) holder;
        myDialog = new Dialog(context);

        Glide.with(myHolder.context).load(koneksi.tampilFotoP + dataList.get(position).getFotopop()).apply(new RequestOptions().placeholder(R.drawable.profile).centerCrop()).into(myHolder.foto);
        myHolder.tvnama.setText(dataList.get(position).getNama());
        myHolder.tvkontak.setText(dataList.get(position).getNohp());
        myHolder.tvstatus.setText(dataList.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private class MyHolder extends RecyclerView.ViewHolder {

        TextView tvnama, tvkontak, tvstatus;
        ImageView foto;
        CardView card;
        Context context;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            card = itemView.findViewById(R.id.cardhelp);
            tvnama = itemView.findViewById(R.id.tv_namahelp);
            tvkontak = itemView.findViewById(R.id.tv_hphelp);
            tvstatus = itemView.findViewById(R.id.tv_statushelp);
            foto = itemView.findViewById(R.id.imghelp);

        }
    }


}