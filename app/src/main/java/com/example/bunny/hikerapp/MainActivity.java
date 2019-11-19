package com.example.bunny.hikerapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    TextView longi, lati, adress;

    LocationManager locationManager;
    LocationListener locationListener;
    Location location;
    double lo , lt;



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }

            }

        }



    }


    public void refresh(View view){

        lati  =(TextView)findViewById(R.id.lati);
        longi = (TextView)findViewById(R.id.lon);
        adress = (TextView)findViewById(R.id.address);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.i("LOCATION.......",location.toString());

                lo =  location.getLongitude();
                lt = location.getLatitude();

                lati.setText(Double.toString(lo));
                longi.setText(Double.toString(lt));

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> add =  geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                    if(add != null && add.size() > 0){


                        String addresss = "";

                        if(add.get(0).getCountryName() != null){
                            addresss += "Country : " + add.get(0).getCountryName() + "\n";
                            if(add.get(0).getAdminArea() != null) {
                                addresss += "State : " + add.get(0).getAdminArea() + "\n";
                                if (add.get(0).getLocality() != null) {
                                    addresss += "City : " + add.get(0).getLocality() + "\n";
                                    if (add.get(0).getPostalCode() != null){
                                        addresss +="PostalCode : "+add.get(0).getPostalCode()+"";


                                    }

                                }
                            }
                        }
                        adress.setText(addresss);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void map(View view){

        Intent intent = new Intent(this,MapsActivity.class);
        intent.putExtra("long",lo);
        intent.putExtra("lati",lt);
        startActivity(intent);


    }



}
