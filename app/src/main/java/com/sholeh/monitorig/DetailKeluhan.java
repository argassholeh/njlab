package com.sholeh.monitorig;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.sholeh.monitorig.config.koneksi.EditComplaint;
import static com.sholeh.monitorig.config.koneksi.UpdateMaintenance;
import static com.sholeh.monitorig.config.koneksi.key_fotomaintenance;

public class DetailKeluhan extends AppCompatActivity implements View.OnClickListener {

    ArrayList<HashMap<String, String>> tampil = new ArrayList<HashMap<String, String>>();
    Toolbar toolBarisi;

    Preferences preferences;
    GlidModuleMe moduleMe = new GlidModuleMe();

    String image;
    TextView tvx_iduser, tvxnama, tvxnamalab, tvxnamabarang, tvxkeluhan,
            tvxtanggal, tvkeluhan, tvxnohp, viduser, vnama, tvxstatusbarang,
            tvxcek, tvperbaiki, tvidperbaiki, tvfotoperbaiki;
    View vkeluhan, vnohp, vkerusakan;
    private ImageView detailfotouser, imgTelp, imgWA, imgPesan, imgkerusakan, imghasilmaintenance;
    String statusc;
    Button btnSelesai, btnperbaiki, btn_aksesasisten, btn_fotomaintenance, btn_cek, btn_operator;
    public String idlab = "";
    public String idbarang = "";
    String imgrusak_ = "";
    String keluhan = "";
    private ProgressDialog progres;

    ArrayList<String> namaaslab;

    LinearLayout ln_imageuser, ln_btncek, ln_tvcek, ln_operator;
    Bitmap bitmap = null;
    Uri imageUri;
    private static final int PICK_IMAGE = 1;
    private static final int PICK_Camera_IMAGE = 2;

    private static int currentPosition = 0;
    private boolean buka = true;

    TextView tv_operator;
    String[] dataItems;
    String[] dataIdItems;
    String[] dataOperatorM;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    JSONArray idkerem = new JSONArray();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailkeluhan);
        toolBarisi = findViewById(R.id.toolbar);
        toolBarisi.setTitle("Detail Complaint");
        setSupportActionBar(toolBarisi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = new Preferences(this);

        detailfotouser = findViewById(R.id.img_detailfotodk);
        tvx_iduser = findViewById(R.id.tv_iduserdk);
        tvxnama = findViewById(R.id.tv_namadk);
        tvxnamalab = findViewById(R.id.tv_namalabdk);
        tvxnamabarang = findViewById(R.id.tv_barangdk);
        tvxnohp = findViewById(R.id.tv_nohp);
        tvxstatusbarang = findViewById(R.id.tvstatusbarang);
        tvxcek = findViewById(R.id.tvcek);
        tvperbaiki = findViewById(R.id.tvn_perbaiki);
        tvidperbaiki = findViewById(R.id.tv_idperbaiki);
        tvfotoperbaiki = findViewById(R.id.tv_fotosudah);
        ln_operator = findViewById(R.id.ln_operator);

        imgWA = findViewById(R.id.imgWA);
        imgPesan = findViewById(R.id.imgPesan);
        imgTelp = findViewById(R.id.imgTelp);
        imgkerusakan = findViewById(R.id.img_detailkerusakan);
        imghasilmaintenance = findViewById(R.id.img_fotoperbaiki);
        ln_imageuser = findViewById(R.id.ln_imageuser);
        ln_btncek = findViewById(R.id.ln_btncek);
        ln_btncek.setOnClickListener(this);
        ln_tvcek = findViewById(R.id.ln_tvcek);
        ln_tvcek.setOnClickListener(this);
        viduser = findViewById(R.id.viduser);
        vnama = findViewById(R.id.v_nama);

        tvxkeluhan = findViewById(R.id.tv_keluhandk);
        tvxtanggal = findViewById(R.id.tv_tanggaldk);
        tvkeluhan = findViewById(R.id.tv_keluhann);
        vkeluhan = findViewById(R.id.v_keluhan);
        vnohp = findViewById(R.id.vnohp);
        vkerusakan = findViewById(R.id.v_fotokerusakan);
        tv_operator = findViewById(R.id.tvItemSelected);

        btnSelesai = findViewById(R.id.btnselesaidk);
        btnSelesai.setOnClickListener(this);
        btnperbaiki = findViewById(R.id.btnperbaikan);
        btnperbaiki.setOnClickListener(this);
        btn_aksesasisten = findViewById(R.id.btnaksesperbaiki);
        btn_aksesasisten.setOnClickListener(this);
        btn_fotomaintenance = findViewById(R.id.btnfotoperbaiki);
        btn_fotomaintenance.setOnClickListener(this);
        btn_cek = findViewById(R.id.btncek);
        btn_cek.setOnClickListener(this);
        btn_operator = findViewById(R.id.btntambah_operator);
        btn_operator.setOnClickListener(this);
        tampilDetailComplaint(getIntent().getStringExtra("id keluhan"));

        ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_PHONE_NUMBERS},
                101);


        imgTelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tvxnohp.getText().toString())));
            }
        });

        imgPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.VIEW");
                Uri data = Uri.parse("sms:" + tvxnohp.getText().toString());
                intent.setData(data);
                startActivity(intent);
            }
        });

        imgWA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.whatsapp.com/send?phone=" + tvxnohp.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        aksestampilAsistenlab();
        tampilOperator();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnselesaidk:
                updatecomplaint(getIntent().getStringExtra("id keluhan"));
                break;
            case R.id.btnaksesperbaiki:
                aksesperbaiki();
                break;
            case R.id.btnfotoperbaiki:
                selectImage();
                break;
            case R.id.btnperbaikan:
                updatemaintenance(getIntent().getStringExtra("id maintenance"));
                break;
            case R.id.btncek:
                pengecekan();
                break;
            case R.id.ln_btncek:
                pengecekan();
                break;
            case R.id.ln_tvcek:
                pengecekan();
                break;
            case R.id.btntambah_operator:
                operator();
                break;
            default:
                break;
        }

    }


    public void tampilDetailComplaint(final String idkeluhan) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.tampilDetailComplaint,
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

                                    GlideApp.with(DetailKeluhan.this)
                                            .load(koneksi.tampilFotoP + c.getString("foto"))
                                            .placeholder(R.drawable.ic_orang_grey_24dp)
                                            .into(detailfotouser);
                                    GlideApp.with(DetailKeluhan.this)
                                            .load(koneksi.tampilFotoTrouble + c.getString("foto_kerusakan"))
                                            .placeholder(R.drawable.no_image)
                                            .into(imgkerusakan);
                                    //bermasalah
