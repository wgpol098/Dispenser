package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_in);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        EditText id = findViewById(R.id.DispenserIDTextBox);
        SharedPreferences sharedpref = this.getSharedPreferences("QrScanner", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpref.edit();
        Boolean scanned = sharedpref.getBoolean("Scanned",false);
        String IdDispenser = sharedpref.getString("QrScan", "");

        //A co się stanie jak podamy ID które nie istnieje?
        if(scanned==true)
        {
            id.setText(IdDispenser);
            editor.putBoolean("Scanned",false);
            editor.commit();
        }
    }

    //Odpalenie skanera QR
    public void fScanButton(View v)
    {
        Intent intent = new Intent(this,QrScannerActivity.class);
        startActivity(intent);
    }

    public void fSignInButton(View v)
    {
        EditText userName = findViewById(R.id.EmailTextBox);
        EditText password = findViewById(R.id.PasswordTextBox);
        EditText dispenserID = findViewById(R.id.DispenserIDTextBox);

        //Sprawdzanie czy użytkownik w ogóle wpisał jakieś dane
        if(userName.getText().toString().isEmpty() || password.getText().toString().isEmpty() || dispenserID.getText().toString().isEmpty())
        {
            DialogFragment dialog = new MyDialog("Bład","Podałeś nieprawidłowe dane!");
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }
        else
        {
            //Generowanie jsona do wysyłania danych na serwer
            //Metoda musi sprawdzać czy taki login jest już w bazie danych i czy zarejestrowała nowego użytkownika.
            JSONObject json1 = new JSONObject();

            try
            {
                json1.put("email",userName.getText().toString());
                json1.put("password",password.getText().toString());
                json1.put("dispenserID",dispenserID.getText().toString());
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            //JSON z odpowiedzią od serwera
            JSONObject json = new JSONObject();

            try
            {
                json.put("answer",true);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            boolean answer=false;
            try
            {
                answer = json.getBoolean("answer");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            //Sprwadzanie czy dodano dane do bazy danych o użytkowniku
            //jeśli użytkownik istnieje
            if(answer==false)
            {
                DialogFragment dialog = new MyDialog("Error","Podany użytkownik już istnieje w bazie danych");
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            }
            else
            {
                DialogFragment dialog = new MyDialog("Sukces","Pomyślnie dodano użytkownika" + userName.getText()+" "+password.getText()+" "+dispenserID.getText());
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            }
        }
    }
}
