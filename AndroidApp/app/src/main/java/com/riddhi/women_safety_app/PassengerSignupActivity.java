package com.riddhi.women_safety_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PassengerSignupActivity extends AppCompatActivity {

    private EditText name, email, phone_no, gender, password, emergency1, emergency2;
    private TextView login;
    private Button sign_up;

    private FirebaseAuth mAuth;
    private  FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_signup);

        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_layout);

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(PassengerSignupActivity.this, PassengerHomeActivity.class);
                    intent.putExtra("user_id", user.getUid());
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        name = (EditText)findViewById(R.id.name);
        phone_no = (EditText)findViewById(R.id.phone_no);
        gender = (EditText)findViewById(R.id.gender);
        login = (TextView) findViewById(R.id.login);
        sign_up = (Button) findViewById(R.id.sign_up);
        emergency1 = (EditText) findViewById(R.id.emergency1);
        emergency2 = (EditText) findViewById(R.id.emergency2);


        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String entered_email = email.getText().toString();
                final String entered_password = password.getText().toString();
                final String entered_name = name.getText().toString();
                final String entered_phone_no = phone_no.getText().toString();
                final String entered_gender = gender.getText().toString();
                final String entered_emergency1 = emergency1.getText().toString();
                final String entered_emergency2 = emergency2.getText().toString();

                if (entered_email.isEmpty()
                        || entered_password.isEmpty()
                        || entered_name.isEmpty()
                        || entered_phone_no.isEmpty()
                        || entered_gender.isEmpty()
                        || entered_emergency1.isEmpty()
                        || entered_emergency2.isEmpty()){
                    Toast.makeText(PassengerSignupActivity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                }

                else if (!entered_gender.equals("Female") && !entered_gender.equals("F") && !entered_gender.equals( "f" ) && !entered_gender.equals("female") && !entered_gender.equals("FEMALE")){
                    Toast.makeText(PassengerSignupActivity.this, "Only Females are allowed to Register", Toast.LENGTH_SHORT).show();
                }

                else{
                    mAuth.createUserWithEmailAndPassword(entered_email, entered_password).addOnCompleteListener(PassengerSignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(PassengerSignupActivity.this, "Sign up error", Toast.LENGTH_SHORT).show();
                            }else{
                                String user_id = mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Passenger").child(user_id);
                                current_user_db.child("name").setValue(entered_name);
                                current_user_db.child("email").setValue(entered_email);
                                current_user_db.child("phone_no").setValue(entered_phone_no);
                                current_user_db.child("emergency1").setValue(entered_emergency1);
                                current_user_db.child("emergency2").setValue(entered_emergency2);
                            }
                        }
                    });
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassengerSignupActivity.this, PassengerLoginActivity.class);
                startActivity(intent);
                finish();
                return;

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
