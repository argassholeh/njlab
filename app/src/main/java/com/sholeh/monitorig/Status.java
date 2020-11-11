package com.sholeh.monitorig;

import android.annotation.SuppressLint;
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

import static com.sholeh.monitorig.config.koneksi.CariStatus;
import static com.sholeh.monitorig.config.koneksi.HapusStatus;
import static com.sholeh.monitorig.config.koneksi.simpanStatus;

public class Status extends AppCompatActivity implements View.OnClickListener {
    EditText ed_idstatus, ed_namastatus;
    Button btnsimpanstatus, btnhapusstatus, btncaristatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Status");
        setSupportActionBar(toolbar);

        ed_idstatus = findViewById(R.id.edIdstatus);
        ed_namastatus = findViewById(R.id.edNamastatus);

        btnsimpanstatus = findViewById(R.id.btnSimpanstatus);
        btnsimpanstatus.setOnClickListener(this);
        btnhapusstatus = findViewById(R.id.btnHapusstatus);
        btnhapusstatus.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSimpanstatus:
                simpanstatus();

                break;

            case R.id.btnHapusstatus:
                hapusstatus();
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
        searchView.setQueryHint("Cari Status");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {
                final String searchnid_ = query;
                String url = CariStatus;
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
                                            ed_idstatus.setText(c.getString(koneksi.key_idstatus));
                                            ed_namastatus.setText(c.getString(koneksi.key_namastatus));
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                } else {
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
                        params.put(koneksi.key_namastatus, searchnid_);
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


    private void simpanstatus() {
        final String namastatus_ = ed_namastatus.getText().toString();
        if (!validasi()) return;
        String url = simpanStatus;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("0")) {
                    Toast.makeText(Status.this, "Nama Status Sudah Ada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Status.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                    kosong();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Status.this, "Cek Koneksi Internet Anda", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_namastatus, namastatus_);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void hapusstatus() {
        final String namastatus_ = ed_namastatus.getText().toString();
        if (!validasi()) return;
        String url = HapusStatus;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")) {
                            Toast.makeText(Status.this, "Berhasil", Toast.LENGTH_SHORT).show();
                            kosong();
                        } else {
                            Toast.makeText(Status.this, "Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Status.this, "Tidak terhubung ke server", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_namastatus, namastatus_);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public  void  kosong(){
        ed_namastatus.setText("");
    }

    private boolean validasi() {
        boolean valid = true;
        final String namastatus_ = ed_namastatus.getText().toString();
        if (namastatus_.isEmpty()) {
            ed_namastatus.setError("tidak boleh kosong");
            valid = false;
        } else {
            ed_namastatus.setError(null);
        }
        return valid;
    }
}

