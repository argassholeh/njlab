package com.sholeh.monitorig;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import static com.sholeh.monitorig.config.koneksi.CariJenis;
import static com.sholeh.monitorig.config.koneksi.Editjenis;
import static com.sholeh.monitorig.config.koneksi.HapusJenis;
import static com.sholeh.monitorig.config.koneksi.simpanJenis;

public class Jenis extends AppCompatActivity implements View.OnClickListener {
    TextView tvxnamajenis, tvxperubahanjenis;

    EditText ed_carijenis, ed_namajenis, ed_perubahanjenis;
    Button btn_updatejenis, btn_simpanjenis, btn_hapusjenis;
    private ProgressDialog progres;
    Toolbar toolBarisi;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jenis);

        toolBarisi = findViewById(R.id.toolbar);
        toolBarisi.setTitle("Jenis Barang");
        setSupportActionBar(toolBarisi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ed_namajenis = findViewById(R.id.ednmJenis);
        ed_perubahanjenis = findViewById(R.id.edperubahanJenis);
        tvxnamajenis = findViewById(R.id.tvnamajenis);
        tvxperubahanjenis = findViewById(R.id.tvperubahanjenis);
        btn_simpanjenis = findViewById(R.id.btnsimpanJenis);
        btn_simpanjenis.setOnClickListener(this);
        btn_updatejenis = findViewById(R.id.btnupdatejenis);
        btn_updatejenis.setOnClickListener(this);
        btn_hapusjenis = findViewById(R.id.btnhapusjenis);
        btn_hapusjenis.setOnClickListener(this);


    }



    @Override
    public boolean onSupportNavigateUp() {
        finish(); //  kalau tanpa ini tombol panahnya tak berfungsi
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnsimpanJenis:
                simpanjenis();
                break;
            case R.id.btnhapusjenis:
                hapusjenis();
                break;
            case R.id.btnupdatejenis:
                editjenis();
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
        searchView.setQueryHint("Cari Nama Jenis");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {
                final String searchnid_ = query;
                String url = CariJenis;
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
                                        for (int i = 0; i < result.length(); i++) {
                                            JSONObject c = result.getJSONObject(i);
                                            ed_namajenis.setText(c.getString(koneksi.key_nama_jenis));

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "Tidak ada", Toast.LENGTH_SHORT).show();
                                    kosong();
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
                        params.put(koneksi.key_nama_jenis, searchnid_);
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

    private void simpanjenis() {
        final String namajenis_ = ed_namajenis.getText().toString();
        if (!validasi()) return;
        progres = new ProgressDialog(this);
        progres.setMessage("Proses...");
        progres.show();
        String url = simpanJenis;
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
                    Toast.makeText(Jenis.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                    kosong();
                    progres.dismiss();

                }
                progres.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Jenis.this, "Internet Kurang Stabil", Toast.LENGTH_LONG).show();
                progres.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_nama_jenis, namajenis_);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void dialogEdit() {
        AlertDialog.Builder build = new AlertDialog.Builder(Jenis.this);
        build.setMessage("Jenis barang sudah ada, apakah anda ingin memperbaruinya ?");
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

    private void editjenis() {
        final String namajenis_ = ed_namajenis.getText().toString();
        final String perubahannamajenis_ = ed_perubahanjenis.getText().toString();
        if (!validasi()) return;
        progres = new ProgressDialog(this);
        progres.setMessage("Proses...");
        progres.show();
        String url = Editjenis;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("1")) {
                    Toast.makeText(Jenis.this, "Berhasil di edit", Toast.LENGTH_SHORT).show();
                    viewSimpan();
                    kosong();
                } else {

                    Toast.makeText(Jenis.this, "Cari Nama Jenis Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }
                progres.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Jenis.this, "Internet Kurang Stabil", Toast.LENGTH_LONG).show();
                progres.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_nama_jenis, namajenis_);
                params.put("perubahan", perubahannamajenis_);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void hapusjenis() {
        final String searchnama_ = ed_namajenis.getText().toString();
        progres = new ProgressDialog(this);
        progres.setMessage("Proses...");
        progres.show();
        String url = HapusJenis;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")) {
                            progres.dismiss();
                            Toast.makeText(Jenis.this, "Berhasil", Toast.LENGTH_SHORT).show();
                            kosong();
                        } else {
                            progres.dismiss();
                            if (!validasi()) return;
//                            Toast.makeText(Jenis.this, "Cari nama jenis terlebih dahulu", Toast.LENGTH_SHORT).show();
                        }
                        progres.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progres.dismiss();
                        Toast.makeText(Jenis.this, "Tidak ada barang", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_nama_jenis, searchnama_);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }





    public void kosong() {
        ed_namajenis.setText("");
    }

    private boolean validasi() {
        boolean valid = true;
        final String namajenis_ = ed_namajenis.getText().toString();
        if (namajenis_.isEmpty()) {
            ed_namajenis.setError("tidak boleh kosong");
            valid = false;
        } else {
            ed_namajenis.setError(null);
        }

        return valid;
    }
    public void viewEdit(){
        ed_namajenis.setEnabled(false);
        btn_simpanjenis.setVisibility(View.GONE);
        tvxperubahanjenis.setVisibility(View.VISIBLE);
        ed_perubahanjenis.setVisibility(View.VISIBLE);
        btn_updatejenis.setVisibility(View.VISIBLE);
    }
    public void viewSimpan(){
        ed_namajenis.setEnabled(true);
        btn_simpanjenis.setVisibility(View.VISIBLE);
        tvxperubahanjenis.setVisibility(View.GONE);
        ed_perubahanjenis.setVisibility(View.GONE);
        btn_updatejenis.setVisibility(View.GONE);
    }
}
