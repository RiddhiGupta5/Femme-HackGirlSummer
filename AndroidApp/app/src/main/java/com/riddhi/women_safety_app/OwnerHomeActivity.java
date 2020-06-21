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

import java.security.acl.Owner;

public class OwnerHomeActivity extends AppCompatActivity {

    private Button view_invites, new_journey, your_journey;

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
                Intent intent = new Intent(OwnerHomeActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_home);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_layout);

        view_invites = (Button) findViewById(R.id.view_invites);
        new_journey = (Button) findViewById(R.id.new_journey);
        your_journey = (Button) findViewById(R.id.your_journey);

        Intent intent = getIntent();
        final String user_id = intent.getStringExtra("user_id");
        Log.i("user_id", user_id);

        view_invites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_intvite_intent = new Intent(OwnerHomeActivity.this, ViewInvitationsActivity.class);
                view_intvite_intent.putExtra("user_id", user_id);
                view_intvite_intent.putExtra("user", 0);
                startActivity(view_intvite_intent);
            }
        });

        new_journey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_intvite_intent = new Intent(OwnerHomeActivity.this, OwnerNewJourneyActivity.class);
                view_intvite_intent.putExtra("user_id", user_id);
                view_intvite_intent.putExtra("user", 0);
                startActivity(view_intvite_intent);
            }
        });

        your_journey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent your_journey_intent = new Intent(OwnerHomeActivity.this, ViewJourneyActivity.class);
                your_journey_intent.putExtra("user_id", user_id);
                your_journey_intent.putExtra("user", 0);
                startActivity(your_journey_intent);
            }
        });

    }
}
