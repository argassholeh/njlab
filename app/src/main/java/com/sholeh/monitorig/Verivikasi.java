package com.sholeh.monitorig;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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


public class Verivikasi extends AppCompatActivity implements View.OnClickListener {
    EditText ed_newpass, ed_repassword;
    Toolbar toolBarisi;
    private ProgressDialog progres;
    Button ubahpass;
    RelativeLayout relativeLayout;
    Preferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verivikasi);

        toolBarisi =  findViewById(R.id.toolbar);
        toolBarisi.setTitle("Verivikasi");
        setSupportActionBar(toolBarisi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        relativeLayout = findViewById(R.id.rel_verivifikasi);
        preferences = new Preferences(this);

        ubahpass = findViewById(R.id.btn_changepass);
        ubahpass.setOnClickListener(this);
        ed_newpass = findViewById(R.id.et_newpassword);
        ed_repassword = findViewById(R.id.et_reenternew);


    }
    @Override
    public boolean onSupportNavigateUp(){
        finish(); //  kalau tanpa ini tombol panahnya tak berfungsi
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_changepass:
                ubahPass();
                break;
            default:
                break;
        }

    }
    public void ubahPass(){
        String newpass_ = ed_newpass.getText().toString();
        final String reenter_ = ed_repassword.getText().toString();

        if (newpass_.equals(reenter_)){
//            Toast.makeText(this, "sama", Toast.LENGTH_SHORT).show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.Editpass,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("1")) {
                                Toast.makeText(Verivikasi.this, "Berhasil", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(Verivikasi.this, "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Verivikasi.this, "Verifikasi", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id_user",preferences.getUsername());
                    params.put(koneksi.key_password,reenter_);

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }else{
            ed_repassword.setError("Please re-enter a new password again");
        }
    }

}


