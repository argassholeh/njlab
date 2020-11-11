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
import android.text.Editable;
import android.text.TextWatcher;
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

import static com.sholeh.monitorig.config.koneksi.CariUser;
import static com.sholeh.monitorig.config.koneksi.EditUser;
import static com.sholeh.monitorig.config.koneksi.HapusUser;
import static com.sholeh.monitorig.config.koneksi.simpanUser;

public class User extends AppCompatActivity implements View.OnClickListener{

    EditText ed_cariuser,ed_iduser, ed_namauser, ed_passuser;
    String status;
    Button btn_cariuser, btn_simpanuser, btn_hapususer, btn_tambahstatus;
    private ProgressDialog progres;
    Toolbar toolBarisi;
    TextView tv_tambahstatus;

    Spinner spinstatus;
    ArrayList<String> arrayDatastatus= new ArrayList<>();
    ArrayList<String> listID_status= new ArrayList<>();
    String[] spinpilih = {"Pilih"};

    private TextWatcher text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

//        toolBarisi =  findViewById(R.id.toolbar);
//        toolBarisi.setTitle("User");
//        setSupportActionBar(toolBarisi);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("User");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//
        setSupportActionBar(toolbar);


        ed_iduser = findViewById(R.id.edIduser);
        ed_namauser = findViewById(R.id.edNamauser);
        ed_passuser = findViewById(R.id.edPassworduser);

        tv_tambahstatus = findViewById(R.id.tv_tambahstatus);
        btn_simpanuser = findViewById(R.id.btnsimpanuser);
        btn_simpanuser.setOnClickListener(this);
        btn_hapususer = findViewById(R.id.btnhapususer);
        btn_hapususer.setOnClickListener(this);
        btn_tambahstatus = findViewById(R.id.btntambahstatus);
        btn_tambahstatus.setOnClickListener(this);

