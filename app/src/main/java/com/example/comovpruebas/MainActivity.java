package com.example.comovpruebas;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void openSecondActivity(View v){
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);

    }
    public void openLocActivity(View v){
        Intent intent = new Intent(this,LocationActivity.class);
        startActivity(intent);
    }
    public void openMapsActivity(View v){
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);
    }
    public void openTelephonyActivity(View v){
        Intent intent = new Intent(this,TelephonyActivity.class);
        startActivity(intent);
    }
    public void onPressedButton1(View v) {
        openMapsActivity(v);
    }
}