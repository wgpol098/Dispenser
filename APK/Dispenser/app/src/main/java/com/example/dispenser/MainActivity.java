package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    public void fdbutton(View v)
    {
        HttpURLConnection dataBaseConnection = null;

        try{
            URL dataBaseURL = new URL("https://api.github.com/");

            dataBaseConnection = (HttpsURLConnection) dataBaseURL.openConnection();
            dataBaseConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");

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
        }
        catch (IOException e)
        {
            //network error/ tell the user
        }
        finally
        {
            dataBaseConnection.disconnect();
        }
    }
    Button dbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbutton=(Button) findViewById(R.id.DiodaButton);
    }
}
