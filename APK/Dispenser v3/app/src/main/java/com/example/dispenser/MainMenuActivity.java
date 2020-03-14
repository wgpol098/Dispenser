package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
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

public class MainMenuActivity extends AppCompatActivity {

    private String userName;
    private String password;
    private String dispenserID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_menu);
    }

    //@Override
    public void onBackPressed()
    {
        finish();
    }

    public void fHistoryButton(View v)
    {
        Intent intent = new Intent(this,HistoryActivity.class);
        startActivity(intent);
    }

    public void fHoursButton(View v)
    {
        Intent intent = new Intent(this,HoursActivity.class);
        startActivity(intent);
    }

    public void fLogOutButton(View v)
    {
        SharedPreferences sharedpref = this.getSharedPreferences("LoginPreferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpref.edit();
        editor.putString("login","");
        editor.putString("password","");
        editor.putInt("IdDispenser",-1);
        editor.commit();
        finish();
    }

    public void fGetButton(View v)
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection ASPNETConnection = null;
                try
                {
                    URL ASPNETURL = new URL("http://192.168.137.1/Api/Dispenser/Post");
                    //URL ASPNETURL = new URL("aktualnyAdresIP/api/Dispenser/Post");
                    ASPNETConnection = (HttpURLConnection) ASPNETURL.openConnection();

                    //InputStream responseBody = ASPNETConnection.getInputStream();
                    //InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                    //JsonReader jsonReader = new JsonReader(responseBodyReader);

                    if (ASPNETConnection.getResponseCode() == 200)
                    {
                        DialogFragment dialog = new MyDialog("Sukces","Działa");
                        dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");

                    }
                    else
                    {
                        //Connection not successfull
                        DialogFragment dialog = new MyDialog("Błąd","Połączenie nie powiodło się");
                        dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                    }

                }
                catch (MalformedURLException e)
                {
                    //bad  URL, tell the user
                    DialogFragment dialog = new MyDialog("Błąd","Zły adres URL");
                    dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                }
                catch (IOException e)
                {
                    //network error/ tell the user
                    DialogFragment dialog = new MyDialog("Błąd","Aplikacja nie ma dostępu do internetu!");
                    dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                }
                finally
                {
                    if(ASPNETConnection != null) ASPNETConnection.disconnect();
                }
/*
                    ASPNETConnection = (HttpsURLConnection) ASPNETURL.openConnection();
                    ASPNETConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");
            /*
            if (dataBaseConnection.getResponseCode() == 200)
            {
                dataBaseConnection.setRequestMethod("POST");
                // Create the data
                String myData = "message=Hello";

                // Enable writing
                dataBaseConnection.setDoOutput(true);

                // Write the data
                dataBaseConnection.getOutputStream().write(myData.getBytes());
                // Success
                // Further processing here
            }
            else {
                // Error handling code goes here
            }

                }

                catch (MalformedURLException e)
                {
                    //bad  URL, tell the user
                    DialogFragment dialog = new MyDialog("Eloszka","Message");
                    dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                }

                catch (IOException e)
                {
                    //network error/ tell the user
                    DialogFragment dialog = new MyDialog("Eluwina","Message");
                    dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                }

                finally
                {
                    ASPNETConnection.disconnect();
                }
                */

            }
        });
    }

}
