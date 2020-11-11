package com.sholeh.monitorig;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import java.util.HashMap;
import java.util.Map;

import static com.sholeh.monitorig.config.koneksi.key_password;
import static com.sholeh.monitorig.config.koneksi.key_username;

public class Akun extends AppCompatActivity implements View.OnClickListener {
    Button masukpass;
    EditText ed_username, password;
    Toolbar toolBarisi;
    Preferences preferences;
    private ProgressDialog progres;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun);
        toolBarisi =  findViewById(R.id.toolbar);
        toolBarisi.setTitle("Akun");
        setSupportActionBar(toolBarisi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        masukpass = findViewById(R.id.btn_masukpass);
        masukpass.setOnClickListener(this);
        ed_username = findViewById(R.id.et_usernameakun);
        password = findViewById(R.id.et_password_akun);
        preferences = new Preferences(this);
        String getuser = preferences.getUsername();
        ed_username.setText(getuser);

    }
    @Override
    public boolean onSupportNavigateUp(){
        finish(); //  kalau tanpa ini tombol panahnya tak berfungsi
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_masukpass:
                userLogin();
                break;
            default:
                break;
        }

    }

    private void userLogin() {
        final String username_  = ed_username.getText().toString().trim();
        final String password_ = password.getText().toString().trim();

//        if (!validasi()) return;
        progres = new ProgressDialog(this);
        progres.setMessage("Proses...");
        progres.show();
        String url = koneksi.akanLogin;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.akanLogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("1")) {
                            Intent intent = new Intent(getApplication(),Verivikasi.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Akun.this, "Invalid password", Toast.LENGTH_SHORT).show();
                            progres.dismiss();
                        }
                        progres.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Akun.this, error.toString(), Toast.LENGTH_LONG).show();
                        progres.dismiss();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(key_username, username_);
                map.put(key_password, password_);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



}
