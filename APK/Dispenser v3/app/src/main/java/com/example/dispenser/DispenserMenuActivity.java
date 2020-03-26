package com.example.dispenser;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DispenserMenuActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener
{
    String idDispenser;
    String login;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dispenser_menu);

        //Czytanie listy dispenserów z SharedPreferences
        SharedPreferences sharedPref = this.getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE);
        idDispenser = sharedPref.getString("IdDispenser",null);
        login = sharedPref.getString("login",null);

        //Jeśli użytkownika nie ma w SharedPreferences
        if(idDispenser==null)
        {
            Bundle b = getIntent().getExtras();
            idDispenser = b.getString("IdDispenser");
        }
        if(login==null)
        {
            Bundle b = getIntent().getExtras();
            login = b.getString("login");
        }

        //Wyciąganie listy dispenserów
        JSONArray JsonDispList=null;
        try
        {
            JsonDispList = new JSONArray(idDispenser);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //Sprawdzanie czy podana osoba jest lekarzem czy zwykłym użytkownikiem

        //Tworzenie jsona do wysłania zapytania
        JSONObject json = new JSONObject();
        try
        {
            json.put("login",login);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //Wysyłanie zapytania do serwera
        Connections connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Account/check","POST",json,true);
        connection.Connect();

        //Jeśli błąd to nie czytam odpowiedzi od serwera
        if(connection.getResponseCode()!=200)
        {
            DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),connection.Error());
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
 //           finish();
        }
        //Jeśli wszystko poszło i serwer działa to trzeba odczytać odpowiedź
        else
        {
            //Odczytywanie czy uzytkownik jest zwykłym userem czy lekarzem
            int auth=-1;
            json = connection.JsonAnswer();
            try
            {
                auth = json.getInt("typeAccount");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }


            //Jeżeli jest zwykłym userem
            if(auth == 0)
            {
                //Tworzenie elementów względem listy dispenserów w SharedPreferences
                LinearLayout linearLayout = findViewById(R.id.linearLayout);
                ScrollView scroll = findViewById(R.id.scrollView);
                linearLayout.setOrientation(linearLayout.VERTICAL);
                scroll.setBackgroundResource(R.drawable.bg_gradient);

                //Tworzenie buttonów do danych dispenserów
                for(int i=0;i<JsonDispList.length();i++)
                {
                    json=null;
                    int IDdispenser=-1;
                    try
                    {
                        json = JsonDispList.getJSONObject(i);
                        IDdispenser = json.getInt("idDispenser");
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    //Jeżeli w json są wpisane jakieś dane
                    if(IDdispenser!=-1)
                    {
                        Button button = new Button(this);
                        button.setId(IDdispenser);
                        button.setTextSize(20);
                        button.setGravity(Gravity.CENTER);
                        button.setOnClickListener(this);
                        button.setOnLongClickListener(this);
                        linearLayout.addView(button);

                        //Przypisywanie name do buttona jeśli user ma przypisaną nazwę
                        sharedPref = this.getSharedPreferences(login, Context.MODE_PRIVATE);
                        button.setText(sharedPref.getString(String.valueOf(IDdispenser),String.valueOf(IDdispenser)));
                    }
                }

                //Jeśli nie ma dispenserów informacja o dodaniu dispensera!
                if(JsonDispList.length()==0)
                {
                    TextView tv = new TextView(this);
                    tv.setText(R.string.no_dispenser);
                    tv.setTextSize(20);
                    tv.setTextColor(Color.WHITE);
                    tv.setPadding(0,20,0,20);
                    tv.setGravity(Gravity.CENTER_HORIZONTAL);
                    linearLayout.addView(tv);
                }

                //Dodanie na końcu buttona do dodawania Dispnesnera do konta
                Button button = new Button(this);
                button.setText(getString(R.string.add));
                button.setId(-1);
                button.setTextSize(20);
                button.setGravity(Gravity.CENTER);
                button.setOnClickListener(this);
                linearLayout.addView(button);
            }
            if(auth>=1)
            {
                finish();
                Intent intent = new Intent(this,DispenserMenuDoctorActivity.class);
                intent.putExtra("IdDispenser",idDispenser);
                intent.putExtra("login",login);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        Button  tmp = (Button) v;
        int id = tmp.getId();

        //Jezeli guzik zawiera IdDispensera to odpal jego opcje
        if(id!=-1)
        {
            Intent intent = new Intent(this,MainMenuActivity.class);
            intent.putExtra("IdDispenser",tmp.getId());
            startActivity(intent);
        }
        //Zrób opcje dotyczącą dodawania dispensera do konta
        else
        {
            Intent intent = new Intent(this,QrScannerActivity.class);
            intent.putExtra("idDispenser",idDispenser);
            intent.putExtra("login",login);
            intent.putExtra("user",true);
            startActivity(intent);
        }
    }

    //Usuwanie dispensera ze swojego konta
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onLongClick(View v)
    {
        //TUTAJ OGARNIJ PRZEKAZYWANIE DALEJ TYCH DANYCH WSZYSTKICH BO POWODUJE BLEDY
        //W SENSIE SA DWA TE SAME DISPENSERY PO DODATNIU N
        //spowodowane to jest tym, ze nie nadpisuje zmiennej lokalnej idDispenser
        //Ale nie działa tylko dla niezapamiętanego użytkownika



        //Tworzenie jsona do wysłania na serwer
        JSONObject json = new JSONObject();

        try
        {
            json.put("login",login);
            json.put("idDispenser",v.getId());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //Połączenie z serwerem w celu DELETE
        //Jeśli otrzymamy odpowiedz 200 to usuwamy z listy w aplikacji
        //W innym przypadku nie można usunąć, bo coś jest nie tak
        Connections connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Dispenser","DELETE",json,false);
        connection.Connect();

        //Jeśli błąd to nie czytam odpowiedzi od serwera
        if(connection.getResponseCode()!=200)
        {
            DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),connection.Error());
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            finish();
        }
        else
        {
            //Odczytywanie danych z SharedPreferences
            SharedPreferences sharedpref = this.getSharedPreferences("LoginPreferences",Context.MODE_PRIVATE);
            String dispenserID = sharedpref.getString("IdDispenser","");

            //Przetwarzanie listy dispenserów
            if(!dispenserID.isEmpty())
            {
                try
                {
                    JSONArray JsonArray = new JSONArray(dispenserID);
                    for(int i=0;i<JsonArray.length();i++)
                    {
                        json = JsonArray.getJSONObject(i);
                        int tmp  = json.getInt("idDispenser");
                        if(v.getId()==tmp) JsonArray.remove(i);
                    }

                    idDispenser = JsonArray.toString();
                    SharedPreferences.Editor editor = sharedpref.edit();
                    editor.putString("IdDispenser",JsonArray.toString());
                    editor.commit();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                try
                {
                    JSONArray JsonArray = new JSONArray(idDispenser);
                    for(int i=0;i<JsonArray.length();i++)
                    {
                        json = JsonArray.getJSONObject(i);
                        int tmp  = json.getInt("idDispenser");
                        if(v.getId()==tmp) JsonArray.remove(i);
                    }
                    idDispenser = JsonArray.toString();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            //Jeśli user nie jest zapamiętany to wystarczy to
            //Usuwanie dispensera z listy
            LinearLayout l = findViewById(R.id.linearLayout);
            l.removeView(v);

            //jeśli usunięto ostatni dispenser
            if(l.getChildCount()==1)
            {
                TextView tv = new TextView(this);
                tv.setText(R.string.no_dispenser);
                tv.setTextSize(20);
                tv.setTextColor(Color.WHITE);
                tv.setPadding(0,20,0,20);
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                l.addView(tv);
            }
        }
        return true;
    }
}
