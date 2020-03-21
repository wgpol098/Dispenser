package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DispenserMenuActivity extends AppCompatActivity implements View.OnClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dispenser_menu);

        //Czytanie listy dispenserów z SharedPreferences
        SharedPreferences sharedPref = this.getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE);
        String dispenserID = sharedPref.getString("IdDispenser",null);

        //Jeśli użytkownika nie ma w SharedPreferences
        if(dispenserID==null)
        {
            Bundle b = getIntent().getExtras();
            dispenserID = b.getString("IdDispenser");
        }

        //Wyciąganie listy dispenserów
        JSONArray JsonDispList=null;
        try {
            JsonDispList = new JSONArray(dispenserID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),JsonDispList.toString());
        dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");

        //Tworzenie elementów względem listy dispenserów w SharedPreferences
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        ScrollView scroll = findViewById(R.id.scrollView);
        linearLayout.setOrientation(linearLayout.VERTICAL);
        scroll.setBackgroundResource(R.drawable.bg_gradient);

        //Jeżeli jest tylko jeden dispenser na koncie to po co to wyświetlać
        if(JsonDispList.length()==1)
        {
            JSONObject json=null;
            int IDdispenser=-1;
            try
            {
                json = JsonDispList.getJSONObject(0);
                IDdispenser = json.getInt("idDispenser");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            Intent intent = new Intent(this,MainMenuActivity.class);
            intent.putExtra("IdDispenser",IDdispenser);
            startActivity(intent);

        }
        //Jeżeli jest więcej dispenserów to twórz guziki
        else
        {
            for(int i=0;i<JsonDispList.length();i++)
            {
                JSONObject json=null;
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
                    button.setText(String.valueOf(IDdispenser));
                    button.setId(IDdispenser);
                    button.setTextSize(20);
                    button.setGravity(Gravity.CENTER);
                    button.setOnClickListener(this);
                    linearLayout.addView(button);
                }
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
            startActivity(intent);
        }
    }
}
