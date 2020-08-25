package com.example.dispenser;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
        //PO CO TO TRZYMAC W SHARED PREFERENCES?
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

            JSONArray JsonArrayAnswer;
            //Jeśli błąd to nie czytam odpowiedzi od serwera
            if(connection.getResponseCode()!=200)
            {
                DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),connection.Error());
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            }
            //Jeśli wszystko poszło i serwer działa to trzeba odczytać odpowiedź
            else
            {
                JsonArrayAnswer = connection.JsonArrayAnswer();

                SharedPreferences.Editor editor = sharedPref.edit();
                dispenserID = JsonArrayAnswer.toString();
                editor.putString("IdDispenser",dispenserID);
                editor.commit();
                finishAffinity();

                //Trzeba teraz przekazywać te dane dobrze
                Intent intent = new Intent(this,DispenserMenuDoctorActivity.class);
                intent.putExtra("IdDispenser",dispenserID);
                intent.putExtra("login",login);
                startActivity(intent);
            }
        }
    }

    public void fdbutton(View v)
    {
            Intent intent = new Intent(this,DispenserMenuActivity.class);
            startActivity(intent);
    }

    public void fPresentationButton(View v)
    {
        Intent intent = new Intent(this,PresentationActivity.class);
        startActivity(intent);
    }

    public void fSignInButton(View v)
    {
        Intent intent = new Intent(this,SignInActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void fLogInButton(View v)
    {
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
                try
                {
                    json = JsonArrayAnswer.getJSONObject(0);
                    if(json.getInt("idDispenser")!=-1) authorization=1;
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
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

            //jeśli użytkownik zyskał autoryzację
            if(authorization==1)
            {
                finishAffinity();
                Intent intent = new Intent(this,DispenserMenuDoctorActivity.class);
                intent.putExtra("login",login);
                intent.putExtra("IdDispenser",JsonArrayAnswer.toString());
                startActivity(intent);
            }
            else
            {
                MyDialog dialog = new MyDialog(getResources().getString(R.string.error),getResources().getString(R.string.wrong_login_password));
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            }
        }
    }
}
