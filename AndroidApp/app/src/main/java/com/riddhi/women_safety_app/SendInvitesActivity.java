package com.riddhi.women_safety_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SendInvitesActivity extends AppCompatActivity {
    
    private EditText start, destination, date, time;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_invites);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_layout);

        final Intent intent = getIntent();
        final String sender_id = intent.getStringExtra("sender_id");
        final int sender = intent.getIntExtra("sender", -1);


        start = (EditText) findViewById(R.id.start);
        destination = (EditText) findViewById(R.id.destination);
        date = (EditText) findViewById(R.id.date);
        time = (EditText) findViewById(R.id.time);
        send = (Button) findViewById(R.id.send);
        
        if(sender == 0){
            String known_start = intent.getStringExtra("start");
            String known_destination = intent.getStringExtra("destination");
            start.setText(known_start);
            destination.setText(known_destination);
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Hide Keyboard
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                final String final_start = start.getText().toString();
                final String final_destination = destination.getText().toString();
                final String final_date = date.getText().toString();
                final String final_time = time.getText().toString();
                final String[] sender_name = new String[1];

                if(sender == 1){
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Passenger").child(sender_id);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                sender_name[0] = dataSnapshot.child("name").getValue().toString();
                                DatabaseReference newRef = FirebaseDatabase.getInstance().getReference("Invite").push();
                                newRef.setValue(new Invitation(sender_id, sender, final_start, final_destination, sender_name[0], newRef.getKey(), final_date, final_time));
                                Toast.makeText(SendInvitesActivity.this, "Sent Invite passenger", Toast.LENGTH_LONG).show();

                                Intent intent1 = new Intent(SendInvitesActivity.this, PassengerHomeActivity.class);
                                intent1.putExtra("user_id", sender_id);
                                startActivity(intent1);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Owner").child(sender_id);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                sender_name[0] = dataSnapshot.child("name").getValue().toString();
                                DatabaseReference newRef = FirebaseDatabase.getInstance().getReference("Invite").push();
                                newRef.setValue(new Invitation(sender_id, sender, final_start, final_destination, sender_name[0], newRef.getKey(), final_date, final_time));
                                Toast.makeText(SendInvitesActivity.this, "Sent Invite", Toast.LENGTH_LONG).show();


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }



            }
        });


    }
}
