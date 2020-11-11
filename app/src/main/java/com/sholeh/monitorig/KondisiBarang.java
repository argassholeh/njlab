package com.sholeh.monitorig;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.sholeh.monitorig.config.koneksi.CariKondisiBarang;
import static com.sholeh.monitorig.config.koneksi.Editkondisibarang;
import static com.sholeh.monitorig.config.koneksi.HapusKondisiBarang;
import static com.sholeh.monitorig.config.koneksi.simpankondisiBarang;

public class KondisiBarang extends AppCompatActivity implements View.OnClickListener {

    Spinner spinlab, spinbarang;
    ArrayList<String> arrayDatalab = new ArrayList<>();
    ArrayList<String> listID_lab = new ArrayList<>();

    ArrayList<String> arrayDatabarang = new ArrayList<>();
    ArrayList<String> listID_barang = new ArrayList<>();
    //
    private ProgressDialog progres;
    Toolbar toolBarisi;


    Button btn_simpankondisi, btn_hapuskondisi;

    String[] labpilih = {"Pilih"};
    RadioGroup rg_status;
    RadioButton rb_baik, rb_rusak, rb_pilih;
    String status;


    Preferences preferences;
    adapterspinner adapterspinner;
    private SimpleDateFormat dateFormat;
    int tgl, bulan, tahun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kondisibarang);
        toolBarisi = findViewById(R.id.toolbar);
        toolBarisi.setTitle("Kondisi Barang");
        setSupportActionBar(toolBarisi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = new Preferences(this);


        spinlab = findViewById(R.id.spin_jenisLabK);
        spinbarang = findViewById(R.id.spin_namabarangk);
        rg_status = findViewById(R.id.rg_statuskondisi);
        rb_baik = findViewById(R.id.rb_baik);
        rb_rusak = findViewById(R.id.rb_rusak);
        rb_pilih = findViewById(R.id.rb_Pilih);
        defaultspinlab();
        rg_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_baik) {
                    status = String.valueOf("baik");
                } else if (checkedId == R.id.rb_rusak) {
                    status = String.valueOf("rusak");
                } else if (checkedId == R.id.rb_Pilih) {
                    status = String.valueOf("Pilih");
                }
            }
        });

        btn_simpankondisi = findViewById(R.id.btnsimpankondisik);
        btn_simpankondisi.setOnClickListener(this);
        btn_hapuskondisi = findViewById(R.id.btnhapuskondisik);
        btn_hapuskondisi.setOnClickListener(this);
