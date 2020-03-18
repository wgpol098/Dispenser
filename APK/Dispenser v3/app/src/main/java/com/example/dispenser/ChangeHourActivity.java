package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.bluetooth.le.AdvertisingSetParameters;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class ChangeHourActivity extends AppCompatActivity {

    int IdDrug;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_change_hour);

        Bundle b = getIntent().getExtras();
        if(b!=null) IdDrug = b.getInt("IdDrug");
        int hour=0;
        int minutes=0;
        String description="";
        int count=0;
        int periodicity=0;

        JSONObject json = new JSONObject();
        if(IdDrug > 0)
        {
            //Dane, które wysyłam na serwer
            JSONObject zap = new JSONObject();
            try
            {
                zap.put("IdDrug",IdDrug);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            //JSON, który odczytuję z serwera
            json = new JSONObject();
            try
            {
                json.put("hour", 12);
                json.put("minutes", 33);
                json.put("description", "Eutanazol");
                json.put("count", 3);
                json.put("periodicity", 1);

                DialogFragment dialog = new MyDialog("Wysyłam IdRekordu odbieram GET:", json.toString());
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        if(IdDrug!=-1)
        {
            try
            {
                hour = json.getInt("hour");
                minutes = json.getInt("minutes");
                description = json.getString("description");
                count = json.getInt("count");
                periodicity = json.getInt("periodicity");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Calendar rightNow = Calendar.getInstance();
            hour = rightNow.get(Calendar.HOUR_OF_DAY);
            minutes = rightNow.get(Calendar.MINUTE);
        }

        EditText h = findViewById(R.id.HoursTextBox);
        EditText m = findViewById(R.id.MinutesTextBox);
        EditText c = findViewById(R.id.CountTextBox);
        EditText p = findViewById(R.id.PeriodicityTextBox);
        EditText d = findViewById(R.id.DescriptionTextBox);
        h.setText(Integer.toString(hour));
        m.setText(Integer.toString(minutes));
        c.setText(Integer.toString(count));
        p.setText(Integer.toString(periodicity));
        d.setText(description);

    }

    public void fOkButton(View v)
    {
        EditText h = findViewById(R.id.HoursTextBox);
        EditText m = findViewById(R.id.MinutesTextBox);
        EditText d = findViewById(R.id.DescriptionTextBox);
        EditText c = findViewById(R.id.CountTextBox);
        EditText p = findViewById(R.id.PeriodicityTextBox);

        //Dodaj sprawdzanie przy add
        int tmphour = Integer.parseInt(h.getText().toString());
        int tmpminutes = Integer.parseInt(m.getText().toString());
        String tmpdescription = d.getText().toString();
        int tmpcount = Integer.parseInt(c.getText().toString());
        int tmpperiodicity = Integer.parseInt(p.getText().toString());

        SharedPreferences sharedPref = this.getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE);
        int dispenserID = sharedPref.getInt("IdDispenser",-1);


        if(tmphour>23 || tmphour<0 || tmpminutes>59 || tmpminutes < 0)
        {
            DialogFragment dialog = new MyDialog("Bład","Co Ty mnie tu za godzinę podajesz");
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }
        else
        {
            //Updatowanie danych
            //Wywala jak nic nie wpiszesz
            if(IdDrug!=-1)
            {
                //Tworzenie jsona do wysłania do serwera
                final JSONObject json = new JSONObject();
                try
                {
                    json.put("idRecord",410);
                    json.put("hour",tmphour);
                    json.put("minutes",tmpminutes);
                    json.put("count",tmpcount);
                    json.put("periodicity",tmpperiodicity);
                    json.put("opis",tmpdescription);
                    json.put("idDispenser",dispenserID);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection ASPNETConnection = null;
                        try
                        {
                            URL ASPNETURL = new URL("http://panda.fizyka.umk.pl:9092/api/Android");
                            ASPNETConnection = (HttpURLConnection) ASPNETURL.openConnection();
                            ASPNETConnection.setRequestProperty("Content-Type","application/json");
                            ASPNETConnection.setRequestProperty("accept", "application/json");
                            ASPNETConnection.setDoOutput(true);
                            ASPNETConnection.setRequestMethod("PUT");
                            DataOutputStream wr = new DataOutputStream(ASPNETConnection.getOutputStream());
                            wr.writeBytes(json.toString());
                            wr.flush();
                            wr.close();


                            if (ASPNETConnection.getResponseCode() == 200)
                            {
                                DialogFragment dialog = new MyDialog("Dodano",String.valueOf(ASPNETConnection.getResponseCode()));
                                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                            }
                            else
                            {
                                //Connection not successfull
                                DialogFragment dialog = new MyDialog("Błąd",String.valueOf(ASPNETConnection.getResponseCode()));
                                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                            }

                        }
                        catch (MalformedURLException e)
                        {
                            //bad  URL, tell the user
                            DialogFragment dialog = new MyDialog("Błąd","Zły adres URL");
                            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                        }
                        catch (IOException e)
                        {
                            //network error/ tell the user
                            DialogFragment dialog = new MyDialog("Błąd","Aplikacja nie ma dostępu do internetu!");
                            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                        }
                        finally
                        {
                            if(ASPNETConnection != null) ASPNETConnection.disconnect();
                        }
                    }
                });

                DialogFragment dialog = new MyDialog("Wysyłam UPDATE",json.toString());
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            }
            //Dodawanie danych
            //To już prawie elegancko zrobione jest ogarnij json i będzie giciorek
            else
            {
                //Tworzenie jsona do wysłania danych na serwer
                final JSONObject json = new JSONObject();
                try
                {
                    json.put("hour",tmphour);
                    json.put("minutes",tmpminutes);
                    json.put("count",tmpcount);
                    json.put("periodicity",tmpperiodicity);
                    json.put("opis",tmpdescription);
                    json.put("idDispenser",dispenserID);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                //Wysyłanie zapytania do serwera
                Connections connect = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Android","POST",json,false);

                //To wywala jak klikniesz drugi raz
                connect.Connect();

                //Czytanie odpowiedzi połączenia
                if(connect.getResponseCode()==200)
                {
                    Toast toast = Toast.makeText(this,R.string.add_hour,Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                }
                else
                {
                    DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),connect.Error());
                    dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                }
            }
        }
    }
}
