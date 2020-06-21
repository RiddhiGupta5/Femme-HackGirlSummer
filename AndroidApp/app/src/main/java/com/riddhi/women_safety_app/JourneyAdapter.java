package com.riddhi.women_safety_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JourneyAdapter  extends RecyclerView.Adapter<JourneyAdapter.JourneyViewHolder>{

    private Context ctx;
    private List<Journey> journeyList;
    private String user_id;
    private int user;


    public JourneyAdapter(Context ctx, List<Journey> journeyList, String user_id, int user) {
        this.ctx = ctx;
        this.journeyList = journeyList;
        this.user_id = user_id;
        this.user = user;
    }

    @NonNull
    @Override
    public JourneyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.journey_list_layout, null);
        return new JourneyAdapter.JourneyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JourneyViewHolder holder, int position) {
        final Journey journey = journeyList.get(position);

        holder.start.setText(journey.getStart());
        holder.destination.setText(journey.getDestination());
        holder.date.setText(journey.getDate());
        holder.time.setText(journey.getTime());

        //Todo: Decide if Owner should be counted here
        int no_passengers;
        if(journey.getPassenger_id()==null)
            no_passengers = 1;
        else
            no_passengers = journey.getPassenger_id().size() + 1;
        holder.no_of_passengers.setText(Integer.toString(no_passengers));

        holder.get_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(user == 0){
                    intent = new Intent(ctx, OwnerJourneyConsoleActivity.class);
                } else {
                    intent = new Intent(ctx, PassengerJourneyConsoleActivity.class);
                }
                intent.putExtra("user_id", user_id);
                intent.putExtra("user", user);
                intent.putExtra("journey_id", journey.getId());
                ctx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return journeyList.size();
    }

    class JourneyViewHolder extends  RecyclerView.ViewHolder{

        TextView start, destination, no_of_passengers, date, time;
        Button get_details;

        public JourneyViewHolder(@NonNull View itemView) {
            super(itemView);

            start = itemView.findViewById(R.id.start);
            destination = itemView.findViewById(R.id.destination);
            no_of_passengers = itemView.findViewById(R.id.no_of_passengers);
            get_details = itemView.findViewById(R.id.get_details);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
        }
    }

}
