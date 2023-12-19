package firebase.gopool.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import firebase.gopool.InfoRideActivity;
import firebase.gopool.R;
import firebase.gopool.model.response.RideResponse;

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.MyViewHolder> {
    private String[] mDataset;
    private Context mContext;
    private ArrayList<RideResponse> ride;

    public RideAdapter(Context c, ArrayList<RideResponse> o){
        this.mContext = c;
        this.ride = o;
    }

    public RideAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.individual_ride_information, parent, false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RideResponse rideI = ride.get(position);
        String from = "From: " + rideI.getAddressStart();
        String to = "To: " + rideI.getAddressEnd();
        String status = "";
        String time = "Time: ", frequent;
        if (!rideI.isFrequent()) {
            String[] arrTime = rideI.getBookingDate().split("-");
            time += arrTime[2] + "/" + arrTime[1] + "/" + arrTime[0];
            frequent = "Trip";
            switch (rideI.getStatusId()) {
                case 1: {
                    status = "Doing";
                    break;
                }
                case 2: {
                    status = "Done";
                    holder.cardView.setCardBackgroundColor(Color.rgb(234, 255, 236));
                    break;
                }
                default: {
                    status = "Canceled";
                    holder.cardView.setCardBackgroundColor(Color.rgb(255, 204, 204));
                }
            }
        } else {
            time += "Everyday";
            frequent = "Frequent route";
            holder.cardView.setCardBackgroundColor(Color.rgb(255, 255, 212));
        }
        String cost = "Cost: " + rideI.getCost().intValue() + " VND";

        holder.typeRide.setText(frequent);
        holder.from.setText(from);
        holder.to.setText(to);
        holder.date.setText(time);
        holder.costs.setText(cost);
        holder.status.setText(status);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, InfoRideActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ride", rideI);
                intent.putExtra("data", bundle);
                mContext.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (ride == null) return 0;
        return ride.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardView;
        LinearLayout view;
        TextView from, to, date, costs, status, typeRide;

        public MyViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView)itemView.findViewById(R.id.rideCardView);
            view = (LinearLayout) itemView.findViewById(R.id.view);
            from = (TextView) itemView.findViewById(R.id.fromTxt);
            to = (TextView) itemView.findViewById(R.id.toTxt);
            date = (TextView) itemView.findViewById(R.id.individualTimeTxt);
            costs = (TextView) itemView.findViewById(R.id.priceTxt);
            status = itemView.findViewById(R.id.statusBooking);
            typeRide = (TextView) itemView.findViewById(R.id.tv_system_suggest);
        }
    }

}