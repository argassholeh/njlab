package com.sholeh.monitorig;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sholeh.monitorig.mrecycler.ModelLab;
import com.sholeh.monitorig.mrecycler.adapterlab;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sholeh.monitorig.config.koneksi.tampilAkun;
import static com.sholeh.monitorig.config.koneksi.tampilFotoAkun;
import static com.sholeh.monitorig.config.koneksi.tampilItemLab;

//n sebenarnya Anda tidak ingin membuka lagi ketika tombol kembali menekan Aktivitas Layar Splash, Aktivitas Layar Selamat Datang, ?
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Fragment fragment;

    Preferences preferences;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButtonuser, floatingActionButtonlab, floatingActionButtonbarang, floatingActionButtonkondisibarang, floatingActionButtonkeluhan;
    FloatingActionButton fabdosen, fabMahsiswa, fabAsistenlab;

    ArrayList<ModelLab> daftar_lab = new ArrayList<ModelLab>();
    adapterlab adapter_lab;
    RecyclerView recycler;
    View v;

    CircleImageView cimageprofile;
    TextView tvMenunama, tvMenuId;

    private int request_code = 1;
    private Bitmap bitmap;
    RequestQueue requestQueue; //permitara la conexion directamente del web service
    StringRequest stringRequest;
    private final static int REQUEST_CODE = 999;

    private KProgressHUD progressDialogHud;
    LinearLayout ln_Kosong;

    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    boolean doubleBackToExitPressedOnce = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        perizinan();

        progressDialogHud = KProgressHUD.create(MainActivity.this);
        ln_Kosong = findViewById(R.id.lnKosong);

        recycler= findViewById(R.id.rvViewLab);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        preferences = new Preferences(this);

//        Toast.makeText(this, ""+preferences.getSPStatus(), Toast.LENGTH_SHORT).show();
        materialDesignFAM =  findViewById(R.id.material_design_android_floating_action_menu);
