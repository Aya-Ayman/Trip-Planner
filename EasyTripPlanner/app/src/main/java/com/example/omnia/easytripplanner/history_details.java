package com.example.omnia.easytripplanner;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.omnia.easytripplanner.database.dao.daoimpl.NoteDAOImpl;
import com.example.omnia.easytripplanner.database.dao.daoimpl.TripDAOImpl;
import com.example.omnia.easytripplanner.database.dto.NoteDTO;
import com.example.omnia.easytripplanner.database.dto.TripDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class history_details extends AppCompatActivity {
    double lat1;
    double long1;
    double lat2;
    double long2;
    double avgLong;
    double avgLat;
    String result = null;
    private static Handler handler;
    ImageView mapImage;
    TextView history_name;
    TextView history_date;
    TextView history_time;
    TextView history_start;
    TextView history_end;
    TextView history_type;
    TextView history_status;
    Button history_delete;
    Integer type;
    ListView list;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    ArrayList<NoteDTO> noteDTOS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        mapImage = findViewById(R.id.mapImage);
        history_name = findViewById(R.id.history_name);
        history_date = findViewById(R.id.history_date);
        history_time = findViewById(R.id.history_time);
        history_start = findViewById(R.id.history_start);
        history_end = findViewById(R.id.history_end);
        history_type = findViewById(R.id.history_type);
        history_delete = findViewById(R.id.history_delete);
        history_status = findViewById(R.id.history_status);

        ////////////////////////////// progres bar //////////////////////////////

        final TripDTO ex2 = (TripDTO) getIntent().getSerializableExtra("history_trip");
        history_name.setText(ex2.getTrip_name());
        history_date.setText(ex2.getTrip_date());
        history_time.setText(ex2.getTrip_time());
        history_end.setText(ex2.getTrip_end_point());
        history_start.setText(ex2.getTrip_start_point());
        history_status.setText(ex2.getTrip_status());
        type = ex2.getTrip_rounded();
        if (type == 1) {
            history_type.setText("Round");
        } else {
            history_type.setText("Single");
        }
        lat1 = ex2.getTrip_start_point_latitude();
        long1 = ex2.getTrip_start_point_longitude();
        lat2 = ex2.getTrip_end_point_latitude();
        long2 = ex2.getTrip_end_point_longitude();
        avgLat = (lat1 + lat2) / 2;
        avgLong = (long1 + long2) / 2;

        ////////////////////////////// static map part //////////////////////////////////

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                Bundle b = msg.getData();
                String res = b.getString("JSON");
                try {
                    JSONObject reader = new JSONObject(res);
                    JSONArray items = reader.getJSONArray("routes");
                    JSONObject jobj1 = items.getJSONObject(0);
                    JSONObject jarray2 = jobj1.getJSONObject("overview_polyline");
                    String code = jarray2.getString("points");
                    for (int i = 0; i < items.length(); ++i) {
                        class LongOperation extends AsyncTask<String, Void, Bitmap> {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                            }

                            @Override
                            protected void onPostExecute(Bitmap bitmap) {
                                super.onPostExecute(bitmap);
                                if (bitmap == null)
                                    mapImage.setImageResource(R.mipmap.cancelled);
                                mapImage.setImageBitmap(bitmap);
                            }

                            @Override
                            protected void onProgressUpdate(Void... values) {
                                super.onProgressUpdate(values);
                            }

                            @Override
                            protected Bitmap doInBackground(String... strings) {
                                String s;
                                s = strings[0];
                                Bitmap bitmap = download(s);
                                return bitmap;
                            }

                            public Bitmap download(String code) {
                                Bitmap b = null;
                                InputStream is;
                                HttpURLConnection connection;
                                URL url1;
                                try {

                                    url1 = new URL("https://maps.googleapis.com/maps/api/staticmap?center=" + avgLat + "," + avgLong + "&" +
                                            "zoom=9&size=800x380&maptype=roadmap&path=weight:7%10Ccolor:orange%7Cenc:" + code + "&key=AIzaSyCeYHDhDctqGmb5APIdyWrd-imDO2DkQHc");
                                    connection = (HttpURLConnection) url1.openConnection();
                                    is = connection.getInputStream();
                                    b = BitmapFactory.decodeStream(is);
                                    is.close();

                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                return b;
                            }
                        }
                        new LongOperation().execute(code);
                    }

                } catch (JSONException e) {
                    Log.i("Test", e.toString());
                }


            }
        }
        ;
        final String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + lat1 + "," + long1 + "&destination=" + lat2 + "," + long2 + "&key=AIzaSyCeYHDhDctqGmb5APIdyWrd-imDO2DkQHc";
        Runnable r = new Runnable() {

            @Override
            public void run() {
                InputStream is;
                HttpURLConnection connection;
                URL url1;
                try {
                    url1 = new URL(url);
                    connection = (HttpURLConnection) url1.openConnection();
                    is = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("JSON", result);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        };
        Thread th = new Thread(r);
        th.start();

        history_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(history_details.this);
                builder.setTitle("Confirm delete");
                builder.setMessage("Are you sure you want to delete this trip");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TripDAOImpl tripDAO2 = new TripDAOImpl(history_details.this);
                        tripDAO2.deleteTrip(ex2.getTrip_id());
                        finish();
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

        //notes
        NoteDAOImpl noteDAO = new NoteDAOImpl(history_details.this);
        //static id must be replaced
        noteDTOS = (ArrayList<NoteDTO>) noteDAO.getAllTripNotes(ex2.getTrip_id());
        list = (ListView) findViewById(R.id.listview2);
        list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.item, arrayList);
        list.setAdapter(adapter);


        for (NoteDTO noteDTO : noteDTOS) {
            arrayList.add(noteDTO.getNote_description());
        }
    }

}


