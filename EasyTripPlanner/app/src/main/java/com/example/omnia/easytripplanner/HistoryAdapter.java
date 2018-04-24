package com.example.omnia.easytripplanner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.omnia.easytripplanner.database.dao.daoimpl.NoteDAOImpl;
import com.example.omnia.easytripplanner.database.dao.daoimpl.TripDAOImpl;
import com.example.omnia.easytripplanner.database.dto.TripDTO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Aya on 3/4/2018.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.TripViewHolder> {
    private Context context;
    List<TripDTO> history_trips;

    HistoryAdapter(Context context, List<TripDTO> trips) {
        this.history_trips = trips;
        this.context = context;
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder
    {
        CardView history_cv;
        TextView history_end;
        TextView history_date;
        TextView history_name;
        TextView history_time;
        ImageButton deleteTripHistory;

        TripViewHolder(View itemView) {
            super(itemView);
            history_cv = (CardView) itemView.findViewById(R.id.history_card);
            history_end = (TextView) itemView.findViewById(R.id.history_end);
            history_date = (TextView) itemView.findViewById(R.id.history_date);
            history_time = (TextView) itemView.findViewById(R.id.history_time);
            history_name = (TextView) itemView.findViewById(R.id.history_name);
            deleteTripHistory = itemView.findViewById(R.id.deleteTripHistory);
        }

    }

    HistoryAdapter(List<TripDTO> trips) {
        this.history_trips = trips;
    }

    @Override
    public int getItemCount() {
        return history_trips.size();
    }

    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_layout, viewGroup, false);
        TripViewHolder tvh = new TripViewHolder(v);
        return tvh;
    }

    @Override
    public void onBindViewHolder(TripViewHolder holder, final int position) {
        holder.history_end.setText(history_trips.get(position).getTrip_end_point());
        holder.history_date.setText(history_trips.get(position).getTrip_date());
        holder.history_name.setText(history_trips.get(position).getTrip_name());
        holder.history_time.setText(history_trips.get(position).getTrip_time());
        holder.history_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), history_details.class);
                Bundle b = new Bundle();

                b.putSerializable("history_trip", (Serializable) history_trips.get(position));


                intent.putExtras(b);
                view.getContext().startActivity(intent);
            }
        });


        holder.deleteTripHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm delete");
                builder.setMessage("Are you sure you want to del this");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NoteDAOImpl noteDAO1 = new NoteDAOImpl(context);
                        noteDAO1.deleteAllNote(history_trips.get(position).getTrip_id());
                        TripDAOImpl tripDAO = new TripDAOImpl(context);
                        tripDAO.deleteTrip(history_trips.get(position).getTrip_id());
                        history_trips.remove(history_trips.get(position));
                        notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });
                builder.show();

            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
