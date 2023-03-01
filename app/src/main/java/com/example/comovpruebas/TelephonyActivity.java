package com.example.comovpruebas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TelephonyActivity extends AppCompatActivity {


    private TelephonyManager telephonyManager;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephony);
        textView = findViewById(R.id.textView4);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    }

    public void onButtonPressed(View v) {
        currentNetworkInfo();
    }

    public void currentNetworkInfo() {

        String text = "";
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);

            return;
        }
        text += telephonyManager.getVoiceNetworkType() + "\n";
        text += telephonyManager.getDataNetworkType() + "\n";
        text += telephonyManager.getSimState() + "\n";
        text += telephonyManager.getNetworkOperator();
        textView.setText(text);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    currentNetworkInfo();
                } else {
                    Toast.makeText(this, "Need permission to work", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void cellInfo(View v) {
        StringBuilder text = new StringBuilder();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE}, 0);

            return;
        }
        List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();
        text.append("Found ").append(cellInfoList.size()).append(" cells\n");
        for(CellInfo info : cellInfoList){
            if(info instanceof CellInfoLte){
                CellInfoLte cellInfoLte=(CellInfoLte) info;
                CellIdentityLte id = cellInfoLte.getCellIdentity();
                text.append("LTE ID:{cid: ").append(id.getCi());
                if (Build.VERSION.SDK_INT< Build.VERSION_CODES.P){
                    text.append(" mcc: ").append(id.getMcc());
                    text.append(" mnc: ").append(id.getMnc());
                }else {
                    text.append(" mcc: ").append(id.getMccString());
                    text.append(" mnc: ").append(id.getMncString());
                }
                text.append(" tac: ").append(id.getTac());
                text.append("} Level: ").append(cellInfoLte.getCellSignalStrength().getLevel()).append("\n");

            }
            textView.setText(text);
        }
    }
}