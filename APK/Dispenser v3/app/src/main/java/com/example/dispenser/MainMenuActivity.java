package com.example.dispenser;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.renderscript.ScriptGroup;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class MainMenuActivity extends AppCompatActivity
{

    int IdDispenser;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_menu);

        //Oczytywanie przekazanych danych
        Bundle b = getIntent().getExtras();
        if(b!=null) IdDispenser = b.getInt("IdDispenser");
        else finish();

        //Ukrywanie buttona settings
        Button btn = findViewById(R.id.SettingButton);
        btn.setVisibility(View.INVISIBLE);
    }

    public void fSettingsButton(View v)
    {
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
    }
    public void fHistoryButton(View v)
    {
        Intent intent = new Intent(this,HistoryActivity.class);
        intent.putExtra("IdDispenser",IdDispenser);
        startActivity(intent);
    }

    public void fHoursButton(View v)
    {
        Intent intent = new Intent(this,HoursActivity.class);
        intent.putExtra("IdDispenser",IdDispenser);
        startActivity(intent);
    }
}
