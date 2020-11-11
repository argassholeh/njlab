package com.sholeh.monitorig;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import static com.sholeh.monitorig.config.koneksi.Cekdaftarnomor;
import static com.sholeh.monitorig.config.koneksi.key_password;
import static com.sholeh.monitorig.config.koneksi.key_username;

public class Login extends AppCompatActivity {

    private EditText ed_Username;
    private EditText ed_Password;

    private String username;
    private String password;
    private String idnya;
    private ProgressBar progress_bar;

    Preferences preferences;

    Button btnMasuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ed_Username = findViewById(R.id.edUser);
        ed_Password = findViewById(R.id.edPassword);
        progress_bar = findViewById(R.id.progress_bar);


        btnMasuk = findViewById(R.id.btnLogin);
        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        preferences = new Preferences(this);

        if (preferences.getSPSudahLogin() && (preferences.getSPStatus().equalsIgnoreCase("mahasiswa"))) {
            this.finish();
            startActivity(new Intent(Login.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));

        } else if (preferences.getSPSudahLogin() && (preferences.getSPStatus().equalsIgnoreCase("dosen"))) {
            this.finish();
            startActivity(new Intent(Login.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));

        } else if (preferences.getSPSudahLogin() && (preferences.getSPStatus().equalsIgnoreCase("dekan"))) {
            this.finish();
            startActivity(new Intent(Login.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        } else if (preferences.getSPSudahLogin() && (preferences.getSPStatus().equalsIgnoreCase("wadek"))) {
            this.finish();
            startActivity(new Intent(Login.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        } else if (preferences.getSPSudahLogin() && (preferences.getSPStatus().equalsIgnoreCase("admin"))) {
            this.finish();
            startActivity(new Intent(Login.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
//            cekdata();
        } else if (preferences.getSPSudahLogin() && (preferences.getSPStatus().equalsIgnoreCase("kaprodi"))) {
            this.finish();
            startActivity(new Intent(Login.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        } else if (preferences.getSPSudahLogin() && (preferences.getSPStatus().equalsIgnoreCase("asisten lab"))) {
            this.finish();
            startActivity(new Intent(Login.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
//            cekdata();
        }

    }

    private void userLogin() {
        username = ed_Username.getText().toString().trim();
        password = ed_Password.getText().toString().trim();

        if (!validasi()) return;
        progress_bar.setVisibility(View.VISIBLE);
        btnMasuk.setVisibility(View.GONE);
        String url = koneksi.akanLogin;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.akanLogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String aktif = "";
                            String username = "";
//                            String status = "";
                            JSONObject jsonObject;
                            jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            //  Toast.makeText(Login.this, status, Toast.LENGTH_SHORT).show();
                            JSONArray result = jsonObject.getJSONArray("Hasil");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                username = c.getString("username"); // tampilkan id alumni
                            }

                            if (status.equalsIgnoreCase("mahasiswa")) {
                                openUtama(username, status);
                                preferences.saveSPBoolean(Preferences.SP_SUDAH_LOGIN, true);
                                preferences.saveSPString(preferences.SP_Aktif, aktif);
                                preferences.saveSPString(preferences.SP_UserName, username);
                                //  Toast.makeText(Login.this, "Sukses Mahasiswa", Toast.LENGTH_LONG).show();

                            } else if (status.equalsIgnoreCase("admin")) {
                                openUtama(username, status);
                                preferences.saveSPBoolean(Preferences.SP_SUDAH_LOGIN, true);
                                preferences.saveSPString(preferences.SP_Aktif, aktif);
                                preferences.saveSPString(preferences.SP_UserName, username);
                                //   Toast.makeText(Login.this, "Sukses Admin", Toast.LENGTH_LONG).show();


                            } else if (status.equalsIgnoreCase("dekan")) {
                                openUtama(username, status);
                                preferences.saveSPBoolean(Preferences.SP_SUDAH_LOGIN, true);
                                preferences.saveSPString(preferences.SP_Aktif, aktif);
                                preferences.saveSPString(preferences.SP_UserName, username);
                                //Toast.makeText(Login.this, "Sukses dekan", Toast.LENGTH_LONG).show();


                            } else if (status.equalsIgnoreCase("wadek")) {
                                openUtama(username, status);
                                preferences.saveSPBoolean(Preferences.SP_SUDAH_LOGIN, true);
                                preferences.saveSPString(preferences.SP_Aktif, aktif);
                                preferences.saveSPString(preferences.SP_UserName, username);
                                //  Toast.makeText(Login.this, "Sukses wadek", Toast.LENGTH_LONG).show();


                            } else if (status.equalsIgnoreCase("dosen")) {
                                openUtama(username, status);
                                preferences.saveSPBoolean(Preferences.SP_SUDAH_LOGIN, true);
                                preferences.saveSPString(preferences.SP_Aktif, aktif);
                                preferences.saveSPString(preferences.SP_UserName, username);
                                //   Toast.makeText(Login.this, "Sukses Dosen", Toast.LENGTH_LONG).show();

                            } else if (status.equalsIgnoreCase("kaprodi")) {
                                openUtama(username, status);
                                preferences.saveSPBoolean(Preferences.SP_SUDAH_LOGIN, true);
                                preferences.saveSPString(preferences.SP_Aktif, aktif);
                                preferences.saveSPString(preferences.SP_UserName, username);
                                Toast.makeText(Login.this, "Sukses Kaprodi", Toast.LENGTH_LONG).show();
                            } else if (status.equalsIgnoreCase("asisten lab")) {

                                openUtama(username, status);
                                preferences.saveSPBoolean(Preferences.SP_SUDAH_LOGIN, true);
                                preferences.saveSPString(preferences.SP_Aktif, aktif);
                                preferences.saveSPString(preferences.SP_UserName, username);
                                // Toast.makeText(Login.this, "Sukses Asisten Lab", Toast.LENGTH_LONG).show();

                            } else {
                                // universal
                                openUtama(username, status);
                                preferences.saveSPBoolean(Preferences.SP_SUDAH_LOGIN, true);
                                preferences.saveSPString(preferences.SP_Aktif, aktif);
                                preferences.saveSPString(preferences.SP_UserName, username);
                                //  Toast.makeText(Login.this, "Sukses", Toast.LENGTH_LONG).show();

                            }
                            preferences.saveSPString(preferences.SP_STATUS, status);
                        } catch (JSONException e) {
                            Toast.makeText(Login.this, "User Name dan Password Salah", Toast.LENGTH_LONG).show();
                            btnMasuk.setVisibility(View.VISIBLE);
                            progress_bar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Login.this, "Tidak Terhubung Ke Server".toString(), Toast.LENGTH_LONG).show();
                        btnMasuk.setVisibility(View.VISIBLE);
                        progress_bar.setVisibility(View.GONE);


                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(key_username, username);
                map.put(key_password, password);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void openUtama(String username, String status) {

        if (status.equalsIgnoreCase("asisten lab") || preferences.getSPStatus().equalsIgnoreCase("admin")) {
            cekdata();

        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(preferences.SP_UserName, username);
            intent.putExtra(preferences.SP_STATUS, status);
            startActivity(intent);
            finish();

        }
    }

    private void cekdata() {
        String url = Cekdaftarnomor;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equalsIgnoreCase("No hp belum terdaftar")) {
                    Intent intent = new Intent(getApplication(), VerifikasiHp.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, "Cek Koneksi Internet Anda", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_iduser, preferences.getUsername());
                params.put("registrasi", "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private boolean validasi() {
        username = ed_Username.getText().toString().trim();
        password = ed_Password.getText().toString().trim();
        boolean valid = true;

        if (username.isEmpty()) {
            ed_Username.setError("tidak boleh kosong");
            valid = false;
        } else {
            ed_Username.setError(null);
        }
        if (username.isEmpty()) {
            ed_Password.setError("tidak boleh kosong");
            valid = false;
        } else {
            ed_Password.setError(null);
        }

        return valid;
    }

}
