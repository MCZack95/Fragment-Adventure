package com.example.fragment_adventure;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    Button searchButton, backButton;
    private GoogleApiClient googleApiClient;
    private LatLng defaultLatLng = new LatLng(1.344233,103.680142);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        setCurrentLocation();
    }

    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onConnected(@Nullable Bundle bundle) {}

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    protected synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    public void searchLocation(View v){
        mapReset();
        EditText location_tf = findViewById(R.id.searchBox);
        String location = location_tf.getText().toString();
        List<Address> addressList = null;

        if(!location.isEmpty()){
            Geocoder geocoder = new Geocoder(v.getContext());
            try{
                addressList = geocoder.getFromLocationName(location,1);
            }catch(IOException e){
                e.printStackTrace();
            }
            if(addressList != null){
                try{
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                    Log.e("Latitude",String.valueOf(address.getLatitude()));
                    Log.e("Longitude",String.valueOf(address.getLongitude()));
                    Log.e("DISTANCE",String.valueOf(getDistance(latLng)));
                    String toastString = "DISTANCE = " + String.valueOf(getDistance(latLng));
                    Toast.makeText(v.getContext(),toastString,Toast.LENGTH_LONG).show();
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Target"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
                } catch(IndexOutOfBoundsException e){
                    Toast.makeText(getApplicationContext(),"Location not found",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(),"Location not found",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setCurrentLocation(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
        mBuilder.setCancelable(true);
        mBuilder.setTitle("Set Current Location:");
        // Set up the input
        final EditText input = new EditText(MapsActivity.this);
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        mBuilder.setView(input);

        // Set up the buttons
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();
                List<Address> addressList = null;

                if(!m_Text.isEmpty()){
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try{
                        addressList = geocoder.getFromLocationName(m_Text,1);
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    if(addressList != null){
                        try{
                            Address address = addressList.get(0);
                            defaultLatLng = new LatLng(address.getLatitude(),address.getLongitude());
                            Log.e("Latitude",String.valueOf(address.getLatitude()));
                            Log.e("Longitude",String.valueOf(address.getLongitude()));
                            mMap.addMarker(new MarkerOptions().position(defaultLatLng).title("Current Location"));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng,17));
                        } catch(IndexOutOfBoundsException e){
                            Toast.makeText(MapsActivity.this,"Location not found",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MapsActivity.this,"Location not found",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });

        mBuilder.show();
    }

    public void mapReset(){
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(defaultLatLng).title("Current Location"));
    }

    public float getDistance(LatLng latLng){
        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> addressList1 = null;
        List<Address> addressList2 = null;
        try{
            addressList1 = geocoder.getFromLocation(defaultLatLng.latitude,defaultLatLng.longitude,1);
            addressList2 = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
        }catch(IOException e){
            e.printStackTrace();
        }
        if(addressList1 != null && addressList2 != null){
            Address address1 = addressList1.get(0);
            Address address2 = addressList2.get(0);

            Location defaultLocation = new Location("Default");

            defaultLocation.setLatitude(address1.getLatitude());
            defaultLocation.setLongitude(address1.getLongitude());

            Location targetLocation = new Location("Target");

            targetLocation.setLatitude(address2.getLatitude());
            targetLocation.setLongitude(address2.getLongitude());

            return defaultLocation.distanceTo(targetLocation);
        }
        return 0;
    }
}
