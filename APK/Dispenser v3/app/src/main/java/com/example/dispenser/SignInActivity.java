package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity
{
    Boolean email_flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_in);

        //Sprwadzanie poprawności danych
        final EditText email = findViewById(R.id.EmailTextBox);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Boolean real=false;
                Boolean dot=false;
                for(int i=0;i<s.length();i++)
                {
                    if(s.charAt(i)=='@') real = true;
                    if(s.charAt(i)=='.') dot=true;
                }

                if(!real && !dot)
                {
                    TextView tv = findViewById(R.id.wrongEmailLabel);
                    tv.setVisibility(View.VISIBLE);
                    email.setTextColor(Color.RED);
                    email_flag=false;
                }
                if(real && dot)
                {
                    TextView tv = findViewById(R.id.wrongEmailLabel);
                    tv.setVisibility(View.INVISIBLE);
                    email.setTextColor(Color.WHITE);
                    email_flag=true;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    //Kliknięcie signinbuttona
    public void fSignInButton(View v)
    {
        EditText userName = findViewById(R.id.EmailTextBox);
        EditText password = findViewById(R.id.PasswordTextBox);
        EditText confirmpassword = findViewById(R.id.confirmPasswordTextBox);
        EditText name = findViewById(R.id.NameTextBox);
        CheckBox doctor = findViewById(R.id.DoctorCheckbox);

        //Sprawdzanie czy użytkownik w ogóle wpisał jakieś dane
        if(userName.getText().toString().isEmpty() || password.getText().toString().isEmpty() || confirmpassword.getText().toString().isEmpty() || name.getText().toString().isEmpty())
        {
            DialogFragment dialog = new MyDialog("Bład","Podałeś nieprawidłowe dane!");
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }
        else
        {
            //Sprawdzenie czy wprowadzone dane są prawidłowe
            if(!email_flag)
            {
                DialogFragment dialog = new MyDialog("Bład","Podałeś zły email!");
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            }
            else
            {
                //Generowanie jsona do wysyłania danych na serwer
                JSONObject json = new JSONObject();
                try
                {
                    json.put("login",userName.getText().toString());
                    json.put("password",password.getText().toString());
                    json.put("name",name.getText().toString());

                    //Sprawdzanie czy konto jest lekarzem
                    if(doctor.isChecked()) json.put("typeAccount",1);
                    else json.put("typeAccount",0);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                //Wysyłanie zapytania na serwer z rejestracją użytkownika POST
                Connections connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Account/register","POST",json,false);
                connection.Connect();

                //Jeśli wszystko git to dodano użytkownika
                if(connection.getResponseCode()==200)
                {
                    Toast toast = Toast.makeText(this,R.string.sign_in_success,Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                }
                else
                {
                    DialogFragment dialog = new MyDialog("Error","Podany użytkownik już istnieje w bazie danych");
                    dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                }
            }
        }
    }
}
