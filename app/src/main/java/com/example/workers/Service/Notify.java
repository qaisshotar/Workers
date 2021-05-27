package com.example.workers.Service;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Notify extends AsyncTask<Void, Void, Void> {

    private String ID, Msg;

    public Notify(String Reciver, String Msg) {
        ID = Reciver;
        this.Msg = Msg;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=AAAAedgWVb4:APA91bFrGWW0dsTE8AkRbVJus68qzkqeaP5x5HrUyzW_B8vfpEbub9ToRJYHM9Ep6iA4_VOq7Ff-LiChDhICdxgtc0JB7aeyZXJi_tCaulqxivBuXiJK-gTJQ5WmQT_bFwS9wKdCJquh");
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject json = new JSONObject();

            json.put("to", ID);

            JSONObject info = new JSONObject();
            info.put("body", Msg);   // Notification title
            info.put("title", "WorkerApp"); // Notification body
            info.put("content_available", "true");
            info.put("priority", "high");

            json.put("notification", info);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            conn.getInputStream();

            // Toast.makeText(getBaseContext(),"Done",Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.d("ErrorOne", "" + e.getMessage());
        }
        return null;
    }
}
