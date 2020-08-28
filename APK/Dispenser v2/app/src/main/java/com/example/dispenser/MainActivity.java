package com.example.dispenser;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        //Jeśli użytkownik jest zapamiętany
        if(!login.isEmpty() && !password.isEmpty())
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
                finishAffinity();
                Intent intent = new Intent(this,DispenserMenuDoctorActivity.class);
                intent.putExtra("IdDispenser",JsonArrayAnswer.toString());
                intent.putExtra("login",login);
                startActivity(intent);
            }
        }
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
                    if(JsonArrayAnswer.getJSONObject(0).getInt("idDispenser")!=-1) authorization=1;
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else authorization=1;

            //jeśli użytkownik zyskał autoryzację
            if(authorization==1)
            {
                //Zapamiętywanie loginu, password i IdDispensera
                CheckBox ch = findViewById(R.id.RememberCheckBox);
                if(ch.isChecked())
                {
                    SharedPreferences sharedpref = this.getSharedPreferences("LoginPreferences",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpref.edit();
                    editor.putString("login",login);
                    editor.putString("password",password);
                    editor.apply();
                }

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