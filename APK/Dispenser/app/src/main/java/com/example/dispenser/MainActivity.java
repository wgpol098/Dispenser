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

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Objects.requireNonNull(getSupportActionBar()).hide();
        }
        setContentView(R.layout.activity_main);

        //Reading user
        SharedPreferences sharedPref = this.getSharedPreferences("LoginPreferences",Context.MODE_PRIVATE);
        String login = sharedPref.getString("login", "");
        String password = sharedPref.getString("password","");

        if(!login.isEmpty() && !password.isEmpty())
        {
            JSONObject json = null;
            try
            {
                json = new JSONObject().put("login",login).put("password",password);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            //Sign In request
            Connections connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Account/login","POST",json,true);
            connection.Connect();

            if(connection.getResponseCode()!=200)
            {
                DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),connection.Error());
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            }
            //Reading answer from server
            else
            {
                JSONArray JsonArrayAnswer = connection.JsonArrayAnswer();
                finishAffinity();
                Intent intent = new Intent(this,DispenserMenuDoctorActivity.class);
                intent.putExtra("IdDispensers",JsonArrayAnswer.toString()).putExtra("login",login);
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
        //Reading data from textbox
        EditText l = findViewById(R.id.LoginTextBox);
        EditText p = findViewById(R.id.PasswordTextBox);
        String login = l.getText().toString();
        String password = p.getText().toString();

        JSONObject json = null;
        try
        {
            json = new JSONObject().put("login",login).put("password",password);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //Sign In request
        Connections connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Account/login","POST",json,true);
        connection.Connect();

        //If ERROR
        if(connection.getResponseCode()!=200)
        {
            DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),connection.Error());
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }
        //If all done, i read answer from server
        else
        {
            JSONArray JsonArrayAnswer = connection.JsonArrayAnswer();
            //Checking user authorization
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

            //If user is authorized
            if(authorization==1)
            {
                //Save login and password
                CheckBox ch = findViewById(R.id.RememberCheckBox);
                if(ch.isChecked())
                {
                    SharedPreferences sharedpref = this.getSharedPreferences("LoginPreferences",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpref.edit();
                    editor.putString("login",login).putString("password",password).apply();
                }

                finishAffinity();
                Intent intent = new Intent(this,DispenserMenuDoctorActivity.class);
                intent.putExtra("login",login).putExtra("IdDispensers",JsonArrayAnswer.toString());
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