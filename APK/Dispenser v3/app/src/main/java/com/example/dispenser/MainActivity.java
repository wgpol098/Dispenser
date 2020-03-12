package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = this.getSharedPreferences("LoginPreferences",Context.MODE_PRIVATE);
        String login = sharedPref.getString("login", "");
        String password = sharedPref.getString("password","");
        int dispenserID = sharedPref.getInt("IdDispenser",-1);

        if(!login.isEmpty() && !password.isEmpty() && dispenserID!=-1)
        {
//            MyDialog dialog = new MyDialog("Błąd",login + " " + password + " " + dispenserID);
//            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");


            //JSON do sprawdzenia połączenia z serwerem (dodaj)


            Intent intent = new Intent(this,MainMenuActivity.class);
            startActivity(intent);
        }
    }

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
        //Odczytywanie danych z textboxów

        EditText l = findViewById(R.id.LoginTextBox);
        EditText p = findViewById(R.id.PasswordTextBox);

        String login = l.getText().toString();
        String password = p.getText().toString();


        //Json z zapytaniem do serwera
        JSONObject zap = new JSONObject();
        try
        {
            zap.put("Login",login);
            zap.put("Password",password);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //Miejsce na stworzenie połączenia z serwerem i wysłanie zapytania

        //Json z odpowiedzią od serwera
        JSONObject json = new JSONObject();
        try
        {
            json.put("authorization",1);
            json.put("IdDispenser",1234);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //Odczytywanie jsona od serwera
        //authorization == 1 to git
        //inna liczba to jakiś błąd
        int authorization=-1;
        int dispenserID=-1;

        try
        {
            authorization = json.getInt("authorization");
            dispenserID = json.getInt("IdDispenser");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        if(authorization==1)
        {
            MainMenuActivity csa = new MainMenuActivity();
            Intent intent = new Intent(this,MainMenuActivity.class);
//            intent.putExtra("userName",login);
//            intent.putExtra("password",password);
//            intent.putExtra("dispenserID",dispenserID);
            startActivity(intent);
        }
        //Tutaj dodaj opcję, że zły login albo hasło
        else
        {
            MyDialog dialog = new MyDialog("Błąd","Zły login lub hasło");
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }

        //Zapamiętywanie loginu, password i IdDispensera
        CheckBox ch = findViewById(R.id.RememberCheckBox);

        if(ch.isChecked() && authorization==1)
        {
            SharedPreferences sharedpref = this.getSharedPreferences("LoginPreferences",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpref.edit();
            editor.putString("login",login);
            editor.putString("password",password);
            editor.putInt("IdDispenser",dispenserID);
            editor.commit();
        }
    }
}
