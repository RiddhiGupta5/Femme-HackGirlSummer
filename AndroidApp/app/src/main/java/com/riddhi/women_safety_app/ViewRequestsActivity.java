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

public class ViewRequestsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RequestAdapter adapter;

    List<Request> requestList;
    DateFormat dateFormat;
    Date currentDate, requestDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_layout);

        final Intent intent = getIntent();
        final String user_id = intent.getStringExtra("user_id");
        final int user = intent.getIntExtra("user", -1);
        String journey_id = null;

        dateFormat = new SimpleDateFormat("dd/MM/yy");
        currentDate = new Date();
        String dateString = dateFormat.format(currentDate);
        try {
            currentDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("Date", dateFormat.format(currentDate));

        if(user==0){
            journey_id = intent.getStringExtra("journey_id");
        }

        requestList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.requests_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Request");
        final String finalJourney_id = journey_id;
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Request request = snapshot.getValue(Request.class);
                    try {
                        requestDate = dateFormat.parse(request.getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(user==1 && request.getReceiver_id().equals(user_id) && request.getIs_approved() == 0 && currentDate.compareTo(requestDate) < 0)
                    {
                        requestList.add(request);
                    } else if(user==0 && request.getReceiver_id().equals(user_id) && request.getJourney_id().equals(finalJourney_id) && request.getIs_approved() == 0 && currentDate.compareTo(requestDate) < 0){
                        requestList.add(request);
                    }
                }

                adapter = new RequestAdapter(ViewRequestsActivity.this, requestList, user_id, user);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
