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

public class OwnerSignupActivity extends AppCompatActivity {

    private EditText name, email, phone_no, gender, password, car_name, car_no;
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
        setContentView(R.layout.activity_owner_signup);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_layout);

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        name = (EditText)findViewById(R.id.name);
        phone_no = (EditText)findViewById(R.id.phone_no);
        gender = (EditText)findViewById(R.id.gender);
        car_name = (EditText)findViewById(R.id.car_name);
        car_no = (EditText)findViewById(R.id.car_no);
        login = (TextView) findViewById(R.id.login);
        sign_up = (Button) findViewById(R.id.sign_up);


        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(OwnerSignupActivity.this, OwnerHomeActivity.class);
                    intent.putExtra("user_id", user.getUid());
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };



        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String entered_email = email.getText().toString();
                final String entered_password = password.getText().toString();
                final String entered_name = name.getText().toString();
                final String entered_phone_no = phone_no.getText().toString();
                final String entered_gender = gender.getText().toString();
                final String entered_car_name = car_name.getText().toString();
                final String entered_car_no = car_no.getText().toString();

                if (entered_email.isEmpty()
                    || entered_password.isEmpty()
                    || entered_name.isEmpty()
                    || entered_phone_no.isEmpty()
                    || entered_gender.isEmpty()
                    || entered_car_name.isEmpty()
                    || entered_car_no.isEmpty()){
                    Toast.makeText(OwnerSignupActivity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                }

                else if (!entered_gender.equals("Female") && !entered_gender.equals("F") && !entered_gender.equals( "f" ) && !entered_gender.equals("female") && !entered_gender.equals("FEMALE")){
                    Toast.makeText(OwnerSignupActivity.this, "Only Females are allowed to Register", Toast.LENGTH_SHORT).show();
                }

                else{
                    mAuth.createUserWithEmailAndPassword(entered_email, entered_password).addOnCompleteListener(OwnerSignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(OwnerSignupActivity.this, "Sign up error", Toast.LENGTH_SHORT).show();
                            }else{
                                String user_id = mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Owner").child(user_id);
                                current_user_db.child("name").setValue(entered_name);
                                current_user_db.child("email").setValue(entered_email);
                                current_user_db.child("phone_no").setValue(entered_phone_no);
                                current_user_db.child("car_name").setValue(entered_car_name);
                                current_user_db.child("car_no").setValue(entered_car_no);
                            }
                        }
                    });
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerSignupActivity.this, OwnerLoginActivity.class);
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
