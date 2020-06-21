package com.riddhi.women_safety_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewPassengersActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PassengerAdapter adapter;

    List<String> passengerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_passengers);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_layout);

        final Intent intent = getIntent();
        final String user_id = intent.getStringExtra("user_id");
        final int user = intent.getIntExtra("user", -1);
        final String journey_id = intent.getStringExtra("journey_id");

        passengerList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.passenger_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Journey");
        ref.child(journey_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Journey journey = dataSnapshot.getValue(Journey.class);
                final List<String> passenger_id_list = journey.getPassenger_id();


                if(passenger_id_list!=null){
                    DatabaseReference ownerRef = FirebaseDatabase.getInstance().getReference().child("Owner");
                    ownerRef.child(journey.getOwner_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            passengerList.add(dataSnapshot.child("name").getValue().toString());
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Passenger");
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                        String snapshot_id = snapshot.getKey();
                                        for(String id : passenger_id_list){
                                            if(snapshot_id.equals(id)){
                                                passengerList.add(snapshot.child("name").getValue().toString());
                                            }
                                        }
                                    }
                                    adapter = new PassengerAdapter(ViewPassengersActivity.this, passengerList, user_id, user, journey_id);
                                    recyclerView.setAdapter(adapter);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    DatabaseReference ownerRef = FirebaseDatabase.getInstance().getReference().child("Owner");
                    ownerRef.child(journey.getOwner_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            passengerList.add(dataSnapshot.child("name").getValue().toString());
                            adapter = new PassengerAdapter(ViewPassengersActivity.this, passengerList, user_id, user, journey_id);
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
