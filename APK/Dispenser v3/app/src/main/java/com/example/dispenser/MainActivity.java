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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import org.json.JSONArray;
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
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        //Czytanie danych zalogowanego użytkownika
        SharedPreferences sharedPref = this.getSharedPreferences("LoginPreferences",Context.MODE_PRIVATE);
        String login = sharedPref.getString("login", "");
        String password = sharedPref.getString("password","");
        String dispenserID = sharedPref.getString("IdDispenser","");

        //Jeśli użytkownik jest zapamiętany
        if(!login.isEmpty() && !password.isEmpty() && !dispenserID.isEmpty())
        {
            //Tworzenie jsona do wysłania na serwer
            JSONObject json = new JSONObject();
            try
            {
                json.put("login",login);
                json.put("password",password);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            //Wysyłanie na serwer zapytania odnoście logowania użytkownika
            Connections connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Account/login","POST",json,true);
            connection.Connect();

            JSONArray JsonArrayAnswer = new JSONArray();
            //Jeśli błąd to nie czytam odpowiedzi od serwera
            if(connection.getResponseCode()!=200)
            {
                DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),connection.Error());
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
//                finish();
            }
            //Jeśli wszystko poszło i serwer działa to trzeba odczytać odpowiedź
            else
            {
                JsonArrayAnswer = connection.JsonArrayAnswer();

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("IdDispenser",JsonArrayAnswer.toString());
                editor.commit();

                finish();
                Intent intent = new Intent(this,DispenserMenuActivity.class);
                startActivity(intent);
            }
        }
        //Animacje guziczków
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.milkshake);
        Button signB = findViewById(R.id.SignInButton);
        Button logB = findViewById(R.id.LogInButton);
        signB.startAnimation(myAnim);
        logB.startAnimation(myAnim);
    }

    public void fdbutton(View v)
    {
            Intent intent = new Intent(this,DispenserMenuActivity.class);
            startActivity(intent);
    }

    public void fSignInButton(View v)
    {
        Intent intent = new Intent(this,SignInActivity.class);
        startActivity(intent);
    }

    public void fLogInButton(View v)
    {
        //Animacja po kliknięcu na przycisk
//        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.milkshake);
//        v.startAnimation(myAnim);

        //Odczytywanie danych z textboxów
        EditText l = findViewById(R.id.LoginTextBox);
        EditText p = findViewById(R.id.PasswordTextBox);
        String login = l.getText().toString();
        String password = p.getText().toString();


        //Json z zapytaniem do serwera
        JSONObject json = new JSONObject();
        try
        {
            json.put("login",login);
            json.put("password",password);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //Wysyłanie na serwer zapytania odnoście logowania użytkownika
        Connections connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Account/login","POST",json,true);
        connection.Connect();

        //Jeśli błąd to nie czytam odpowiedzi od serwera
        if(connection.getResponseCode()!=200)
        {
            DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),connection.Error());
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            //finish();
        }
        //Jeśli wszystko poszło i serwer działa to trzeba odczytać odpowiedź
        else
        {
            JSONArray JsonArrayAnswer = connection.JsonArrayAnswer();
            //Analiza tablicy Json odczytanej od serwera
            //Sprawdzanie czy użytkownik uzyskał autoryzację
            int authorization=0;
            if(JsonArrayAnswer.length()==1)
            {
                json=null;
                int spr=-1;
                try
                {
                    json = JsonArrayAnswer.getJSONObject(0);
                    spr = json.getInt("idDispenser");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                if(spr!=-1) authorization = 1;
            }
            else authorization=1;


            //Zapamiętywanie loginu, password i IdDispensera
            CheckBox ch = findViewById(R.id.RememberCheckBox);

            if(ch.isChecked() && authorization==1)
            {
                SharedPreferences sharedpref = this.getSharedPreferences("LoginPreferences",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpref.edit();
                editor.putString("login",login);
                editor.putString("password",password);
                editor.putString("IdDispenser",JsonArrayAnswer.toString());
                editor.commit();
            }

            if(authorization==1)
            {
                finish();
                Intent intent = new Intent(this,DispenserMenuActivity.class);
                intent.putExtra("login",login);
                intent.putExtra("IdDispenser",JsonArrayAnswer.toString());
                startActivity(intent);
            }
            //Tutaj dodaj opcję, że zły login albo hasło
            else
            {
                MyDialog dialog = new MyDialog("Błąd","Zły login lub hasło");
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            }
        }
    }
}
