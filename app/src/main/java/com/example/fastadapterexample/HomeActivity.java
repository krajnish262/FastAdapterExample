package com.example.fastadapterexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    public void fast_adapter_click(View view) {
        startActivity(new Intent(this,MainActivity.class));
    }

    public void image_glide(View view) {
    }

}