//
        if(preferences.getSPStatus().equalsIgnoreCase("admin")){
            materialDesignFAM.setVisibility(View.VISIBLE);
        }
        fabdosen = findViewById(R.id.fab_dosen);
        fabdosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Keluhan.class);
                startActivity(intent);

            }
        });
        fabMahsiswa = findViewById(R.id.fab_mahasiswa);
        fabMahsiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Keluhan.class);
                startActivity(intent);

            }
        });

        fabAsistenlab = findViewById(R.id.fab_asistenlab);
        fabAsistenlab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Keluhan.class);
                startActivity(intent);

            }
        });


        if(preferences.getSPStatus().equalsIgnoreCase("mahasiswa")){
            fabMahsiswa.setVisibility(View.VISIBLE);
        }else if(preferences.getSPStatus().equalsIgnoreCase("admin")||preferences.getSPStatus().equalsIgnoreCase("mahasiswa")){
            fabdosen.setVisibility(View.GONE);
//            cekdata();
        }else if(preferences.getSPStatus().equalsIgnoreCase("asisten lab")||preferences.getSPStatus().equalsIgnoreCase("mahasiswa")){
            fabdosen.setVisibility(View.GONE);
            fabMahsiswa.setVisibility(View.GONE);
            fabAsistenlab.setVisibility(View.VISIBLE);
//            cekdata();
        }

        floatingActionButtonlab = findViewById(R.id.material_design_floating_action_menu_lab);
        floatingActionButtonlab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //untuk aksi ketika di klik
                Intent intent = new Intent(getApplication(), lab.class);
                startActivity(intent);

            }
        });

        fabAsistenlab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //untuk aksi ketika di klik
                Intent intent = new Intent(getApplication(), DataKeluhan.class);
                startActivity(intent);

            }
        });

        floatingActionButtonbarang = findViewById(R.id.material_design_floating_action_menu_databarang);
        floatingActionButtonbarang.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //untuk aksi ketika di klik
                Intent intent = new Intent(getApplication(), Barang.class);
                startActivity(intent);

            }
        });
        floatingActionButtonuser = findViewById(R.id.material_design_floating_action_menu_user);
        floatingActionButtonuser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //untuk aksi ketika di klik
                Intent intent = new Intent(getApplication(), User.class);
                startActivity(intent);

            }
        });
        floatingActionButtonkondisibarang = findViewById(R.id.material_design_floating_action_menu_kondisi);
        floatingActionButtonkondisibarang.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //untuk aksi ketika di klik
                Intent intent = new Intent(getApplication(), KondisiBarang.class);
                startActivity(intent);

            }
        });

        floatingActionButtonkeluhan = findViewById(R.id.material_design_floating_action_menu_keluhan);
        floatingActionButtonkeluhan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), DataKeluhan.class);
                startActivity(intent);

            }
        });


        View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);
        tvMenunama = header.findViewById(R.id.tv_menunama);
        tvMenuId = header.findViewById(R.id.tv_menu_id);
        tvMenuId.setText(preferences.getUsername());

        requestQueue = Volley.newRequestQueue(this);
        cimageprofile =header.findViewById(R.id.image_menu_profile); // R.id.image_menu nav header main
        cimageprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ImageProfile.class);
                startActivity(intent);
            }
        });

        tampilLab();
    }

    private void ProgresDialog(){
        progressDialogHud.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Memuat Data...")
                .setCancellable(false);
        progressDialogHud.show();
    }

    private void perizinan() {
        ActivityCompat.requestPermissions(MainActivity.this,
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


    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//           keluar();
//        }
        //

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    @Override
    public void onResume()
    {
        tampilakun(); // biar reload data atau refresh ketika sudah ganti foto profile
        super.onResume();
        // Load data and do stuff
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_aboutUnuja) {
            Intent it = new Intent(MainActivity.this,ViewWeb.class);
            startActivity(it);
        } else if (id == R.id.nav_akun) {
            Intent it = new Intent(MainActivity.this,Akun.class);
            startActivity(it);
        } else if (id == R.id.nav_help) {
            Intent it = new Intent(MainActivity.this, About.class);
            startActivity(it);
        } else if (id == R.id.nav_exit) {
            logout();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public  void logout (){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakah anda yakin, ingin logout akun ?");
        builder.setCancelable(true);
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                preferences.saveSPBoolean(preferences.SP_SUDAH_LOGIN, false);
                finish();
                Toast.makeText(getApplication(), "Berhasil Keluar", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplication(), Login.class));
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void keluar(){
        AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this);
        build.setMessage("Anda yakin ingin keluar ?");
        build.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
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

    private void tampilLab() {
        ProgresDialog();
        String url = tampilItemLab ;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("1")) {
                            try {
                                JSONObject jsonObject;
                                jsonObject = new JSONObject(response);
                                JSONArray result = jsonObject.getJSONArray("Hasil");
                                daftar_lab.clear();
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject c = result.getJSONObject(i);
                                    String id = c.getString("id_lab");
                                    String judul = c.getString("nama_lab");
                                    String image = c.getString("foto_lab");
                                    daftar_lab.add(new ModelLab(id, judul,image));

                                }
                                adapter_lab= new adapterlab(daftar_lab);
                                recycler.setAdapter(adapter_lab);
                                recycler.setVisibility(View.VISIBLE);
                                ln_Kosong.setVisibility(View.GONE);
                                progressDialogHud.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                recycler.setVisibility(View.GONE);
                                ln_Kosong.setVisibility(View.VISIBLE);
                                progressDialogHud.dismiss();
                            }

                        }else{
                            recycler.setVisibility(View.GONE);
                            ln_Kosong.setVisibility(View.VISIBLE);
                            progressDialogHud.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        recycler.setVisibility(View.GONE);
                        ln_Kosong.setVisibility(View.VISIBLE);
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
    public void tampilakun() {
        String url = tampilAkun;
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
                                    String username = c.getString("nama");
                                    tvMenunama.setText(username);
                                    // jika glid app error maka di rebuild project
                                    GlideApp.with(MainActivity.this)
                                            .load(tampilFotoAkun+ c.getString("foto"))
                                            .placeholder(R.drawable.profile)
                                            .into(cimageprofile);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),""+ error, Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", preferences.getUsername());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
