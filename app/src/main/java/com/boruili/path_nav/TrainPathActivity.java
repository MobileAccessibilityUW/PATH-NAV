package com.boruili.path_nav;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

public class TrainPathActivity extends AppCompatActivity {

    LocationManager locationManager;
    mLocationListener locationListener;
    PrintStream ps;
    ArrayList<Vector2> rawCordinates;

    public static final double RDP_EPSILON = 0.5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_path);
        String filename = getIntent().getStringExtra("FILE_NAME");
        Log.d("DIR", this.getFilesDir() + "");

        File file = new File(this.getFilesDir(), filename);
        try {
            ps = new PrintStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void start(View view) {
        locationListener = new mLocationListener();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);

    }

    public void stop(View view) {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
            return;
        }

        // simplify the raw coordinates by using RDP Algorithm
        ArrayList<Vector2> result = LineSimplification.DouglasPeucker(rawCordinates, 0, rawCordinates.size() - 1, RDP_EPSILON);
        for (int i = 0; i < result.size(); i++) {
            String coordinates = result.get(i).x + "," + result.get(i).y;
            ps.println(coordinates);

        }

        locationManager.removeUpdates(locationListener);
        locationManager = null;


        ps.close();

    }
    private class mLocationListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location loc)
        {
            if (loc != null)
            {
                rawCordinates.add(new Vector2(loc.getLongitude(), loc.getLatitude()));
                String coordinates = loc.getLongitude() + "," + loc.getLatitude();
                Log.d("[CURRENT LOCATION]", coordinates);

                // Do something knowing the location changed by the distance you requested

            }
        }

        @Override
        public void onProviderDisabled(String arg0)
        {
            // Do something here if you would like to know when the provider is disabled by the user
        }

        @Override
        public void onProviderEnabled(String arg0)
        {
            // Do something here if you would like to know when the provider is enabled by the user
        }

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2)
        {
            // Do something here if you would like to know when the provider status changes
        }
    }
}
