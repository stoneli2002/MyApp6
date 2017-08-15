package com.example.lizq.myapp6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DemoSurfaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_demo_surface);
        setContentView(new DemoSurfaceView(this));
    }
}
