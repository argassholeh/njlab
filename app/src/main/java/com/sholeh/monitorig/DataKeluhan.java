package com.sholeh.monitorig;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sholeh.monitorig.mrecycler.ModelK;
import com.sholeh.monitorig.mrecycler.adapterk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.sholeh.monitorig.config.koneksi.tampilComplaint;

public class DataKeluhan extends AppCompatActivity {
    Spinner spin_tangalp;

    String[] tglpilih = {"Pilih"};

    Toolbar toolBarisi;
    ArrayList<ModelK> daftar_keluhan = new ArrayList<ModelK>();
    adapterk adapterkeluhan;
    RecyclerView recycler;
    View v;

    Preferences preferences;

    private KProgressHUD progressDialogHud;
    LinearLayout lnKosong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_keluhan);
        toolBarisi = findViewById(R.id.toolbar);
        toolBarisi.setTitle("Keluhan");
        setSupportActionBar(toolBarisi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialogHud = KProgressHUD.create(DataKeluhan.this);
        lnKosong = findViewById(R.id.lnKosong);


        recycler= findViewById(R.id.rvdatap);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager); // data 0 recycler masih tetap ada : https://stackoverflow.com/questions/32678632/is-there-a-callback-for-when-recyclerview-has-finished-showing-its-items-after-i/32679359

        preferences = new Preferences(this);
//        Toast.makeText(this, ""+preferences.getSPStatus(), Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onResume()
    {
        super.onResume();
        tampilComplaint();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void ProgresDialog(){
        progressDialogHud.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Memuat Data...")
                .setCancellable(false);
        progressDialogHud.show();
    }


    private void tampilComplaint() {
        ProgresDialog();
        String url = tampilComplaint ;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("hahaha", response);
                        daftar_keluhan.clear();
                        if(response.contains("1")) {
                            try {
                                JSONObject jsonObject;
                                jsonObject = new JSONObject(response);
                                JSONArray result = jsonObject.getJSONArray("Hasil");
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject c = result.getJSONObject(i);

                                    String idtrouble = c.getString("id_trouble");
                                    String idmaintenance = c.getString("id_maintenance");
                                    String iduser = c.getString("id_user");
                                    String nama = c.getString("nama");
                                    String namalab = c.getString("nama_lab");
                                    String namabarang = c.getString("nama_barang");
                                    String tanggal = c.getString("tanggal");
                                    String status = c.getString("status");
                                    String statusbarang;
                                    String foto = c.getString("foto");
                                    String fotokerusakan = c.getString("foto_kerusakan");
                                    if (status.equalsIgnoreCase("m")){
                                        statusbarang = "Sudah di perbaiki";
                                    }else{
                                        statusbarang = "Bermasalah";
                                    }
                                    daftar_keluhan.add(new ModelK(idtrouble,idmaintenance,iduser, nama, namalab,namabarang,tanggal,statusbarang, foto, fotokerusakan));

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
                        adapterkeluhan= new adapterk(DataKeluhan.this,daftar_keluhan);
                        recycler.setAdapter(adapterkeluhan);
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

    public void dialog() {
        AlertDialog.Builder build = new AlertDialog.Builder(DataKeluhan.this);
        build.setMessage("Laporan complaint tidak ada");
        build.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
//                Toast.makeText(KondisiBarang.this, "ok", Toast.LENGTH_SHORT).show();
            }
        });
        build.create().show();
    }


}
