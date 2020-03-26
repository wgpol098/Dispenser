package com.example.dispenser;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ResourceBundle;

public class AddDispenserActivity extends AppCompatActivity {

    private int idDispenser=0;
    private int ControlSum=0;
    private String idDispensers;
    private String login;
    private boolean user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_dispenser);

        //Odczytywanie przekazanego kodu
        Bundle b = getIntent().getExtras();
        if(b!=null)
        {
            //Jeśli user nie jest zapamiętany
            idDispenser = Integer.parseInt(b.getString("QrScan"));
            idDispensers = b.getString("idDispenser");
            login = b.getString("login");
            user = b.getBoolean("user");
        }
        else
        {
            //Jeśli user jest zapamiętany to pobierz dane z shared preferences
            SharedPreferences sharedPref = this.getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE);
            idDispensers = sharedPref.getString("IdDispenser", "");
        }

        //Generowanie sumy kontrolnej
        ValidationCodeGenerator val = new ValidationCodeGenerator(idDispenser);
        val.Generate();
        ControlSum = val.getValidationCode();

        //Tworzenie listenera jak się zmieni tekst w textboxie
        final EditText code = findViewById(R.id.MD5TextBox);
        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>3)
                {
                    StringBuilder tmp= new StringBuilder();
                    for(int i=0;i<3;i++) tmp.append(s.charAt(i));
                    code.setText(tmp.toString());
                    code.setSelection(3);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    //Akcja po kliknięciu ADD
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void fAddDispenserButton(View v)
    {
        EditText tv = findViewById(R.id.MD5TextBox);

        //Sprwadzdanie czy user podał w ogóle kod weryfikacyjny
        if(!tv.getText().toString().isEmpty())
        {
            if (ControlSum == Integer.parseInt(tv.getText().toString()))
            {
                //Tworzenie jsona do wysłania elementów
                JSONObject json = new JSONObject();
                try
                {
                    json.put("login", login);
                    json.put("idDispenser", idDispenser);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                //Wysłanie informacji na serwer jeśli suma kontrolna się zgadza metoda POST
                Connections connection = new Connections(this, "http://panda.fizyka.umk.pl:9092/api/Dispenser", "POST", json, false);
                connection.Connect();

                //Jeśli błąd to nie czytam odpowiedzi od serwera
                if (connection.getResponseCode() != 200)
                {
                    DialogFragment dialog = new MyDialog(getResources().getString(R.string.error), connection.Error());
                    dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                }
                //Jeśli wszystko poszło i serwer działa to powiadamiam o tym użytkownika
                else
                {
                    //Dodanie nowego dispensera do listy dispenserów w aplikacji
                    JSONArray JsonArray = null;
                    json = new JSONObject();
                    try
                    {
                        JsonArray = new JSONArray(idDispensers);
                        json.put("idDispenser", idDispenser);
                        JsonArray.put(json);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    //Najpierw sprawdź czy są sharedpreferences tj użytkownik jest zapisany
                    SharedPreferences sharedPref = this.getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE);
                    String dispenserID = sharedPref.getString("IdDispenser", "");

                    Intent intent;
                    if(user==true)
                    {
                       intent = new Intent(this, DispenserMenuActivity.class);
                    }
                    else
                    {
                        intent = new Intent(this,DispenserMenuDoctorActivity.class);
                    }


                    //Czyli jeżeli użytkownik jest zapamiętany
                    if (!dispenserID.isEmpty())
                    {
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("IdDispenser", JsonArray.toString());
                        editor.commit();
                    }
                    //Jeśli użytkownik nie jest zapamiętany
                    else
                    {
                        intent.putExtra("IdDispenser", JsonArray.toString());
                        intent.putExtra("login",login);
                    }

                    //Zapamiętywanie nazwy dispensera w pamięci aplikacji dla danego usera
                    sharedPref = this.getSharedPreferences(login, Context.MODE_PRIVATE);
                    EditText tmp = findViewById(R.id.dispenserNameTextBox);
                    String name = tmp.getText().toString();
                    if(name.isEmpty()) name = String.valueOf(idDispenser);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(String.valueOf(idDispenser),name);
                    editor.commit();

                    //zamknięcie poprzednich aktywności
                    finishAffinity();

                    //Odpalenie nowej aktywności i wyświetlenie komunikatu
                    startActivity(intent);
                    Toast toast = Toast.makeText(this, R.string.add_dispenser, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
            else
            {
                DialogFragment dialog = new MyDialog(getResources().getString(R.string.error), getString(R.string.wrong_validation_code));
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            }
        }
        else
        {
            DialogFragment dialog = new MyDialog(getResources().getString(R.string.error), getString(R.string.wrong_validation_code));
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }
    }
}
