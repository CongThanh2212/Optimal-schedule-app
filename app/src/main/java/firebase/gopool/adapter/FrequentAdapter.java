package firebase.gopool.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import firebase.gopool.MapFrequentRouteActivity;
import firebase.gopool.R;
import firebase.gopool.model.response.FrequentResponse;

public class FrequentAdapter extends RecyclerView.Adapter<FrequentAdapter.MyViewHolder> {
    private String[] mDataset;
    private Context mContext;
    private ArrayList<FrequentResponse> frequents;

    public FrequentAdapter(Context c, ArrayList<FrequentResponse> o){
        this.mContext = c;
        this.frequents = o;
    }

    public FrequentAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.individual_frequent_route, parent, false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FrequentResponse route = frequents.get(position);

        String from = "From: " + route.getAddressStart().replaceAll("\n", ", ");
        String to = "To: " + route.getAddressEnd().replaceAll("\n", ", ");
        if (route.isShared()) {
            System.out.println("share");
            holder.cardView.setCardBackgroundColor(Color.rgb(234, 255, 236));
            holder.frequentRouteStatusTextview.setText("Shared");
            holder.frequentRouteStatusTextview.setTextColor(Color.rgb(0, 160, 66));
        }

        holder.from.setText(from);
        holder.to.setText(to);
        holder.length.setText("Length: " + route.getLengthRoute() + " km");
        holder.date.setText("Time: " + route.getTimeStart() + " - " + route.getTimeEnd());

        holder.view.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, MapFrequentRouteActivity.class);
            intent.putExtra("data", route);
            mContext.startActivity(intent);
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (frequents == null) return 0;
        return frequents.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout view;
        TextView from, to, date, length, frequentRouteStatusTextview;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.bookingCardView);
            view = (LinearLayout) itemView.findViewById(R.id.view);
            from = (TextView) itemView.findViewById(R.id.fromTxt);
            to = (TextView) itemView.findViewById(R.id.toTxt);
            date = (TextView) itemView.findViewById(R.id.individualDateTxt);
            length = (TextView) itemView.findViewById(R.id.lengthTxt);
            frequentRouteStatusTextview = (TextView) itemView.findViewById(R.id.frequentRouteStatusTextview);

        }
    }

}