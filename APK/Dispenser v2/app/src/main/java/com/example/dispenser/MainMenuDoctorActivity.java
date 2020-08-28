package com.example.dispenser;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.StrictMath.abs;

public class MainMenuDoctorActivity extends AppCompatActivity implements View.OnClickListener
{
    //Trzymanie otrzymanych danych o historii
    int idDispenser=0;
    JSONArray ArrayHistory;
    JSONArray ArrayPlans;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_menu_doctor);

        //Pobieranie przekazanych danych
        Bundle b = getIntent().getExtras();
        if(b!=null) idDispenser = b.getInt("idDispenser");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onResume()
    {
        super.onResume();

        //Zmienna przechowująca informacje ile i która tablica była pusta
        //0 - wszystko git
        //1 - brak planów bądź historii
        //2 - brak planów i historii - wtedy musisz pokazać komunikat
        byte blank=0;

        //obiekty do wstawiania danych
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();

        //Tworzenie jsona do wysłania rządania o historię i plan danego dispensera
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

        //Tworzenie buttona do dodawania danych
        Button button = new Button(this);
        button.setId(-1);
        button.setTextSize(20);
        button.setText(R.string.add);
        button.setGravity(Gravity.CENTER);
        button.setOnClickListener(this);
        button.setBackgroundResource(R.drawable.bg_rounded_control);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30, 20, 30, 0);
        linearLayout.addView(button,layoutParams);

        //Tworzenie labela do zaznaczenia, że jest to plan
        TextView tv = new TextView(this);
        tv.setTextSize(20);
        tv.setTextColor(Color.WHITE);
        tv.setText(R.string.plan);
        linearLayout.addView(tv,layoutParams);

        TableRow tablerow = new TableRow(this);
        tablerow.setBackgroundColor(Color.WHITE);
        tablerow.setMinimumHeight(4);
        linearLayout.addView(tablerow,layoutParams);


        //GETDOCTORPLANS
        Connections connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Android/GetDoctorPlan","POST",json,true);
        connection.Connect();

        //Jeśli błąd to nie czytam odpowiedzi od serwera
        if(connection.getResponseCode()!=200) blank++;
        //Jeśli wszystko poszło i serwer działa to trzeba odczytać odpowiedź
        else
        {
            JSONArray JsonArray = connection.JsonArrayAnswer();
            ArrayPlans = JsonArray;
            if(JsonArray.length()==0) blank++;

            //Odczytywanie danych i tworzenie kontrolek do wyświetlania danych
            for(int i=0;i<JsonArray.length();i++)
            {
                //Odczytywanie danych o konkretnym leku
                String description = "";
                String start = "";
                String firsthour = "";
                int periodicity = 0;
                int idRecord = 0;
                JSONArray didntTakeArray = new JSONArray();

                try
                {
                    JSONObject json1 = JsonArray.getJSONObject(i);
                    description = json1.getString("description");
                    start = json1.getString("start");
                    firsthour = json1.getString("firstHour");
                    periodicity = json1.getInt("periodicity");
                    didntTakeArray = json1.getJSONArray("tabDidnttake");
                    idRecord = json1.getInt("idRecord") * -1;
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                //Tworzenie kontrolki
                tv = new TextView(this);
                tv.setTextColor(Color.WHITE);
                tv.setText(R.string.description);
                tv.append(": " + description);
                tv.setId(idRecord);
                tv.append(System.getProperty("line.separator"));
                tv.append(getResources().getString(R.string.start_date));
                tv.append(": ");
                tv.append(start.replace('-','.') + " " + firsthour.replace('-',':'));
                tv.append(System.getProperty("line.separator"));
                tv.append(getResources().getString(R.string.end_date) + ": " + getResources().getString(R.string.now));
                tv.append(System.getProperty("line.separator"));
                tv.append(getResources().getString(R.string.periodicity) + ": " + periodicity);
                tv.setOnClickListener(this);
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
                linearLayout.addView(tv,layoutParams);
            }
        }

        //Tworzenie labela do zaznaczenia, że jest to Historia
        tv = new TextView(this);
        tv.setTextSize(20);
        tv.setTextColor(Color.WHITE);
        tv.setText(R.string.history);
        linearLayout.addView(tv,layoutParams);

        tablerow = new TableRow(this);
        tablerow.setBackgroundColor(Color.WHITE);
        tablerow.setMinimumHeight(4);
        linearLayout.addView(tablerow,layoutParams);

        //GETDOCTORHISTORY
        //Wysyłanie zapytania do serwera
        connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Android/GetDoctorHistory","POST",json,true);
        connection.Connect();

        //Jeśli błąd to nie czytam odpowiedzi od serwera
        if(connection.getResponseCode()!=200) blank++;
        //Jeśli wszystko poszło i serwer działa to trzeba odczytać odpowiedź
        else
        {
            JSONArray JsonArray = connection.JsonArrayAnswer();
            ArrayHistory = JsonArray;
            if(JsonArray.length()==0) blank++;

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
                //if(periodicity==0) periodicity=1;

                //Tworzenie kontrolki
                tv = new TextView(this);
                tv.setText(R.string.description);
                tv.append(": ");
                tv.append(description);
                tv.append(System.getProperty("line.separator"));
                tv.append(getResources().getString(R.string.start_date) + ": " + start + " " + firsthour);
                tv.append(System.getProperty("line.separator"));
                tv.append(getResources().getString(R.string.end_date) + ": " + end);
                tv.append(System.getProperty("line.separator"));
                if(periodicity != 0)
                {
                    tv.append(getResources().getString(R.string.periodicity) + ": " + String.valueOf(periodicity));
                    tv.append(System.getProperty("line.separator"));
                }
                tv.setPadding(10,10,10,10);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10,10,10,10);
                params.gravity = Gravity.CENTER;
                //To jest coś takie głupie
                params.width = 1000;
                tv.setLayoutParams(params);

                //Tworzenie gradientu dla TextView
                GradientDrawable gd = new GradientDrawable();
                gd.setCornerRadius(45);
                if(didntTakeArray.length() == 0) gd.setColor(Color.GREEN);
                else gd.setColor(Color.rgb(255,50,50));
                gd.setStroke(20,Color.alpha(0));
                gd.setStroke(2, Color.BLACK);

                tv.setBackground(gd);
                //wyświetlanie kiedy nie wziął leku
                for(int j=0;j<didntTakeArray.length();j++)
                {
                    JSONObject jtmp;
                    try
                    {
                        jtmp = didntTakeArray.getJSONObject(j);
                        tv.append(System.getProperty("line.separator"));
                        tv.append(jtmp.getString("date"));
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                tv.setOnClickListener(this);
                tv.setId(i);
                linearLayout.addView(tv);
            }
        }

        //Jeśli dispenser nie ma planów ani historii to wyświetl napisik
        if(blank==2)
        {
            tv = new TextView(this);
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
        //-1 - dodawanie elementu
        // >=0 - element historii
        // <-1 - element planu
        int id = v.getId();

        //jeśli jest to element historii
        if(id>=0)
        {
            JSONObject json=null;

            try
            {
                json = ArrayHistory.getJSONObject(id);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            Intent intent = new Intent(this,HistoryDoctorActivity.class);
            intent.putExtra("info",json.toString());
            startActivity(intent);
        }
        //jeśli jest to button od dodawania
        else if(id==-1)
        {
            Intent intent = new Intent(this,ChangeHourActivity.class);
            intent.putExtra("idRecord",-1);
            intent.putExtra("IdDispenser",idDispenser);
            startActivity(intent);
        }
        //jesli jest to element planu
        else
        {
            id = abs(id);
            Intent intent = new Intent(this,ChangeHourActivity.class);
            intent.putExtra("idRecord",id);
            intent.putExtra("IdDispenser",idDispenser);
            startActivity(intent);
        }

    }
}
