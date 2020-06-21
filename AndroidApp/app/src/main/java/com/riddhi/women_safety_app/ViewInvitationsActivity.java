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

import me.xdrop.fuzzywuzzy.FuzzySearch;

public class ViewInvitationsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    InvitationAdapter adapter;

    List<Invitation> invitationList;
    DateFormat dateFormat;
    Date currentDate, invitationDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invitations);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_layout);

        final Intent intent = getIntent();
        final String user_id = intent.getStringExtra("user_id");
        final int user = intent.getIntExtra("user", -1);
        final int sender;
        String journey_id = null, start = null, destination = null;

        dateFormat = new SimpleDateFormat("dd/MM/yy");
        currentDate = new Date();
        String dateString = dateFormat.format(currentDate);
        try {
            currentDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("Date", dateFormat.format(currentDate));


        if(user == 0) {
            sender = 1;
            journey_id = intent.getStringExtra("journey_id");
            start = intent.getStringExtra("start");
            destination = intent.getStringExtra("destination");
        }
        else
            sender = 0;

        invitationList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.invites_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Invite");
        final String finalJourney_id = journey_id;
        final String finalStart = start;
        final String finalDestination = destination;
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Invitation invitation = snapshot.getValue(Invitation.class);
                    List<String> sent_request = invitation.getSent_request();

                    try {
                        invitationDate = dateFormat.parse(invitation.getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(invitation.getSender() == sender && currentDate.compareTo(invitationDate) < 0)
                    {

                        if(sent_request!=null){
                            Log.i("hello", "There are some passengers");
                            if(!sent_request.contains(user_id)){
                                Log.i("hello", user_id + "is not present");
                                if(finalStart!=null && finalDestination!=null){
                                    Log.i("hello", finalStart + " : " + finalDestination);
                                    if(FuzzySearch.weightedRatio(invitation.getStart(),finalStart)>=60 && FuzzySearch.weightedRatio(invitation.getDestination(),finalDestination)>=60){
                                        Log.i("hello","Matched???????????");
                                        invitationList.add(invitation);
                                    } else {
                                        Log.i("hello", "Nothing Matched??????????");
                                    }

                                } else {
                                    Log.i("hello", "Everything is nullLLLLLLLLLLL");
                                    invitationList.add(invitation);
                                }
                            }
                        } else {
                            Log.i("hello", "There are NO NO passengers");
                            if(finalStart!=null && finalDestination!=null){
                                Log.i("hello", user_id + "is not present");
                                if(FuzzySearch.weightedRatio(invitation.getStart(),finalStart)>=60 && FuzzySearch.weightedRatio(invitation.getDestination(),finalDestination)>=60){
                                    Log.i("hello","Matched???????????");
                                    invitationList.add(invitation);
                                } else{
                                    Log.i("hello", "Nothing Matched??????????");
                                }

                            } else {
                                Log.i("hello", "Everything is nullLLLLLLLLLLL");
                                invitationList.add(invitation);
                            }

                        }
                    }
                }
                adapter = new InvitationAdapter(ViewInvitationsActivity.this, invitationList, user_id, user, finalJourney_id);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
