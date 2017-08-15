package com.example.lizq.myapp6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;

public class WebViewActivity extends AppCompatActivity {

    private EditText pathET;
    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        pathET = (EditText) findViewById(R.id.pathET);
        webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setBuiltInZoomControls(true);	// 放大缩小按钮
        webView.getSettings().setJavaScriptEnabled(true);		// JS允许
        webView.setWebChromeClient(new WebChromeClient());		// Chrome内核

    }

    public void onClick(View view){
        webView.loadUrl(pathET.getText().toString());
//    	startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(pathET.getText().toString())));
    }
}
