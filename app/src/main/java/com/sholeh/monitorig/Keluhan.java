package com.sholeh.monitorig;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.sholeh.monitorig.config.koneksi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.sholeh.monitorig.config.koneksi.Edithp;
import static com.sholeh.monitorig.config.koneksi.simpancomplaintBarang;
import static com.sholeh.monitorig.config.koneksi.simpankeluhanBarang;

public class Keluhan extends AppCompatActivity implements View.OnClickListener {

    Spinner spin_lab, spin_barang;
    ArrayList<String> arrayDatalab = new ArrayList<>();
    ArrayList<String> listID_lab = new ArrayList<>();

    ArrayList<String> arrayDatabarang = new ArrayList<>();
    ArrayList<String> listID_barang = new ArrayList<>();
    //
    private ProgressDialog progres;
    Toolbar toolBarisi;

    EditText ed_iduser, ed_keluhan, ed_nohpuser;
    TextView tvxkeluhan;


    Button btn_simpankeluhan, btn_simpancomplaint, btn_fotokeruskan, btn_simpanperbaikan;
    String[] spinpilih = {"Pilih"};

    Preferences preferences;
    private final static int REQUEST_CODE = 999;

    ImageView imgKerusakan;
    Bitmap bitmap = null;
    Uri imageUri;
    private static final int PICK_IMAGE = 1;
    private static final int PICK_Camera_IMAGE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keluhan);

        toolBarisi = findViewById(R.id.toolbar);
        toolBarisi.setTitle("Complaint");
        setSupportActionBar(toolBarisi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spin_lab = findViewById(R.id.spinlabk);
        spin_barang = findViewById(R.id.spinbarangk);
        ed_iduser = findViewById(R.id.ediduser);
        ed_keluhan = findViewById(R.id.edkeluhanBarang);
        tvxkeluhan = findViewById(R.id.tv_keluhan);
        imgKerusakan = findViewById(R.id.img_fotokerusakan);

        btn_simpankeluhan = findViewById(R.id.btnsimpankeluhan);
        btn_simpankeluhan.setOnClickListener(this);
        btn_simpancomplaint = findViewById(R.id.btnsimpancomplaint);
        btn_fotokeruskan = findViewById(R.id.btnfotokerusakan);

        btn_simpanperbaikan = findViewById(R.id.btnsimpanperbaikan);

        btn_simpancomplaint.setOnClickListener(this);
        preferences = new Preferences(this);
        String iduser = preferences.getUsername();
        ed_iduser.setText(iduser);
        defaultspinlab();

        spin_lab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    tampilJenislab();
                }
                return false;
            }
        });
        spin_barang.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    tampilSpinBarang();
                }
                return false;
            }
        });

        if (preferences.getSPStatus().equalsIgnoreCase("mahasiswa")) {
            tvxkeluhan.setVisibility(View.GONE);
            ed_keluhan.setVisibility(View.GONE);
            btn_simpancomplaint.setVisibility(View.GONE);
            btn_simpankeluhan.setVisibility(View.VISIBLE);
        } else if (preferences.getSPStatus().equalsIgnoreCase("asisten lab")) {
            tvxkeluhan.setVisibility(View.GONE);
            ed_keluhan.setVisibility(View.GONE);
            btn_simpancomplaint.setVisibility(View.GONE);
            btn_simpankeluhan.setVisibility(View.GONE);
            btn_fotokeruskan.setText("Foto Hasil Perbaikan");
            btn_simpanperbaikan.setVisibility(View.VISIBLE);
        }

        btn_fotokeruskan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnsimpankeluhan:
                cekdata();
                break;
            case R.id.btnsimpancomplaint:
                simpancomplaint();
                break;
            case R.id.btnsimpanperbaikan:
                //simpanperbaikan();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); //  kalau tanpa ini tombol panahnya tak berfungsi
        return true;
    }

    public void defaultspinlab() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, spinpilih);
        spin_lab.setAdapter(adapter);
        spin_barang.setAdapter(adapter);
    }

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.sholeh.monitoriglab",
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KEYHASH", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    //Code Program pada Method dibawah ini akan Berjalan saat Option Menu Dibuat
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;

    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem register = menu.findItem(R.id.darutat);
        if (!preferences.getSPStatus().equalsIgnoreCase("mahasiswa")) {
            register.setVisible(true);
        } else {
            register.setVisible(false);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.darutat:
                Intent intent = new Intent(getApplicationContext(), Helpme.class);
                startActivity(intent);
                return true;

        }
        return false;
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
                        ArrayAdapter<String> adap = new ArrayAdapter<String>(Keluhan.this, R.layout.support_simple_spinner_dropdown_item, arrayDatalab);
                        spin_lab.setAdapter(adap);
                    } else {
                        Toast.makeText(Keluhan.this, "Data Belum Ada", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("Data Belum Ada", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Keluhan.this, "Internet Kurang Stabil ", Toast.LENGTH_SHORT).show();
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
                        ArrayAdapter<String> adap = new ArrayAdapter<String>(Keluhan.this, R.layout.support_simple_spinner_dropdown_item, arrayDatabarang);
                        spin_barang.setAdapter(adap);
                    } else {
                        Toast.makeText(Keluhan.this, "Data Belum Ada", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("Data Belum Ada", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Keluhan.this, "Internet Kurang Stabil ", Toast.LENGTH_SHORT).show();
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

    private void cekdata() {
        // mahasiswa
        if (spin_lab.getSelectedItem().toString().trim().equals("Pilih")) {
            Toast.makeText(Keluhan.this, "Lab harus di tentukan", Toast.LENGTH_SHORT).show();
        } else if (spin_barang.getSelectedItem().toString().trim().equals("Pilih")) {
            Toast.makeText(Keluhan.this, "Barang harus di pilih", Toast.LENGTH_SHORT).show();
        } else {
            final String idlab_ = listID_lab.get(spin_lab.getSelectedItemPosition());
            final String idbarang_ = listID_barang.get(spin_barang.getSelectedItemPosition());
            final String keluhan_ = ed_keluhan.getText().toString();
            final String blumverifikasi = "tidak";
            final String foto_ = "";
            if (!validasi()) return;
            progres = new ProgressDialog(this);
            progres.setMessage("Proses...");
            progres.show();
            String url = simpankeluhanBarang;
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                    Toast.makeText(Keluhan.this, ""+response, Toast.LENGTH_SHORT).show();
                    if (response.equalsIgnoreCase("No hp belum terdaftar")) {
                        dialognohp();
                    } else if (response.equalsIgnoreCase("Kondisi barang sudah di laporkan")) {
                        dialog();
                    } else if (response.equalsIgnoreCase("verifikasi")) {
                        dialogsimpan();
                    } else {
                        Toast.makeText(Keluhan.this, "gagal", Toast.LENGTH_SHORT).show();
//                       dialogverifikasi();
//                        Toast.makeText(Keluhan.this, "Laporan terkirim ke admin", Toast.LENGTH_SHORT).show();
//                        kosong();
                        //  finish();

                    }
                    progres.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Keluhan.this, "Cek Koneksi Internet Anda", Toast.LENGTH_LONG).show();
                    progres.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(koneksi.key_iduser, preferences.getUsername());
                    params.put(koneksi.key_idlab, idlab_);
                    params.put(koneksi.key_idbarang, idbarang_);
                    params.put(koneksi.key_fotokerusakan, foto_);
                    params.put(koneksi.key_keluhan, keluhan_);
                    params.put("verifikasi", blumverifikasi);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    private void simpandata() {
        // mahasiswa
        final String idlab_ = listID_lab.get(spin_lab.getSelectedItemPosition());
        final String idbarang_ = listID_barang.get(spin_barang.getSelectedItemPosition());
        final String keluhan_ = ed_keluhan.getText().toString();
        final String jawab = "iya";
        if (!validasi()) return;
        progres = new ProgressDialog(this);
        progres.setMessage("Proses...");
        progres.show();
        String url = simpankeluhanBarang;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Keluhan.this, "Laporan terkirim ke admin", Toast.LENGTH_SHORT).show();
                kosong();
                finish();
                progres.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(Keluhan.this, "Cek Koneksi Internet Anda", Toast.LENGTH_LONG).show();
                Toast.makeText(Keluhan.this, "Silahkan foto kerusakan terlebih dahulu", Toast.LENGTH_SHORT).show(); // error ketika tidak memilih foto
                progres.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_iduser, preferences.getUsername());
                params.put(koneksi.key_idlab, idlab_);
                params.put(koneksi.key_idbarang, idbarang_);
                params.put(koneksi.key_fotokerusakan, getStringImage(bitmap));
                params.put(koneksi.key_keluhan, keluhan_);
                params.put("verifikasi", jawab);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void simpancomplaint() {
        // selain mahasiswa
        if (spin_lab.getSelectedItem().toString().trim().equals("Pilih")) {
            Toast.makeText(Keluhan.this, "Lab harus di tentukan", Toast.LENGTH_SHORT).show();
        } else if (spin_barang.getSelectedItem().toString().trim().equals("Pilih")) {
            Toast.makeText(Keluhan.this, "Barang harus di pilih", Toast.LENGTH_SHORT).show();
        } else {
            final String idlab_ = listID_lab.get(spin_lab.getSelectedItemPosition());
            final String idbarang_ = listID_barang.get(spin_barang.getSelectedItemPosition());
            final String keluhan_ = ed_keluhan.getText().toString();
            if (!validasi()) return;
            progres = new ProgressDialog(this);
            progres.setMessage("Proses...");
            progres.show();
            String url = simpancomplaintBarang;
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("0")) {
                        dialog();
                    } else {
//                        Toast.makeText(Keluhan.this, ""+response, Toast.LENGTH_SHORT).show();
                        Toast.makeText(Keluhan.this, "Laporan terkirim ke admin", Toast.LENGTH_SHORT).show();
                        kosong();
                        finish();
                    }
                    progres.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Keluhan.this, "Cek Koneksi Internet Anda", Toast.LENGTH_LONG).show();
                    progres.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(koneksi.key_iduser, preferences.getUsername());
                    params.put(koneksi.key_idlab, idlab_);
                    params.put(koneksi.key_idbarang, idbarang_);
                    params.put(koneksi.key_keluhan, keluhan_);
                    if (bitmap != null) {
                        params.put(koneksi.key_fotokerusakan, getStringImage(bitmap));
                    }
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    private void simpanperbaikan() {
        if (spin_lab.getSelectedItem().toString().trim().equals("Pilih")) {
            Toast.makeText(Keluhan.this, "Lab harus di tentukan", Toast.LENGTH_SHORT).show();
        } else if (spin_barang.getSelectedItem().toString().trim().equals("Pilih")) {
            Toast.makeText(Keluhan.this, "Barang harus di pilih", Toast.LENGTH_SHORT).show();
        } else {
            final String idlab_ = listID_lab.get(spin_lab.getSelectedItemPosition());
            final String idbarang_ = listID_barang.get(spin_barang.getSelectedItemPosition());
            final String keluhan_ = ed_keluhan.getText().toString();
            if (!validasi()) return;
            progres = new ProgressDialog(this);
            progres.setMessage("Proses...");
            progres.show();
            String url = simpancomplaintBarang;
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("0")) {
                        dialog();
                    } else {
//                        Toast.makeText(Keluhan.this, ""+response, Toast.LENGTH_SHORT).show();
                        Toast.makeText(Keluhan.this, "Laporan terkirim ke admin", Toast.LENGTH_SHORT).show();
                        kosong();
                        finish();
                    }
                    progres.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Keluhan.this, "Cek Koneksi Internet Anda", Toast.LENGTH_LONG).show();
                    progres.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(koneksi.key_iduser, preferences.getUsername());
                    params.put(koneksi.key_idlab, idlab_);
                    params.put(koneksi.key_idbarang, idbarang_);
                    params.put(koneksi.key_keluhan, keluhan_);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    public void dialog() {
        AlertDialog.Builder build = new AlertDialog.Builder(Keluhan.this);
        build.setMessage("Kondisi barang tersebut sudah ada yang melaporkan kepada admin dan masih dalam proses maintenance, Terimakasih");
        build.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
//                Toast.makeText(KondisiBarang.this, "ok", Toast.LENGTH_SHORT).show();
            }
        });
        build.create().show();
    }

    public void dialognohp() {
        AlertDialog.Builder build = new AlertDialog.Builder(Keluhan.this);
        build.setMessage("Apakah keluhan anda bisa di pertanggung jawabkan ?, Verifikasi nomor hp anda untuk lanjut");
        build.setPositiveButton("Konfirmasi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startLoginPage(LoginType.PHONE);
            }
        });

        build.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        build.create().show();
    }

    public void dialogsimpan() {
        AlertDialog.Builder build = new AlertDialog.Builder(Keluhan.this);
        build.setMessage("Apakah keluhan anda bisa di pertanggung jawabkan ?");
        build.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                finish();
                simpandata();

            }
        });

        build.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        build.create().show();
    }

    private void startLoginPage(LoginType loginType) {
        if (loginType == LoginType.EMAIL) {
            Intent intent = new Intent(this, AccountKitActivity.class);
            AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                    new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.EMAIL, AccountKitActivity.ResponseType.TOKEN);// use token when enable client acces token flow is YES
            intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build()); //ResponseType.CODE /TOKEN
            startActivityForResult(intent, REQUEST_CODE);
        } else if (loginType == LoginType.PHONE) {
            Intent intent = new Intent(this, AccountKitActivity.class);
            AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                    new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE, AccountKitActivity.ResponseType.TOKEN);// use token when enable client acces token flow is NO
            intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());//ResponseType.CODE / TOKEN
            startActivityForResult(intent, REQUEST_CODE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (result.getError() != null) {
                Toast.makeText(this, "" + result.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
                return;
            } else if (result.wasCancelled()) {
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
                return;

            } else {
                if (result.getAccessToken() != null) {

//                    Toast.makeText(this,"Succes ! "+result.getAccessToken().getAccountId(), Toast.LENGTH_LONG).show();
                    // Toast.makeText(this,"", Toast.LENGTH_LONG).show();
                } else

                    //  Toast.makeText(this,"", Toast.LENGTH_LONG).show();
//                    Toast.makeText(this,"Succes ! "+result.getAuthorizationCode().substring(0,10), Toast.LENGTH_LONG).show();

                    startActivity(new Intent(this, Keluhan.class));
                updatehp();
//                finish();
            }
        }

        Uri selectedImageUri = null;
        String filePath = null;
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    selectedImageUri = data.getData();
                }
                break;
            case PICK_Camera_IMAGE:
                if (resultCode == RESULT_OK) {
                    selectedImageUri = imageUri;
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        if (selectedImageUri != null) {
            try {
                String filemanagerstring = selectedImageUri.getPath();
                String selectedImagePath = getPath(selectedImageUri);

                if (selectedImagePath != null) {
                    filePath = selectedImagePath;
                } else if (filemanagerstring != null) {
                    filePath = filemanagerstring;
                } else {
                    Toast.makeText(Keluhan.this, "Unknown path",
                            Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                }

                if (filePath != null) {
                    decodeFile(filePath);
                } else {
                    bitmap = null;
                }
            } catch (Exception e) {
                Toast.makeText(Keluhan.this, "Internal error",
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }
    }


    private void updatehp() {
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(Account account) {
                final String hp = String.format(account.getPhoneNumber() == null ? "" : account.getPhoneNumber().toString());
                String url = Edithp;
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")) {
                            Toast.makeText(Keluhan.this, "Nomor HP berhasil di verifikasi", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(Keluhan.this, "Gagal ", Toast.LENGTH_SHORT).show();
                        }
                        progres.dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Keluhan.this, "Internet Kurang Stabil", Toast.LENGTH_LONG).show();
                        progres.dismiss();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put(koneksi.key_iduser, preferences.getUsername());
                        params.put(koneksi.key_nohp, hp);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }

            @Override
            public void onError(AccountKitError accountKitError) {

            }
        });

    }

    private void perizinan() {
        ActivityCompat.requestPermissions(Keluhan.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA},
                99);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 99: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    perizinan();
                }
                return;
            }
        }
    }

    void selectImage() {
            final CharSequence[] options = {"Ambil Foto"};
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    Keluhan.this);
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Ambil Foto")) {
                        String fileName = "new-photo-name.jpg";
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, fileName);
                        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
                        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                        startActivityForResult(intent, PICK_Camera_IMAGE);

                    }
    //                else if (options[item].equals("Gallery")) {
    //                    Intent intent = new Intent(Intent.ACTION_PICK);
    //                    intent.setType("image/*");
    //                    startActivityForResult(intent, PICK_IMAGE);
    //                }
                }
            });
            builder.show();
    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public void decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        final int REQUIRED_SIZE = 1024;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);
        imgKerusakan.setImageBitmap(bitmap);
        imgKerusakan.setVisibility(View.VISIBLE);

    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public void kosong() {
        defaultspinlab();
        ed_keluhan.setText("");
        imgKerusakan.setVisibility(View.GONE);

    }

    private boolean validasi() {
        boolean valid = true;
        final String id_user = ed_iduser.getText().toString();
        if (id_user.isEmpty()) {
            ed_iduser.setError("tidak boleh kosong");
            valid = false;
        } else {
            ed_iduser.setError(null);
        }


        return valid;
    }

}
