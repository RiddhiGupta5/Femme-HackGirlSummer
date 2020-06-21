package com.riddhi.women_safety_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PassengerAdapter extends RecyclerView.Adapter<PassengerAdapter.PassengerViewHolder>{

    private Context ctx;
    private List<String> passengerList;
    private String user_id;
    private int user;
    private String journey_id;

    public PassengerAdapter(Context ctx, List<String> passengerList, String user_id, int user, String journey_id) {
        this.ctx = ctx;
        this.passengerList = passengerList;
        this.user_id = user_id;
        this.user = user;
        this.journey_id = journey_id;
    }

    @NonNull
    @Override
    public PassengerAdapter.PassengerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.passenger_list_layout, null, false);
        return new PassengerAdapter.PassengerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerAdapter.PassengerViewHolder holder, int position) {

        String passenger = passengerList.get(position);
        holder.passenger.setText(passenger);

    }

    @Override
    public int getItemCount() {
        return passengerList.size();
    }

    class PassengerViewHolder extends  RecyclerView.ViewHolder{

        TextView passenger;

        public PassengerViewHolder(@NonNull View itemView) {
            super(itemView);

            passenger = itemView.findViewById(R.id.passenger);
        }
    }

}
