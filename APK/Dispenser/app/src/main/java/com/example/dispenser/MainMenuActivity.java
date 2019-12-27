package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainMenuActivity extends AppCompatActivity {

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
    public void fGetButton(View v)
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection ASPNETConnection = null;

                try
                {
                    URL ASPNETURL = new URL("http:/google.com");
                    ASPNETConnection = (HttpURLConnection) ASPNETURL.openConnection();
                    ASPNETConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");

                    if (ASPNETConnection.getResponseCode() == 200)
                    {
                        DialogFragment dialog = new MyDialog("Sukces","Działa!");
                        dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                    }
                    else
                    {
                        //Connection not successfull
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
                    ASPNETConnection.disconnect();
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
    Button hisButton;
    Button hourButton;
    Button getButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        hisButton = findViewById(R.id.HistoryButton);
        hourButton = findViewById(R.id.HoursButton);
        getButton =  findViewById(R.id.GetButton);
    }
}
