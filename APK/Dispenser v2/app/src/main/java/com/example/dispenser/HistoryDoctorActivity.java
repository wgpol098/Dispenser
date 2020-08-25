package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class HistoryDoctorActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_history_doctor);

        //Odczytywanie przekazanych danych i ich analiza
        String description = "";
        String start = "";
        String end = "";
        String firstHour = "";
        int periodicity=0;
        int count=0;

        JSONObject json = null;
        Bundle b = getIntent().getExtras();
        if(b!=null)
        {
            try
            {
                json= new JSONObject(b.getString("info"));

                //Odczytywanie przekazanych danych
                description = json.getString("description");
                start = json.getString("start");
                end = json.getString("end");
                firstHour = json.getString("firstHour");
                periodicity = json.getInt("periodicity");
                count = json.getInt("count");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),json.toString());
        dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");

        //ANALIZA PRZEKAZANYCH DANYCH

        //wyznaczanie dat na podstawie periodyczności
        String[] hour = firstHour.split("-");
        String[] dats = new String[count];
        for(int i=0;i<count;i++)
        {
            int tmp = Integer.parseInt(hour[0])+periodicity*i;
            tmp=tmp%24;
            dats[i] = tmp + ":" + hour[1];
            //h = Integer.getInteger(hour[0]);
            //ihour*=periodicity;
            //dats[i] = String.valueOf(ihour);
            //dats[i] = hour[0]+periodicity*count;
        }

        //obiekty do wstawiania danych
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        ScrollView scroll = findViewById(R.id.scrollView);
        //scroll.setBackgroundResource(R.drawable.bg_gradient);

        //Tworzenie kontrolki, która wyświetli przetworzone dane
        TextView tv = new TextView(this);
        tv.setText(description);
        tv.append(System.getProperty("line.separator"));
        tv.append(start + " -- " + end);
        //Wyświetlanie dokładnych dat w których były brane leki XD
        for(int i=0;i<dats.length;i++)
        {
            tv.append(System.getProperty("line.separator"));
            tv.append(dats[i]);
        }
        linearLayout.addView(tv);
    }
}
