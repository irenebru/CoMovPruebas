package com.example.comovpruebas;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {
    private String tech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CompoundButton check =(CompoundButton)findViewById(R.id.toggleButton2);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) tech ="LTE";
                else tech = "GSM";
            }
        });
        check.setChecked(true);

    }


    public void openMapsActivity(View v,String tech){
        Intent intent = new Intent(this,MapsActivity.class);
        intent.putExtra("tech",tech);
        startActivity(intent);
    }
    public void openTelephonyActivity(View v){
        Intent intent = new Intent(this,TelephonyActivity.class);
        startActivity(intent);
    }
    public void onPressedButton1(View v) {
        openMapsActivity(v,tech);
    }
}