//                                    GlideApp.with(DetailKeluhan.this)
//                                            .load(koneksi.tampilFotoTrouble + c.getString("foto_maintenance"))
//                                            .placeholder(R.drawable.no_image)
//                                            .into(imghasilmaintenance);

                                    imgrusak_ = c.getString("foto_kerusakan");
//                                    tvidperbaiki.setText(c.getString("id_maintenance"));
                                    tvx_iduser.setText(c.getString("id_user"));
                                    tvxnama.setText(c.getString("nama"));
                                    tvxnohp.setText(c.getString("no_hp"));
                                    idlab = c.getString("id_lab");
                                    tvxnamalab.setText(c.getString("nama_lab"));
                                    idbarang = c.getString("id_barang");
                                    tvxnamabarang.setText(c.getString("nama_barang"));
                                    keluhan = c.getString("keluhan");
                                    tvxkeluhan.setText(keluhan);
                                    tvxtanggal.setText(c.getString("tanggal"));
                                    statusc = c.getString("nama_status");
                                    String status = c.getString("status");
                                    if (status.equalsIgnoreCase("m")) {
                                        tvxstatusbarang.setText("Sudah di perbaiki");
                                        btn_aksesasisten.setVisibility(View.GONE);

                                    } else {
                                        tvxstatusbarang.setText("Bermasalah");
                                        btn_cek.setVisibility(View.GONE);
                                    }

                                }
                                aksesdetail();
                                if (imgrusak_.equalsIgnoreCase("null") || keluhan.equalsIgnoreCase("null")) {
                                    tvxkeluhan.setVisibility(View.GONE);
                                    vkeluhan.setVisibility(View.GONE);
                                } else {
                                    imgkerusakan.setVisibility(View.VISIBLE);
                                    vkerusakan.setVisibility(View.VISIBLE);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(getApplication(), response, Toast.LENGTH_SHORT).show();
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
                params.put("id_trouble", idkeluhan);


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
        requestQueue.add(stringRequest);
    }

    public void tampilOperatorMaintenance(final String idmaintenance) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.tampilOperatorMaintenance,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("1")) {
                            namaaslab = new ArrayList<>();
                            try {

                                JSONObject jsonObject;
                                jsonObject = new JSONObject(response);
                                JSONArray result = jsonObject.getJSONArray("Hasil");
                                tampil.clear();
//                                dataOperatorM = new String[result.length()];
                                String item="";
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject c = result.getJSONObject(i);
                                    item +=c.getString("nama");
                                    if (i != result.length() - 1) { // jika i sudah sama dengan panjang data maka berhentilah menambah,
                                        item = item + ", ";
                                    }

                                    //bermasalah
                                    GlideApp.with(DetailKeluhan.this)
                                            .load(koneksi.tampilFotoTrouble + c.getString("foto_maintenance"))
                                            .placeholder(R.drawable.no_image)
                                            .into(imghasilmaintenance);
                                }
                                tvidperbaiki.setText(item);




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(getApplication(), response, Toast.LENGTH_SHORT).show();
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
                params.put("id_maintenance", idmaintenance);


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
        requestQueue.add(stringRequest);
    }

    public void tampilOperator() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.tampilOperator,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("1")) {
                            namaaslab = new ArrayList<>();
                            try {
                                JSONObject jsonObject;
                                jsonObject = new JSONObject(response);
                                JSONArray result = jsonObject.getJSONArray("Hasil");
                                tampil.clear();
                                dataItems = new String[result.length()];
                                dataIdItems = new String[result.length()];
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject c = result.getJSONObject(i);
                                    String iduser = c.getString("id_user");
                                    String nama = c.getString("nama");
                                    dataItems[i] = nama;
                                    dataIdItems[i] = iduser;
                                }
                                checkedItems = new boolean[dataItems.length];
//                                dataItems = getResources().getStringArray(namaaslab); // tampil data

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(getApplication(), response, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailKeluhan.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
        requestQueue.add(stringRequest);
    }

    private void updatecomplaint(final String idkeluhan) {
        final String idlab_ = idlab;
        final String idbarang_ = idbarang;
        progres = new ProgressDialog(this);
        progres.setMessage("Proses...");
        progres.show();
        String url = EditComplaint;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("1")) {
                    Toast.makeText(DetailKeluhan.this, "Berhasil", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getApplication(), DataKeluhan.class);
//                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(DetailKeluhan.this, "Gagal", Toast.LENGTH_SHORT).show();
                }
                progres.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailKeluhan.this, "Internet Kurang Stabil", Toast.LENGTH_LONG).show();
                progres.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_idkeluhan, idkeluhan);
                params.put(koneksi.key_idlab, idlab_);
                params.put(koneksi.key_idbarang, idbarang_);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void updatemaintenance(final String idmaintenance) {
        progres = new ProgressDialog(this);
        progres.setMessage("Proses...");
        progres.show();
        String url = UpdateMaintenance;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                if (response.contains("1")) {
                    Toast.makeText(DetailKeluhan.this, "Berhasil", Toast.LENGTH_SHORT).show();
                    finish();
//                } else {
//                    Toast.makeText(DetailKeluhan.this, "pilih foto "+response, Toast.LENGTH_SHORT).show();
//                }

                progres.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(DetailKeluhan.this, "Gagal"+error, Toast.LENGTH_LONG).show();
                progres.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_maintenance", idmaintenance);
                params.put("id_user", idkerem.toString());
                params.put(key_fotomaintenance, getStringImage(bitmap));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                    Toast.makeText(DetailKeluhan.this, "Unknown path",
                            Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                }

                if (filePath != null) {
                    decodeFile(filePath);
                } else {
                    bitmap = null;
                }
            } catch (Exception e) {
                Toast.makeText(DetailKeluhan.this, "Internal error",
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }
    }


    private void selectImage() {
        final CharSequence[] options = {"Ambil Foto"};
        AlertDialog.Builder builder = new AlertDialog.Builder(
                DetailKeluhan.this);
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
        imghasilmaintenance.setImageBitmap(bitmap);
        imghasilmaintenance.setVisibility(View.VISIBLE);

        ln_operator.setVisibility(View.VISIBLE);
        imgkerusakan.setVisibility(View.GONE);
        vkerusakan.setVisibility(View.GONE);

    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void operator() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(DetailKeluhan.this);
        mBuilder.setTitle(R.string.dialog_title);
        mBuilder.setMultiChoiceItems(dataItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if (isChecked) {
                    mUserItems.add(position); // true
                } else {
                    mUserItems.remove((Integer.valueOf(position))); // false

                }
//
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                idkerem = new JSONArray();
                for (int i = 0; i < mUserItems.size(); i++) {
                    item = item + dataItems[mUserItems.get(i)];
                    if (i != mUserItems.size() - 1) {
                        item = item + ", ";
                    }
                    idkerem.put(dataIdItems[i]);
                }
//                for (int i = 0; i < mUserItems.size(); i++) {
//                    item = item + dataIdItems[mUserItems.get(i)];
//                    if (i != mUserItems.size() - 1) {
//                        item = item + ", ";
//                    }
//                }



                tv_operator.setVisibility(View.VISIBLE);
                tv_operator.setText(item);
                btnperbaiki.setVisibility(View.VISIBLE);
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }


    public void aksesdetail() {
        // tampil detailnya berdasarkan status yg user yg complaint
        if (statusc.equalsIgnoreCase("mahasiswa")) {
            tvxkeluhan.setVisibility(View.GONE);
            vkeluhan.setVisibility(View.GONE);
        } else if (statusc.equalsIgnoreCase("asisten lab")) {
            tvxkeluhan.setVisibility(View.GONE);
            vkeluhan.setVisibility(View.GONE);
            imgTelp.setVisibility(View.GONE);
            imgWA.setVisibility(View.GONE);
            imgPesan.setVisibility(View.GONE);
            btnSelesai.setVisibility(View.GONE);
        } else {
            //admin

            vnohp.setVisibility(View.GONE);
            tvxnohp.setVisibility(View.GONE);
            imgTelp.setVisibility(View.GONE);
            imgWA.setVisibility(View.GONE);
            imgPesan.setVisibility(View.GONE);
        }
    }

    public void aksestampilAsistenlab() {
        preferences.getSPStatus();
        if (preferences.getSPStatus().equalsIgnoreCase("asisten lab")) {
            ln_imageuser.setVisibility(View.GONE);
            tvx_iduser.setVisibility(View.GONE);
            viduser.setVisibility(View.GONE);
            tvxnama.setVisibility(View.GONE);
            vnama.setVisibility(View.GONE);
            tvxnohp.setVisibility(View.GONE);
            vnohp.setVisibility(View.GONE);
            imgTelp.setVisibility(View.GONE);
            imgWA.setVisibility(View.GONE);
            imgPesan.setVisibility(View.GONE);
            btnSelesai.setVisibility(View.GONE);
            btn_aksesasisten.setVisibility(View.VISIBLE);
        } else {
            // admin
            ln_btncek.setVisibility(View.VISIBLE);
            ln_tvcek.setVisibility(View.VISIBLE);
        }
    }

    public void aksesperbaiki() {
        btn_aksesasisten.setVisibility(View.GONE);
        btn_fotomaintenance.setVisibility(View.VISIBLE);
//        btnperbaiki.setVisibility(View.VISIBLE); TAMPILKAN SETELAH MEMIMILH IMAGE
    }

    public void pengecekan() {
        if (buka) {
            tampilOperatorMaintenance(getIntent().getStringExtra("id maintenance"));
            buka = false;
            imgkerusakan.setVisibility(View.GONE);
            vkerusakan.setVisibility(View.GONE);
            imghasilmaintenance.setVisibility(View.VISIBLE);
            tvperbaiki.setVisibility(View.VISIBLE);
            tvidperbaiki.setVisibility(View.VISIBLE);
            tvfotoperbaiki.setVisibility(View.VISIBLE);
        } else {
            buka = true;
            imgkerusakan.setVisibility(View.VISIBLE);
            vkerusakan.setVisibility(View.VISIBLE);
            imghasilmaintenance.setVisibility(View.GONE);
            tvperbaiki.setVisibility(View.GONE);
            tvidperbaiki.setVisibility(View.GONE);
            tvfotoperbaiki.setVisibility(View.GONE);
        }

    }


}
