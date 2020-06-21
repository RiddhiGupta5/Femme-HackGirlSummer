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

public class PassengerJourneyConsoleActivity extends AppCompatActivity {

    private Button get_partner, tracking;
    private String user_id, journey_id;
    private  int user = 1;

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
                Intent intent = new Intent(PassengerJourneyConsoleActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_journey_console);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_layout);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        journey_id = intent.getStringExtra("journey_id");

        get_partner = (Button) findViewById(R.id.get_partners);
        tracking = (Button) findViewById(R.id.tracking);

        get_partner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_passengers_intent = new Intent(PassengerJourneyConsoleActivity.this, ViewPassengersActivity.class);
                view_passengers_intent.putExtra("user_id", user_id);
                view_passengers_intent.putExtra("user", user);
                view_passengers_intent.putExtra("journey_id", journey_id);
                startActivity(view_passengers_intent);
            }
        });

        tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PassengerJourneyConsoleActivity.this, "Starting Tracking", Toast.LENGTH_SHORT).show();
                Intent view_passengers_intent = new Intent(PassengerJourneyConsoleActivity.this, PassengerMapsActivity.class);
                view_passengers_intent.putExtra("user_id", user_id);
                view_passengers_intent.putExtra("user", user);
                view_passengers_intent.putExtra("journey_id", journey_id);

                startActivity(view_passengers_intent);
            }
        });
    }
}
