package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HoursActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{


    //JSONArray hoursArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_hours);

        //Dane, które wysyłam na serwer
        SharedPreferences sharedPref = this.getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE);
        int dispenserID = sharedPref.getInt("IdDispenser",-1);
        JSONObject json = new JSONObject();

        try
        {
            json.put("IdDispenser",dispenserID);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //Dane które otrzymuję od serwera
        JSONObject hour1 = new JSONObject();
        JSONObject hour2 = new JSONObject();
        JSONObject hour3 = new JSONObject();
        try
        {
            hour1.put("hour",12);
            hour1.put("minutes",45);
            hour1.put("description","Apap");
            hour1.put("id",5435);

            hour2.put("hour",14);
            hour2.put("minutes",45);
            hour2.put("description","Nerłokol");
            hour2.put("id",54352);

            hour3.put("hour",18);
            hour3.put("minutes",45);
            hour3.put("description","Eutanazol");
            hour3.put("id",5123);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();

        jsonArray.put(hour1);
        jsonArray.put(hour2);
        jsonArray.put(hour3);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(linearLayout.VERTICAL);
        linearLayout.setBackgroundResource(R.drawable.bg_gradient);

        DialogFragment dialog = new MyDialog("Wysyłam IdDispenser a odbieram GET:",jsonArray.toString());
        dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");

        for(int i=0;i<jsonArray.length();i++)
        {
            JSONObject tmp=null;
            String str1="";
            String str2="";
            String str3="";
            int IdDrug=-2;
            try
            {
                tmp = jsonArray.getJSONObject(i);
                if(tmp!=null)
                {
                    str1 = tmp.getString("hour");
                    str2 = tmp.getString("minutes");
                    str3 = tmp.getString("description");
                    IdDrug = Integer.parseInt(tmp.getString("id"));
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            Button button = new Button(this);
            button.setText(str1 + ":" + str2 + " - " + str3);
            button.setId(IdDrug);
            button.setTextSize(20);
            //button.setBackgroundResource(R.color.zxing_transparent);
            button.setGravity(Gravity.CENTER);
            button.setOnClickListener(this);
            button.setOnLongClickListener(this);

            linearLayout.addView(button);
        }

        //Dodanie na końcu buttona do dodawania godzin
        Button button = new Button(this);
        button.setText(getString(R.string.add));
        button.setId(-1);
        button.setTextSize(20);
        button.setGravity(Gravity.CENTER);
        button.setOnClickListener(this);
        linearLayout.addView(button);

        this.setContentView(linearLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onClick(View v)
    {
        Button  tmp = (Button) v;
        Intent intent = new Intent(this,ChangeHourActivity.class);
        intent.putExtra("IdDrug",tmp.getId());
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v)
    {
        DialogFragment dialog = new MyDialog("Wysyłam IdRekordu DELETE","I usuniesz dane");
        dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");

        //Tworzenie jsona, który muszę wysłać na serwer, żeby usunął dane
        Button tmp = (Button) v;
        final JSONObject json = new JSONObject();
        try
        {
            json.put("idRecord",208);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection ASPNETConnection = null;
                try
                {
                    URL ASPNETURL = new URL("http://panda.fizyka.umk.pl:9092/api/Android");
                    ASPNETConnection = (HttpURLConnection) ASPNETURL.openConnection();
                    ASPNETConnection.setRequestProperty("Content-Type","application/json");
                    ASPNETConnection.setRequestProperty("accept", "application/json");
                    ASPNETConnection.setDoOutput(true);
                    ASPNETConnection.setRequestMethod("DELETE");
                    DataOutputStream wr = new DataOutputStream(ASPNETConnection.getOutputStream());
                    wr.writeBytes(json.toString());
                    wr.flush();
                    wr.close();


                    if (ASPNETConnection.getResponseCode() == 200)
                    {
                        DialogFragment dialog = new MyDialog("Dodano",String.valueOf(ASPNETConnection.getResponseCode()));
                        dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                    }
                    else
                    {
                        //Connection not successfull
                        DialogFragment dialog = new MyDialog("Błąd",String.valueOf(ASPNETConnection.getResponseCode()));
                        dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                    }

                }
                catch (MalformedURLException e)
                {
                    //bad  URL, tell the user
                    DialogFragment dialog = new MyDialog("Błąd","Zły adres URL");
                    dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                }
                catch (IOException e)
                {
                    //network error/ tell the user
                    DialogFragment dialog = new MyDialog("Błąd","Aplikacja nie ma dostępu do internetu!");
                    dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                }
                finally
                {
                    if(ASPNETConnection != null) ASPNETConnection.disconnect();
                }
            }
        });
        return true;
    }
}
