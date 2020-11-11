package com.sholeh.monitorig;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sholeh.monitorig.config.koneksi;
import com.sholeh.monitorig.mrecycler.adapter.ExpandableListBarang;
import com.sholeh.monitorig.mrecycler.model.ChildModel;
import com.sholeh.monitorig.mrecycler.model.ParentModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailLab extends AppCompatActivity {


    ArrayList<HashMap<String, String>> tampil = new ArrayList<HashMap<String, String>>();
    private ImageView detailfotolab;
    GlidModuleMe moduleMe = new GlidModuleMe();
    String image;
    Toolbar toolBarisi;

    //Expandable RecyclerView in Android
    ExpandableListView expandableListView;
    List<ParentModel> listParen;
    HashMap<ParentModel, List<ChildModel>> listChild;
    ExpandableListBarang adapterList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lab);

        toolBarisi = findViewById(R.id.toolbar);
        toolBarisi.setTitle("Detail Lab");
        setSupportActionBar(toolBarisi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        detailfotolab = findViewById(R.id.image_detailfotolab);
        expandableListView = findViewById(R.id.exp_list);
        recyclerView = findViewById(R.id.myRecyclerViewp);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tampildetaillab(getIntent().getStringExtra("id lab"));
        tampil_data(getIntent().getStringExtra("id lab"));

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String data = listChild.get(listParen.get(groupPosition)).get(childPosition).getNama_barang();
//                Toast.makeText(getApplicationContext(), "Posisi "+childPosition,Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); //  kalau tanpa ini tombol panahnya tak berfungsi
        return true;
    }

    private void tampildetaillab(final String idlab) {
        image = getIntent().getStringExtra("foto_lab");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.tampilDetailLab,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")) {
                            try {
                                JSONObject jsonObject;
                                jsonObject = new JSONObject(response);
                                JSONArray result = jsonObject.getJSONArray("Hasil");
                                tampil.clear();
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject c = result.getJSONObject(i);
                                    String title = c.getString("nama_lab");
                                    toolBarisi.setTitle(title);
                                    Glide.with(getApplicationContext()).load(image).apply(new RequestOptions().placeholder(R.drawable.no_image)).into(detailfotolab);
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), "Data belum ada", Toast.LENGTH_SHORT).show();

                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Data belum ada", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_idlab, idlab);


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void tampil_data(final String idlab) {
        listParen = new ArrayList<ParentModel>();
        listChild = new HashMap<ParentModel, List<ChildModel>>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.tampilTotalbarang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int sukses;
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            sukses = jsonObject.getInt("success");
                            if (sukses==1){
                                JSONArray result = jsonObject.getJSONArray("Hasil");
                                listParen.clear();
                                listChild.clear();
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject c = result.getJSONObject(i);
                                    String namajenis = c.getString("nama_jenis");
                                    String total = c.getString("total");
                                    String baik = c.getString("baik");
                                    String rusak = c.getString("rusak");
                                    JSONArray barangrusak = c.getJSONArray("semua_barang");

                                    listParen.add(new ParentModel(namajenis,total,baik,rusak,barangrusak));
                                    Log.d("perke-"+i+"",namajenis);
                                    List<ChildModel> Child = new ArrayList<ChildModel>();
                                    for (int j = 0; j < barangrusak.length(); j++){
                                        JSONObject jsonObject1 = barangrusak.getJSONObject(j);
                                        String brg = jsonObject1.getString("barang");
                                        String brg_status = jsonObject1.getString("status_barang");
                                        Child.add(new ChildModel(brg, brg_status));
                                    }
                                    listChild.put(listParen.get(i),Child);
                                }
                                adapterList = new ExpandableListBarang(DetailLab.this, listParen, listChild);
                                expandableListView.setAdapter(adapterList);
                            } else {
                                Toast.makeText(getApplicationContext(), "Data belum ada", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Eror", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            Log.d("kucing",e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailLab.this, "Tidak tersambung ke server", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_idlab, idlab);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
