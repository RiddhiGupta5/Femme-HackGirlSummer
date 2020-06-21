package com.riddhi.women_safety_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import afu.org.checkerframework.checker.igj.qual.I;

public class OwnerJourneyConsoleActivity extends AppCompatActivity {

    private Button send_invites, your_passengers, received_requests, send_requests;
    private String user_id;
    private int user = 0;
    private String journey_id;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(OwnerJourneyConsoleActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_journey_console);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_layout);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        journey_id = intent.getStringExtra("journey_id");

        send_invites = (Button) findViewById(R.id.send_invites);
        your_passengers = (Button) findViewById(R.id.your_passengers);
        received_requests = (Button) findViewById(R.id.received_requests);
        send_requests = (Button) findViewById(R.id.send_requests);

        send_invites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                ref.child("Journey").child(journey_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Getting Journey data from firebase
                        final Journey journey = dataSnapshot.getValue(Journey.class);

                        //checking if invite was sent or not
                        if(journey.getSent_invite()==1){
                            //Invite already sent
                            Toast.makeText(OwnerJourneyConsoleActivity.this, "Invite Already Sent", Toast.LENGTH_SHORT).show();
                        } else {
                            //Invite Not sent

                            //setting value of sent_invite to 1
                            journey.setSent_invite(1);
                            DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("Journey");
                            newRef.child(journey.getId()).setValue(journey);

                            //getting name of the logged in owner to set sender name
                            DatabaseReference senderNameRef = FirebaseDatabase.getInstance().getReference().child("Owner").child(user_id);
                            senderNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        //getting sender name
                                        String sender_name = dataSnapshot.child("name").getValue().toString();


                                        //Making new invite
                                        final DatabaseReference inv_ref = FirebaseDatabase.getInstance().getReference().child("Invite").push();
                                        final Invitation invitation = new Invitation(user_id, user, journey.getStart(), journey.getDestination(), sender_name, inv_ref.getKey(), journey_id, journey.getDate(), journey.getTime());

                                        //getting all passengers to whom request is already sent
                                        DatabaseReference req_ref = FirebaseDatabase.getInstance().getReference().child("Request");
                                        req_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                    Request request = snapshot.getValue(Request.class);
                                                    if(request.getJourney_id().equals(journey_id)){
                                                        invitation.addSentRequest(request.getReceiver_id());
                                                    }
                                                }

                                                //Finally setting Value
                                                inv_ref.setValue(invitation);
                                                Toast.makeText(OwnerJourneyConsoleActivity.this, "Invitations Sent", Toast.LENGTH_SHORT).show();
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

        your_passengers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_passengers_intent = new Intent(OwnerJourneyConsoleActivity.this, ViewPassengersActivity.class);
                view_passengers_intent.putExtra("user_id", user_id);
                view_passengers_intent.putExtra("user", user);
                view_passengers_intent.putExtra("journey_id", journey_id);
                startActivity(view_passengers_intent);
            }
        });

        received_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent received_requests_intent = new Intent(OwnerJourneyConsoleActivity.this, ViewRequestsActivity.class);
                received_requests_intent.putExtra("user_id", user_id);
                received_requests_intent.putExtra("user", user);
                received_requests_intent.putExtra("journey_id", journey_id);
                startActivity(received_requests_intent);
            }
        });

        send_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent send_requests_intent = new Intent(OwnerJourneyConsoleActivity.this, ViewInvitationsActivity.class);
                send_requests_intent.putExtra("user_id", user_id);
                send_requests_intent.putExtra("user", user);
                send_requests_intent.putExtra("journey_id", journey_id);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Journey");
                ref.child(journey_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Journey journey = dataSnapshot.getValue(Journey.class);
                        send_requests_intent.putExtra("start", journey.getStart());
                        send_requests_intent.putExtra("destination", journey.getDestination());
                        startActivity(send_requests_intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }
}
