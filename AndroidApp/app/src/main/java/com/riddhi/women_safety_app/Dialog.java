package com.riddhi.women_safety_app;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dialog extends AppCompatDialogFragment {

    private String user_id;

    public Dialog(String user_id) {
        this.user_id = user_id;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup, null);

        builder.setView(view)
                .setTitle("ALERT!")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                 })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS);
                        if(permissionCheck== PackageManager.PERMISSION_GRANTED){
                            MyMessage();
                        } else {
                            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.SEND_SMS}, 1);
                        }
                    }
                });
        return builder.create();
    }

    private void MyMessage() {
        Toast.makeText(getActivity(), "Alert Sent!!", Toast.LENGTH_SHORT).show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Passenger");
        ref.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String phone1 = dataSnapshot.child("emergency1").getValue(String.class);
                String phone2 = dataSnapshot.child("emergency2").getValue(String.class);
                String message = name + " is in Emergency. She urgently needs your help!";

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phone1, null, message, null, null);
                smsManager.sendTextMessage(phone2, null, message, null, null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case 1:
                if(grantResults.length>=0 && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                    MyMessage();
                } else {
                    Toast.makeText(getActivity(), "You don't have required permissions", Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }
}
