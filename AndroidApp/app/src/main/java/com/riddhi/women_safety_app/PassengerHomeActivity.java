package com.riddhi.women_safety_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PassengerHomeActivity extends AppCompatActivity {

    private Button view_invites, send_invites, received_requests, fixed_journeys, alert;
    private String user_id;

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
                Intent intent = new Intent(PassengerHomeActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_home);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        Log.i("user_id", user_id);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_layout);

        view_invites = (Button) findViewById(R.id.view_invites);
        send_invites = (Button) findViewById(R.id.send_invites);
        received_requests = (Button) findViewById(R.id.received_requests);
        fixed_journeys = (Button) findViewById(R.id.fixed_journeys);
        alert = (Button) findViewById(R.id.alert);

        view_invites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_intvite_intent = new Intent(PassengerHomeActivity.this, ViewInvitationsActivity.class);
                view_intvite_intent.putExtra("user_id", user_id);
                view_intvite_intent.putExtra("user", 1);
                startActivity(view_intvite_intent);
            }
        });

        send_invites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] sender_name = new String[1];
                final String name;
                final Intent send_invite_intent = new Intent(PassengerHomeActivity.this, SendInvitesActivity.class);
                send_invite_intent.putExtra("sender_id", user_id);
                send_invite_intent.putExtra("sender", 1);
                startActivity(send_invite_intent);

            }
        });

        received_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_intvite_intent = new Intent(PassengerHomeActivity.this, ViewRequestsActivity.class);
                view_intvite_intent.putExtra("user_id", user_id);
                view_intvite_intent.putExtra("user", 1);
                startActivity(view_intvite_intent);
            }
        });

        fixed_journeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fixed_journey_intent = new Intent(PassengerHomeActivity.this, ViewJourneyActivity.class);
                fixed_journey_intent.putExtra("user_id", user_id);
                fixed_journey_intent.putExtra("user", 1);
                startActivity(fixed_journey_intent);
            }
        });

        alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

    }

    public void openDialog(){
        Dialog dialog = new Dialog(user_id);
        dialog.show(getSupportFragmentManager(), "pop up");

    }
}
