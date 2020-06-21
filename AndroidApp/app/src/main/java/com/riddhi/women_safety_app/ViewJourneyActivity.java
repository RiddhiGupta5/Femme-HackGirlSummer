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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewJourneyActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    JourneyAdapter adapter;
    List<Journey> journeyList;

    DateFormat dateFormat;
    Date currentDate, journeyDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_journey);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_layout);

        final Intent intent = getIntent();
        final String user_id = intent.getStringExtra("user_id");
        final int user = intent.getIntExtra("user", -1);

        dateFormat = new SimpleDateFormat("dd/MM/yy");
        currentDate = new Date();
        String dateString = dateFormat.format(currentDate);
        try {
            currentDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("Date", dateFormat.format(currentDate));

        journeyList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.journey_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.i("Hello", "We have started__________________");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Journey");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Journey journey = snapshot.getValue(Journey.class);
                    String journey_owner_id = journey.getOwner_id();
                    try {
                        journeyDate = dateFormat.parse(journey.getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(user==0 && journey_owner_id.equals(user_id) && currentDate.compareTo(journeyDate) < 0)
                    {
                        journeyList.add(journey);
                    } else if(user==1 && currentDate.compareTo(journeyDate) < 0){
                        List<String> passenger_id = journey.getPassenger_id();
                        if(passenger_id!=null){
                            if(passenger_id.contains(user_id)){
                                journeyList.add(journey);
                            }
                        }
                    }
                }
                Log.i("Journeys", journeyList.toString());
                adapter = new JourneyAdapter(ViewJourneyActivity.this, journeyList, user_id, user);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
