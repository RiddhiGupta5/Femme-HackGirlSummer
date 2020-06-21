package com.riddhi.women_safety_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private Context ctx;
    private List<Request> requestList;
    private String user_id;
    private int user;

    public RequestAdapter(Context ctx, List<Request> requestList, String user_id, int user) {
        this.ctx = ctx;
        this.requestList = requestList;
        this.user_id = user_id;
        this.user = user;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.request_list_layout, null);
        return new RequestAdapter.RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, final int position) {

        final Request request = requestList.get(position);

        holder.sender_name.setText(request.getSender_name());
        holder.start.setText(request.getStart());
        holder.destination.setText(request.getDestination());
        holder.date.setText(request.getDate());
        holder.time.setText(request.getTime());
        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("User", Integer.toString(user));
                Log.i("User id", user_id);
                String receiver_id = request.getSender_id();
                String model;
                if(user == 0) {
                    model = "Passenger";

                } else {
                    model = "Owner";
                }
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(model).child(receiver_id);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String receiver_phone_no = dataSnapshot.child("phone_no").getValue().toString();
                        Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", receiver_phone_no, null));
                        smsIntent.putExtra("sms_body", "Hey! I wish to be your cab partner for the upcoming journey.");
                        ctx.startActivity(smsIntent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("Request").child(request.getId()).child("is_approved").setValue(1);

                if(user==1){
                    ref.child("Journey").child(request.getJourney_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Journey journey = dataSnapshot.getValue(Journey.class);
                            journey.addPassengerId(user_id);
                            DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("Journey");
                            newRef.child(journey.getId()).setValue(journey);
                            Toast.makeText(ctx, "Request has been Approved", Toast.LENGTH_SHORT).show();
                            requestList.remove(position);
                            notifyItemRemoved(position);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    ref.child("Journey").child(request.getJourney_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final Journey journey = dataSnapshot.getValue(Journey.class);
                            journey.addPassengerId(request.getSender_id());
                            DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("Journey");
                            newRef.child(journey.getId()).setValue(journey);
                            Toast.makeText(ctx, "Request has been Approved", Toast.LENGTH_SHORT).show();
                            requestList.remove(position);
                            notifyItemRemoved(position);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }




            }
        });

    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder{

        TextView sender_name, start, destination, date, time;
        Button approve, chat;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            sender_name = itemView.findViewById(R.id.sender_name);
            start = itemView.findViewById(R.id.start);
            destination = itemView.findViewById(R.id.destination);
            approve = itemView.findViewById(R.id.approve_request);
            chat = itemView.findViewById(R.id.chat);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);

        }
    }



}
