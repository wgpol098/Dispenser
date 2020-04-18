package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DispenserMenuDoctorActivity extends AppCompatActivity implements View.OnClickListener
{
    private String idDispenser;
    private String login;
    private Context context;
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


        //Tworzenie listy dispenserów dla lekarza

        final LinearLayout linearLayout = findViewById(R.id.linearLayout);
//        ScrollView scroll = findViewById(R.id.scrollView);
        //linearLayout.setOrientation(linearLayout.VERTICAL);
//        scroll.setBackgroundResource(R.drawable.bg_gradient);

        JSONArray JsonArray=null;
        try
        {
            JsonArray = new JSONArray(idDispenser);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //Tworzenie całej listy dispenserów dla lekarza
        for(int i=0;i<JsonArray.length();i++)
        {
            JSONObject json;
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
//                button.setOnLongClickListener(this);
                linearLayout.addView(button);

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

                        if(DispenserName.contains(text) || tmp.contains(text))
                        {
                            Button button = new Button(context);
                            button.setId(id);
                            button.setTextSize(20);
                            button.setGravity(Gravity.CENTER);
                            button.setOnClickListener((View.OnClickListener) context);
//                button.setOnLongClickListener(this);
                            linearLayout.addView(button);

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
        intent.putExtra("user",false);
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
        Button btn = (Button)v;
        Intent intent = new Intent(this,MainMenuDoctorActivity.class);
        intent.putExtra("idDispenser",btn.getId());
        startActivity(intent);
    }
}
