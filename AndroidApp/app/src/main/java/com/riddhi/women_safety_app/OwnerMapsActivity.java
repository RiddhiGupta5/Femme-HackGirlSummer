package com.riddhi.women_safety_app;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class OwnerMapsActivity extends FragmentActivity implements OnMapReadyCallback, RoutingListener {

    private GoogleMap mMap;
    private AutoCompleteTextView start, destination;
    private Button save, next;
    private List<Polyline> polylines;
    private Route selectedRoute;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private String user_id, date, time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_maps);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");

        final String[] final_start = {""};
        final String[] final_destination = {""};

        polylines = new ArrayList<>();

        start = (AutoCompleteTextView) findViewById(R.id.start);
        destination = (AutoCompleteTextView) findViewById(R.id.destination);
        save = (Button) findViewById(R.id.save);
        next = (Button) findViewById(R.id.next);

        start.setAdapter(new PlaceAutocompleteSuggestAdapter(OwnerMapsActivity.this, android.R.layout.simple_expandable_list_item_1));
        destination.setAdapter(new PlaceAutocompleteSuggestAdapter(OwnerMapsActivity.this, android.R.layout.simple_expandable_list_item_1));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear map
                mMap.clear();

                //Get entered locations
                final_start[0] = start.getText().toString();
                final_destination[0] = destination.getText().toString();

                //Hide Keyboard
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                Geocoder geocoder = new Geocoder(OwnerMapsActivity.this, Locale.getDefault());
                try {
                    LatLng place1=null, place2=null;
                    if(!final_start[0].equals("Your Location")){
                        List<Address> startAddressList = geocoder.getFromLocationName(final_start[0], 1);
                        if(startAddressList.size()>0){
                            Address startAddress = startAddressList.get(0);
                            place1 = new LatLng(startAddress.getLatitude(), startAddress.getLongitude());
                        } else {
                            Toast.makeText(OwnerMapsActivity.this, "Couldn't find loc1", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        place1 = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        List<Address> addressList= geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                        final_start[0] = addressList.get(0).getLocality();
                    }

                    if(!final_destination[0].equals("Your Location")){
                        List<Address>  destinationAddressList = geocoder.getFromLocationName(final_destination[0], 1);
                        if(destinationAddressList.size()>0){
                            Address destinationAddress = destinationAddressList.get(0);
                            place2 = new LatLng(destinationAddress.getLatitude(), destinationAddress.getLongitude());
                        } else {
                            Toast.makeText(OwnerMapsActivity.this, "Couldn't find loc2", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        place2 = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        List<Address> addressList= geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                        final_destination[0] = addressList.get(0).getLocality();
                    }



                    if(place1!=null && place2!=null){
                        //Add Markers
                        mMap.addMarker(new MarkerOptions().position(place1).title(final_start[0]));
                        mMap.addMarker(new MarkerOptions().position(place2).title(final_destination[0]));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(place1));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

                        //Draw Route
                        Routing routing = new Routing.Builder()
                                .travelMode(AbstractRouting.TravelMode.DRIVING)
                                .withListener(OwnerMapsActivity.this)
                                .waypoints(place1, place2)
                                .key(BuildConfig.ApiKey)
                                .build();
                        routing.execute();


                        Log.i("length", routing.toString());

                        selectedRoute = (routing.get()).get(0);

                    } else {
                        Toast.makeText(OwnerMapsActivity.this, "Couldn't find location ??????????????", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<coordinates> newList = new ArrayList<>();
                LatLng prevPoint;
                LatLng point;
                int length = selectedRoute.getPoints().size();
                float results[] = new float[10];

                for(int i=1; i<length; i++){
                    prevPoint = selectedRoute.getPoints().get(i-1);
                    point = selectedRoute.getPoints().get(i);
                    if(i%20==0){
                        Location.distanceBetween(prevPoint.latitude, prevPoint.longitude, point.latitude, point.longitude, results);
                        newList.add(new coordinates(point.latitude, point.longitude, results[0]));
                    }
                }
                Log.i("LENGTH", Integer.toString(newList.size()));

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Journey").push();
                ref.setValue(new Journey(ref.getKey(), user_id, final_start[0], final_destination[0], newList, date, time));

                Intent new_intent = new Intent(OwnerMapsActivity.this, OwnerJourneyConsoleActivity.class);
                new_intent.putExtra("user_id", user_id);
                new_intent.putExtra("user", 0);
                new_intent.putExtra("journey_id", ref.getKey());
                startActivity(new_intent);

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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(OwnerMapsActivity.this);
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    currentLocation = location;
                    mMap.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title("You are Here"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                }
            }
        });
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRoutingCancelled() {

    }

    private  void erasePolylines(){
        for(Polyline line:polylines){
            line.remove();
        }
        polylines.clear();
    }

}
