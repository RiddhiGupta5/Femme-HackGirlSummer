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

public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.InvitationViewHolder> {

    private Context ctx;
    private List<Invitation> invitationList;
    private String user_id;
    private int user;
    private String journey_id;


    public InvitationAdapter(Context ctx, List<Invitation> invitation, String user_id, int user, String journey_id) {
        this.ctx = ctx;
        this.invitationList = invitation;
        this.user_id = user_id;
        this.user = user;
        this.journey_id = journey_id;
    }

    @NonNull
    @Override
    public InvitationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.list_layout, null);
        return new InvitationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvitationViewHolder holder, final int position) {
        final Invitation invitation = invitationList.get(position);
        final String[] sender_name = new String[1];

        holder.sender_name.setText(invitation.getSender_name());
        holder.start.setText(invitation.getStart());
        holder.destination.setText(invitation.getDestination());
        holder.date.setText(invitation.getDate());
        holder.time.setText(invitation.getTime());
        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("User", Integer.toString(user));
                Log.i("User id", user_id);
                final String receiver_id = invitation.getSender_id();
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
        holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(user == 0){
                    if(journey_id == null){
                        Toast.makeText(ctx, "Make a Journey and then send a new request to the passenger", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ctx, OwnerNewJourneyActivity.class);
                        intent.putExtra("user_id", user_id);
                        ctx.startActivity(intent);
                    } else {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Owner").child(user_id);
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    sender_name[0] = dataSnapshot.child("name").getValue().toString();
                                    DatabaseReference newRef = FirebaseDatabase.getInstance().getReference("Request").push();
                                    newRef.setValue(new Request(newRef.getKey(), user, user_id, invitation.getSender_id(), 0, journey_id, sender_name[0], invitation.getStart(), invitation.getDestination(), invitation.getDate(), invitation.getTime()));
                                    Toast.makeText(ctx, "Request Sent", Toast.LENGTH_LONG).show();
                                    DatabaseReference invRef = FirebaseDatabase.getInstance().getReference().child("Invite");
                                    invRef.child(invitation.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Invitation invite = dataSnapshot.getValue(Invitation.class);
                                            invite.addSentRequest(user_id);
                                            invite.addSentRequest(invitation.getSender_id());
                                            Log.i("user_id", user_id);
                                            Log.i("receiver_id", invitation.getSender_id());
                                            //Todo: The invitation is no longer visible to the operson to whom request is sent
                                            DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("Invite");
                                            newRef.child(invitation.getId()).setValue(invite);
                                            invitationList.remove(position);
                                            notifyItemRemoved(position);
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

                } else {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Passenger").child(user_id);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                sender_name[0] = dataSnapshot.child("name").getValue().toString();
                                DatabaseReference newRef = FirebaseDatabase.getInstance().getReference("Request").push();
                                newRef.setValue(new Request(newRef.getKey(), user, user_id, invitation.getSender_id(), 0, invitation.getJourney_id(), sender_name[0], invitation.getStart(), invitation.getDestination(), invitation.getDate(), invitation.getTime()));
                                Toast.makeText(ctx, "Request Sent", Toast.LENGTH_LONG).show();
                                DatabaseReference invRef = FirebaseDatabase.getInstance().getReference().child("Invite");
                                invRef.child(invitation.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Invitation invite = dataSnapshot.getValue(Invitation.class);
                                        invite.addSentRequest(user_id);
                                        DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("Invite");
                                        newRef.child(invitation.getId()).setValue(invite);
                                        invitationList.remove(position);
                                        notifyItemRemoved(position);
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
        });
    }

    @Override
    public int getItemCount() {
        return invitationList.size();
    }

    class InvitationViewHolder extends  RecyclerView.ViewHolder{

        TextView sender_name, start, destination, date, time;
        Button send, chat;

        public InvitationViewHolder(@NonNull View itemView) {
            super(itemView);

            sender_name = itemView.findViewById(R.id.sender_name);
            start = itemView.findViewById(R.id.start);
            destination = itemView.findViewById(R.id.destination);
            send = itemView.findViewById(R.id.send_request);
            chat = itemView.findViewById(R.id.chat);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);

        }
    }

}
