package com.example.hikerpath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.service.autofill.TextValueSanitizer;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView lat,lng,full,acc,alt;
    String fullAddress;


    LocationListener locationListener;
    LocationManager locationManager;
    Geocoder geocoder;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
       //

        if(requestCode == 1)
        {
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lat = (TextView) findViewById(R.id.tvLatitude);
        lng = (TextView) findViewById(R.id.tvLongitude);
        full = (TextView) findViewById(R.id.tvFullAddress);
        acc = (TextView) findViewById(R.id.tvAccuracy);
        alt = (TextView) findViewById(R.id.tvAltitude);




        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());




                try {

                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    if(addresses != null && addresses.size()>0)
                    {   fullAddress = addresses.get(0).getAdminArea()+", "+addresses.get(0).getSubAdminArea()+", "+addresses.get(0).getPostalCode();
                        Log.i("current", addresses.get(0).toString());
                        Log.i("State- ",addresses.get(0).getAdminArea());
                        if(location != null)
                        {
                            lat.setText(String.valueOf("Latitude: " +location.getLatitude()));
                            lng.setText(String.valueOf("Longitude: " +location.getLongitude()));
                            full.setText(fullAddress);
                            acc.setText("GPS Accuracy: " +String.valueOf(location.getAccuracy()));
                            alt.setText("Altitude: " +String.valueOf(location.getAltitude()));




                        }


                    }
                    else
                    {
                        Log.i("current","no location");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

        };

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


            if(lastKnownLocation != null)
            {

                Log.i("last",lastKnownLocation.toString());

                lat.setText(String.valueOf("Latitude: " +lastKnownLocation.getLatitude()));
                lng.setText(String.valueOf("Longitude: " +lastKnownLocation.getLongitude()));
                acc.setText("GPS Accuracy: " +String.valueOf(lastKnownLocation.getAccuracy()));
                alt.setText("Altitude: " +String.valueOf(lastKnownLocation.getAltitude()));




            }


        }




    }

    public void updateLocation(Location location)
    {
        Log.i("location",location.toString());
    }
}