        spinstatus = findViewById(R.id.spin_status);
        defaultspinstatus();
        spinstatus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    tampilSpinStatus();
//                    Toast.makeText(KondisiBarang.this, "klik", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        // input otomatis password dengan id user
        text = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ed_passuser.setText(ed_iduser.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        ed_iduser.addTextChangedListener(text);

    }
    public void defaultspinstatus(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, spinpilih);
        spinstatus.setAdapter(adapter);

    }
    @Override
    public boolean onSupportNavigateUp(){
        finish(); //  kalau tanpa ini tombol panahnya tak berfungsi
        return true;
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
        searchView.setQueryHint("Cari User");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {
                final String searchnid_ = query;
                String url = CariUser;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.contains("1")) {
                                    try {
                                        JSONObject jsonObject;
                                        jsonObject = new JSONObject(response);
                                        JSONArray result = jsonObject.getJSONArray("Hasil");
                                        arrayDatastatus.clear();
                                         listID_status.clear();
                                        for (int i = 0; i < result.length(); i++) {
                                            JSONObject c = result.getJSONObject(i);
                                            arrayDatastatus.add(c.getString("nama_status"));
                                            listID_status.add(c.getString("id_status"));
                                            ed_iduser.setText(c.getString(koneksi.key_iduser));
                                            ed_namauser.setText(c.getString(koneksi.key_nama));
                                        }
                                        ArrayAdapter<String> adap1 = new ArrayAdapter<String>(User.this, R.layout.support_simple_spinner_dropdown_item, arrayDatastatus);
                                        spinstatus.setAdapter(adap1);
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
                        params.put(koneksi.key_iduser, searchnid_);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
                requestQueue.add(stringRequest);
               // Hasil.setText("Hasil Pencarian: "+query);
//                Toast.makeText(getApplicationContext(),query, Toast.LENGTH_SHORT).show();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnsimpanuser:
                simpanuser();
                break;

            case R.id.btnhapususer:
                hapususer();
                break;
            case R.id.btntambahstatus:
                Intent intent = new Intent(getApplicationContext(),Status.class);
                startActivity(intent);
                break;
            case R.id.tv_tambahstatus:
                Intent i = new Intent(getApplicationContext(),Status.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }
    private void simpanuser() {
        if (spinstatus.getSelectedItem().toString().trim().equals("Pilih") ) {
            Toast.makeText(User.this, "Status User Harus di pilih", Toast.LENGTH_SHORT).show();
        }else {
            final String iduser_ = ed_iduser.getText().toString();
            final String namauser_ = ed_namauser.getText().toString();
            final String idstatus_ = listID_status.get(spinstatus.getSelectedItemPosition());
            final String pass_ = ed_passuser.getText().toString();
            if (!validasi()) return;
            progres = new ProgressDialog(this);
            progres.setMessage("Proses...");
            progres.show();
            String url = simpanUser;
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("0")){
////                        Toast.makeText(Barang.this, "Berhasil", Toast.LENGTH_SHORT).show();
////                        kosong();
////                        progres.dismiss();
                        dialogEdit();
                    }else  {
                        Toast.makeText(User.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                        kosong();
                        progres.dismiss();

                    }
                    progres.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(User.this, "Internet Kurang Stabil", Toast.LENGTH_LONG).show();
                    progres.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(koneksi.key_iduser, iduser_);
                    params.put(koneksi.key_nama, namauser_);
                    params.put(koneksi.key_idstatus,idstatus_);
                    params.put(koneksi.key_password,pass_);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }
    public void dialogEdit(){
        AlertDialog.Builder build = new AlertDialog.Builder(User.this);
        build.setMessage("User sudah terdaftar, apakah anda ingin memperbaruinya ?");
        build.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edituser();
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
    private void edituser() {
        final String iduser_ = ed_iduser.getText().toString();
        final String namauser_ = ed_namauser.getText().toString();
        final String idstatus_ = listID_status.get(spinstatus.getSelectedItemPosition());
        final String pass_ = ed_passuser.getText().toString();
        if (!validasi()) return;
        progres = new ProgressDialog(this);
        progres.setMessage("Proses...");
        progres.show();
        String url = EditUser;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("1")) {
                    Toast.makeText(User.this, "Berhasil di edit", Toast.LENGTH_SHORT).show();
                    kosong();
                }else{
                    Toast.makeText(User.this, "Gagal di edit", Toast.LENGTH_SHORT).show();
                }
                progres.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(User.this, "Internet Kurang Stabil", Toast.LENGTH_LONG).show();
                progres.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_iduser, iduser_);
                params.put(koneksi.key_nama, namauser_);
                params.put(koneksi.key_idstatus,idstatus_);
                params.put(koneksi.key_password,pass_);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    private void hapususer() {
        final String iduser_ = ed_iduser.getText().toString();
        progres = new ProgressDialog(this);
        progres.setMessage("Proses...");
        progres.show();
        String url = HapusUser;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")) {
                            progres.dismiss();
                            Toast.makeText(User.this, "Berhasil", Toast.LENGTH_SHORT).show();
                            kosong();
                        } else {
                            progres.dismiss();
                            Toast.makeText(User.this, "Gagal", Toast.LENGTH_SHORT).show();
                        }
                        progres.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progres.dismiss();
                        Toast.makeText(User.this, "Tidak ada user", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_iduser, iduser_);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public  void  tampilSpinStatus(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.tampilSpinstatus, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int sukses;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    sukses = jsonObject.getInt("success");
                    arrayDatastatus.clear();
                    listID_status.clear();
                    if (sukses == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("Hasil");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            arrayDatastatus.add(object.getString("nama_status"));
                            listID_status.add(object.getString("id_status"));
                        }
                        ArrayAdapter<String> adap = new ArrayAdapter<String>(User.this, R.layout.support_simple_spinner_dropdown_item, arrayDatastatus);
                        spinstatus.setAdapter(adap);
                    } else {
                        Toast.makeText(User.this, "Tidak Ada", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("Data Belum Ada", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(User.this, "Internet Kurang Stabil ", Toast.LENGTH_SHORT).show();
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



    public void kosong(){
        ed_iduser.setText("");
        ed_namauser.setText("");
        ed_passuser.setText("");
        defaultspinstatus();
//        ed_iduser.requestFocus();

    }

    private boolean validasi() {
        boolean valid = true;
        final String iduser_ = ed_iduser.getText().toString();
        final String namauser_ = ed_namauser.getText().toString();
        final String pass_ = ed_passuser.getText().toString();
        if (iduser_.isEmpty()) {
            ed_iduser.setError("tidak boleh kosong");
            valid = false;
        } else {
            ed_iduser.setError(null);
        }
        if (namauser_.isEmpty()) {
            ed_namauser.setError("tidak boleh kosong");
            valid = false;
        } else {
            ed_namauser.setError(null);
        }
        if (pass_.isEmpty()) {
            ed_passuser.setError("tidak boleh kosong");
            valid = false;
        } else {
            ed_passuser.setError(null);
        }

        return valid;
    }
}
