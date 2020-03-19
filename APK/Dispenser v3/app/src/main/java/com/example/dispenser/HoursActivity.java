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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HoursActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    int IdDispenser=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_hours);

        //Dane, które wysyłam na serwer
        Bundle b = getIntent().getExtras();
        IdDispenser = b.getInt("IdDispenser");

        //Tworzenie jsona do wysyłania danych
        JSONObject json = new JSONObject();
        try
        {
            json.put("idDispenser",IdDispenser);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        Connections connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Android/GetPlan","POST",json,true);
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


        for(int i=0;i<jsonArray.length();i++)
        {
            JSONObject tmp=null;
            String str1="";
            String str2="";
            String str3="";
            int IdRecord=-2;
            try
            {
                tmp = jsonArray.getJSONObject(i);
                if(tmp!=null)
                {
                    str1 = tmp.getString("hour");
                    if(tmp.getString("minutes").length()==1) str2 = 0+ tmp.getString("minutes");
                    else str2 = tmp.getString("minutes");
                    str3 = tmp.getString("description");
                    IdRecord = Integer.parseInt(tmp.getString("idRecord"));
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            Button button = new Button(this);
            button.setText(str1 + ":" + str2 + " - " + str3);
            button.setId(IdRecord);
            button.setTextSize(20);
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
    }

    @Override
    public void onClick(View v)
    {
        Button  tmp = (Button) v;
        Intent intent = new Intent(this,ChangeHourActivity.class);
        intent.putExtra("idRecord",tmp.getId());
        intent.putExtra("IdDispenser",IdDispenser);
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v)
    {
        //Dodaj powiadomienia, czy usunąć//

        //Tworzenie jsona, który muszę wysłać na serwer, żeby usunął dane
        Button tmp = (Button) v;
        JSONObject json = new JSONObject();
        try
        {
            json.put("idRecord",tmp.getId());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        Connections connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Android","DELETE",json,false);
        connection.Connect();

        if(connection.getResponseCode()==200)
        {
            Toast toast = Toast.makeText(this,R.string.delete_hour,Toast.LENGTH_LONG);
            toast.show();
        }
        else
        {
            DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),connection.Error());
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }

        return true;
    }
}
