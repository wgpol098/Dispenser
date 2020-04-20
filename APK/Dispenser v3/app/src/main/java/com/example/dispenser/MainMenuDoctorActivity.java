package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainMenuDoctorActivity extends AppCompatActivity implements View.OnClickListener
{
    //Trzymanie otrzymanych danych o historii i o zmiennej
    int n=1000;
    JSONArray ArrayHistory;
    JSONArray ArrayPlans;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_menu_doctor);

        //Zmienna przechowująca informacje ile i która tablica była pusta
        //0 - wzsystko git
        //1 - brak planów bądź historii
        //2 - brak planów i historii - wtedy musisz pokazać komunikat
        byte blank=0;
        //Pobieranie przekazanych danych
        int idDispenser=0;
        Bundle b = getIntent().getExtras();
        if(b!=null) idDispenser = b.getInt("idDispenser");

        //obkiety do wstawiania danych
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        ScrollView scroll = findViewById(R.id.scrollView);
        //scroll.setBackgroundResource(R.drawable.bg_gradient);

        //Tworzenie jsona do wysłania jsona o historię i plan danego dispensera
        //Trzeba dodać info, że jest pusty i nie ma żadnych danych
        JSONObject json = new JSONObject();
        try
        {
            json.put("idDispenser",idDispenser);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        //GETDOCTORPLANS
        Connections connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Android/GetDoctorPlan","POST",json,true);
        connection.Connect();

        //Jeśli błąd to nie czytam odpowiedzi od serwera
        if(connection.getResponseCode()!=200)
        {
            DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),connection.Error());
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }
        //Jeśli wszystko poszło i serwer działa to trzeba odczytać odpowiedź
        else
        {
            JSONArray JsonArray = connection.JsonArrayAnswer();
            ArrayPlans = JsonArray;
            if(JsonArray.length()==0) blank++;

            //sprawdzanie odpowiedzi od serwera
//            DialogFragment dialog = new MyDialog("Plan",JsonArray.toString());
//            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");

            //Odczytywanie danych i tworzenie kontrolek do wyświetlania danych
            for(int i=0;i<JsonArray.length();i++)
            {
                //Odczytywanie danych o konkretnym leku
                String description = "";
                String start = "";
                String firsthour = "";
                int periodicity = 0;
                JSONArray didntTakeArray = new JSONArray();

                try
                {
                    JSONObject json1 = JsonArray.getJSONObject(i);
                    description = json1.getString("description");
                    start = json1.getString("start");
                    firsthour = json1.getString("firstHour");
                    periodicity = json1.getInt("periodicity");
                    didntTakeArray = json1.getJSONArray("tabDidnttake");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                //Tworzenie kontrolki
                TextView tv = new TextView(this);
                tv.setText(description);
                tv.append(System.getProperty("line.separator"));
                tv.append(start + " " + firsthour + " - Nał");
                tv.append(System.getProperty("line.separator"));
                tv.append(String.valueOf(periodicity));
                //wyświetlanie kiedy nie wziął leku
                for(int j=0;j<didntTakeArray.length();j++)
                {
                    JSONObject jtmp;
                    try
                    {
                        jtmp = didntTakeArray.getJSONObject(j);
                        String stmp = jtmp.getString("date");
                        tv.append(System.getProperty("line.separator"));
                        tv.append(stmp);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                linearLayout.addView(tv);
            }

        }



        //GETDOCTORHISTORY
        //Wysyłanie zapytania do serwera
        connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Android/GetDoctorHistory","POST",json,true);
        connection.Connect();

        //Jeśli błąd to nie czytam odpowiedzi od serwera
        if(connection.getResponseCode()!=200)
        {
            DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),connection.Error());
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }
        //Jeśli wszystko poszło i serwer działa to trzeba odczytać odpowiedź
        else
        {
            JSONArray JsonArray = connection.JsonArrayAnswer();
            ArrayHistory = JsonArray;
            if(JsonArray.length()==0) blank++;

            //sprawdzanie odpowiedzi od serwera
//            DialogFragment dialog = new MyDialog("History",JsonArray.toString());
//            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");

            //Tworzenie kontrolek, które wyświetlają dane z serwera
            for(int i=JsonArray.length()-1;0<=i;i--)
            {
                //Odczytywanie danych o konkretnym leku
                String description = "";
                String start = "";
                String end = "";
                String firsthour = "";
                int periodicity = 0;
                int count = 0;
                JSONArray didntTakeArray = new JSONArray();

                try
                {
                    JSONObject json1 = JsonArray.getJSONObject(i);
                    description = json1.getString("description");
                    start = json1.getString("start");
                    end = json1.getString("end");
                    firsthour = json1.getString("firstHour");
                    periodicity = json1.getInt("periodicity");
                    count = json1.getInt("count");
                    didntTakeArray = json1.getJSONArray("tabDidnttake");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                //Modyfikacja danych
                firsthour = firsthour.replace("-",":");
                if(periodicity==0) periodicity=1;

                //Tworzenie kontrolki
                TextView tv = new TextView(this);
                tv.setText(description);
                tv.append(System.getProperty("line.separator"));
                if(start.equals(end)) tv.append(start + " " + firsthour);
                else tv.append(start + " " + firsthour + " - " + end);
                tv.append(System.getProperty("line.separator"));
                tv.append(String.valueOf(periodicity*count));
                if(didntTakeArray.length()==0) tv.setBackgroundColor(Color.GREEN);
                else tv.setBackgroundColor(Color.RED);
                //wyświetlanie kiedy nie wziął leku
                for(int j=0;j<didntTakeArray.length();j++)
                {
                    JSONObject jtmp;
                    try
                    {
                        jtmp = didntTakeArray.getJSONObject(j);
                        String stmp = jtmp.getString("date");
                        tv.append(System.getProperty("line.separator"));
                        tv.append(stmp);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                tv.setOnClickListener(this);
                tv.setId(i+n);
                linearLayout.addView(tv);
            }
        }

        //Jeśli dispenser nie ma planów ani historii to wyświetl napisik
        if(blank==2)
        {
            TextView tv = new TextView(this);
            tv.setText(R.string.no_history_plans);
            tv.setTextSize(20);
            tv.setTextColor(Color.BLACK);
            tv.setPadding(0,20,0,20);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            linearLayout.addView(tv);
        }
    }

    @Override
    public void onClick(View v)
    {
        //Odczytywanie jakie Id ma button i sprawdzanie czy to jest historia czy to jest plan
        int id = v.getId()-n;
        JSONObject json=null;

        try
        {
            json = ArrayHistory.getJSONObject(id);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


//        DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),json.toString());
//        dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");

        Intent intent = new Intent(this,HistoryDoctorActivity.class);
        intent.putExtra("info",json.toString());
        startActivity(intent);
    }
}
