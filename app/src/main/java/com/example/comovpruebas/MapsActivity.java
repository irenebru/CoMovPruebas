package com.example.comovpruebas;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.comovpruebas.databinding.ActivityMapsBinding;

import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private FusedLocationProviderClient client;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LocationCallback callback;
    private Location location;
    private TelephonyManager telephonyManager;
    private int r;
    private int g;
    private int b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = LocationServices.getFusedLocationProviderClient(this);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                switch (CellInfo()){
                    case 0:
                        r=204;
                        g=255;
                        b=255;
                        break;
                    case 1:
                        r=51;
                        g=153;
                        b=255;
                        break;
                    case 2:
                        r=0;
                        g=204;
                        b=204;
                        break;
                    case 3:
                        r=0;
                        g=51;
                        b=102;
                        break;
                    case 4:
                        r=0;
                        g=102;
                        b=51;
                        break;
                }
                    location = locationResult.getLastLocation();
                        Circle cirlce = mMap.addCircle(new CircleOptions()
                                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                                .radius(5)
                                .strokeColor(Color.WHITE)
                                .fillColor(Color.rgb(r,g,b)));



            }
        };
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        showPosition();
        showLocationUpdate();

    }

    private void showPosition() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

            return;
        }
        mMap.setMyLocationEnabled(true);


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showPosition();
                } else {
                    Toast.makeText(this, "Need permission to work", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public void showLocationUpdate() {
        LocationRequest locationRequest = new LocationRequest.Builder(2000).setMinUpdateDistanceMeters(10).setMinUpdateIntervalMillis(1000).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

            return;
        }
        client.requestLocationUpdates(locationRequest, callback, null);


    }
    protected void onPause() {

        super.onPause();
        client.removeLocationUpdates(callback);
    }
    protected void onResume() {

        super.onResume();
        if(client!=null){
            showLocationUpdate();
        }
    }
    public int CellInfo() {
        StringBuilder text = new StringBuilder();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE}, 0);

            return 0;
        }
        int max=0;
        List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();
        //text.append("Found ").append(cellInfoList.size()).append(" cells\n");
        for(CellInfo info : cellInfoList){
            if(info instanceof CellInfoLte){
                CellInfoLte cellInfoLte=(CellInfoLte) info;
                CellIdentityLte id = cellInfoLte.getCellIdentity();
               // text.append("LTE ID:{cid: ").append(id.getCi());
              //  if (Build.VERSION.SDK_INT< Build.VERSION_CODES.P){
               //     text.append(" mcc: ").append(id.getMcc());
                //    text.append(" mnc: ").append(id.getMnc());
                //}else {
                 //   text.append(" mcc: ").append(id.getMccString());
                 //   text.append(" mnc: ").append(id.getMncString());
                //}
                //text.append(" tac: ").append(id.getTac());
               // text.append("} Level: ").append(cellInfoLte.getCellSignalStrength().getLevel()).append("\n");
                if(cellInfoLte.getCellSignalStrength().getLevel()>max)
                    max=cellInfoLte.getCellSignalStrength().getLevel();

            }
           // textView.setText(text);
        }
        return max;
    }

}