//
        spinlab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carikbarang();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinbarang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carikbarang();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//
        spinlab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    tampilJenislab();
                }
                return false;
            }
        });
        spinbarang.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    tampilSpinBarang();
                }
                return false;
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); //  kalau tanpa ini tombol panahnya tak berfungsi
        return true;
    }

    public void defaultspinlab() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, labpilih);
        spinlab.setAdapter(adapter);
        spinbarang.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnsimpankondisik:
                simpanbarang();
                break;
            case R.id.btnhapuskondisik:
                hapuskondisibarang();
                break;


            default:
                break;
        }
    }

    public void tampilJenislab() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.tampilSpinlab, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int sukses;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    sukses = jsonObject.getInt("success");
                    arrayDatalab.clear();
                    listID_lab.clear();
                    if (sukses == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("Hasil");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            arrayDatalab.add(object.getString("nama_lab"));
                            listID_lab.add(object.getString("id_lab"));

                        }
                        ArrayAdapter<String> adap = new ArrayAdapter<String>(KondisiBarang.this, R.layout.support_simple_spinner_dropdown_item, arrayDatalab);
                        spinlab.setAdapter(adap);
                    } else {
                        Toast.makeText(KondisiBarang.this, "Data Belum Ada", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("Data Belum Ada", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(KondisiBarang.this, "Internet Kurang Stabil ", Toast.LENGTH_SHORT).show();
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

    public void tampilSpinBarang() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.tampilSpinbarang, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int sukses;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    sukses = jsonObject.getInt("success");
                    arrayDatabarang.clear();
                    listID_barang.clear();
                    if (sukses == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("Hasil");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            arrayDatabarang.add(object.getString("nama_barang"));
                            listID_barang.add(object.getString("id_barang"));

                        }
                        ArrayAdapter<String> adap = new ArrayAdapter<String>(KondisiBarang.this, R.layout.support_simple_spinner_dropdown_item, arrayDatabarang);
                        spinbarang.setAdapter(adap);
                    } else {
                        Toast.makeText(KondisiBarang.this, "Data Belum Ada", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("Data Belum Ada", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(KondisiBarang.this, "Internet Kurang Stabil ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> mapp = new HashMap<>();
                return mapp;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void simpanbarang() {
        if (spinlab.getSelectedItem().toString().trim().equals("Pilih")) {
            Toast.makeText(KondisiBarang.this, "Lab harus di tentukan", Toast.LENGTH_SHORT).show();
        } else if (spinbarang.getSelectedItem().toString().trim().equals("Pilih")) {
            Toast.makeText(KondisiBarang.this, "Barang harus di pilih", Toast.LENGTH_SHORT).show();
        } else if (rg_status.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "silahkan pilih status barang terlebih dahulu", Toast.LENGTH_SHORT).show();
        } else if (status.equalsIgnoreCase("Pilih")) {
            Toast.makeText(getApplicationContext(), "silahkan pilih status barang terlebih dahulu", Toast.LENGTH_SHORT).show();
        } else {
            final String idlab_ = listID_lab.get(spinlab.getSelectedItemPosition());
            final String idbarang_ = listID_barang.get(spinbarang.getSelectedItemPosition());
            final String status_ = status.toString();

            progres = new ProgressDialog(this);
            progres.setMessage("Proses...");
            progres.show();
            String url = simpankondisiBarang;
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("0")) {
                        dialogEdit();
                    } else {
                        Toast.makeText(KondisiBarang.this, "Berhasil di simpan", Toast.LENGTH_SHORT).show();
                        kosong();
                        progres.dismiss();

                    }
                    progres.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(KondisiBarang.this, "Internet Kurang Stabil", Toast.LENGTH_LONG).show();
                    progres.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(koneksi.key_idlab, idlab_);
                    params.put(koneksi.key_idbarang, idbarang_);
                    params.put(koneksi.key_status, status_);

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    public void dialogEdit() {
        AlertDialog.Builder build = new AlertDialog.Builder(KondisiBarang.this);
        build.setMessage("Kondisi barang sudah ada, apakah anda ingin memperbaruinya ?");
        build.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editkondisibarang();
//                Toast.makeText(KondisiBarang.this, "ok", Toast.LENGTH_SHORT).show();
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

    private void editkondisibarang() {
        if (rg_status.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "silahkan pilih status barang terlebih dahulu", Toast.LENGTH_SHORT).show();
        } else if (status.equalsIgnoreCase("Pilih")) {
            Toast.makeText(getApplicationContext(), "silahkan pilih status barang terlebih dahulu", Toast.LENGTH_SHORT).show();
        } else {
            final String idlab_ = listID_lab.get(spinlab.getSelectedItemPosition());
            final String idbarang_ = listID_barang.get(spinbarang.getSelectedItemPosition());
            final String status_ = status.toString();
            progres = new ProgressDialog(this);
            progres.setMessage("Proses...");
            progres.show();
            String url = Editkondisibarang;
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("1")) {
                        Toast.makeText(KondisiBarang.this, "Berhasil di perbarui", Toast.LENGTH_SHORT).show();
                        kosong();
                    } else {
                        Toast.makeText(KondisiBarang.this, "Gagal di edit", Toast.LENGTH_SHORT).show();
                    }
                    progres.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(KondisiBarang.this, "Internet Kurang Stabil", Toast.LENGTH_LONG).show();
                    progres.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(koneksi.key_idlab, idlab_);
                    params.put(koneksi.key_idbarang, idbarang_);
                    params.put(koneksi.key_status, status_);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    private void hapuskondisibarang() {
        if (spinlab.getSelectedItem().toString().trim().equals("Pilih") && spinbarang.getSelectedItem().toString().trim().equals("Pilih")) {
            Toast.makeText(KondisiBarang.this, "Lab atau Nama Barang harus di tentukan", Toast.LENGTH_SHORT).show();
        } else {
            final String idlab_ = listID_lab.get(spinlab.getSelectedItemPosition());
            final String idbarang_ = listID_barang.get(spinbarang.getSelectedItemPosition());
            progres = new ProgressDialog(this);
            progres.setMessage("Proses...");
            progres.show();
            String url = HapusKondisiBarang;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("1")) {
                                progres.dismiss();
                                Toast.makeText(KondisiBarang.this, "Berhasil", Toast.LENGTH_SHORT).show();
                                kosong();
                            } else {
                                progres.dismiss();
                                Toast.makeText(KondisiBarang.this, "Data Tidak ada", Toast.LENGTH_SHORT).show();
                            }
                            progres.dismiss();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progres.dismiss();
                            Toast.makeText(KondisiBarang.this, "Tidak ada barang", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(koneksi.key_idlab, idlab_);
                    params.put(koneksi.key_idbarang, idbarang_);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    private void carikbarang() {
        if (spinlab.getSelectedItem().toString().trim().equals("Pilih") && spinbarang.getSelectedItem().toString().trim().equals("Pilih")) {

        } else if (spinlab.getSelectedItem().toString().trim().equals("Pilih") || spinbarang.getSelectedItem().toString().trim().equals("Pilih")) {

        } else {

            final String idlab_ = listID_lab.get(spinlab.getSelectedItemPosition());
            final String idbarang_ = listID_barang.get(spinbarang.getSelectedItemPosition());
            String url = CariKondisiBarang;
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
                                        String status_ = c.getString(koneksi.key_status);
                                        if (status_.toString().equals("baik")) {
                                            rb_baik.setChecked(true);
                                        } else if (status_.toString().equals("rusak")) {
                                            rb_rusak.setChecked(true);
                                        } else {
                                            Toast.makeText(KondisiBarang.this, "Tidak ada status)", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                kosong2();
//                                Toast.makeText(getApplicationContext(), "Tidak ada", Toast.LENGTH_SHORT).show();
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
                    params.put(koneksi.key_idlab, idlab_);
                    params.put(koneksi.key_idbarang, idbarang_);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }


    public void kosong() {
        defaultspinlab();
        rb_baik.setChecked(false); // ketika pernah di pilih sebelumnya pas tidak bisa di pilih lagi?
        rb_rusak.setChecked(false);
        rb_pilih.setChecked(true);



    }

    public void kosong2() {
        rb_baik.setChecked(false); // ketika pernah di pilih sebelumnya pas tidak bisa di pilih lagi?
        rb_rusak.setChecked(false);
        rb_pilih.setChecked(true);
    }


}

