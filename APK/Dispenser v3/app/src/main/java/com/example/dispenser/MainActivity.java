package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

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
            Intent intent = new Intent(this,MainMenuActivity.class);
            startActivity(intent);
    }

    public void fSignInButton(View v)
    {
        Intent intent = new Intent(this,SignInActivity.class);
        startActivity(intent);
    }

    public void fLogInButton(View v)
    {
        //User authorization
        Boolean authorization=true;
        String userName="Patryk";
        String password="Najlepszy";
        String dispenserID="123";
        if(authorization==true)
        {
            MainMenuActivity csa = new MainMenuActivity();
            Intent intent = new Intent(this,MainMenuActivity.class);
            intent.putExtra("userName",userName);
            intent.putExtra("password",password);
            intent.putExtra("dispenserID",dispenserID);
            startActivity(intent);
        }
        else
        {
            DialogFragment dialog = new MyDialog("Błąd","Zły login lub hasło");
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
