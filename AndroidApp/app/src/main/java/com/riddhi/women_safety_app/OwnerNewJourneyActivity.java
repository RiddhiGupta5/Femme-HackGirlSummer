package com.riddhi.women_safety_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class OwnerNewJourneyActivity extends AppCompatActivity {

    private EditText date, time;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_new_journey);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_layout);

        Intent inte = getIntent();
        final String user_id = inte.getStringExtra("user_id");

        date = (EditText) findViewById(R.id.date);
        time = (EditText) findViewById(R.id.time);
        next = (Button) findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide Keyboard
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                Intent intent = new Intent(OwnerNewJourneyActivity.this, OwnerMapsActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("date", date.getText().toString());
                intent.putExtra("time", time.getText().toString());
                startActivity(intent);
            }
        });

    }
}
