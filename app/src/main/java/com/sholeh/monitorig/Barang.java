package com.sholeh.monitorig;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sholeh.monitorig.config.koneksi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.sholeh.monitorig.config.koneksi.CariBarang;
import static com.sholeh.monitorig.config.koneksi.Editbarang;
import static com.sholeh.monitorig.config.koneksi.HapusBarang;
import static com.sholeh.monitorig.config.koneksi.simpanBarang;

public class Barang extends AppCompatActivity implements View.OnClickListener {


    TextView tvxnamabarang, tvxperubahanbarang;

    EditText ed_caribarang, ed_namabarang, ed_perubahanbarang;
    Button btn_updatebarang, btn_simpanbarang, btn_hapusbarang, btn_tambahjenis;
    private ProgressDialog progres;
    Toolbar toolBarisi;

    ArrayList<String> arrayDatajenis = new ArrayList<>();
    ArrayList<String> listID_jenis = new ArrayList<>();
    String[] pilih = {"Pilih"};
    Spinner spinjenis;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barang);

        toolBarisi = findViewById(R.id.toolbar);
        toolBarisi.setTitle("Barang");
        setSupportActionBar(toolBarisi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinjenis = findViewById(R.id.spin_jenisbarang);
        ed_namabarang = findViewById(R.id.ednmBarang);
        ed_perubahanbarang = findViewById(R.id.edperubahanBarang);
        tvxnamabarang = findViewById(R.id.tvnamabarang);
        tvxperubahanbarang = findViewById(R.id.tvperubahanbarang);
        btn_simpanbarang = findViewById(R.id.btnsimpanbarang);
        btn_simpanbarang.setOnClickListener(this);
        btn_updatebarang = findViewById(R.id.btnupdatebarang);
        btn_updatebarang.setOnClickListener(this);
        btn_hapusbarang = findViewById(R.id.btnhapusbarang);
        btn_hapusbarang.setOnClickListener(this);
        btn_tambahjenis = findViewById(R.id.btntambahjenis);
        btn_tambahjenis.setOnClickListener(this);

        defaultspin();
        spinjenis.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    tampilJenis();
                }
                return false;
            }
        });
    }

    public void defaultspin() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, pilih);
        spinjenis.setAdapter(adapter);

    }


    @Override
    public boolean onSupportNavigateUp() {
        finish(); //  kalau tanpa ini tombol panahnya tak berfungsi
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnsimpanbarang:
                simpanbarang();
                break;
            case R.id.btnhapusbarang:
                hapusbarang();
                break;
            case R.id.btnupdatebarang:
                editbarang();
                break;

            case R.id.btntambahjenis:
                Intent i = new Intent(getApplicationContext(),Jenis.class);
                startActivity(i);
                break;

            default:
                break;
        }
    }

    //Code Program pada Method dibawah ini akan Berjalan saat Option Menu Dibuat
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Memanggil/Memasang menu item pada toolbar dari layout menu_bar.xml
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        MenuItem searchIem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchIem.getActionView();
        searchView.setFocusable(false);
        searchView.setQueryHint("Cari Nama Barang");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {
                final String searchnid_ = query;
                String url = CariBarang;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.contains("1")) {
                                    try {
                                        JSONObject jsonObject;
                                        jsonObject = new JSONObject(response);
                                        JSONArray result = jsonObject.getJSONArray("Hasil");
                                        viewSimpan();
                                        arrayDatajenis.clear();
                                        listID_jenis.clear();
                                        for (int i = 0; i < result.length(); i++) {
                                            JSONObject c = result.getJSONObject(i);
                                            ed_namabarang.setText(c.getString(koneksi.key_nama_barang));
                                            arrayDatajenis.add(c.getString("nama_jenis"));
                                            listID_jenis.add(c.getString("id_jenis"));


                                        }
                                        ArrayAdapter<String> adap = new ArrayAdapter<String>(Barang.this, R.layout.support_simple_spinner_dropdown_item, arrayDatajenis);
                                        spinjenis.setAdapter(adap);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    kosong();
                                    Toast.makeText(getApplicationContext(), "Tidak ada", Toast.LENGTH_SHORT).show();
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
                        params.put(koneksi.key_nama_barang, searchnid_);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
                requestQueue.add(stringRequest);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    private void simpanbarang() {
        if (spinjenis.getSelectedItem().toString().trim().equals("Pilih")) {
            Toast.makeText(Barang.this, "Jenis barang harus di tentukan", Toast.LENGTH_SHORT).show();
        } else {
            final String idjenis_ = listID_jenis.get(spinjenis.getSelectedItemPosition());
            final String namabarang_ = ed_namabarang.getText().toString();
            if (!validasi()) return;
            progres = new ProgressDialog(this);
            progres.setMessage("Proses...");
            progres.show();
            String url = simpanBarang;
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("0")) {
//                    Toast.makeText(Barang.this, "Barang Sudah Ada", Toast.LENGTH_SHORT).show();
//                        kosong();
//                        progres.dismiss();
                        dialogEdit();
                    } else {
                        Toast.makeText(Barang.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                        kosong();
                        progres.dismiss();

                    }
                    progres.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Barang.this, "Internet Kurang Stabil", Toast.LENGTH_LONG).show();
                    progres.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(koneksi.key_idjenis, idjenis_);
                    params.put(koneksi.key_nama_barang, namabarang_);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    public void dialogEdit() {
        AlertDialog.Builder build = new AlertDialog.Builder(Barang.this);
        build.setMessage("Barang sudah ada, apakah anda ingin memperbaruinya ?");
        build.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewEdit();
//                editbarang();


            }
        });

        build.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        build.create().show();
    }

    private void editbarang() {
        if (spinjenis.getSelectedItem().toString().trim().equals("Pilih")) {
            Toast.makeText(Barang.this, "Jenis barang harus di tentukan", Toast.LENGTH_SHORT).show();
        } else {
            final String idjenis_ = listID_jenis.get(spinjenis.getSelectedItemPosition());
            final String namabarang_ = ed_namabarang.getText().toString();
            final String perubahannamabarang_ = ed_perubahanbarang.getText().toString();
            if (!validasi()) return;
            progres = new ProgressDialog(this);
            progres.setMessage("Proses...");
            progres.show();
            String url = Editbarang;
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("1")) {
                        Toast.makeText(Barang.this, "Berhasil di edit", Toast.LENGTH_SHORT).show();
                        viewSimpan();
                        kosong();
                    } else {
                        Toast.makeText(Barang.this, "Cari Barang Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                    }
                    progres.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Barang.this, "Internet Kurang Stabil", Toast.LENGTH_LONG).show();
                    progres.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(koneksi.key_idjenis, idjenis_);
                    params.put(koneksi.key_nama_barang, namabarang_);
                    params.put("perubahan", perubahannamabarang_);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }



    }

    private void caribarang() {

        final String searchnama_ = ed_caribarang.getText().toString();
        progres = new ProgressDialog(this);
        progres.setMessage("Proses...");
        progres.show();
        String url = CariBarang;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")) {
                            try {
                                JSONObject jsonObject;
                                jsonObject = new JSONObject(response);
                                JSONArray result = jsonObject.getJSONArray("Hasil");

                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject c = result.getJSONObject(i);
                                    ed_namabarang.setText(c.getString(koneksi.key_nama_barang));

                                }
                                progres.dismiss();
                            } catch (JSONException e) {
                                progres.dismiss();
                                e.printStackTrace();
                            }

                        } else {
                            progres.dismiss();
                            Toast.makeText(getApplicationContext(), "Tidak ada", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progres.dismiss();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_nama_barang, searchnama_);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void hapusbarang() {
        final String searchnama_ = ed_namabarang.getText().toString();
        progres = new ProgressDialog(this);
        progres.setMessage("Proses...");
        progres.show();
        String url = HapusBarang;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")) {
                            progres.dismiss();
                            Toast.makeText(Barang.this, "Berhasil", Toast.LENGTH_SHORT).show();
                            kosong();
                        } else {
                            progres.dismiss();
                            Toast.makeText(Barang.this, "Cari barang terlebih dahulu", Toast.LENGTH_SHORT).show();
                        }
                        progres.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progres.dismiss();
                        Toast.makeText(Barang.this, "Tidak ada barang", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_nama_barang, searchnama_);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void tampilJenis() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.tampilSpinJenis, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int sukses;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    sukses = jsonObject.getInt("success");
                    arrayDatajenis.clear();
                    listID_jenis.clear();
                    if (sukses == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("Hasil");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            arrayDatajenis.add(object.getString("nama_jenis"));
                            listID_jenis.add(object.getString("id_jenis"));

                        }
                        ArrayAdapter<String> adap = new ArrayAdapter<String>(Barang.this, R.layout.support_simple_spinner_dropdown_item, arrayDatajenis);
                        spinjenis.setAdapter(adap);
                    } else {
                        Toast.makeText(Barang.this, "Data Belum Ada", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("Data Belum Ada", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Barang.this, "Internet Kurang Stabil ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> mapp = new HashMap<>();
//                mapp.put("id_kecamatan", idkat);
                return mapp;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    public void kosong() {
        ed_namabarang.setText("");
        defaultspin();
    }

    private boolean validasi() {
        boolean valid = true;
        final String namabarang_ = ed_namabarang.getText().toString();
        if (namabarang_.isEmpty()) {
            ed_namabarang.setError("tidak boleh kosong");
            valid = false;
        } else {
            ed_namabarang.setError(null);
        }

        return valid;
    }
    public void viewEdit(){
        ed_namabarang.setEnabled(false);
        btn_simpanbarang.setVisibility(View.GONE);
        tvxperubahanbarang.setVisibility(View.VISIBLE);
        ed_perubahanbarang.setVisibility(View.VISIBLE);
        btn_updatebarang.setVisibility(View.VISIBLE);
    }
    public void viewSimpan(){
        ed_namabarang.setEnabled(true);
        btn_simpanbarang.setVisibility(View.VISIBLE);
        tvxperubahanbarang.setVisibility(View.GONE);
        ed_perubahanbarang.setVisibility(View.GONE);
        btn_updatebarang.setVisibility(View.GONE);
    }

}
