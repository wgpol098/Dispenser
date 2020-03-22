package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ResourceBundle;

public class AddDispenserActivity extends AppCompatActivity {

    int idDispenser=0;
    int ControlSum=0;
    String idDispensers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_dispenser);

        //Odczytywanie przekazanego kodu
        Bundle b = getIntent().getExtras();
        String qrCode=null;
        if(b!=null)
        {
            qrCode = b.getString("QrScan");
            idDispensers = b.getString("idDispenser");
        }

        //Zapisywanie idDispensera
        idDispenser = Integer.parseInt(qrCode);

        //Obliczanie sumy kontrolnej.
        int sum=0;
        int control=124;
        char[] digits=qrCode.toCharArray();

        for(int i=0;i<qrCode.length();i++)
        {
            int number = Character.getNumericValue(digits[i]);
            number *= control*(i+1);
            if(number>999) number=number%1000;
            sum+=number;

            control+=9;
        }
        if(sum<1000) sum*=control;
        sum = sum % 1000;
        if(sum==0)
        {
            sum=control* qrCode.length() * (qrCode.length() % 10 + 10);
            sum = sum % 1000;
        }

        EditText tv = findViewById(R.id.DebugTextBox);
        if(qrCode!=null) tv.setText(Integer.toString(sum));

        //Zapusywanie sumy kontrolnej
        ControlSum=sum;
    }

    //Akcja po kliknięciu ADD
    public void fAddDispenserButton(View v)
    {
        EditText tv = findViewById(R.id.MD5TextBox);
        if(ControlSum==Integer.parseInt(tv.getText().toString()))
        {
            DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),"Suma się zgadza mordeczko");
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");

            //Wysłanie informacji na serwer jeśli suma kontrolna się zgadza metoda PUT


            //Jeśli wszystko się zgadza i serwer zwraca 200 to dodaj idDispenser do SharedPreferences
            //Najpierw sprawdź czy są sharedpreferences tj użytkownik jest zapisany
            SharedPreferences sharedPref = this.getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE);
            String dispenserID = sharedPref.getString("IdDispenser","");

            //Odczytywanie danych z SharedPreferences i dodanie do tablicy json nowego Dispensera
            //Czyli jeżeli użytkownik jest zapamiętany
            if(!dispenserID.isEmpty())
            {
                JSONArray JsonArray = null;
                JSONObject json = null;
                try
                {
                    json = new JSONObject();
                    JsonArray = new JSONArray(dispenserID);
                    json.put("idDispenser",idDispenser);
                    JsonArray.put(json);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("IdDispenser",JsonArray.toString());
                editor.commit();

                Intent intent = new Intent(this,DispenserMenuActivity.class);
                startActivity(intent);
            }
            //Jeśli użytkownik nie jest zapamiętany
            //Tu coś wywala
            else
            {
                JSONObject json = new JSONObject();
                try
                {
                    json.put("idDispenser",idDispenser);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                JSONArray JsonArray = null;
                try
                {
                    JsonArray = new JSONArray(idDispensers);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                JsonArray.put(json);

                Intent intent = new Intent(this,DispenserMenuActivity.class);
                intent.putExtra("IdDispenser",JsonArray.toString());
                startActivity(intent);
            }
        }
        else
        {
            DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),"Błędna suma!");
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }


    }
}
