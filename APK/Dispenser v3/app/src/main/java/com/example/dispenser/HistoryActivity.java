package com.example.dispenser;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

import static org.apache.http.params.CoreProtocolPNames.USER_AGENT;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_history);

        //Tworzenie json, który jest wysyłany do serwera
        Bundle b = getIntent().getExtras();
        int dispenserID = b.getInt("IdDispenser");

        //Tworzenie jsona do wysłania metody
        final JSONObject json = new JSONObject();
        try
        {
            json.put("idDispenser",dispenserID);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //Wysyłanie zapytania do serwera
        Connections connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Android/GetHistory","POST",json,true);
        connection.Connect();

        JSONArray jsonArray = null;
        //Jeśli błąd to nie czytam odpowiedzi od serwera
        if(connection.getResponseCode()!=200)
        {
            DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),connection.Error());
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            finish();
        }
        //Jeśli wszystko poszło i serwer działa to trzeba odczytać odpowiedź
        else
        {
            jsonArray = connection.JsonArrayAnswer();
        }


        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        ScrollView scroll = findViewById(R.id.scrollView);
        linearLayout.setOrientation(linearLayout.VERTICAL);
        scroll.setBackgroundResource(R.drawable.bg_gradient);
        linearLayout.setPadding(20,40,20,20);

        for(int i=0;i<jsonArray.length();i++)
        {
            JSONObject tmp=null;
            Date datetime=null;
            int nr_window=-1;
            String description="";
            int flag=-10;
            try
            {
                 tmp = jsonArray.getJSONObject(i);
                 if(tmp!=null)
                 {
                     datetime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(tmp.getString("dateAndTime"));
                     nr_window = tmp.getInt("noWindow");
                     description = tmp.getString("description");
                     flag = tmp.getInt("flag");
                 }
            }
            catch (JSONException | ParseException e)
            {
                e.printStackTrace();
            }
            if(tmp!=null)
            {

                //Tworzenie textView do wypisywania danych
                TextView tv = new TextView(this);
                tv.setText(i+1 + ": " + description + ", " + datetime.getDate()+ "." + datetime.getMonth() + "." + datetime.getYear() + " " + datetime.getHours() + ":" + datetime.getMinutes());
                tv.setTextSize(20);
                tv.setPadding(0,20,0,20);
                tv.setGravity(Gravity.CENTER_HORIZONTAL);

                //Tworzenie gradientu do tła
                GradientDrawable shape = new GradientDrawable();
                shape.setShape(GradientDrawable.RECTANGLE);
                shape.setColor(Color.RED);
                shape.setCornerRadius(30);

                if(flag==1) shape.setColor(Color.GREEN);
                else if(flag==0) shape.setColor(Color.RED);
                else shape.setColor(Color.YELLOW);

                tv.setBackgroundDrawable(shape);
                linearLayout.addView(tv);
            }
        }

        //Jeśli w historii nic nie ma wyświetl o tym informacje
        if(jsonArray.length()==0)
        {
            TextView tv = new TextView(this);
            tv.setText(R.string.no_history);
            tv.setTextSize(20);
            tv.setTextColor(Color.WHITE);
            tv.setPadding(0,20,0,20);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            linearLayout.addView(tv);
        }
    }
}
