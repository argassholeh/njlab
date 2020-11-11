package com.sholeh.monitorig;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

import static com.sholeh.monitorig.config.koneksi.Edithp;

public class VerifikasiHp extends AppCompatActivity {

    Button btnVerifikasiHP;


    private final static int REQUEST_CODE = 999;
    Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi_hp);

        preferences = new Preferences(this);
        btnVerifikasiHP = findViewById(R.id.btn_verhp);
        btnVerifikasiHP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginPage(LoginType.PHONE);
            }
        });

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
                    updatehp();
                } else

                    //  Toast.makeText(this,"", Toast.LENGTH_LONG).show();
//                    Toast.makeText(this,"Succes ! "+result.getAuthorizationCode().substring(0,10), Toast.LENGTH_LONG).show();

//                    startActivity(new Intent(this, Keluhan.class));
                    updatehp();
//                finish();
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
                            Toast.makeText(VerifikasiHp.this, "Nomor HP berhasil di verifikasi", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplication(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {

                            Toast.makeText(VerifikasiHp.this, "Gagal ", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VerifikasiHp.this, "Internet Kurang Stabil", Toast.LENGTH_LONG).show();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put(koneksi.key_iduser, preferences.getUsername());
                        params.put(koneksi.key_nohp, hp);
                        params.put("registrasi", "iya");
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

}
