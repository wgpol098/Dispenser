package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DispenserMenuDoctorActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private String idDispenser;
    private String login;
    private Context context;
    private boolean doctor = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dispenser_menu_doctor);
        context = this;

        //Musisz sprawdzać czy jest w SharedPreferences też

        //Czytanie przekazanych danych
        Bundle b = getIntent().getExtras();
        if(b!=null)
        {
            idDispenser = b.getString("IdDispenser");
            login = b.getString("login");
        }

        //Wysyłanie zapytania czy dana osoba jest lekarzem, czy nie
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
        }
        //jeśli wszystko jest zrobione prawidłowe to czytam odpowiedź
        else
        {
            //Odczytywanie czy uzytkownik jest zwykłym userem czy lekarzem
            json = connection.JsonAnswer();
            try
            {
                if(json.getInt("typeAccount") > 0) doctor = true;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        //Tworzenie listy dispenserów

        final LinearLayout linearLayout = findViewById(R.id.linearLayout);
        JSONArray JsonArray=null;
        try
        {
            JsonArray = new JSONArray(idDispenser);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //Tworzenie całej listy dispenserów
        for(int i=0;i<JsonArray.length();i++)
        {
            int iddispenser=0;
            String DispenserName="";
            try
            {
                 json = JsonArray.getJSONObject(i);
                 iddispenser = json.getInt("idDispenser");
                 DispenserName = json.getString("name");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            //jeśli w jsonie są jakieś dane
            if(iddispenser!=-1)
            {
                Button button = new Button(this);
                button.setId(iddispenser);
                button.setTextSize(20);
                button.setGravity(Gravity.CENTER);
                button.setOnClickListener(this);
                button.setOnLongClickListener(this);
                button.setBackgroundResource(R.drawable.bg_rounded_control);

                //Tworzenie layouta
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(30, 20, 30, 0);
                linearLayout.addView(button,layoutParams);

                //Wyświetlanie nazwy dispensera
                if(!DispenserName.isEmpty()) button.setText(DispenserName);
                if(DispenserName=="null") button.setText(String.valueOf(iddispenser));
            }
        }

        //Wyszukiwanie dispenserów
        final EditText find = findViewById(R.id.FindEditBox);
        find.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                //Sprawdzanie czy istnieje dispenser o danym id, bądź nazwie na liście
                try
                {
                    linearLayout.removeAllViews();

                    JSONArray JsonArray = new JSONArray(idDispenser);
                    String text = find.getText().toString();

                    for(int i=0;i<JsonArray.length();i++)
                    {
                        JSONObject json = JsonArray.getJSONObject(i);
                        String tmp = String.valueOf(json.getInt("idDispenser"));
                        int id = json.getInt("idDispenser");
                        String DispenserName = json.getString("name");

                        //Sprawdzanie tylko na małych literach
                        if(DispenserName.toLowerCase().contains(text.toLowerCase()) || tmp.contains(text))
                        {
                            Button button = new Button(context);
                            button.setId(id);
                            button.setTextSize(20);
                            button.setGravity(Gravity.CENTER);
                            button.setOnClickListener((View.OnClickListener) context);
                            button.setOnLongClickListener((View.OnLongClickListener) context);
                            button.setBackgroundResource(R.drawable.bg_rounded_control);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(30, 20, 30, 0);
                            linearLayout.addView(button,layoutParams);

                            //Wyświetlanie nazwy dispensera
                            if(!DispenserName.isEmpty()) button.setText(DispenserName);
                            if(DispenserName=="null") button.setText(String.valueOf(id));
                        }
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void fQRButton(View v)
    {
        Intent intent = new Intent(this,QrScannerActivity.class);
        intent.putExtra("idDispenser",idDispenser);
        intent.putExtra("login",login);



        //ACZKOLWIEK MASZ TO DO SPRAWDZENIA
        intent.putExtra("user",!doctor);
        startActivity(intent);
    }

    public void fLogOutButton(View v)
    {
        SharedPreferences sharedpref = this.getSharedPreferences("LoginPreferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpref.edit();
        editor.putString("login",null);
        editor.putString("password",null);
        editor.putString("IdDispenser",null);
        editor.commit();

        finish();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v)
    {
        //jeśli nie jest doktorem to odpal kalendarz
        if (doctor == false)
        {
            Button btn = (Button)v;
            Intent intent = new Intent(this,CalendarActivity.class);
            intent.putExtra("idDispenser",btn.getId());
            startActivity(intent);
        }
        //Jeśli nie jest doktorem
        else
        {
            Button btn = (Button)v;
            Intent intent = new Intent(this,MainMenuDoctorActivity.class);
            intent.putExtra("idDispenser",btn.getId());
            startActivity(intent);
        }
    }

    @Override
    public boolean onLongClick(View v)
    {
        //Tworzenie jsona do wysłania na serwer, żeby usunąć dane w bazie danych
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

        //Tworzenie połączenia do wysłania
        Connections connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Dispenser","DELETE",json,false);
        connection.Connect();

        //Jeśli błąd to nie czytam odpowiedzi od serwera
        if(connection.getResponseCode()!=200)
        {
            DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),connection.Error());
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            finish();
        }
        //Jeśli wszystko poszło dobrze to usuń dispenser z lisy
        else
        {
            //Usuwanie dispensera z listy kontrolek
            LinearLayout linearLayout = findViewById(R.id.linearLayout);
            linearLayout.removeView(v);

            //Jeśli usunięto ustatni dispenser z listy
            if(linearLayout.getChildCount()==0)
            {
                TextView tv = new TextView(this);
                tv.setText(R.string.no_dispenser);
                tv.setTextSize(20);
                tv.setTextColor(Color.WHITE);
                tv.setPadding(0,20,0,20);
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                linearLayout.addView(tv);
            }

            //Usuwanie dispensera z listy dispenerów
            try
            {
                JSONArray jsonArray = new JSONArray(idDispenser);
                JSONArray jsonArray1 = new JSONArray();
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject json1 = jsonArray.getJSONObject(i);
                    int id = json1.getInt("idDispenser");
                    if(id != v.getId()) jsonArray1.put(json1);
                }
                idDispenser = jsonArray1.toString();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return true;
    }
}
