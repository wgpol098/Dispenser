package com.example.dispenser;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
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

    @Override
    public void onResume()
    {
        super.onResume();

        //Zmienna przechowująca informacje ile i która tablica była pusta
        //0 - wzsystko git
        //1 - brak planów bądź historii
        //2 - brak planów i historii - wtedy musisz pokazać komunikat
        byte blank=0;

        //obkiety do wstawiania danych
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();
        ScrollView scroll = findViewById(R.id.scrollView);
        scroll.setBackgroundResource(R.drawable.bg_gradient);

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

        //Tworzenie buttona do dodawania danych
        Button button = new Button(this);
        button.setId(-1);
        button.setTextSize(20);
        button.setText(R.string.add);
        button.setGravity(Gravity.CENTER);
        button.setOnClickListener(this);
//                button.setOnLongClickListener(this);
        linearLayout.addView(button);


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
                TextView tv = new TextView(this);
                tv.setTextColor(Color.WHITE);
                tv.setText(description);
                tv.setId(idRecord);
                tv.append(System.getProperty("line.separator"));
                tv.append(start + " " + firsthour + " - Nał");
                tv.append(System.getProperty("line.separator"));
                tv.append(String.valueOf(periodicity));
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
                tv.setId(i);
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
