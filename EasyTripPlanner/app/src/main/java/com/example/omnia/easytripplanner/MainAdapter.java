package com.example.omnia.easytripplanner;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.omnia.easytripplanner.database.dao.daoimpl.NoteDAOImpl;
import com.example.omnia.easytripplanner.database.dao.daoimpl.ProfileDAOImpl;
import com.example.omnia.easytripplanner.database.dao.daoimpl.TripDAOImpl;
import com.example.omnia.easytripplanner.database.dto.ProfileDTO;
import com.example.omnia.easytripplanner.database.dto.TripDTO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Champion on 12-Mar-18.
 */


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.TripViewHolder> {
    private Context context;
    private static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1222;
    List<TripDTO> trips;
    AlarmManager alarmManager;
    AlarmManager alarmManagerRound;
    PendingIntent pendingIntent;
    PendingIntent pendingIntentRound;
    String email;
    ProfileDTO profileDTO;
    ProfileDAOImpl profileDAO;

    MainAdapter(Context context, List<TripDTO> trips) {
        this.trips = trips;
        this.context = context;
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView end;
        TextView date;
        TextView time;
        ImageButton deleteBtn;
        Button startBtn;
        TextView name;

        TripViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card);
            end = (TextView) itemView.findViewById(R.id.end);
            date = (TextView) itemView.findViewById(R.id.date);
            name = (TextView) itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            startBtn = itemView.findViewById(R.id.startTrip);
            deleteBtn = itemView.findViewById(R.id.deleteTrip);
        }

    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_cardview_layout, viewGroup, false);
        TripViewHolder tvh = new TripViewHolder(v);
        return tvh;
    }

    @Override
    public void onBindViewHolder(TripViewHolder holder, final int position) {
        holder.end.setText(trips.get(position).getTrip_end_point());
        holder.date.setText(trips.get(position).getTrip_date());
        holder.name.setText(trips.get(position).getTrip_name());
        holder.time.setText(trips.get(position).getTrip_time());
        profileDTO = new ProfileDTO();
        profileDAO = new ProfileDAOImpl(context);
        SharedPreferencesManager preferencesManager = new SharedPreferencesManager(context);
        email = preferencesManager.getEmail();
        profileDTO = profileDAO.getProfileByEmail(email);
        holder.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddTrip.class);
                view.getContext().startActivity(intent);


            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final int goingtrip = trips.get(position).getTrip_id();
                final int backtrip = trips.get(position).getTrip_id() + 1;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm delete");
                builder.setMessage("Are you sure you want to del this");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (trips.get(position).getTrip_rounded() == 1) {
                            TripDAOImpl tripDAO = new TripDAOImpl(context);
                            NoteDAOImpl noteDAO1 = new NoteDAOImpl(context);

                            Log.i("tt2nd trip id", "   " + backtrip);
                            TripDTO tripDTO2 = new TripDTO();
                            tripDTO2 = tripDAO.getTripByName(trips.get(position).getTrip_name() + " back", profileDTO.getProfile_id());
                            Log.i("2nd trip id after  ", "onClick:  " + tripDTO2.getTrip_id());
                            ///////// alarm  ///////////////
                            Log.i("tt2nd trip object", "onClick: " + tripDTO2);
                            if (tripDTO2.getTrip_id() != null) {
                                noteDAO1.deleteAllNote(tripDTO2.getTrip_id());
                                tripDAO.deleteTrip(tripDTO2.getTrip_id());
                                alarmManagerRound = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                                Intent myIntentRound = new Intent(context, AlarmReceiver.class);
                                pendingIntentRound = PendingIntent.getBroadcast(
                                        context, backtrip, myIntentRound, PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmManagerRound.cancel(pendingIntentRound);
                                pendingIntentRound.cancel();
                                trips.remove(trips.get(position));
                                notifyDataSetChanged();
                            }
                            alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                            Intent myIntent = new Intent(context,
                                    AlarmReceiver.class);
                            pendingIntent = PendingIntent.getBroadcast(
                                    context, goingtrip, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.cancel(pendingIntent);
                            pendingIntent.cancel();
                            ///////// delete trip 1 ///////////////
                            noteDAO1.deleteAllNote(goingtrip);
                            tripDAO.deleteTrip(goingtrip);
                            trips.remove(trips.get(position));
                            notifyDataSetChanged();
                        } else {
                            alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                            Intent myIntent = new Intent(context,
                                    AlarmReceiver.class);
                            pendingIntent = PendingIntent.getBroadcast(
                                    context, trips.get(position).getTrip_id(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.cancel(pendingIntent);
                            pendingIntent.cancel();
                            TripDAOImpl tripDAO = new TripDAOImpl(context);
                            tripDAO.deleteTrip(goingtrip);
                            trips.remove(trips.get(position));
                            notifyDataSetChanged();
                        }

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


        holder.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int goingtrip = trips.get(position).getTrip_id();

                double long1 = trips.get(position).getTrip_start_point_longitude();
                double lat1 = trips.get(position).getTrip_start_point_latitude();
                double long2 = trips.get(position).getTrip_end_point_longitude();
                double lat2 = trips.get(position).getTrip_end_point_latitude();

                TripDAOImpl tripDAO = new TripDAOImpl(context);
                trips.get(position).setTrip_status(com.example.omnia.easytripplanner.database.dto.Status.DONE);
                tripDAO.updateTrip(trips.get(position));

                alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                Intent myIntent = new Intent(context, AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(
                        context, goingtrip, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + lat1 + "," + long1 + "&daddr=" + lat2 + "," + long2));
                context.startActivity(intent);
            }
        });
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                Bundle b = new Bundle();

                b.putSerializable("trip", (Serializable) trips.get(position));

                intent.putExtras(b);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void removeItemAtPosition(int position) {
        trips.remove(position);
    }

}