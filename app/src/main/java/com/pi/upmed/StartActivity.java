package com.pi.upmed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.pi.upmed.view_med.MainActivityMed;
import com.pi.upmed.view_pac.MainActivityPac;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        FrameLayout frameLayoutMed = (FrameLayout) findViewById(R.id.select_app_med);
        //frameLayoutMed.setBackgroundColor(0xFFFFF2CD);#D1C4E9
        frameLayoutMed.setBackgroundColor(0xFFD1C4E9);
        FrameLayout frameLayoutPac = (FrameLayout) findViewById(R.id.select_app_pac);
        //frameLayoutPac.setBackgroundColor(0xFFFFF2CD);
        frameLayoutPac.setBackgroundColor(0xFFD1C4E9);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // getIntent() should always return the most recent
        setIntent(intent);
    }

    public void clickMed(View view) {

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.select_app_med);
        frameLayout.setBackgroundColor(Color.WHITE);

        Intent intent = new Intent(this, MainActivityMed.class);
        startActivity(intent);
    }

    public void clickPac(View view) {

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.select_app_pac);
        frameLayout.setBackgroundColor(Color.WHITE);

        Intent intent = new Intent(this, MainActivityPac.class);
        startActivity(intent);
    }
}
