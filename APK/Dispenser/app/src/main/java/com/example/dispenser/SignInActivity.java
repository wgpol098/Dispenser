package com.example.dispenser;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.graphics.Color;
import android.os.Build;
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

import java.util.Objects;

public class SignInActivity extends AppCompatActivity
{
    Boolean email_flag=false, password_flag=false, cpassword_flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Objects.requireNonNull(getSupportActionBar()).hide();
        }
        setContentView(R.layout.activity_sign_in);

        //Checking passwords
        final EditText confirm_password = findViewById(R.id.confirmPasswordTextBox);
        confirm_password.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                EditText password = findViewById(R.id.PasswordTextBox);
                if(confirm_password.getText().toString().equals(password.getText().toString())) cpassword_flag=true;
                TextView tv = findViewById(R.id.wrongPasswordLabel2);
                if(cpassword_flag) tv.setVisibility(View.INVISIBLE);
                else tv.setVisibility(View.VISIBLE);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        //Checking password
        //1 digit, 1 small letter, 1 big letter and 1 special
        final EditText password = findViewById(R.id.PasswordTextBox);
        password.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                boolean Number=false, Uletter=false, Letter=false, Special=false;

                if(s.length()>7)
                {
                    for(int i=0;i<s.length();i++)
                    {
                        if(!Character.isLetterOrDigit(s.charAt(i))) Special=true;
                        else
                        {
                            if(Character.isUpperCase(s.charAt(i))) Uletter = true;
                            if(Character.isDigit(s.charAt(i))) Number = true;
                            if(Character.isLowerCase(s.charAt(i))) Letter = true;
                        }
                    }
                }

                TextView tv = findViewById(R.id.wrongPasswordLabel);
                if(!Uletter || !Number || !Letter || !Special)
                {
                    tv.setVisibility(View.VISIBLE);
                    password_flag=false;
                }
                else
                {
                    tv.setVisibility(View.INVISIBLE);
                    password_flag = true;
                }

                EditText cpassword = findViewById(R.id.confirmPasswordTextBox);
                if(password.getText().toString().equals(cpassword.getText().toString()))
                {
                    cpassword_flag=true;
                    TextView tv1 = findViewById(R.id.wrongPasswordLabel2);
                    tv1.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        //Checking email properties
        final EditText email = findViewById(R.id.EmailTextBox);
        email.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                boolean real=false, dot=false;
                for(int i=0;i<s.length();i++)
                {
                    if(s.charAt(i)=='@') real = true;
                    if(s.charAt(i)=='.') dot=true;
                }

                TextView tv = findViewById(R.id.wrongEmailLabel);
                if(!real || !dot)
                {
                    tv.setVisibility(View.VISIBLE);
                    email.setTextColor(Color.RED);
                    email_flag=false;
                }
                if(real && dot)
                {
                    tv.setVisibility(View.INVISIBLE);
                    email.setTextColor(Color.BLACK);
                    email_flag=true;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    //SingInButton Click
    public void fSignInButton(View v)
    {
        EditText userName = findViewById(R.id.EmailTextBox);
        EditText password = findViewById(R.id.PasswordTextBox);
        EditText confirmpassword = findViewById(R.id.confirmPasswordTextBox);
        EditText name = findViewById(R.id.NameTextBox);
        CheckBox doctor = findViewById(R.id.DoctorCheckbox);

        //If controls is empty
        if(userName.getText().toString().isEmpty() || password.getText().toString().isEmpty() || confirmpassword.getText().toString().isEmpty() )
        {
            //Showing ERROR labels
            if(userName.getText().toString().isEmpty())
            {
                TextView tv = findViewById(R.id.wrongEmailLabel);
                tv.setVisibility(View.VISIBLE);
            }
            if(password.getText().toString().isEmpty())
            {
                TextView tv = findViewById(R.id.wrongPasswordLabel);
                tv.setVisibility(View.VISIBLE);
            }

            DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),getResources().getString(R.string.something_went_wrong));
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }
        //If controls isn't empty
        else
        {
            //Checking given data
            if(!email_flag || !password_flag || !cpassword_flag)
            {
                DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),getResources().getString(R.string.wrong_email));
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            }
            else
            {
                //Creating json to sending to server
                JSONObject json = new JSONObject();
                try
                {
                    //Checking name
                    String Sname="";
                    if(!name.getText().toString().isEmpty()) Sname = name.getText().toString();
                    json.put("login",userName.getText().toString()).put("password",password.getText().toString()).put("name",Sname);

                    //Checking typeAccount (doctor or user)
                    if(doctor.isChecked()) json.put("typeAccount",1);
                    else json.put("typeAccount",0);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                //Sending request to server with register user info
                Connections connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Account/register","POST",json,false);
                connection.Connect();

                if(connection.getResponseCode()==200)
                {
                    Toast toast = Toast.makeText(this,R.string.sign_in_success,Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                }
                else
                {
                    DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),getResources().getString(R.string.user_already_exists));
                    dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                }
            }
        }
    }
}