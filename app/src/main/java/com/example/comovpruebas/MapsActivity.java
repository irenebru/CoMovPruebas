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
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import com.example.comovpruebas.DataPoint;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private FusedLocationProviderClient client;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LocationCallback callback;
    private Location location;
    private TelephonyManager telephonyManager;
    private  RequestQueue queue;
    private int r;
    private int g;
    private int b;

    private int stage;

    private int idpoint;

    private int currentmnc;
    private int currentmcc;
    private int currentlac;
    private int currentcellid;

    private int currentsignal;

    private static final String FILENAME="datapoints.json";

    private List<DataPoint> listdatapoints;

    private String tech;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = LocationServices.getFusedLocationProviderClient(this);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        queue = Volley.newRequestQueue(this);
        Bundle extras = getIntent().getExtras();
        tech = extras.getString("tech");
        stage =1;
        idpoint = 0;
        currentcellid = 0;
        currentlac = 0;
        currentmcc = 0;
        currentmnc = 0;
        listdatapoints = new LinkedList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                switch (currentsignal = CellInfo()) {
                    case 0:
                        r = 204;
                        g = 255;
                        b = 255;
                        break;
                    case 1:
                        r = 51;
                        g = 153;
                        b = 255;
                        break;
                    case 2:
                        r = 0;
                        g = 204;
                        b = 204;
                        break;
                    case 3:
                        r = 0;
                        g = 51;
                        b = 102;
                        break;
                    case 4:
                        r = 0;
                        g = 102;
                        b = 51;
                        break;
                }
                location = locationResult.getLastLocation();
                int color;
                if(stage%2==0)color=Color.BLACK;
                else color=Color.WHITE;
                idpoint++;
                listdatapoints.add(new DataPoint(idpoint,currentsignal,stage,currentmnc,currentmcc,currentlac,currentcellid,tech));
                Circle cirlce = mMap.addCircle(new CircleOptions()
                        .center(new LatLng(location.getLatitude(), location.getLongitude()))
                        .radius(25)
                        .strokeColor(color)
                        .fillColor(Color.rgb(r, g, b)));


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
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydn"));
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
        LocationRequest locationRequest = new LocationRequest.Builder(2000).setMinUpdateDistanceMeters(50).setMinUpdateIntervalMillis(1000).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build();
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
        if (client != null) {
            showLocationUpdate();
        }
    }

    public void onStagePressed(View v){
        nextStage();
        String toast = getResources().getString(R.string.ToastStage)+": "+stage;
        Toast.makeText(MapsActivity.this,toast,Toast.LENGTH_SHORT).show();
        return;
    }
    private void nextStage(){
        stage++;
        return;
    }

    public void onStorePressed(View v){
        JsonObject res=new JsonObject();
        JsonArray datapointsArray=new JsonArray();
        for(DataPoint d:listdatapoints){
            datapointsArray.add(d.toJson());
        }
        res.add("datapoint",datapointsArray);
        try {
            StorageHelper.saveStringToFile(FILENAME,res.toString(),this);
        } catch (IOException e) {
            Log.e("MainActivity","Error saving file: ",e);
        }
        String toast = getResources().getString(R.string.ToastStore)+": "+stage;
        Toast.makeText(MapsActivity.this,toast,Toast.LENGTH_SHORT).show();
        return;
    }

    public int CellInfo() {
        StringBuilder text = new StringBuilder();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 0);

            return 0;
        }
        int max = 0;
        List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();
        //text.append("Found ").append(cellInfoList.size()).append(" cells\n");
        for (CellInfo info : cellInfoList) {
            if ((tech == "LTE") & info instanceof CellInfoLte) {
                CellInfoLte cellInfoLte = (CellInfoLte) info;
                CellIdentityLte id = cellInfoLte.getCellIdentity();
                markStation(id.getMcc(),id.getMnc(),id.getTac(),id.getCi());
;
                if (cellInfoLte.getCellSignalStrength().getLevel() > max) {
                    max = cellInfoLte.getCellSignalStrength().getLevel();
                    currentmnc = id.getMnc();
                    currentmcc = id.getMcc();
                    currentcellid = id.getCi();
                    currentlac = id.getTac();
                }

            } else if ((tech == "GSM") & (info instanceof CellInfoGsm)) {
                CellInfoGsm cellInfoGsm = (CellInfoGsm) info;
                CellIdentityGsm id = cellInfoGsm.getCellIdentity();
                markStation(id.getMcc(),id.getMnc(),id.getLac(),id.getCid());
                if (cellInfoGsm.getCellSignalStrength().getLevel() > max) {
                    max = cellInfoGsm.getCellSignalStrength().getLevel();
                    currentmnc = id.getMnc();
                    currentmcc = id.getMcc();
                    currentcellid = id.getCid();
                    currentlac = id.getLac();
                }
            }

        }
        return max;
    }

    private void markStation(int mcc, int mnc, int lac,int cellid) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://data.mongodb-api.com/app/data-fcpji/endpoint/db/getcellinfo?mcc=" + mcc + "&mnc=" + mnc + "&area=" + lac + "&cellid=" + cellid;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesa la respuesta JSON
                        try{
                            double lat = response.getDouble("lat");

                            double lon = response.getDouble("lon");
                            LatLng antena = new LatLng(lat, lon);
                            mMap.addMarker(new MarkerOptions().position(antena).title(getResources().getString(R.string.Antenna)));

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Maneja el error
            }
        });
        queue.add(jsonObjectRequest);
        }


}


