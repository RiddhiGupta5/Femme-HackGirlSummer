package com.riddhi.women_safety_app;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PassengerMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private String user_id, journey_id;
    private int user = 1;

    private LocationListener locationListener;
    private LocationManager locationManager;

    private final long MIN_TIME = 1000; //in millseconds
    private final long MIN_DIST = 5; //in meters

    private LatLng currentLocation, previousLocation;
    private Journey journey;
    private  List<coordinates> path_coordinates;

    private float results[] = new float[10];
    private int coordinate;

    private TextView distanceView, coordinateView, yourCoordinateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_maps);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        journey_id = intent.getStringExtra("journey_id");



        Log.e("HEYYY", journey_id);

        distanceView = findViewById(R.id.distance);
        coordinateView = findViewById(R.id.coordinate);
        yourCoordinateView = findViewById(R.id.coordinateYour);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Journey");

        ref.child(journey_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                journey = dataSnapshot.getValue(Journey.class);
                path_coordinates = journey.getPath_coordinates();
                Log.i("COORDINATES", path_coordinates.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION},1);

        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.INTERNET},1);
        }

        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(PassengerMapsActivity.this);
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    //mMap.addMarker(new MarkerOptions().position(currentLocation).title("You are Here").icon(BitmapDescriptorFactory.defaultMarker()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
                    previousLocation = currentLocation;
                    if (path_coordinates!=null){
                        Location.distanceBetween(currentLocation.latitude, currentLocation.longitude, path_coordinates.get(0).getLatitude(), path_coordinates.get(0).getLongitude(), results);
                        if (results[0]>5000){
                            Toast.makeText(PassengerMapsActivity.this, "You are away from starting point", Toast.LENGTH_SHORT).show();
                        }
                        coordinate = 0;
                        Log.i("DISTANCE", Float.toString(results[0]));
                        distanceView.setText(Float.toString(results[0]));
                        coordinateView.setText(path_coordinates.get(coordinate).getLatitude()+" "+path_coordinates.get(coordinate).getLongitude());
                        yourCoordinateView.setText(currentLocation.latitude+" "+currentLocation.longitude);
                    }else {
                        Toast.makeText(PassengerMapsActivity.this, "Unable to access locations", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mMap.clear();
                currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(currentLocation).title("You are Here").icon(BitmapDescriptorFactory.defaultMarker()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                if(path_coordinates!=null){
                    Location.distanceBetween(previousLocation.latitude, previousLocation.longitude, currentLocation.latitude, currentLocation.longitude, results);
                    Log.i("DISTANCE", Float.toString(results[0]));
                    if(coordinate==222222){
                        distanceView.setText(Float.toString(results[0]));
                        coordinateView.setText(path_coordinates.get(coordinate).getLatitude()+" "+path_coordinates.get(coordinate).getLongitude());
                        yourCoordinateView.setText(currentLocation.latitude+" "+currentLocation.longitude);
                        Toast.makeText(PassengerMapsActivity.this, "You are moving far from destination", Toast.LENGTH_SHORT).show();
                    }
                    if (results[0] >= path_coordinates.get(coordinate).getDistance()){
                        Location.distanceBetween(currentLocation.latitude, currentLocation.longitude, path_coordinates.get(coordinate).getLatitude(), path_coordinates.get(coordinate).getLongitude(), results);
                        distanceView.setText(Float.toString(results[0]));
                        coordinateView.setText(path_coordinates.get(coordinate).getLatitude()+" "+path_coordinates.get(coordinate).getLongitude());
                        yourCoordinateView.setText(currentLocation.latitude+" "+currentLocation.longitude);
                        if (results[0]>5000){
                            Toast.makeText(PassengerMapsActivity.this, "You are away from expected position", Toast.LENGTH_SHORT).show();
                        } else {
                            if (coordinate < path_coordinates.size()){
                                coordinate++;
                                previousLocation = currentLocation;
                            } else {
                                Toast.makeText(PassengerMapsActivity.this, "You are near your destination, coordinate="+coordinate, Toast.LENGTH_SHORT).show();
                                coordinate = 222222;
                            }

                        }
                    }
                } else {
                    Toast.makeText(PassengerMapsActivity.this, "Unable to access locations", Toast.LENGTH_SHORT).show();
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

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        } catch (SecurityException e){
            e.printStackTrace();
        }


    }
}
