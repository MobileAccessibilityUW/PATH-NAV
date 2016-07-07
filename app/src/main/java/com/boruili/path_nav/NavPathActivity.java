package com.boruili.path_nav;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NavPathActivity extends AppCompatActivity {

    LocationManager locationManager;
    mLocationListener locationListener;
    Scanner scan;
    int curIndex;
    List<Location> locationList;
    TextToSpeech t1;
    long timestamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_path);

        String filename = getIntent().getStringExtra("FILE_NAME");
        File file = new File(this.getFilesDir(), filename);
        Log.d("DIR", this.getFilesDir() + "");
        Log.d("FILE_NAME", filename);

        ///////// PARSE THE LOCATION //////////
        try {
            scan = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        locationList = new ArrayList<>();
        while (scan.hasNextLine()) {
            String[] rawLocation = scan.nextLine().split(",");
            Location location = new Location("");
            location.setLongitude(Double.parseDouble(rawLocation[0]));
            location.setLatitude(Double.parseDouble(rawLocation[1]));
            locationList.add(location);
        }
        curIndex = 0;
        timestamp = 0;


        /////// set up text to speech ///////
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                }
            }
        });
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
//        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
//            return;
//        }
//        locationManager.removeUpdates(locationListener);
//        locationManager = null;
//        scan.close();
//        t1.speak("finished!", TextToSpeech.QUEUE_FLUSH, null);
//        t1.stop();
    }

    @Override
    public void onBackPressed() {

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
            return;
        }
        locationManager.removeUpdates(locationListener);
        locationManager = null;
        // Otherwise defer to system default behavior.
        super.onBackPressed();
    }

    private class mLocationListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location loc)
        {
            if (loc != null)
            {
                String coordinates = loc.getLongitude() + "," + loc.getLatitude();
                Log.d("[CURRENT BEARING]", coordinates);
                Location tempLocation = locationList.get(curIndex);
                if (Math.abs(loc.getLatitude()-tempLocation.getLatitude()) <= 0.00005 ||
                        Math.abs(loc.getLongitude() - tempLocation.getLongitude()) <= 0.00005) {
                    curIndex++;
                }


                if (curIndex == locationList.size()) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
                        return;
                    }
                    locationManager.removeUpdates(locationListener);
                    locationManager = null;
                    scan.close();

                    t1.speak("finished!", TextToSpeech.QUEUE_FLUSH, null);
                    t1.stop();
                } else {
                    double expectedBearing = loc.bearingTo(locationList.get(curIndex));

                    if (System.currentTimeMillis() - timestamp > 10000 && Math.abs(loc.getBearing() - expectedBearing) > 2) {
                        timestamp = System.currentTimeMillis();
                        t1.speak("OFF by" + Math.round(loc.getBearing() - expectedBearing), TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
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
