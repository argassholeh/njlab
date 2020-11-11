package com.sholeh.monitorig;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ViewWeb extends AppCompatActivity {

    WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_web);

        web = findViewById(R.id.web_view);
        web.setWebViewClient(new WebViewClient());
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setAppCacheEnabled(true);
        String ua =  "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
        web.getSettings().setUserAgentString(ua);
        web.loadUrl("https://ft.unuja.ac.id/");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // menampilkan panah untuk kembali ke halaman sebelumnya yg di ovveride onSupportNavigateUp() sehingga ketika di klik panahnya hilang (finish)
        getSupportActionBar().setTitle("About"); // // hanya title di activity

    }
    @Override
    public boolean onSupportNavigateUp(){
        finish(); // agar activity ini tertutup ketika di tekan tanda panah dan kembali ke activity sebelumnya
        return true;
    }


    @Override
    public void onBackPressed() {
        if(web.canGoBack()){
            web.goBack();
        }else{
            finish();
        }
    }
}