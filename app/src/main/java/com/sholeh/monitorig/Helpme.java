package com.sholeh.monitorig;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sholeh.monitorig.config.koneksi;
import com.sholeh.monitorig.mrecycler.KlikListener;
import com.sholeh.monitorig.mrecycler.ModelHelp;
import com.sholeh.monitorig.mrecycler.RecyclerClick;
import com.sholeh.monitorig.mrecycler.adapterhelp;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.sholeh.monitorig.config.koneksi.tampilhelp;

public class Helpme extends AppCompatActivity {
    Toolbar toolBarisi;
    AlertDialog dialog;
    ArrayList<ModelHelp> daftar_data = new ArrayList<ModelHelp>();
    adapterhelp adapterhelp;
    RecyclerView recycler;

    private KProgressHUD progressDialogHud;
    LinearLayout lnKosong;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpme);

        toolBarisi =  findViewById(R.id.toolbar);
        toolBarisi.setTitle("Help");
        setSupportActionBar(toolBarisi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recycler= findViewById(R.id.rvhelp);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        progressDialogHud = KProgressHUD.create(Helpme.this);
        lnKosong = findViewById(R.id.lnKosong);


        recycler.addOnItemTouchListener(new RecyclerClick(this, recycler, new KlikListener() {
            @Override
            public void onClick(View view, int position) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Helpme.this);
                LayoutInflater inflater = LayoutInflater.from(Helpme.this);
                final View dialogView = inflater.inflate(R.layout.custompopup, null);
                builder.setView(dialogView);

                TextView tvxnama = dialogView.findViewById(R.id.tv_popnama);
                final TextView tvxNohp = dialogView.findViewById(R.id.tv_pophp);
                TextView tvxStatus = dialogView.findViewById(R.id.tv_popStatus);
                TextView tvxclose = dialogView.findViewById(R.id.txtclose);


                ImageView imgtelp = dialogView.findViewById(R.id.imgTelp);
                ImageView imgpesan = dialogView.findViewById(R.id.imgPesan);
                ImageView imgwa = dialogView.findViewById(R.id.imgWA);
                ImageView imgprofil = dialogView.findViewById(R.id.img_userpop);

                tvxnama.setText(daftar_data.get(position).getNama());
                tvxNohp.setText(daftar_data.get(position).getNohp());
                tvxStatus.setText(daftar_data.get(position).getStatus());

                GlideApp.with(inflater.getContext())
                        .load(koneksi.tampilFotoP+daftar_data.get(position).getFotopop())
                        .placeholder(R.drawable.ic_orang_grey_24dp)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgprofil);
                imgtelp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tvxNohp.getText().toString())));
                    }
                });

                imgpesan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("android.intent.action.VIEW");
                        Uri data = Uri.parse("sms:"+tvxNohp.getText().toString());
                        intent.setData(data);
                        startActivity(intent);
                    }
                });

                imgwa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "https://api.whatsapp.com/send?phone="+tvxNohp.getText().toString();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });

                tvxclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

//                builder.setCancelable(false);
//                builder.setNegativeButton("TUTUP", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });






                dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        }
    @Override
    public void onResume()
    {
        super.onResume();
        tampildata();
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish(); //  kalau tanpa ini tombol panahnya tak berfungsi
        return true;
    }
    private void ProgresDialog(){
        progressDialogHud.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Memuat Data...")
                .setCancellable(false);
        progressDialogHud.show();
    }

    private void tampildata() {
        ProgresDialog();
        String url = tampilhelp;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        daftar_data.clear();
                        if(response.contains("1")) {
                            try {
                                JSONObject jsonObject;
                                jsonObject = new JSONObject(response);
                                JSONArray result = jsonObject.getJSONArray("Hasil");
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject c = result.getJSONObject(i);
                                    String nama = c.getString("nama");
                                    String nohp = c.getString("no_hp");
                                    String status = c.getString("nama_status");
                                    String foto = c.getString("foto");
                                    String statushelp;
                                    if (status.equalsIgnoreCase("admin")){
                                        statushelp = "Kepala Lab";
                                    }else{
                                        statushelp= "Asisten Lab";
                                    }
                                    daftar_data.add(new ModelHelp(nama, nohp,statushelp, foto));

                                }
                            } catch (JSONException e) {
                                recycler.setVisibility(View.GONE);
                                lnKosong.setVisibility(View.VISIBLE);
                                progressDialogHud.dismiss();
                            }

                        }else{
                            recycler.setVisibility(View.GONE);
                            lnKosong.setVisibility(View.VISIBLE);
                            progressDialogHud.dismiss();
                        }
                        // di taruh sini supaya ketika data tidak ada langsung tereload
                        adapterhelp= new adapterhelp(Helpme.this,daftar_data);
                        recycler.setAdapter(adapterhelp);
                        recycler.setVisibility(View.VISIBLE);
                        lnKosong.setVisibility(View.GONE);
                        progressDialogHud.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        recycler.setVisibility(View.GONE);
                        lnKosong.setVisibility(View.VISIBLE);
                        progressDialogHud.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
